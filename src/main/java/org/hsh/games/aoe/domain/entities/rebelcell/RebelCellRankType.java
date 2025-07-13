package org.hsh.games.aoe.domain.entities.rebelcell;

/**
 * Enum representing different ranks within a rebel cell hierarchy.
 * Each rank has specific privileges and responsibilities.
 * 
 * @author devTASE
 */
public enum RebelCellRankType {
    HACKER("Hacker", "Rebel Hacker", "💻", "Expert in bypassing security systems and gathering intelligence"),
    OPERATOR("Operator", "Rebel Operator", "📡", "Strategist in coordinating mission logistics and communications"),
    CELL_LEADER("Cell Leader", "Rebel Cell Leader", "👨‍✈️", "Leader of the cell guiding the strategic direction"),
    INFILTRATOR("Infiltrator", "Rebel Infiltrator", "🥷", "Specialist in covert missions and intelligence gathering");
    
    private final String rank;
    private final String displayName;
    private final String emoji;
    private final String description;
    
    RebelCellRankType(String rank, String displayName, String emoji, String description) {
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
     * Gets the rebel cell rank by display name
     * @param displayName the display name to search for
     * @return the matching RebelCellRank, or null if not found
     */
    public static RebelCellRankType getByDisplayName(String displayName) {
        for (RebelCellRankType rank : values()) {
            if (rank.getDisplayName().equalsIgnoreCase(displayName)) {
                return rank;
            }
        }
        return null;
    }
    
    /**
     * Checks if this rank has leadership privileges
     * @return true if rank is CELL_LEADER
     */
    public boolean hasLeadershipPrivileges() {
        return this == CELL_LEADER;
    }
    
    /**
     * Checks if this rank can infiltrate secure locations
     * @return true if rank is INFILTRATOR
     */
    public boolean canInfiltrate() {
        return this == INFILTRATOR;
    }
}
