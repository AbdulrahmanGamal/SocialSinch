package com.social.backendless.model;

/**
 * Enum with the different status considered for a Chat Message
 */

public enum ChatStatus {
    FAILED,
    WAITING,
    SEND,
    SENT,
    DELIVERED,
    RECEIVED,
    READ,
    ONLINE,
    OFFLINE,
    TYPING,
    SENT_READ;

    /**
     * Returns the ChatStatus based on the String value provided
     * @param chatStatus <b>(String)</b> - String representing the ChatStatus
     * @return <b>(ChatStatus)</b> - Returns the enum value of the input String
     */
    public static ChatStatus fromString(final String chatStatus) {
        for (ChatStatus status : values()) {
            if (chatStatus.equalsIgnoreCase(status.toString())) {
                return status;
            }
        }
        return null;
    }
}
