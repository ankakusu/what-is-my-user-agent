package com.vlkan.whatismyuseragent.devicemap.attribute;

import java.util.function.Predicate;

public interface AttributeMatcher {

    public Predicate<String> getPredicate();

}
