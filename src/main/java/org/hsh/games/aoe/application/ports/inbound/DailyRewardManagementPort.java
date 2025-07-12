package org.hsh.games.aoe.application.ports.inbound;

import org.hsh.games.aoe.application.dto.DailyRewardDTO;
import org.hsh.games.aoe.application.dto.ResourceDTO;
import org.hsh.games.aoe.domain.valueobjects.PlayerId;

import java.util.List;

/**
 * Port for managing daily rewards in hexagonal architecture
 * 
 * @author devTASE
 */
public interface DailyRewardManagementPort {
    
    /**
     * Claims the daily reward for a player
     * @param playerId The player claiming the reward
     * @return List of resources awarded to the player
     * @throws IllegalStateException if already claimed today
     */
    List<ResourceDTO> claimDailyReward(PlayerId playerId);
    
    /**
     * Claims daily reward with option to deposit to guild vault
     * @param playerId The player claiming the reward
     * @param depositToGuild true to deposit to guild vault, false for player inventory
     * @return List of resources (empty if deposited to guild)
     * @throws IllegalStateException if already claimed today
     * @throws IllegalArgumentException if player not in guild when trying to deposit
     */
    List<ResourceDTO> claimDailyRewardWithGuildOption(PlayerId playerId, boolean depositToGuild);
    
    /**
     * Checks if player has already claimed today's reward
     * @param playerId The player to check
     * @return true if already claimed today
     */
    boolean hasClaimedToday(PlayerId playerId);
    
    /**
     * Gets the current daily reward streak for a player
     * @param playerId The player to check
     * @return Current streak count
     */
    int getCurrentStreak(PlayerId playerId);
    
    /**
     * Gets the next reward that will be available to claim
     * @param playerId The player to check
     * @return The next daily reward info
     */
    DailyRewardDTO getNextReward(PlayerId playerId);
    
    /**
     * Checks if guild vault deposit is available
     * @return true if guild service is configured and available
     */
    boolean isGuildVaultDepositAvailable();
}
