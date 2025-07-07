package org.hsh.games.aoe.entities.guild;

/**
 * Enum representing different types of guild missions.
 * Each mission type has specific objectives and rewards.
 * 
 * @author devTASE
 */
public enum MissionType {
    
    RESOURCE_RUN("Resource Run", "Resource Gathering Mission", "⛏️", "Collect valuable resources from dangerous territories"),
    TERRITORY_CONQUEST("Territory Conquest", "Territory Conquest Mission", "🏰", "Claim and conquer new territories for the guild"),
    ESPIONAGE("Espionage", "Espionage Mission", "🕵️‍♂️", "Gather intelligence on enemy guilds and their activities"),
    VAULT_DEFENSE("Vault Defense", "Vault Defense Mission", "🛡️", "Protect the guild's treasury from enemy raids");
    
    private final String missionType;
    private final String displayName;
    private final String emoji;
    private final String description;
    
    MissionType(String missionType, String displayName, String emoji, String description) {
        this.missionType = missionType;
        this.displayName = displayName;
        this.emoji = emoji;
        this.description = description;
    }
    
    public String getMissionType() {
        return missionType;
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
     * Gets the mission type by display name
     * @param displayName the display name to search for
     * @return the matching MissionType, or null if not found
     */
    public static MissionType getByDisplayName(String displayName) {
        for (MissionType missionType : values()) {
            if (missionType.getDisplayName().equalsIgnoreCase(displayName)) {
                return missionType;
            }
        }
        return null;
    }
    
    /**
     * Checks if this mission type requires stealth
     * @return true if mission type is ESPIONAGE
     */
    public boolean requiresStealth() {
        return this == ESPIONAGE;
    }
    
    /**
     * Checks if this mission type is combat-oriented
     * @return true if mission type involves combat
     */
    public boolean isCombatMission() {
        return this == TERRITORY_CONQUEST || this == VAULT_DEFENSE;
    }
    
    /**
     * Checks if this mission type is resource-oriented
     * @return true if mission type involves resource gathering
     */
    public boolean isResourceMission() {
        return this == RESOURCE_RUN;
    }
}
