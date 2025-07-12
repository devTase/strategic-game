package org.hsh.games.aoe.application.ports.outbound;

import org.hsh.games.aoe.domain.entities.resources.ResourceType;
import org.hsh.games.aoe.domain.valueobjects.PlayerId;

/**
 * Guild service port for guild-related operations
 * 
 * @author devTASE
 */
public interface GuildServicePort {
    
    /**
     * Checks if a player is in a guild
     * @param playerId The player to check
     * @return true if player is in a guild
     */
    boolean isPlayerInGuild(PlayerId playerId);
    
    /**
     * Deposits resources to player's guild vault
     * @param playerId The player making the deposit
     * @param resourceType The type of resource
     * @param amount The amount to deposit
     * @throws IllegalArgumentException if player not in guild
     * @throws IllegalStateException if deposit fails
     */
    void depositToGuildVault(PlayerId playerId, ResourceType resourceType, int amount);
    
    /**
     * Gets the guild ID for a player
     * @param playerId The player to check
     * @return Guild ID or null if not in guild
     */
    String getPlayerGuildId(PlayerId playerId);
}
