package com.sh32bit.airline.enums;

public enum Role {
    ADMIN("ROLE_ADMIN"),
    AGENT("ROLE_AGENT"),
    CUSTOMER("ROLE_CUSTOMER");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    @Override
    public String toString() {
        return authority;
    }
}
