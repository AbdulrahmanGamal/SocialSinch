package com.social.backendless.model;

/**
 * Enum with the different event status considered
 */

public enum EventStatus {
    ONLINE,
    OFFLINE,
    TYPING;

    /**
     * Returns the EventStatus based on the String value provided
     * @param eventStatus <b>(String)</b> - String representing the EventStatus
     * @return <b>(EventStatus)</b> - Returns the enum value of the input String
     */
    public static EventStatus fromString(final String eventStatus) {
        for (EventStatus status : values()) {
            if (eventStatus.equalsIgnoreCase(status.toString())) {
                return status;
            }
        }
        return null;
    }
}
