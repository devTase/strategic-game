package org.hsh.games.aoe.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EraAge mission generation multipliers
 * 
 * @author devTASE
 */
public class EraAgeMissionMultipliersTest {

    @Test
    public void testMissionGenerationMultipliers() {
        // When/Then - Verify progression of mission generation multipliers
        assertEquals(1.0, EraAge.STONE_AGE.getMissionGenerationMultiplier(), 0.001);
        assertEquals(1.2, EraAge.BRONZE_AGE.getMissionGenerationMultiplier(), 0.001);
        assertEquals(1.5, EraAge.IRON_AGE.getMissionGenerationMultiplier(), 0.001);
        assertEquals(1.8, EraAge.MEDIEVAL_AGE.getMissionGenerationMultiplier(), 0.001);
        assertEquals(2.1, EraAge.RENAISSANCE.getMissionGenerationMultiplier(), 0.001);
        assertEquals(2.5, EraAge.INDUSTRIAL_AGE.getMissionGenerationMultiplier(), 0.001);
        assertEquals(3.0, EraAge.MODERN_AGE.getMissionGenerationMultiplier(), 0.001);
        assertEquals(3.5, EraAge.INFORMATION_AGE.getMissionGenerationMultiplier(), 0.001);
        assertEquals(4.0, EraAge.FUTURE_AGE.getMissionGenerationMultiplier(), 0.001);
    }

    @Test
    public void testMissionDifficultyModifiers() {
        // When/Then - Verify progression of mission difficulty modifiers
        assertEquals(0.8, EraAge.STONE_AGE.getMissionDifficultyModifier(), 0.001);
        assertEquals(0.9, EraAge.BRONZE_AGE.getMissionDifficultyModifier(), 0.001);
        assertEquals(1.0, EraAge.IRON_AGE.getMissionDifficultyModifier(), 0.001);
        assertEquals(1.1, EraAge.MEDIEVAL_AGE.getMissionDifficultyModifier(), 0.001);
        assertEquals(1.2, EraAge.RENAISSANCE.getMissionDifficultyModifier(), 0.001);
        assertEquals(1.3, EraAge.INDUSTRIAL_AGE.getMissionDifficultyModifier(), 0.001);
        assertEquals(1.4, EraAge.MODERN_AGE.getMissionDifficultyModifier(), 0.001);
        assertEquals(1.5, EraAge.INFORMATION_AGE.getMissionDifficultyModifier(), 0.001);
        assertEquals(1.6, EraAge.FUTURE_AGE.getMissionDifficultyModifier(), 0.001);
    }

    @Test
    public void testMissionDurationModifiers() {
        // When/Then - Verify progression of mission duration modifiers
        assertEquals(0.7, EraAge.STONE_AGE.getMissionDurationModifier(), 0.001);
        assertEquals(0.8, EraAge.BRONZE_AGE.getMissionDurationModifier(), 0.001);
        assertEquals(1.0, EraAge.IRON_AGE.getMissionDurationModifier(), 0.001);
        assertEquals(1.1, EraAge.MEDIEVAL_AGE.getMissionDurationModifier(), 0.001);
        assertEquals(1.2, EraAge.RENAISSANCE.getMissionDurationModifier(), 0.001);
        assertEquals(1.3, EraAge.INDUSTRIAL_AGE.getMissionDurationModifier(), 0.001);
        assertEquals(1.4, EraAge.MODERN_AGE.getMissionDurationModifier(), 0.001);
        assertEquals(1.5, EraAge.INFORMATION_AGE.getMissionDurationModifier(), 0.001);
        assertEquals(1.7, EraAge.FUTURE_AGE.getMissionDurationModifier(), 0.001);
    }

    @Test
    public void testMultiplierProgression() {
        // Given - All eras
        EraAge[] eras = EraAge.values();
        
        // When/Then - Verify that generation multipliers increase with era level
        for (int i = 1; i < eras.length; i++) {
            assertTrue(eras[i].getMissionGenerationMultiplier() > eras[i-1].getMissionGenerationMultiplier(),
                "Mission generation multiplier should increase with era progression");
        }
        
        // When/Then - Verify that difficulty modifiers increase with era level (except first era)
        for (int i = 2; i < eras.length; i++) {
            assertTrue(eras[i].getMissionDifficultyModifier() > eras[i-1].getMissionDifficultyModifier(),
                "Mission difficulty modifier should increase with era progression");
        }
        
        // When/Then - Verify that duration modifiers generally increase with era level
        for (int i = 2; i < eras.length; i++) {
            assertTrue(eras[i].getMissionDurationModifier() >= eras[i-1].getMissionDurationModifier(),
                "Mission duration modifier should increase or stay same with era progression");
        }
    }

    @Test
    public void testMultiplierRanges() {
        // When/Then - All multipliers should be positive and reasonable
        for (EraAge era : EraAge.values()) {
            assertTrue(era.getMissionGenerationMultiplier() > 0, 
                "Mission generation multiplier should be positive for " + era);
            assertTrue(era.getMissionGenerationMultiplier() <= 5.0, 
                "Mission generation multiplier should be reasonable for " + era);
            
            assertTrue(era.getMissionDifficultyModifier() > 0, 
                "Mission difficulty modifier should be positive for " + era);
            assertTrue(era.getMissionDifficultyModifier() <= 2.0, 
                "Mission difficulty modifier should be reasonable for " + era);
            
            assertTrue(era.getMissionDurationModifier() > 0, 
                "Mission duration modifier should be positive for " + era);
            assertTrue(era.getMissionDurationModifier() <= 2.0, 
                "Mission duration modifier should be reasonable for " + era);
        }
    }

    @Test
    public void testIronAgeAsBaseline() {
        // When/Then - Iron Age should be the baseline (1.0) for difficulty
        assertEquals(1.0, EraAge.IRON_AGE.getMissionDifficultyModifier(), 0.001);
        assertEquals(1.0, EraAge.IRON_AGE.getMissionDurationModifier(), 0.001);
        
        // When/Then - Earlier eras should have easier difficulty
        assertTrue(EraAge.STONE_AGE.getMissionDifficultyModifier() < 1.0);
        assertTrue(EraAge.BRONZE_AGE.getMissionDifficultyModifier() < 1.0);
        
        // When/Then - Later eras should have harder difficulty
        assertTrue(EraAge.MEDIEVAL_AGE.getMissionDifficultyModifier() > 1.0);
        assertTrue(EraAge.FUTURE_AGE.getMissionDifficultyModifier() > 1.0);
    }
}
