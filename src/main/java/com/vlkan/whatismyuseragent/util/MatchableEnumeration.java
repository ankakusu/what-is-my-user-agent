package com.vlkan.whatismyuseragent.util;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public interface MatchableEnumeration {

    public Predicate<String> getPredicate();

    public static <T extends MatchableEnumeration> T parseEnumeration(
            String name, T[] enumerations, T defaultEnumeration) {
        return Optional.ofNullable(name)
                .flatMap(nonNullName ->
                        Arrays.stream(enumerations)
                                .filter(enumeration -> enumeration.getPredicate().test(nonNullName))
                                .findFirst())
                .orElse(defaultEnumeration);
    }

}
