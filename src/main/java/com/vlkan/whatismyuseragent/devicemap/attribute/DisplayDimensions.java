package com.vlkan.whatismyuseragent.devicemap.attribute;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Value;

@Value
@Immutable
public class DisplayDimensions {

    private final int width;

    private final int height;

}
