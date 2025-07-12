package org.hsh.games.aoe.domain.entities;

import org.hsh.games.aoe.domain.entities.buildings.ConstructionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TechPhase entity
 * 
 * @author devTASE
 */
@DisplayName("TechPhase Entity Tests")
class TechPhaseTest {

    @Test
    @DisplayName("Should have all tech phases with correct levels")
    void shouldHaveAllTechPhasesWithCorrectLevels() {
        // Given & When & Then
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
    @DisplayName("Should have correct phase names")
    void shouldHaveCorrectPhaseNames() {
        // Given & When & Then
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
    @DisplayName("Should get tech phase by level")
    void shouldGetTechPhaseByLevel() {
        // Given & When & Then
        assertEquals(TechPhase.UPRISING, TechPhase.getByLevel(1));
        assertEquals(TechPhase.AUGMENTED_STREETS, TechPhase.getByLevel(2));
        assertEquals(TechPhase.NEURAL_NEXUS, TechPhase.getByLevel(3));
        assertEquals(TechPhase.DRONE_DOMINION, TechPhase.getByLevel(4));
        assertEquals(TechPhase.QUANTUM_DAWN, TechPhase.getByLevel(5));
        assertEquals(TechPhase.SINGULARITY_PREP, TechPhase.getByLevel(6));
        assertEquals(TechPhase.POST_SINGULARITY, TechPhase.getByLevel(7));
        assertEquals(TechPhase.HYPER_MESH, TechPhase.getByLevel(8));
        assertEquals(TechPhase.EXO_REALITY, TechPhase.getByLevel(9));
    }

    @Test
    @DisplayName("Should throw exception for invalid level")
    void shouldThrowExceptionForInvalidLevel() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> TechPhase.getByLevel(0));
        assertThrows(IllegalArgumentException.class, () -> TechPhase.getByLevel(10));
        assertThrows(IllegalArgumentException.class, () -> TechPhase.getByLevel(-1));
    }

    @Test
    @DisplayName("Should get maximum level")
    void shouldGetMaximumLevel() {
        // When
        int maxLevel = TechPhase.getMaxLevel();

        // Then
        assertEquals(9, maxLevel);
    }

    @Test
    @DisplayName("Should get next phase correctly")
    void shouldGetNextPhaseCorrectly() {
        // Given & When & Then
        assertEquals(TechPhase.AUGMENTED_STREETS, TechPhase.UPRISING.getNextPhase());
        assertEquals(TechPhase.NEURAL_NEXUS, TechPhase.AUGMENTED_STREETS.getNextPhase());
        assertEquals(TechPhase.DRONE_DOMINION, TechPhase.NEURAL_NEXUS.getNextPhase());
        assertEquals(TechPhase.QUANTUM_DAWN, TechPhase.DRONE_DOMINION.getNextPhase());
        assertEquals(TechPhase.SINGULARITY_PREP, TechPhase.QUANTUM_DAWN.getNextPhase());
        assertEquals(TechPhase.POST_SINGULARITY, TechPhase.SINGULARITY_PREP.getNextPhase());
        assertEquals(TechPhase.HYPER_MESH, TechPhase.POST_SINGULARITY.getNextPhase());
        assertEquals(TechPhase.EXO_REALITY, TechPhase.HYPER_MESH.getNextPhase());
    }

    @Test
    @DisplayName("Should return same phase when already at maximum level")
    void shouldReturnSamePhaseWhenAlreadyAtMaximumLevel() {
        // Given
        TechPhase maxPhase = TechPhase.EXO_REALITY;

        // When
        TechPhase nextPhase = maxPhase.getNextPhase();

        // Then
        assertEquals(maxPhase, nextPhase);
    }

