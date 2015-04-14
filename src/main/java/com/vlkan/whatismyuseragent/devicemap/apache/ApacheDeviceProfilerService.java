package com.vlkan.whatismyuseragent.devicemap.apache;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.vlkan.whatismyuseragent.devicemap.DeviceAttributes;
import com.vlkan.whatismyuseragent.devicemap.DeviceProfilerService;
import com.vlkan.whatismyuseragent.devicemap.DeviceType;
import com.vlkan.whatismyuseragent.devicemap.IllegalUserAgentException;
import com.vlkan.whatismyuseragent.devicemap.attribute.DeviceOs;
import com.vlkan.whatismyuseragent.devicemap.attribute.MobileBrowser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.devicemap.DeviceMapClient;
import org.apache.devicemap.loader.LoaderOption;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.util.Map;

@Slf4j
@ThreadSafe
public class ApacheDeviceProfilerService extends DeviceProfilerService<ApacheDeviceAttributes> {

    public final static String APACHE_DEVICE_DATA_DIRECTORY =
            ApacheDeviceProfilerService.class.getResource("devicedata").getPath();

    @Getter(AccessLevel.PROTECTED)
    protected final ImmutableList<DeviceTypePredicate<String>> userAgentPredicates;

    @Getter(AccessLevel.PROTECTED)
    protected final ImmutableList<DeviceTypePredicate<AttributePredicateArgument>> attributePredicates;

    private static final DeviceMapClient deviceMapClient = createDeviceClientMap();

    private static DeviceMapClient createDeviceClientMap() {
        DeviceMapClient client = new DeviceMapClient();
        try { client.initDeviceData(LoaderOption.FOLDER, APACHE_DEVICE_DATA_DIRECTORY); }
        catch (IOException e) {
            log.error("could not initiate Apache Device Map data", e);
            throw new RuntimeException(e);
        }
        log.debug("Apache DeviceMap client initialized.");
        return client;
    }

    public ApacheDeviceProfilerService() {
        this.userAgentPredicates = createUserAgentPredicates();
        this.attributePredicates = createAttributePredicates();
    }

    private ImmutableList<DeviceTypePredicate<String>> createUserAgentPredicates() {
        return ImmutableList.of(createGooglebotMobileUserAgentPredicate());
    }

    private ImmutableList<DeviceTypePredicate<AttributePredicateArgument>> createAttributePredicates() {
        return ImmutableList.of(
                createSeoAgentAttributePredicate(),
                createTabletPredicate(),
                createDesktopPredicate(),
                createMobilePredicate());
    }

    private DeviceTypePredicate<AttributePredicateArgument> createSeoAgentAttributePredicate() {
        return new DeviceTypePredicate<>(DeviceType.SEO_AGENT, arg -> arg.getDeviceAttributes().isBot());
    }

    private DeviceTypePredicate<AttributePredicateArgument> createTabletPredicate() {
        return new DeviceTypePredicate<>(DeviceType.TABLET, arg -> arg.getDeviceAttributes().isTablet());
    }

    private DeviceTypePredicate<AttributePredicateArgument> createDesktopPredicate() {
        return new DeviceTypePredicate<>(DeviceType.DESKTOP, arg -> arg.getDeviceAttributes().isDesktop());
    }

    private DeviceTypePredicate<AttributePredicateArgument> createMobilePredicate() {
        return new DeviceTypePredicate<>(DeviceType.MOBILE,
                arg -> {
                    MobileBrowser browser = arg.getDeviceAttributes().getMobileBrowser();
                    DeviceOs os = arg.getDeviceAttributes().getDeviceOs();
                    return browser != null && (
                            !MobileBrowser.UNKNOWN.equals(browser) ||
                                    DeviceOs.ANDROID.equals(os) ||
                                    DeviceOs.BLACKBERRY.equals(os));
                });
    }

    protected ApacheDeviceAttributes findDeviceAttributes(String userAgent) {
        Map<String, String> attributes = deviceMapClient.classify(userAgent);
        if (attributes == null || attributes.isEmpty()) throw new IllegalUserAgentException(userAgent);
        return new ApacheDeviceAttributes(ImmutableMap.copyOf(
                Maps.filterValues(attributes, DeviceAttributes::isNonEmptyStringAttribute)));
    }

}
