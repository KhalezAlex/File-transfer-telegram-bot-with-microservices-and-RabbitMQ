package org.klozevitz.service.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ServiceCommands {
    START("/start"),
    HELP("/help"),
    REGISTRATION("/registration"),
    CANCEL("/cancel");

    private final String command;

    @Override
    public String toString() {
        return command;
    }

    public boolean equals(String command) {
        return this.toString().equals(command);
    }
}
