package org.hsh.games.aoe.entities.rebelcell;

/**
 * Enum representing the status of a territory controlled by Rebel Cells.
 * Tracks the state of territorial control and influence.
 * 
 * @author devTASE
 */
public enum TerritoryStatus {
    CONTROLLED("Controlled", "⚡", "Territory is under full rebel control"),
    CONTESTED("Contested", "⚔️", "Territory is being disputed"),
    ABANDONED("Abandoned", "🏚️", "Territory has been abandoned"),
    NEUTRAL("Neutral", "🏳️", "Territory is neutral, no faction control"),
    UNDER_SIEGE("Under Siege", "⚔️", "Territory is being attacked"),
    CLAIMED_BY_GUILD("Claimed by Guild", "🏴", "Territory is claimed by a guild"),
    FORTIFIED("Fortified", "🏰", "Territory has enhanced defenses");

    private final String displayName;
    private final String emoji;
    private final String description;

    TerritoryStatus(String displayName, String emoji, String description) {
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
     * Checks if the territory is controlled by a guild
     * @return true if territory is controlled by a guild
     */
    public boolean isControlledByGuild() {
        return this == CONTROLLED || this == CLAIMED_BY_GUILD || this == FORTIFIED;
    }
}
