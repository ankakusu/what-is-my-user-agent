package com.vlkan.whatismyuseragent.devicemap;


import com.google.common.collect.ImmutableMap;
import com.vlkan.whatismyuseragent.devicemap.attribute.DeviceOs;
import com.vlkan.whatismyuseragent.devicemap.attribute.DisplayDimensions;
import com.vlkan.whatismyuseragent.devicemap.attribute.InputDevice;
import com.vlkan.whatismyuseragent.devicemap.attribute.MobileBrowser;
import lombok.Getter;
import lombok.Value;

import javax.annotation.concurrent.Immutable;
import java.util.Map;
import java.util.Optional;

import static com.vlkan.whatismyuseragent.util.Preconditions.checkNotNull;

@Immutable
public class DeviceAttributes {

    @Getter
    private final ImmutableMap<String, String> attributes;

    public DeviceAttributes(ImmutableMap<String, String> attributes) {
        this.attributes = checkNotNull(attributes, "attributes");
    }

    @Value
    @Immutable
    protected static class Parser {

        private final ImmutableMap<String, String> attributes;

        public boolean getBoolean(String key) {
            return Boolean.parseBoolean(attributes.get(key));
        }

        public DeviceOs getDeviceOs(String key) {
            return DeviceOs.parse(attributes.get(key));
        }

        public InputDevice getInputDevice(String key) {
            return InputDevice.parse(attributes.get(key));
        }

        public Optional<Integer> getInteger(String key) {
            try { return Optional.of(Integer.parseInt(attributes.get(key))); }
            catch (NumberFormatException nfe) { return Optional.empty(); }
        }

        public MobileBrowser getMobileBrowser(String key) {
            return MobileBrowser.parse(attributes.get(key));
        }

        public Optional<String> getString(String key) {
            return Optional.ofNullable(attributes.get(key))
                    .map(String::trim)
                    .filter(DeviceAttributes::isNonEmptyStringAttribute);
        }

        public Optional<DisplayDimensions> getDisplayDimensions(String widthKey, String heightKey) {
            return getInteger(widthKey).flatMap(
                    width -> getInteger(heightKey).map(
                            height -> new DisplayDimensions(width, height)));
        }

    }

    public static boolean isNonEmptyStringAttribute(String attribute) {
        return Optional.ofNullable(attribute)
                .map(String::trim)
                .filter(trimmedAttribute -> !trimmedAttribute.isEmpty() && !trimmedAttribute.equals("-"))
                .isPresent();
    }

}
