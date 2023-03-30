package com.panda_cookie.spend_thrift;

public enum Size {
    SMALL("small"),
    MEDIUM("medium"),
    LARGE("large"),
    EXTRA_LARGE("extra_large");

    private String stringValue;

    Size(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public static Size fromStringValue(String stringValue) {
        for (Size size : Size.values()) {
            if (size.getStringValue().equalsIgnoreCase(stringValue)) {
                return size;
            }
        }
        throw new IllegalArgumentException("Invalid string value for Size: " + stringValue);
    }
}
