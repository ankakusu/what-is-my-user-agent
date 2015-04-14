package com.vlkan.whatismyuseragent.util;

public enum Preconditions {;

    public static <T> T checkNotNull(T value, String name) {
        if (value == null) throw new NullPointerException(name + " cannot be null");
        return value;
    }

    public static <T> T checkNotNull(T value) {
        return checkNotNull(value, "argument");
    }

}