    @Test
    @DisplayName("Should have base requirements for all phases")
    void shouldHaveBaseRequirementsForAllPhases() {
        // Given
        TechPhase phase = TechPhase.UPRISING;

        // When
        Map<ConstructionType, Integer> requirements = phase.getRequirementsForNextLevel();

        // Then
        assertNotNull(requirements);
        assertTrue(requirements.containsKey(ConstructionType.COMMAND_CENTER));
        assertTrue(requirements.containsKey(ConstructionType.OP_BASE));
        assertEquals(1, requirements.get(ConstructionType.COMMAND_CENTER));
        assertEquals(1, requirements.get(ConstructionType.OP_BASE));
    }

    @Test
    @DisplayName("Should have progressive requirements for advanced phases")
    void shouldHaveProgressiveRequirementsForAdvancedPhases() {
        // Given
        TechPhase phase = TechPhase.EXO_REALITY;

        // When
        Map<ConstructionType, Integer> requirements = phase.getRequirementsForNextLevel();

        // Then
        assertNotNull(requirements);
        assertTrue(requirements.containsKey(ConstructionType.RESOURCE_DEPOT));
        assertTrue(requirements.containsKey(ConstructionType.PROCESSING_PLANT));
        assertTrue(requirements.containsKey(ConstructionType.TRAINING_FACILITY));
        assertTrue(requirements.containsKey(ConstructionType.WEAPONS_RANGE));
        assertTrue(requirements.containsKey(ConstructionType.TECH_FOUNDRY));
        assertTrue(requirements.containsKey(ConstructionType.COMM_RELAY));
        assertTrue(requirements.containsKey(ConstructionType.RESEARCH_LAB));
        assertTrue(requirements.containsKey(ConstructionType.NEURAL_NEXUS));
        
        // All should have level 9 requirements
        assertEquals(9, requirements.get(ConstructionType.COMMAND_CENTER));
        assertEquals(9, requirements.get(ConstructionType.OP_BASE));
        assertEquals(9, requirements.get(ConstructionType.RESOURCE_DEPOT));
        assertEquals(9, requirements.get(ConstructionType.PROCESSING_PLANT));
        assertEquals(9, requirements.get(ConstructionType.TRAINING_FACILITY));
        assertEquals(9, requirements.get(ConstructionType.WEAPONS_RANGE));
        assertEquals(9, requirements.get(ConstructionType.TECH_FOUNDRY));
        assertEquals(9, requirements.get(ConstructionType.COMM_RELAY));
        assertEquals(9, requirements.get(ConstructionType.RESEARCH_LAB));
        assertEquals(9, requirements.get(ConstructionType.NEURAL_NEXUS));
    }

    @Test
    @DisplayName("Should have correct mission generation multipliers")
    void shouldHaveCorrectMissionGenerationMultipliers() {
        // Given & When & Then
        assertEquals(1.0, TechPhase.UPRISING.getMissionGenerationMultiplier());
        assertEquals(1.3, TechPhase.AUGMENTED_STREETS.getMissionGenerationMultiplier());
        assertEquals(1.7, TechPhase.NEURAL_NEXUS.getMissionGenerationMultiplier());
        assertEquals(2.2, TechPhase.DRONE_DOMINION.getMissionGenerationMultiplier());
        assertEquals(2.8, TechPhase.QUANTUM_DAWN.getMissionGenerationMultiplier());
        assertEquals(3.5, TechPhase.SINGULARITY_PREP.getMissionGenerationMultiplier());
        assertEquals(4.3, TechPhase.POST_SINGULARITY.getMissionGenerationMultiplier());
        assertEquals(5.2, TechPhase.HYPER_MESH.getMissionGenerationMultiplier());
        assertEquals(6.4, TechPhase.EXO_REALITY.getMissionGenerationMultiplier());
    }

