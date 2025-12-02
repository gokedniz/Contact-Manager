package com.cmpe343.project.model;

public enum Role {
    TESTER,
    JUNIOR_DEVELOPER,
    SENIOR_DEVELOPER,
    MANAGER;

    public static Role fromDbValue(String value) {
        switch (value.toUpperCase()) {
            case "TESTER":
                return TESTER;
            case "JUNIOR":
                return JUNIOR_DEVELOPER;
            case "SENIOR":
                return SENIOR_DEVELOPER;
            case "MANAGER":
                return MANAGER;
            default:
                throw new IllegalArgumentException("Unknown role: " + value);
        }
    }
}
