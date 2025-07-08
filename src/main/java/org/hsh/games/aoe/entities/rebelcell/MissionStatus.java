package org.hsh.games.aoe.entities.rebelcell;

/**
 * Enum representing the status of a Rebel Cell mission.
 * Tracks the lifecycle of missions from creation to completion.
 * 
 * @author devTASE
 */
public enum MissionStatus {
    PENDING("Pending", "⏳", "Mission is waiting to be started"),
    IN_PROGRESS("In Progress", "🚀", "Mission is currently being executed"),
    COMPLETED("Completed", "✅", "Mission completed successfully"),
    FAILED("Failed", "❌", "Mission failed or was aborted"),
    CANCELLED("Cancelled", "🚫", "Mission was cancelled before execution"),
    CANCELED("Canceled", "🚫", "Mission was canceled before execution");

    private final String displayName;
    private final String emoji;
    private final String description;

    MissionStatus(String displayName, String emoji, String description) {
        this.displayName = displayName;
        this.emoji = emoji;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayNameWithEmoji() {
        return emoji + " " + displayName;
    }

    /**
     * Checks if the mission is in an active state
     * @return true if mission is active
     */
    public boolean isActive() {
        return this == IN_PROGRESS;
    }

    /**
     * Checks if the mission can be started
     * @return true if mission can be started
     */
    public boolean canBeStarted() {
        return this == PENDING;
    }
}