    @Test
    @DisplayName("Should have correct mission difficulty modifiers")
    void shouldHaveCorrectMissionDifficultyModifiers() {
        // Given & When & Then
        assertEquals(0.7, TechPhase.UPRISING.getMissionDifficultyModifier());
        assertEquals(0.85, TechPhase.AUGMENTED_STREETS.getMissionDifficultyModifier());
        assertEquals(1.0, TechPhase.NEURAL_NEXUS.getMissionDifficultyModifier());
        assertEquals(1.15, TechPhase.DRONE_DOMINION.getMissionDifficultyModifier());
        assertEquals(1.3, TechPhase.QUANTUM_DAWN.getMissionDifficultyModifier());
        assertEquals(1.5, TechPhase.SINGULARITY_PREP.getMissionDifficultyModifier());
        assertEquals(1.7, TechPhase.POST_SINGULARITY.getMissionDifficultyModifier());
        assertEquals(1.9, TechPhase.HYPER_MESH.getMissionDifficultyModifier());
        assertEquals(2.2, TechPhase.EXO_REALITY.getMissionDifficultyModifier());
    }

    @Test
    @DisplayName("Should have correct mission duration modifiers")
    void shouldHaveCorrectMissionDurationModifiers() {
        // Given & When & Then
        assertEquals(0.6, TechPhase.UPRISING.getMissionDurationModifier());
        assertEquals(0.75, TechPhase.AUGMENTED_STREETS.getMissionDurationModifier());
        assertEquals(1.0, TechPhase.NEURAL_NEXUS.getMissionDurationModifier());
        assertEquals(1.2, TechPhase.DRONE_DOMINION.getMissionDurationModifier());
        assertEquals(1.4, TechPhase.QUANTUM_DAWN.getMissionDurationModifier());
        assertEquals(1.6, TechPhase.SINGULARITY_PREP.getMissionDurationModifier());
        assertEquals(1.8, TechPhase.POST_SINGULARITY.getMissionDurationModifier());
        assertEquals(2.1, TechPhase.HYPER_MESH.getMissionDurationModifier());
        assertEquals(2.5, TechPhase.EXO_REALITY.getMissionDurationModifier());
    }

    @Test
    @DisplayName("Should have increasing multipliers for advanced phases")
    void shouldHaveIncreasingMultipliersForAdvancedPhases() {
        // Given
        TechPhase[] phases = TechPhase.values();

        // When & Then
        for (int i = 1; i < phases.length; i++) {
            TechPhase current = phases[i];
            TechPhase previous = phases[i - 1];
            
            // Mission generation multiplier should increase
            assertTrue(current.getMissionGenerationMultiplier() > previous.getMissionGenerationMultiplier());
            
            // Mission difficulty modifier should increase
            assertTrue(current.getMissionDifficultyModifier() > previous.getMissionDifficultyModifier());
            
            // Mission duration modifier should increase
            assertTrue(current.getMissionDurationModifier() > previous.getMissionDurationModifier());
        }
    }

    @Test
    @DisplayName("Should have requirements that scale with level")
    void shouldHaveRequirementsThatScaleWithLevel() {
        // Given
        TechPhase level3Phase = TechPhase.NEURAL_NEXUS;
        TechPhase level5Phase = TechPhase.QUANTUM_DAWN;

        // When
        Map<ConstructionType, Integer> level3Requirements = level3Phase.getRequirementsForNextLevel();
        Map<ConstructionType, Integer> level5Requirements = level5Phase.getRequirementsForNextLevel();

        // Then
        assertTrue(level3Requirements.containsKey(ConstructionType.PROCESSING_PLANT));
        assertTrue(level5Requirements.containsKey(ConstructionType.WEAPONS_RANGE));
        
        // Level 5 should have more requirements than level 3
        assertTrue(level5Requirements.size() > level3Requirements.size());
    }

    @Test
    @DisplayName("Should have unique requirements for each phase")
    void shouldHaveUniqueRequirementsForEachPhase() {
        // Given
        TechPhase[] phases = TechPhase.values();

        // When & Then
        for (TechPhase phase : phases) {
            Map<ConstructionType, Integer> requirements = phase.getRequirementsForNextLevel();
            
            assertNotNull(requirements);
            assertFalse(requirements.isEmpty());
            
            // All requirement values should match the phase level
            for (Integer requirement : requirements.values()) {
                assertEquals(phase.getLevel(), requirement);
            }
        }
    }
}
