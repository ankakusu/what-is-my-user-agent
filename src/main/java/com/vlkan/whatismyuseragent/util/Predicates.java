package com.vlkan.whatismyuseragent.util;

import com.vlkan.whatismyuseragent.devicemap.attribute.AttributeMatcher;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

import static com.vlkan.whatismyuseragent.util.Preconditions.checkNotNull;

public enum Predicates {;

    public static Predicate<String> alwaysFalse() {
        return input -> false;
    }

    public static Predicate<String> startsWithIgnoreCase(final String start) {
        checkNotNull(start);
        return input -> input != null && input.toLowerCase().startsWith(start);
    }

    public static Predicate<String> equalsIgnoreCase(final String... expecteds) {
        checkNotNull(expecteds);
        return input -> Arrays.asList(expecteds).stream()
                .anyMatch(expected -> expected.equalsIgnoreCase(input));
    }

    public static Predicate<String> matchesIgnoreCase(final String regex) {
        checkNotNull(regex);
        return input -> input != null && input.toLowerCase().matches(regex);
    }

}
