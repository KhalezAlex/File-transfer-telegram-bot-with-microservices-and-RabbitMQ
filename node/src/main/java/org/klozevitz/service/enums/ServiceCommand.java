package org.klozevitz.service.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ServiceCommand {
    START("/start"),
    HELP("/help"),
    REGISTRATION("/registration"),
    CANCEL("/cancel");

    private final String value;

    @Override
    public String toString() {
        return value;
    }

    public static ServiceCommand fromValue(String v) {
        for (ServiceCommand c: ServiceCommand.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        return null;
    }
}
