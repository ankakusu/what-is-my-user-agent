package com.vlkan.whatismyuseragent.devicemap;

import lombok.Getter;

public class IllegalUserAgentException extends IllegalArgumentException {

    @Getter
    private final String userAgent;

    public IllegalUserAgentException(String userAgent) {
        super("Illegal User-Agent: " + userAgent);
        this.userAgent = userAgent;
    }

    public IllegalUserAgentException(String userAgent, Throwable cause) {
        super("Illegal User-Agent: " + userAgent, cause);
        this.userAgent = userAgent;
    }

}
