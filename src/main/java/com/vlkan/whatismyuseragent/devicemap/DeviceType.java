package com.vlkan.whatismyuseragent.devicemap;

import com.google.common.collect.ImmutableMap;

public enum DeviceType {

    /**
     * head up displays, smart watches, -3”, minimal controls
     */
    MINI,

    /**
     * smart phone, phablet, -6”, game device, touch enabled
     */
    MOBILE,

    /**
     * touch enabled, 7”-13”
     */
    TABLET,

    /**
     * greyscale, e-Ink, fixed fonts, -10”
     */
    EREADER,

    /**
     * PC, laptop, notebook mouse/keyboard, 11”-
     */
    DESKTOP,

    /**
     * smart TV, set top box, streaming device, 40”-
     */
    TV,

    /**
     * cinema, tv walls, 100”-, minimal controls
     */
    MAXI,

    /**
     * search bots from major search engines
     */
    SEO_AGENT,

    /**
     * fallback for everything with insufficient UA, not found in the DDR or service timeout: catch when calling
     */
    UNKNOWN;

    public DeviceProfile toDeviceProfile() {
        ImmutableMap<String, String> attributes = ImmutableMap.of();
        DeviceAttributes deviceAttributes = new DeviceAttributes(attributes);
        return new DeviceProfile(this, deviceAttributes);
    }

}
