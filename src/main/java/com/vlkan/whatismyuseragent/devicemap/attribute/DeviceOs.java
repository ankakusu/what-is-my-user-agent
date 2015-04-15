package com.vlkan.whatismyuseragent.devicemap.attribute;

import com.vlkan.whatismyuseragent.util.MatchableEnumeration;
import lombok.Getter;

import java.util.function.Predicate;

import static com.vlkan.whatismyuseragent.util.Preconditions.checkNotNull;
import static com.vlkan.whatismyuseragent.util.Predicates.*;

public enum DeviceOs implements MatchableEnumeration {

    ANDROID     ("Android",     equalsIgnoreCase("android")),
    BADA        ("Bada",        startsWithIgnoreCase("bada")),
    BLACKBERRY  ("BlackBerry",  matchesIgnoreCase("^(bb|blackberry).*")),
    BREW        ("Brew",        startsWithIgnoreCase("brew")),
    FIREFOX     ("Firefox",     startsWithIgnoreCase("firefox")),
    LINUX       ("Linux",       startsWithIgnoreCase("linux")),
    MTK_NUCLEUS ("MTK/Nucleus", startsWithIgnoreCase("mtk/nucleus")),
    MAEMO       ("Maemo",       equalsIgnoreCase("maemo")),
    MEEGO       ("MeeGO",       equalsIgnoreCase("meego")),
    NOKIA       ("Nokia",       startsWithIgnoreCase("nokia")),
    OSX         ("OSX",         equalsIgnoreCase("osx")),
    PALM        ("Palm",        startsWithIgnoreCase("palm")),
    RIM         ("RIM",         startsWithIgnoreCase("rim")),
    SYMBIAN     ("Symbian",     startsWithIgnoreCase("symbian")),
    WINDOWS     ("Windows",     startsWithIgnoreCase("windows")),
    IOS         ("iOS",         matchesIgnoreCase("^(ios|iphone).*")),
    WEBOS       ("WebOS",       equalsIgnoreCase("webos")),
    UNKNOWN     ("Unknown",     alwaysFalse());

    private final String name;

    @Getter
    private final transient Predicate<String> predicate;

    private DeviceOs(String name, Predicate<String> predicate) {
        this.name = checkNotNull(name, "name");
        this.predicate = checkNotNull(predicate, "predicate");
    }

    public static DeviceOs parse(String name) {
        return MatchableEnumeration.parseEnumeration(name, values(), UNKNOWN);
    }

    @Override
    public String toString() { return name; }

}
