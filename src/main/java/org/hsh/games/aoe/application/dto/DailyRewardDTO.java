package org.hsh.games.aoe.application.dto;

import java.util.List;

/**
 * Data Transfer Object for Daily Rewards
 * 
 * @author devTASE
 */
public record DailyRewardDTO(
    int day,
    List<ResourceDTO> baseRewards,
    List<ResourceDTO> adjustedRewards,
    boolean isSpecialReward,
    String description
) {
    
    public static DailyRewardDTO from(org.hsh.games.aoe.domain.entities.DailyReward dailyReward, 
                                    org.hsh.games.aoe.domain.entities.TechPhase techPhase) {
        List<ResourceDTO> baseRewards = dailyReward.baseRewards().stream()
            .map(ResourceDTO::from)
            .toList();
            
        List<ResourceDTO> adjustedRewards = dailyReward.getAdjustedRewards(techPhase).stream()
            .map(ResourceDTO::from)
            .toList();
        
        return new DailyRewardDTO(
            dailyReward.day(),
            baseRewards,
            adjustedRewards,
            dailyReward.isSpecialReward(),
            dailyReward.description()
        );
    }
    
    @Override
    public String toString() {
        return String.format("Dia %d: %s", day, description);
    }
}
