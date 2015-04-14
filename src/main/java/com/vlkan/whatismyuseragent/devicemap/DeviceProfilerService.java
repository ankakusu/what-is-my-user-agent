package com.vlkan.whatismyuseragent.devicemap;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.concurrent.ThreadSafe;
import java.util.function.Predicate;

@Slf4j
@ThreadSafe
public abstract class DeviceProfilerService<T extends DeviceAttributes> {

    @Value
    protected class AttributePredicateArgument {

        private final String userAgent;

        private final T deviceAttributes;

    }

    /**
     * A predicate container to check if a
     * {@link DeviceProfilerService.AttributePredicateArgument} is of
     * provided {@link com.vlkan.whatismyuseragent.devicemap.DeviceType}.
     */
    @AllArgsConstructor
    protected class DeviceTypePredicate<C> {

        @Getter
        private final DeviceType deviceType;

        private final Predicate<C> predicate;

        public boolean test(C context) { return predicate.test(context); }

    }

    /**
     * Compares the given User-Agent against the Googlebot Mobile User-Agent list provided by
     * <a href="https://developers.google.com/webmasters/mobile-sites/references/googlebot">Google</a>.
     */
    protected DeviceTypePredicate<String> createGooglebotMobileUserAgentPredicate() {
        return new DeviceTypePredicate<>(DeviceType.MOBILE,
                userAgent -> userAgent != null && userAgent.contains(
                        "(compatible; Googlebot-Mobile/2.1; +http://www.google.com/bot.html)"));
    }

    protected abstract ImmutableList<DeviceTypePredicate<String>> getUserAgentPredicates();

    protected abstract ImmutableList<DeviceTypePredicate<AttributePredicateArgument>> getAttributePredicates();

    protected abstract T findDeviceAttributes(String userAgent);

    public DeviceProfile findDeviceProfile(String userAgent) {
        log.trace("Profiling User-Agent: {}", userAgent);
        checkEmptyUserAgent(userAgent);
        DeviceType deviceType = findDeviceType(userAgent);
        if (!DeviceType.UNKNOWN.equals(deviceType)) return deviceType.toDeviceProfile();
        T deviceAttributes = findDeviceAttributes(userAgent);
        AttributePredicateArgument requestContext = new AttributePredicateArgument(userAgent, deviceAttributes);
        deviceType = findDeviceType(requestContext);
        return new DeviceProfile(deviceType, deviceAttributes);
    }

    protected static void checkEmptyUserAgent(String userAgent) {
        if (userAgent == null || userAgent.trim().isEmpty())
            throw new IllegalUserAgentException(userAgent);
    }

    protected DeviceType findDeviceType(String userAgent) {
        return getUserAgentPredicates().stream()
                .filter(predicate -> predicate.test(userAgent))
                .map(DeviceTypePredicate::getDeviceType)
                .findFirst().orElse(DeviceType.UNKNOWN);
    }

    protected DeviceType findDeviceType(AttributePredicateArgument arg) {
        return getAttributePredicates().stream()
                .filter(predicate -> predicate.test(arg))
                .map(DeviceTypePredicate::getDeviceType)
                .findFirst().orElse(DeviceType.UNKNOWN);
    }

}
