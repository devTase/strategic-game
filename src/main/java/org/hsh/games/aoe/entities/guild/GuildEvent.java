package org.hsh.games.aoe.entities.guild;

import org.hsh.games.aoe.entities.ResourceType;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Record representing a guild event that occurred in the guild's history.
 * This is an immutable data structure that contains event information.
 * 
 * @param id          Unique identifier for the event
 * @param guildId     ID of the guild this event belongs to
 * @param eventType   Type of event that occurred
 * @param description Description of what happened
 * @param playerId    ID of the player involved (can be null for system events)
 * @param timestamp   When the event occurred
 * 
 * @author devTASE
 */
public record GuildEvent(
        String id,
        String guildId,
        GuildEventType eventType,
        String description,
        String playerId,
        LocalDateTime timestamp
) {
    
    /**
     * Constructor with validation
     */
    public GuildEvent {
        Objects.requireNonNull(id, "Event ID cannot be null");
        Objects.requireNonNull(guildId, "Guild ID cannot be null");
        Objects.requireNonNull(eventType, "Event type cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        Objects.requireNonNull(timestamp, "Timestamp cannot be null");
        
        if (id.isBlank()) {
            throw new IllegalArgumentException("Event ID cannot be blank");
        }
        
        if (guildId.isBlank()) {
            throw new IllegalArgumentException("Guild ID cannot be blank");
        }
        
        if (description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be blank");
        }
    }
    
    /**
     * Creates a new guild event with current timestamp
     * @param guildId Guild ID
     * @param eventType Event type
     * @param description Event description
     * @param playerId Player involved (can be null)
     * @return A new GuildEvent instance
     */
    public static GuildEvent createNew(String guildId, GuildEventType eventType, String description, String playerId) {
        String eventId = generateEventId(guildId, eventType);
        return new GuildEvent(eventId, guildId, eventType, description, playerId, LocalDateTime.now());
    }
    
    /**
     * Creates a guild creation event
     * @param guildId Guild ID
     * @param founderId Founder player ID
     * @return A new GuildEvent for guild creation
     */
    public static GuildEvent createGuildCreated(String guildId, String founderId) {
        String description = String.format("Guild was founded by %s", founderId);
        return createNew(guildId, GuildEventType.CREATED, description, founderId);
    }
    
    /**
     * Creates a member joined event
     * @param guildId Guild ID
     * @param playerId Player who joined
     * @return A new GuildEvent for member joining
     */
    public static GuildEvent createMemberJoined(String guildId, String playerId) {
        String description = String.format("Player %s joined the guild", playerId);
        return createNew(guildId, GuildEventType.MEMBER_JOINED, description, playerId);
    }
    
    /**
     * Creates a member left event
     * @param guildId Guild ID
     * @param playerId Player who left
     * @return A new GuildEvent for member leaving
     */
    public static GuildEvent createMemberLeft(String guildId, String playerId) {
        String description = String.format("Player %s left the guild", playerId);
        return createNew(guildId, GuildEventType.MEMBER_JOINED, description, playerId);
    }
    
    /**
     * Creates a member promoted event
     * @param guildId Guild ID
     * @param playerId Player who was promoted
     * @param newRank New rank assigned
     * @return A new GuildEvent for member promotion
     */
    public static GuildEvent createMemberPromoted(String guildId, String playerId, GuildRank newRank) {
        String description = String.format("Player %s was promoted to %s", playerId, newRank.getDisplayName());
        return createNew(guildId, GuildEventType.MEMBER_JOINED, description, playerId);
    }
    
    /**
     * Creates a mission completed event
     * @param guildId Guild ID
     * @param missionId Mission that was completed
     * @param playerId Player who led the mission (can be null)
     * @return A new GuildEvent for mission completion
     */
    public static GuildEvent createMissionCompleted(String guildId, String missionId, String playerId) {
        String description = String.format("Mission %s was successfully completed", missionId);
        return createNew(guildId, GuildEventType.MISSION_COMPLETED, description, playerId);
    }
    
    /**
     * Creates a territory conquered event
     * @param guildId Guild ID
     * @param playerId Player who conquered the territory
     * @param territoryId Territory that was conquered
     * @return A new GuildEvent for territory conquest
     */
    public static GuildEvent createTerritoryConquered(String guildId, String playerId, String territoryId) {
        String description = String.format("Territory %s was conquered by %s", territoryId, playerId);
        return createNew(guildId, GuildEventType.TERRITORY_CONQUERED, description, playerId);
    }
    
    /**
     * Creates a territory lost event
     * @param guildId Guild ID
     * @param territoryId Territory that was lost
     * @return A new GuildEvent for territory loss
     */
    public static GuildEvent createTerritoryLost(String guildId, String territoryId) {
        String description = String.format("Territory %s was lost", territoryId);
        return createNew(guildId, GuildEventType.TERRITORY_CONQUERED, description, null);
    }
    
    /**
     * Creates a spy report received event
     * @param guildId Guild ID
     * @param targetGuildId Guild that was spied on
     * @param intelligenceType Type of intelligence gathered
     * @return A new GuildEvent for spy report
     */
    public static GuildEvent createSpyReportReceived(String guildId, String targetGuildId, String intelligenceType) {
        String description = String.format("Spy report received about %s - %s", targetGuildId, intelligenceType);
        return createNew(guildId, GuildEventType.SPY_REPORT, description, null);
    }
    
    /**
     * Creates a resource deposited event
     * @param guildId Guild ID
     * @param playerId Player who made the deposit
     * @param resourceType Type of resource deposited
     * @param amount Amount deposited
     * @return A new GuildEvent for resource deposit
     */
    public static GuildEvent createResourceDeposited(String guildId, String playerId, ResourceType resourceType, int amount) {
        String description = String.format("Player %s deposited %d %s to the guild vault", playerId, amount, resourceType.getDescription());
        return createNew(guildId, GuildEventType.MEMBER_JOINED, description, playerId); // Using MEMBER_JOINED as a generic event
    }
    
    /**
     * Creates a resource withdrawn event
     * @param guildId Guild ID
     * @param playerId Player who made the withdrawal
     * @param resourceType Type of resource withdrawn
     * @param amount Amount withdrawn
     * @return A new GuildEvent for resource withdrawal
     */
    public static GuildEvent createResourceWithdrawn(String guildId, String playerId, ResourceType resourceType, int amount) {
        String description = String.format("Player %s withdrew %d %s from the guild vault", playerId, amount, resourceType.getDescription());
        return createNew(guildId, GuildEventType.MEMBER_JOINED, description, playerId); // Using MEMBER_JOINED as a generic event
    }
    
    /**
     * Creates a vault upgraded event
     * @param guildId Guild ID
     * @param additionalCapacity Additional capacity added
     * @return A new GuildEvent for vault upgrade
     */
    public static GuildEvent createVaultUpgraded(String guildId, int additionalCapacity) {
        String description = String.format("Guild vault capacity increased by %d units", additionalCapacity);
        return createNew(guildId, GuildEventType.CREATED, description, null); // Using CREATED as a generic system event
    }
    
    /**
     * Checks if this event involves a specific player
     * @param checkPlayerId Player ID to check
     * @return true if this event involves the player
     */
    public boolean involvesPlayer(String checkPlayerId) {
        return Objects.equals(playerId, checkPlayerId);
    }
    
    /**
     * Checks if this event is recent (within the last 24 hours)
     * @return true if the event occurred within the last 24 hours
     */
    public boolean isRecent() {
        return timestamp.isAfter(LocalDateTime.now().minusHours(24));
    }
    
    /**
     * Gets the age of this event in hours
     * @return The number of hours since the event occurred
     */
    public long getAgeInHours() {
        return java.time.Duration.between(timestamp, LocalDateTime.now()).toHours();
    }
    
    /**
     * Gets a formatted string representation of the event
     * @return Formatted string with event details
     */
    public String getFormattedEvent() {
        String playerInfo = playerId != null ? " (Player: " + playerId + ")" : "";
        return String.format("%s %s - %s%s\n[%s]",
                           eventType.getEmoji(),
                           eventType.getDisplayName(),
                           description,
                           playerInfo,
                           timestamp.toLocalDate());
    }
    
    /**
     * Gets a summary string for display purposes
     * @return Short summary of the event
     */
    public String getSummary() {
        return String.format("%s %s", eventType.getEmoji(), description);
    }
    
    /**
     * Generates a unique event ID
     * @param guildId Guild ID
     * @param eventType Event type
     * @return Generated event ID
     */
    private static String generateEventId(String guildId, GuildEventType eventType) {
        return String.format("%s_%s_%d", 
                           guildId, 
                           eventType.name(), 
                           System.currentTimeMillis());
    }
}
