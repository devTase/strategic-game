package org.hsh.games.aoe.services;

import org.hsh.games.aoe.entities.rebelcell.*;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * Service class for managing shadow territories including listing, conquest initiation, 
 * and updating territory status.
 * Uses thread-safe collections for concurrent access.
 * 
 * @author devTASE
 */
public class ShadowTerritoryService {
    
    private final Map<String, ShadowTerritory> territories;
    private final Map<String, Set<String>> guildTerritories; // GuildId -> Set of territoryIds
    private final GuildService guildService;

    /**
     * Constructor injection for dependencies
     * @param guildService Guild service for guild operations
     */
    public ShadowTerritoryService(GuildService guildService) {
        this.territories = new ConcurrentHashMap<>();
        this.guildTerritories = new ConcurrentHashMap<>();
        this.guildService = Objects.requireNonNull(guildService, "Guild service cannot be null");
    }

    /**
     * Lists all shadow territories
     * @return Set of all shadow territories
     */
    public Set<ShadowTerritory> listTerritories() {
        return Set.copyOf(territories.values());
    }
    
    /**
     * Initiates a conquest for a territory by a guild
     * @param territoryId ID of the territory to conquer
     * @param guildId ID of the guild initiating conquest
     * @param leaderPlayerId ID of the player leading the conquest
     * @return Updated shadow territory with new status
     * @throws IllegalArgumentException if conquest is invalid
     */
    public ShadowTerritory initiateConquest(String territoryId, String guildId, String leaderPlayerId) {
        Objects.requireNonNull(territoryId, "Territory ID cannot be null");
        Objects.requireNonNull(guildId, "Guild ID cannot be null");
        Objects.requireNonNull(leaderPlayerId, "Leader player ID cannot be null");

        ShadowTerritory territory = getTerritoryById(territoryId);
        if (territory == null || !territory.isAvailableForConquest()) {
            throw new IllegalArgumentException("Territory cannot be conquered");
        }

        Guild guild = guildService.getGuildById(guildId);
        if (guild == null) {
            throw new IllegalArgumentException("Guild not found");
        }

        if (!guild.hasMember(leaderPlayerId) || !guild.getLeader().playerId().equals(leaderPlayerId)) {
            throw new IllegalArgumentException("Only guild leaders can initiate a conquest");
        }

        ShadowTerritory updatedTerritory = territory.claimByGuild(guildId);
        territories.put(territoryId, updatedTerritory);
        guildTerritories.computeIfAbsent(guildId, k -> ConcurrentHashMap.newKeySet()).add(territoryId);

        guildService.broadcastEvent(guildId, String.format("Territory %s is now under siege by %s", territoryId, leaderPlayerId));

        return updatedTerritory;
    }

    /**
     * Updates the status of a territory
     * @param territoryId ID of the territory
     * @param status New status to assign
     * @return Updated shadow territory
     * @throws IllegalArgumentException if update is invalid
     */
    public ShadowTerritory updateTerritoryStatus(String territoryId, TerritoryStatus status) {
        Objects.requireNonNull(territoryId, "Territory ID cannot be null");
        Objects.requireNonNull(status, "Territory status cannot be null");

        ShadowTerritory territory = getTerritoryById(territoryId);
        if (territory == null) {
            throw new IllegalArgumentException("Territory not found");
        }

        // Cannot update to neutral if claimed
        if (status == TerritoryStatus.NEUTRAL && territory.status().isControlledByGuild()) {
            throw new IllegalArgumentException("Controlled territories cannot be set to neutral");
        }

        ShadowTerritory updatedTerritory = switch (status) {
            case NEUTRAL -> territory.liberate();
            case UNDER_SIEGE -> territory.putUnderSiege();
            case CLAIMED_BY_GUILD -> territory.claimByGuild(territory.owningGuildId());
            default -> territory.withStatus(status);
        };

        territories.put(territoryId, updatedTerritory);

        return updatedTerritory;
    }

    /**
     * Gets a territory by its ID
     * @param territoryId ID of the territory
     * @return ShadowTerritory instance or null if not found
     */
    public ShadowTerritory getTerritoryById(String territoryId) {
        return territories.get(territoryId);
    }

    /**
     * Generates a unique territory ID
     * @return Generated territory ID
     */
    private String generateTerritoryId() {
        return "territory_" + UUID.randomUUID().toString().substring(0, 8);
    }
}
