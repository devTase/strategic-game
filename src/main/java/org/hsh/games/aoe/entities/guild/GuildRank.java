package org.hsh.games.aoe.entities.guild;

/**
 * Enum representing different ranks within a guild hierarchy.
 * Each rank has specific privileges and responsibilities.
 * 
 * @author devTASE
 */
public enum GuildRank {
    
    LEADER("Leader", "Guild Leader", "👑", "The supreme commander of the guild with all privileges"),
    OFFICER("Officer", "Guild Officer", "⚔️", "High-ranking member with administrative privileges"),
    MEMBER("Member", "Guild Member", "🛡️", "Regular member with standard guild privileges"),
    RECRUIT("Recruit", "Guild Recruit", "🎯", "New member still proving their worth to the guild"),
    SPY("Spy", "Guild Spy", "🕵️", "Covert operative gathering intelligence for the guild");
    
    private final String rank;
    private final String displayName;
    private final String emoji;
    private final String description;
    
    GuildRank(String rank, String displayName, String emoji, String description) {
        this.rank = rank;
        this.displayName = displayName;
        this.emoji = emoji;
        this.description = description;
    }
    
    public String getRank() {
        return rank;
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
     * Gets the guild rank by display name
     * @param displayName the display name to search for
     * @return the matching GuildRank, or null if not found
     */
    public static GuildRank getByDisplayName(String displayName) {
        for (GuildRank rank : values()) {
            if (rank.getDisplayName().equalsIgnoreCase(displayName)) {
                return rank;
            }
        }
        return null;
    }
    
    /**
     * Checks if this rank has administrative privileges
     * @return true if rank is LEADER or OFFICER
     */
    public boolean hasAdminPrivileges() {
        return this == LEADER || this == OFFICER;
    }
    
    /**
     * Checks if this rank can recruit new members
     * @return true if rank is LEADER or OFFICER
     */
    public boolean canRecruit() {
        return this == LEADER || this == OFFICER;
    }
}
