package org.hsh.games.aoe.domain.entities.rebelcell;

import org.hsh.games.aoe.domain.entities.resources.ResourceType;

/**
 * Represents a generic event within a Rebel Cell.
 * Events are immutable and represent historical actions within the cell.
 *
 * @author devTASE
 */
public class GuildEvent {
    private final String eventId;
    private final String description;

    private GuildEvent(String eventId, String description) {
        this.eventId = eventId;
        this.description = description;
    }

    public static GuildEvent createGuildCreated(String guildId, String founderId) {
        return new GuildEvent(guildId, "Guild created by " + founderId);
    }

    public static GuildEvent createMemberJoined(String guildId, String memberId) {
        return new GuildEvent(guildId, memberId + " joined the guild.");
    }

    public static GuildEvent createResourceDeposited(String guildId, String playerId, ResourceType type, int amount) {
        return new GuildEvent(guildId, playerId + " deposited " + amount + " " + type + ".");
    }

    public static GuildEvent createMemberLeft(String guildId, String playerId) {
        return new GuildEvent(guildId, playerId + " left the guild.");
    }

    public static GuildEvent createMemberPromoted(String guildId, String playerId, RebelCellRankType newRank) {
        return new GuildEvent(guildId, playerId + " promoted to " + newRank.getDisplayName() + ".");
    }

    public static GuildEvent createResourceWithdrawn(String guildId, String playerId, ResourceType type, int amount) {
        return new GuildEvent(guildId, playerId + " withdrew " + amount + " " + type + ".");
    }

    public static GuildEvent createTerritoryConquered(String guildId, String playerId, String territoryId) {
        return new GuildEvent(guildId, playerId + " conquered territory " + territoryId + ".");
    }

    public static GuildEvent createTerritoryLost(String guildId, String territoryId) {
        return new GuildEvent(guildId, "Territory " + territoryId + " was lost.");
    }

    public static GuildEvent createSpyReportReceived(String guildId, String targetGuildId, String intelligenceType) {
        return new GuildEvent(guildId, "Spy report received on " + targetGuildId + " (" + intelligenceType + ").");
    }

    public static GuildEvent createVaultUpgraded(String guildId, int additionalCapacity) {
        return new GuildEvent(guildId, "Vault capacity increased by " + additionalCapacity + ".");
    }

    public String getDescription() {
        return description;
    }

    public String getEventId() {
        return eventId;
    }

    /**
     * Gets a summary of the event
     * @return Event summary
     */
    public String getSummary() {
        return description;
    }
}
