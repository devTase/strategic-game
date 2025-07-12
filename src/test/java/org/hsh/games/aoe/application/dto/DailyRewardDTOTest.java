package org.hsh.games.aoe.application.dto;

import org.hsh.games.aoe.domain.entities.DailyReward;
import org.hsh.games.aoe.domain.entities.TechPhase;
import org.hsh.games.aoe.domain.entities.resources.ResourceAmount;
import org.hsh.games.aoe.domain.entities.resources.ResourceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DailyRewardDTO
 * 
 * @author devTASE
 */
@DisplayName("DailyRewardDTO Tests")
class DailyRewardDTOTest {

    @Test
    @DisplayName("Should create DailyRewardDTO from DailyReward")
    void shouldCreateDailyRewardDTOFromDailyReward() {
        // Given
        List<ResourceAmount> baseRewards = List.of(
            new ResourceAmount(ResourceType.ENERGY, 50),
            new ResourceAmount(ResourceType.DATA, 30)
        );
        DailyReward dailyReward = new DailyReward(1, baseRewards, false, "Basic rewards");
        TechPhase techPhase = TechPhase.UPRISING;

        // When
        DailyRewardDTO dto = DailyRewardDTO.from(dailyReward, techPhase);

        // Then
        assertEquals(1, dto.day());
        assertEquals("Basic rewards", dto.description());
        assertFalse(dto.isSpecialReward());
        assertEquals(2, dto.baseRewards().size());
        assertEquals(2, dto.adjustedRewards().size());
        
        // Check base rewards
        assertEquals(ResourceType.ENERGY, dto.baseRewards().get(0).type());
        assertEquals(50, dto.baseRewards().get(0).amount());
        
        // Check adjusted rewards (should be same for UPRISING phase with multiplier 1.0)
        assertEquals(ResourceType.ENERGY, dto.adjustedRewards().get(0).type());
        assertEquals(50, dto.adjustedRewards().get(0).amount());
    }

    @Test
    @DisplayName("Should format toString correctly")
    void shouldFormatToStringCorrectly() {
        // Given
        List<ResourceAmount> baseRewards = List.of(
            new ResourceAmount(ResourceType.ENERGY, 100)
        );
        DailyReward dailyReward = new DailyReward(7, baseRewards, true, "Special weekly reward");
        TechPhase techPhase = TechPhase.UPRISING;

        // When
        DailyRewardDTO dto = DailyRewardDTO.from(dailyReward, techPhase);

        // Then
        assertEquals("Dia 7: Special weekly reward", dto.toString());
    }

    @Test
    @DisplayName("Should handle special rewards correctly")
    void shouldHandleSpecialRewardsCorrectly() {
        // Given
        List<ResourceAmount> baseRewards = List.of(
            new ResourceAmount(ResourceType.QUANTUM_ENERGY, 10)
        );
        DailyReward dailyReward = new DailyReward(30, baseRewards, true, "Monthly treasure");
        TechPhase techPhase = TechPhase.QUANTUM_DAWN;

        // When
        DailyRewardDTO dto = DailyRewardDTO.from(dailyReward, techPhase);

        // Then
        assertEquals(30, dto.day());
        assertTrue(dto.isSpecialReward());
        assertEquals("Monthly treasure", dto.description());
        assertEquals(1, dto.baseRewards().size());
        
        // Check that tech phase multiplier is applied to adjusted rewards
        int adjustedAmount = dto.adjustedRewards().get(0).amount();
        assertTrue(adjustedAmount >= 10); // Should be at least base amount
    }
}
