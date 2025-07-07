package org.hsh.games.aoe.entities.guild;

/**
 * Enum representing different types of guild events that can occur.
 * Each event type represents a significant action or milestone in guild history.
 * 
 * @author devTASE
 */
public enum GuildEventType {
    
    CREATED("Created", "Guild Created", "🏛️", "A new guild has been established"),
    MEMBER_JOINED("Member Joined", "Member Joined Guild", "👥", "A new member has joined the guild"),
    MISSION_COMPLETED("Mission Completed", "Mission Completed", "✅", "Guild members have successfully completed a mission"),
    TERRITORY_CONQUERED("Territory Conquered", "Territory Conquered", "🏰", "The guild has conquered a new territory"),
    SPY_REPORT("Spy Report", "Spy Report Received", "📊", "Intelligence report has been received from a guild spy");
    
    private final String eventType;
    private final String displayName;
    private final String emoji;
    private final String description;
    
    GuildEventType(String eventType, String displayName, String emoji, String description) {
        this.eventType = eventType;
        this.displayName = displayName;
        this.emoji = emoji;
        this.description = description;
    }
    
    public String getEventType() {
        return eventType;
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
     * Gets the guild event type by display name
     * @param displayName the display name to search for
     * @return the matching GuildEventType, or null if not found
     */
    public static GuildEventType getByDisplayName(String displayName) {
        for (GuildEventType eventType : values()) {
            if (eventType.getDisplayName().equalsIgnoreCase(displayName)) {
                return eventType;
            }
        }
        return null;
    }
    
    /**
     * Checks if this event type is related to member management
     * @return true if event type is MEMBER_JOINED
     */
    public boolean isMemberEvent() {
        return this == MEMBER_JOINED;
    }
    
    /**
     * Checks if this event type is related to guild activities
     * @return true if event type is MISSION_COMPLETED or TERRITORY_CONQUERED
     */
    public boolean isActivityEvent() {
        return this == MISSION_COMPLETED || this == TERRITORY_CONQUERED;
    }
    
    /**
     * Checks if this event type is related to intelligence gathering
     * @return true if event type is SPY_REPORT
     */
    public boolean isIntelligenceEvent() {
        return this == SPY_REPORT;
    }
    
    /**
     * Checks if this event type is a founding event
     * @return true if event type is CREATED
     */
    public boolean isFoundingEvent() {
        return this == CREATED;
    }
    
    /**
     * Gets the priority level of this event type for notifications
     * @return priority level (1-5, where 1 is highest priority)
     */
    public int getPriorityLevel() {
        return switch (this) {
            case CREATED -> 1;
            case TERRITORY_CONQUERED -> 2;
            case MISSION_COMPLETED -> 3;
            case SPY_REPORT -> 4;
            case MEMBER_JOINED -> 5;
        };
    }
}
