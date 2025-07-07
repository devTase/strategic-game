package org.hsh.games.aoe.entities.guild;

/**
 * Enum representing different status states of guild missions.
 * Each status indicates the current state of a mission's progression.
 * 
 * @author devTASE
 */
public enum MissionStatus {
    
    PENDING("Pending", "Mission Pending", "⏳", "Mission is waiting to be started"),
    IN_PROGRESS("In Progress", "Mission In Progress", "🔄", "Mission is currently active and being executed"),
    COMPLETED("Completed", "Mission Completed", "✅", "Mission has been successfully completed"),
    FAILED("Failed", "Mission Failed", "❌", "Mission has failed and cannot be completed"),
    CANCELLED("Cancelled", "Mission Cancelled", "🚫", "Mission has been cancelled before completion");
    
    private final String status;
    private final String displayName;
    private final String emoji;
    private final String description;
    
    MissionStatus(String status, String displayName, String emoji, String description) {
        this.status = status;
        this.displayName = displayName;
        this.emoji = emoji;
        this.description = description;
    }
    
    public String getStatus() {
        return status;
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
     * Gets the mission status by display name
     * @param displayName the display name to search for
     * @return the matching MissionStatus, or null if not found
     */
    public static MissionStatus getByDisplayName(String displayName) {
        for (MissionStatus status : values()) {
            if (status.getDisplayName().equalsIgnoreCase(displayName)) {
                return status;
            }
        }
        return null;
    }
    
    /**
     * Checks if the mission is active
     * @return true if status is IN_PROGRESS
     */
    public boolean isActive() {
        return this == IN_PROGRESS;
    }
    
    /**
     * Checks if the mission is finished (completed or failed)
     * @return true if status is COMPLETED, FAILED, or CANCELLED
     */
    public boolean isFinished() {
        return this == COMPLETED || this == FAILED || this == CANCELLED;
    }
    
    /**
     * Checks if the mission can be started
     * @return true if status is PENDING
     */
    public boolean canBeStarted() {
        return this == PENDING;
    }
    
    /**
     * Checks if the mission was successful
     * @return true if status is COMPLETED
     */
    public boolean wasSuccessful() {
        return this == COMPLETED;
    }
}
