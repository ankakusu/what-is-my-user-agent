package com.vlkan.whatismyuseragent.devicemap;

import com.google.common.collect.ImmutableMap;
import com.vlkan.whatismyuseragent.util.MatchableEnumeration;
import lombok.Getter;

import java.util.function.Predicate;

import static com.vlkan.whatismyuseragent.util.Preconditions.checkNotNull;
import static com.vlkan.whatismyuseragent.util.Predicates.alwaysFalse;
import static com.vlkan.whatismyuseragent.util.Predicates.equalsIgnoreCase;

public enum DeviceType implements MatchableEnumeration {

    /**
     * head up displays, smart watches, -3”, minimal controls
     */
    MINI        ("Mini",        equalsIgnoreCase("mini")),

    /**
     * smart phone, phablet, -6”, game device, touch enabled
     */
    MOBILE      ("Mobile",      equalsIgnoreCase("mobile")),

    /**
     * touch enabled, 7”-13”
     */
    TABLET      ("Tablet",      equalsIgnoreCase("tablet")),

    /**
     * greyscale, e-Ink, fixed fonts, -10”
     */
    EREADER     ("E-reader",    equalsIgnoreCase("e-reader")),

    /**
     * PC, laptop, notebook mouse/keyboard, 11”-
     */
    DESKTOP     ("Desktop",     equalsIgnoreCase("desktop")),

    /**
     * smart TV, set top box, streaming device, 40”-
     */
    TV          ("TV",          equalsIgnoreCase("tv")),

    /**
     * cinema, tv walls, 100”-, minimal controls
     */
    MAXI        ("Maxi",        equalsIgnoreCase("maxi")),

    /**
     * search bots from major search engines
     */
    SEO_AGENT   ("SEO Agent",   equalsIgnoreCase("seo agent")),

    /**
     * fallback for everything with insufficient UA, not found in the DDR or service timeout: catch when calling
     */
    UNKNOWN     ("Unknown",     alwaysFalse());

    private final String name;

    @Getter
    private transient final Predicate<String> predicate;

    DeviceType(String name, Predicate<String> predicate) {
        this.name = checkNotNull(name, "name");
        this.predicate = checkNotNull(predicate, "predicate");
    }

    public static DeviceType parse(String name) {
        return MatchableEnumeration.parseEnumeration(name, values(), UNKNOWN);
    }

    public DeviceProfile toDeviceProfile() {
        ImmutableMap<String, String> attributes = ImmutableMap.of();
        DeviceAttributes deviceAttributes = new DeviceAttributes(attributes);
        return new DeviceProfile(this, deviceAttributes);
    }

    @Override
    public String toString() { return name; }

}
