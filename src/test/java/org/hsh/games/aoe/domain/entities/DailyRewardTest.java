package org.hsh.games.aoe.domain.entities;

import org.hsh.games.aoe.domain.entities.resources.ResourceAmount;
import org.hsh.games.aoe.domain.entities.resources.ResourceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DailyReward entity
 * 
 * @author devTASE
 */
@DisplayName("DailyReward Entity Tests")
class DailyRewardTest {

    @Test
    @DisplayName("Should return adjusted rewards based on tech phase")
    void shouldReturnAdjustedRewardsBasedOnTechPhase() {
        // Given
        ResourceAmount reward1 = new ResourceAmount(ResourceType.DATA, 100);
        ResourceAmount reward2 = new ResourceAmount(ResourceType.ENERGY, 200);
        List<ResourceAmount> baseRewards = List.of(reward1, reward2);
        DailyReward reward = new DailyReward(1, baseRewards, false, "Regular rewards");

        // When
        List<ResourceAmount> adjustedRewardsUprising = reward.getAdjustedRewards(TechPhase.UPRISING);
        List<ResourceAmount> adjustedRewardsNeuralNexus = reward.getAdjustedRewards(TechPhase.NEURAL_NEXUS);

        // Then
        assertEquals(100, adjustedRewardsUprising.get(0).getAmount());
        assertEquals(200, adjustedRewardsUprising.get(1).getAmount());
        assertEquals(160, adjustedRewardsNeuralNexus.get(0).getAmount());
        assertEquals(320, adjustedRewardsNeuralNexus.get(1).getAmount());
    }

    @Test
    @DisplayName("Should return adjusted rewards with deprecated method (EraAge)")
    void shouldReturnAdjustedRewardsWithDeprecatedMethod() {
        // Given
        ResourceAmount reward1 = new ResourceAmount(ResourceType.COMPONENTS, 50);
        List<ResourceAmount> baseRewards = List.of(reward1);
        DailyReward reward = new DailyReward(5, baseRewards, true, "Special reward");

        TechPhase correspondingPhase = TechPhase.getByLevel(3); // corresponds to NEURAL_NEXUS

        // When
        List<ResourceAmount> adjustedRewardsEra = reward.getAdjustedRewards(correspondingPhase);
        List<ResourceAmount> adjustedRewardsPhase = reward.getAdjustedRewards(correspondingPhase);

        // Then
        assertEquals(adjustedRewardsPhase, adjustedRewardsEra);
    }

    @Test
    @DisplayName("Should correctly format toString")
    void shouldCorrectlyFormatToString() {
        // Given
        DailyReward reward = new DailyReward(3, List.of(), false, "Some description");

        // When
        String description = reward.toString();

        // Then
        assertEquals("Dia 3: Some description", description);
    }
}
