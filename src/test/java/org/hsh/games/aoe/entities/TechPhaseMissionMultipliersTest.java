package org.hsh.games.aoe.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TechPhase mission generation multipliers
 * 
 * @author devTASE
 */
public class TechPhaseMissionMultipliersTest {

    @Test
    public void testMissionGenerationMultipliers() {
        // When/Then - Verify progression of mission generation multipliers
        assertEquals(1.0, TechPhase.UPRISING.getMissionGenerationMultiplier(), 0.001);
        assertEquals(1.3, TechPhase.AUGMENTED_STREETS.getMissionGenerationMultiplier(), 0.001);
        assertEquals(1.7, TechPhase.NEURAL_NEXUS.getMissionGenerationMultiplier(), 0.001);
        assertEquals(2.2, TechPhase.DRONE_DOMINION.getMissionGenerationMultiplier(), 0.001);
        assertEquals(2.8, TechPhase.QUANTUM_DAWN.getMissionGenerationMultiplier(), 0.001);
        assertEquals(3.5, TechPhase.SINGULARITY_PREP.getMissionGenerationMultiplier(), 0.001);
        assertEquals(4.3, TechPhase.POST_SINGULARITY.getMissionGenerationMultiplier(), 0.001);
        assertEquals(5.2, TechPhase.HYPER_MESH.getMissionGenerationMultiplier(), 0.001);
        assertEquals(6.4, TechPhase.EXO_REALITY.getMissionGenerationMultiplier(), 0.001);
    }

    @Test
    public void testMissionDifficultyModifiers() {
        // When/Then - Verify progression of mission difficulty modifiers
        assertEquals(0.7, TechPhase.UPRISING.getMissionDifficultyModifier(), 0.001);
        assertEquals(0.85, TechPhase.AUGMENTED_STREETS.getMissionDifficultyModifier(), 0.001);
        assertEquals(1.0, TechPhase.NEURAL_NEXUS.getMissionDifficultyModifier(), 0.001);
        assertEquals(1.15, TechPhase.DRONE_DOMINION.getMissionDifficultyModifier(), 0.001);
        assertEquals(1.3, TechPhase.QUANTUM_DAWN.getMissionDifficultyModifier(), 0.001);
        assertEquals(1.5, TechPhase.SINGULARITY_PREP.getMissionDifficultyModifier(), 0.001);
        assertEquals(1.7, TechPhase.POST_SINGULARITY.getMissionDifficultyModifier(), 0.001);
        assertEquals(1.9, TechPhase.HYPER_MESH.getMissionDifficultyModifier(), 0.001);
        assertEquals(2.2, TechPhase.EXO_REALITY.getMissionDifficultyModifier(), 0.001);
    }

    @Test
    public void testMissionDurationModifiers() {
        // When/Then - Verify progression of mission duration modifiers
        assertEquals(0.6, TechPhase.UPRISING.getMissionDurationModifier(), 0.001);
        assertEquals(0.75, TechPhase.AUGMENTED_STREETS.getMissionDurationModifier(), 0.001);
        assertEquals(1.0, TechPhase.NEURAL_NEXUS.getMissionDurationModifier(), 0.001);
        assertEquals(1.2, TechPhase.DRONE_DOMINION.getMissionDurationModifier(), 0.001);
        assertEquals(1.4, TechPhase.QUANTUM_DAWN.getMissionDurationModifier(), 0.001);
        assertEquals(1.6, TechPhase.SINGULARITY_PREP.getMissionDurationModifier(), 0.001);
        assertEquals(1.8, TechPhase.POST_SINGULARITY.getMissionDurationModifier(), 0.001);
        assertEquals(2.1, TechPhase.HYPER_MESH.getMissionDurationModifier(), 0.001);
        assertEquals(2.5, TechPhase.EXO_REALITY.getMissionDurationModifier(), 0.001);
    }

    @Test
    public void testMultiplierProgression() {
        // Given - All tech phases
        TechPhase[] phases = TechPhase.values();
        
        // When/Then - Verify that generation multipliers increase with phase level
        for (int i = 1; i < phases.length; i++) {
            assertTrue(phases[i].getMissionGenerationMultiplier() > phases[i-1].getMissionGenerationMultiplier(),
                "Mission generation multiplier should increase with phase progression");
        }
        
        // When/Then - Verify that difficulty modifiers increase with phase level (except first phase)
        for (int i = 2; i < phases.length; i++) {
            assertTrue(phases[i].getMissionDifficultyModifier() > phases[i-1].getMissionDifficultyModifier(),
                "Mission difficulty modifier should increase with phase progression");
        }
        
        // When/Then - Verify that duration modifiers generally increase with phase level
        for (int i = 2; i < phases.length; i++) {
            assertTrue(phases[i].getMissionDurationModifier() >= phases[i-1].getMissionDurationModifier(),
                "Mission duration modifier should increase or stay same with phase progression");
        }
    }

    @Test
    public void testMultiplierRanges() {
        // When/Then - All multipliers should be positive and reasonable
        for (TechPhase phase : TechPhase.values()) {
            assertTrue(phase.getMissionGenerationMultiplier() > 0, 
                "Mission generation multiplier should be positive for " + phase);
            assertTrue(phase.getMissionGenerationMultiplier() <= 10.0, 
                "Mission generation multiplier should be reasonable for " + phase);
            
            assertTrue(phase.getMissionDifficultyModifier() > 0, 
                "Mission difficulty modifier should be positive for " + phase);
            assertTrue(phase.getMissionDifficultyModifier() <= 3.0, 
                "Mission difficulty modifier should be reasonable for " + phase);
            
            assertTrue(phase.getMissionDurationModifier() > 0, 
                "Mission duration modifier should be positive for " + phase);
            assertTrue(phase.getMissionDurationModifier() <= 3.0, 
                "Mission duration modifier should be reasonable for " + phase);
        }
    }

