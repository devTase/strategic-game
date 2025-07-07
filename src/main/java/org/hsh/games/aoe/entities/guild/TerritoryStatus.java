package org.hsh.games.aoe.entities.guild;

/**
 * Enum representing different status states of territories in the guild system.
 * Each status indicates the current control and conflict state of a territory.
 * 
 * @author devTASE
 */
public enum TerritoryStatus {
    
    NEUTRAL("Neutral", "Neutral Territory", "⚪", "Territory belongs to no guild and is available for conquest"),
    CLAIMED_BY_GUILD("Claimed by Guild", "Guild Territory", "🏴", "Territory is controlled by a guild and generates resources"),
    UNDER_SIEGE("Under Siege", "Territory Under Siege", "⚔️", "Territory is being contested and under active attack");
    
    private final String status;
    private final String displayName;
    private final String emoji;
    private final String description;
    
    TerritoryStatus(String status, String displayName, String emoji, String description) {
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
     * Gets the territory status by display name
     * @param displayName the display name to search for
     * @return the matching TerritoryStatus, or null if not found
     */
    public static TerritoryStatus getByDisplayName(String displayName) {
        for (TerritoryStatus status : values()) {
            if (status.getDisplayName().equalsIgnoreCase(displayName)) {
                return status;
            }
        }
        return null;
    }
    
    /**
     * Checks if the territory is available for conquest
     * @return true if territory is NEUTRAL or UNDER_SIEGE
     */
    public boolean isAvailableForConquest() {
        return this == NEUTRAL || this == UNDER_SIEGE;
    }
    
    /**
     * Checks if the territory is generating resources
     * @return true if territory is CLAIMED_BY_GUILD
     */
    public boolean isGeneratingResources() {
        return this == CLAIMED_BY_GUILD;
    }
    
    /**
     * Checks if the territory is in conflict
     * @return true if territory is UNDER_SIEGE
     */
    public boolean isInConflict() {
        return this == UNDER_SIEGE;
    }
    
    /**
     * Checks if the territory is controlled by a guild
     * @return true if territory is CLAIMED_BY_GUILD or UNDER_SIEGE
     */
    public boolean isControlledByGuild() {
        return this == CLAIMED_BY_GUILD || this == UNDER_SIEGE;
    }
}
