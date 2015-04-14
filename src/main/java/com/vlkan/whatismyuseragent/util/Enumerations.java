package com.vlkan.whatismyuseragent.util;

import com.vlkan.whatismyuseragent.devicemap.attribute.AttributeMatcher;

import java.util.Arrays;
import java.util.Optional;

public enum Enumerations {;

    public static <T extends AttributeMatcher> T parseEnumeration(String name, T[] enums, T defaultEnum) {
        return Optional.ofNullable(name)
                .flatMap(nonNullName ->
                        Arrays.asList(enums).stream()
                                .filter(enumeration -> enumeration.getPredicate().test(nonNullName))
                                .findFirst())
                .orElse(defaultEnum);
    }

}
