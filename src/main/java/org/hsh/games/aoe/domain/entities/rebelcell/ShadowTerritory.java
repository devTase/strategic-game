package org.hsh.games.aoe.domain.entities.rebelcell;

/**
 * Represents a territory controlled by a Rebel Cell.
 * Territories provide strategic advantages and resources.
 * 
 * @author devTASE
 */
public class ShadowTerritory {
    private final String territoryId;
    private final String name;
    private final String controllingGuildId;
    private final TerritoryStatus status;

    private ShadowTerritory(String territoryId, String name, String controllingGuildId, TerritoryStatus status) {
        this.territoryId = territoryId;
        this.name = name;
        this.controllingGuildId = controllingGuildId;
        this.status = status;
    }

    public static ShadowTerritory createNew(String territoryId, String name, String controllingGuildId) {
        return new ShadowTerritory(territoryId, name, controllingGuildId, TerritoryStatus.CONTROLLED);
    }

    public static ShadowTerritory createNeutral(String territoryId, String name) {
        return new ShadowTerritory(territoryId, name, null, TerritoryStatus.NEUTRAL);
    }

    public ShadowTerritory withStatus(TerritoryStatus newStatus) {
        return new ShadowTerritory(territoryId, name, controllingGuildId, newStatus);
    }

    public String getTerritoryId() {
        return territoryId;
    }

    public String getName() {
        return name;
    }

    public String getControllingGuildId() {
        return controllingGuildId;
    }

    public TerritoryStatus getStatus() {
        return status;
    }

    /**
     * Checks if the territory is available for conquest
     * @return true if territory can be conquered
     */
    public boolean isAvailableForConquest() {
        return status == TerritoryStatus.NEUTRAL || status == TerritoryStatus.CONTESTED;
    }

    /**
     * Claims the territory for a guild
     * @param guildId ID of the guild claiming the territory
     * @return Updated territory with new status
     */
    public ShadowTerritory claimByGuild(String guildId) {
        return new ShadowTerritory(territoryId, name, guildId, TerritoryStatus.UNDER_SIEGE);
    }

    /**
     * Gets the territory status
     * @return Current territory status
     */
    public TerritoryStatus status() {
        return status;
    }

    /**
     * Liberates the territory (makes it neutral)
     * @return Updated territory with neutral status
     */
    public ShadowTerritory liberate() {
        return new ShadowTerritory(territoryId, name, null, TerritoryStatus.NEUTRAL);
    }

    /**
     * Puts the territory under siege
     * @return Updated territory with under siege status
     */
    public ShadowTerritory putUnderSiege() {
        return new ShadowTerritory(territoryId, name, controllingGuildId, TerritoryStatus.UNDER_SIEGE);
    }

    /**
     * Gets the ID of the owning guild
     * @return Guild ID that owns this territory
     */
    public String owningGuildId() {
        return controllingGuildId;
    }

    /**
     * Gets formatted information about the territory
     * @return Formatted string with territory details
     */
    public String getFormattedInfo() {
        return String.format("Territory: %s [%s] - Status: %s - Guild: %s", 
                name, territoryId, status, controllingGuildId != null ? controllingGuildId : "None");
    }
}