    @Test
    public void testNeuralNexusAsBaseline() {
        // When/Then - Neural Nexus should be the baseline (1.0) for difficulty and duration
        assertEquals(1.0, TechPhase.NEURAL_NEXUS.getMissionDifficultyModifier(), 0.001);
        assertEquals(1.0, TechPhase.NEURAL_NEXUS.getMissionDurationModifier(), 0.001);
        
        // When/Then - Earlier phases should have easier difficulty
        assertTrue(TechPhase.UPRISING.getMissionDifficultyModifier() < 1.0);
        assertTrue(TechPhase.AUGMENTED_STREETS.getMissionDifficultyModifier() < 1.0);
        
        // When/Then - Later phases should have harder difficulty
        assertTrue(TechPhase.DRONE_DOMINION.getMissionDifficultyModifier() > 1.0);
        assertTrue(TechPhase.EXO_REALITY.getMissionDifficultyModifier() > 1.0);
    }

    @Test
    public void testPhaseNames() {
        // When/Then - Verify all phase names are correctly set
        assertEquals("Uprising", TechPhase.UPRISING.getPhaseName());
        assertEquals("Augmented Streets", TechPhase.AUGMENTED_STREETS.getPhaseName());
        assertEquals("Neural Nexus", TechPhase.NEURAL_NEXUS.getPhaseName());
        assertEquals("Drone Dominion", TechPhase.DRONE_DOMINION.getPhaseName());
        assertEquals("Quantum Dawn", TechPhase.QUANTUM_DAWN.getPhaseName());
        assertEquals("Singularity Prep", TechPhase.SINGULARITY_PREP.getPhaseName());
        assertEquals("Post-Singularity", TechPhase.POST_SINGULARITY.getPhaseName());
        assertEquals("Hyper-Mesh", TechPhase.HYPER_MESH.getPhaseName());
        assertEquals("Exo-Reality", TechPhase.EXO_REALITY.getPhaseName());
    }

    @Test
    public void testPhaseLevels() {
        // When/Then - Verify all phase levels are correctly set from 1 to 9
        assertEquals(1, TechPhase.UPRISING.getLevel());
        assertEquals(2, TechPhase.AUGMENTED_STREETS.getLevel());
        assertEquals(3, TechPhase.NEURAL_NEXUS.getLevel());
        assertEquals(4, TechPhase.DRONE_DOMINION.getLevel());
        assertEquals(5, TechPhase.QUANTUM_DAWN.getLevel());
        assertEquals(6, TechPhase.SINGULARITY_PREP.getLevel());
        assertEquals(7, TechPhase.POST_SINGULARITY.getLevel());
        assertEquals(8, TechPhase.HYPER_MESH.getLevel());
        assertEquals(9, TechPhase.EXO_REALITY.getLevel());
    }

    @Test
    public void testGetByLevel() {
        // When/Then - Test level lookup functionality
        assertEquals(TechPhase.UPRISING, TechPhase.getByLevel(1));
        assertEquals(TechPhase.NEURAL_NEXUS, TechPhase.getByLevel(3));
        assertEquals(TechPhase.EXO_REALITY, TechPhase.getByLevel(9));
        
        // When/Then - Test invalid level throws exception
        assertThrows(IllegalArgumentException.class, () -> TechPhase.getByLevel(0));
        assertThrows(IllegalArgumentException.class, () -> TechPhase.getByLevel(10));
    }

    @Test
    public void testGetNextPhase() {
        // When/Then - Test phase progression
        assertEquals(TechPhase.AUGMENTED_STREETS, TechPhase.UPRISING.getNextPhase());
        assertEquals(TechPhase.NEURAL_NEXUS, TechPhase.AUGMENTED_STREETS.getNextPhase());
        assertEquals(TechPhase.DRONE_DOMINION, TechPhase.NEURAL_NEXUS.getNextPhase());
        assertEquals(TechPhase.QUANTUM_DAWN, TechPhase.DRONE_DOMINION.getNextPhase());
        assertEquals(TechPhase.SINGULARITY_PREP, TechPhase.QUANTUM_DAWN.getNextPhase());
        assertEquals(TechPhase.POST_SINGULARITY, TechPhase.SINGULARITY_PREP.getNextPhase());
        assertEquals(TechPhase.HYPER_MESH, TechPhase.POST_SINGULARITY.getNextPhase());
        assertEquals(TechPhase.EXO_REALITY, TechPhase.HYPER_MESH.getNextPhase());
        
        // When/Then - Last phase should return itself (no next phase)
        assertEquals(TechPhase.EXO_REALITY, TechPhase.EXO_REALITY.getNextPhase());
    }

    @Test
    public void testRequirementsForNextLevel() {
        // When/Then - Verify requirements are properly set for each phase
        for (TechPhase phase : TechPhase.values()) {
            assertNotNull(phase.getRequirementsForNextLevel(), 
                "Requirements should not be null for " + phase);
            assertFalse(phase.getRequirementsForNextLevel().isEmpty(), 
                "Requirements should not be empty for " + phase);
        }
    }
}
