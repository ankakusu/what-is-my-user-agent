package com.vlkan.whatismyuseragent.devicemap;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;

import static com.vlkan.whatismyuseragent.util.Preconditions.checkNotNull;

@Immutable
public class DeviceProfile {

    @Getter
    private final DeviceType deviceType;

    @Getter
    private final DeviceAttributes deviceAttributes;

    @Getter
    private static final DeviceProfile unknownDeviceProfile;

    static {
        DeviceAttributes emptyDeviceAttributes = new DeviceAttributes(ImmutableMap.of());
        unknownDeviceProfile = new DeviceProfile(DeviceType.UNKNOWN, emptyDeviceAttributes);
    }

    public DeviceProfile(DeviceType deviceType, DeviceAttributes deviceAttributes) {
        this.deviceType = checkNotNull(deviceType, "device type");
        this.deviceAttributes = checkNotNull(deviceAttributes, "device attributes");
    }

}
