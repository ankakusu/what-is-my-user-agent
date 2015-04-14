package com.vlkan.whatismyuseragent.devicemap.attribute;

import lombok.Getter;

import java.util.function.Predicate;

import static com.vlkan.whatismyuseragent.util.Enumerations.parseEnumeration;
import static com.vlkan.whatismyuseragent.util.Preconditions.checkNotNull;
import static com.vlkan.whatismyuseragent.util.Predicates.*;

public enum MobileBrowser implements AttributeMatcher {

    ACCESS_NETFRONT ("Access Netfront",     equalsIgnoreCase("access netfront")),
    ANDROID_WEBKIT  ("Android Webkit",      equalsIgnoreCase("android webkit")),
    BLACKBERRY      ("BlackBerry",          matchesIgnoreCase("^black ?berry.*")),
    DOLFIN          ("Dolfin",              startsWithIgnoreCase("dolfin")),
    FIREFOX         ("Firefox",             equalsIgnoreCase("firefox")),
    IE              ("Internet Explorer",   equalsIgnoreCase("iemobile", "microsoft mobile explorer")),
    MAUI            ("MAUI Wap",            startsWithIgnoreCase("maui")),
    MOTOROLA        ("Motorola",            startsWithIgnoreCase("motorola")),
    MYRIAD          ("Myriad",              equalsIgnoreCase("myriad")),
    NOKIA           ("Nokia",               startsWithIgnoreCase("nokia")),
    OPENWAVE        ("Openwave",            startsWithIgnoreCase("openwave")),
    OPERA           ("Opera",               startsWithIgnoreCase("opera")),
    POLARIS         ("Polaris",             equalsIgnoreCase("polaris")),
    PHANTOM         ("Phantom",             equalsIgnoreCase("phantom")),
    SAFARI          ("Safari",              equalsIgnoreCase("safari")),
    TELECA_OBIGO    ("Teleca-Obigo",        equalsIgnoreCase("teleca-obigo")),
    UNKNOWN         ("Unknown",             alwaysFalse());

    private final String name;

    @Getter
    private final transient Predicate<String> predicate;

    private MobileBrowser(String name, Predicate<String> predicate) {
        checkNotNull(name, "name cannot be null");
        checkNotNull(predicate, "predicate cannot be null");
        this.name = name;
        this.predicate = predicate;
    }

    public static MobileBrowser parse(String name) {
        return parseEnumeration(name, values(), UNKNOWN);
    }

    @Override
    public String toString() {
        return name;
    }

}
