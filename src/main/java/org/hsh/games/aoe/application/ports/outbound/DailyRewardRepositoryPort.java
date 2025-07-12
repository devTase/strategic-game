package org.hsh.games.aoe.application.ports.outbound;

import org.hsh.games.aoe.domain.entities.DailyReward;
import org.hsh.games.aoe.domain.valueobjects.PlayerId;

import java.util.Optional;

/**
 * Repository port for daily reward data operations
 * 
 * @author devTASE
 */
public interface DailyRewardRepositoryPort {
    
    /**
     * Saves or updates player streak information
     * @param playerId The player ID
     * @param streak Current streak count
     * @param lastClaimDate Date of last claim
     */
    void savePlayerStreak(PlayerId playerId, int streak, String lastClaimDate);
    
    /**
     * Gets the current streak for a player
     * @param playerId The player ID
     * @return Current streak count, 0 if no previous claims
     */
    int getPlayerStreak(PlayerId playerId);
    
    /**
     * Gets the last claim date for a player
     * @param playerId The player ID
     * @return Last claim date string, empty if never claimed
     */
    String getLastClaimDate(PlayerId playerId);
    
    /**
     * Gets a daily reward by day number
     * @param day The day number (1-30)
     * @return The daily reward for that day
     */
    Optional<DailyReward> getDailyRewardByDay(int day);
    
    /**
     * Gets all available daily rewards
     * @return List of all daily rewards
     */
    java.util.List<DailyReward> getAllDailyRewards();
}
