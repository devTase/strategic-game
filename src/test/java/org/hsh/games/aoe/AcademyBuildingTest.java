package org.hsh.games.aoe;

import org.hsh.games.aoe.entities.*;
import org.hsh.games.aoe.services.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Training Facility (Academy) Building Tests")
public class AcademyBuildingTest {

    private PlayerService playerService;
    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player("devTASE");
        playerService = new PlayerService(player);
    }

    @Test
    @DisplayName("Should verify Training Facility construction type exists with correct properties")
    public void testTrainingFacilityConstructionTypeExists() {
        // Test that TRAINING_FACILITY enum exists and has correct properties (equivalent to old ACADEMY)
        ConstructionType trainingFacility = ConstructionType.TRAINING_FACILITY;
        
        assertNotNull(trainingFacility);
        assertEquals("Training Facility", trainingFacility.getName());
        assertEquals(5, trainingFacility.getMaxLevel());
        assertEquals(2, trainingFacility.getAmountConstructionsAllowed());
        // For cyber-themed academy, we expect costs for training facilities
        assertNotNull(trainingFacility.getBaseResourcesCost());
        assertNotNull(trainingFacility.getBaseResourcesProduction());
        assertNotNull(trainingFacility.getBaseProductionTime());
    }

    @Test
    @DisplayName("Should verify Training Facility is available in Phase 2 (not Phase 1)")
    public void testTrainingFacilityAvailableInPhase2() {
        // Training Facility is available in Phase 2 (AUGMENTED_STREETS), not Phase 1
        assertEquals(TechPhase.UPRISING, player.getTechPhase());
        
        // In Phase 1, Training Facility should NOT be available
        List<Building> phase1Buildings = playerService.getBuildingList();
        boolean hasTrainingFacilityInPhase1 = phase1Buildings.stream()
                .anyMatch(building -> building.getConstructionTypeName().equals("Training Facility"));
        
        assertFalse(hasTrainingFacilityInPhase1, "Training Facility should NOT be available in Phase 1");
        
        // Advance to Phase 2 and check availability
        playerService.setLevel(2);
        assertEquals(TechPhase.AUGMENTED_STREETS, player.getTechPhase());
        
        List<Building> phase2Buildings = playerService.getBuildingList();
        boolean hasTrainingFacilityInPhase2 = phase2Buildings.stream()
                .anyMatch(building -> building.getConstructionTypeName().equals("Training Facility"));
        
        assertTrue(hasTrainingFacilityInPhase2, "Training Facility should be available in Phase 2");
    }

    @Test
    @DisplayName("Should verify Training Facility becomes mandatory requirement for higher phases")
    public void testTrainingFacilityMandatoryForHigherPhases() {
        // Training Facility is NOT required for UPRISING (phase 1)
        TechPhase uprising = TechPhase.UPRISING;
        assertFalse(uprising.getRequirementsForNextLevel().containsKey(ConstructionType.TRAINING_FACILITY), 
                "Training Facility should not be required for Phase 1");
        
        // Training Facility becomes required from Phase 4 onwards
        TechPhase droneDominion = TechPhase.DRONE_DOMINION; // Phase 4
        assertTrue(droneDominion.getRequirementsForNextLevel().containsKey(ConstructionType.TRAINING_FACILITY),
                "Training Facility should be required for Phase 4");
        assertEquals(4, droneDominion.getRequirementsForNextLevel().get(ConstructionType.TRAINING_FACILITY));
    }

    @Test
    @DisplayName("Should validate mandatory building requirements for Phase 1")
    public void testMandatoryBuildingValidation() {
        // Test the mandatory building validation methods for Phase 1
        List<ConstructionType> missingBuildings = playerService.getMissingMandatoryBuildings();
        
        // For Phase 1 (UPRISING), only COMMAND_CENTER and OP_BASE are required
        // TRAINING_FACILITY is NOT required for Phase 1
        assertFalse(missingBuildings.contains(ConstructionType.TRAINING_FACILITY), 
                "Training Facility should not be required for Phase 1");
        assertTrue(missingBuildings.contains(ConstructionType.COMMAND_CENTER),
                "Command Center should be required for Phase 1");
        assertTrue(missingBuildings.contains(ConstructionType.OP_BASE),
                "Op Base should be required for Phase 1");
        
        // Validation should return false since mandatory buildings are not built
        assertFalse(playerService.validateMandatoryBuildings());
    }

    @Test
    @DisplayName("Should verify Training Facility availability in Phase 2")
    public void testTrainingFacilityCanBeBuiltInPhase2() {
        // Advance to Phase 2 where Training Facility becomes available
        playerService.setLevel(2);
        
        // Test that Training Facility can be found in the building list
        List<Building> buildings = playerService.getBuildingList();
        Building trainingFacility = buildings.stream()
                .filter(building -> building.getConstructionTypeName().equals("Training Facility"))
                .findFirst()
                .orElse(null);
        
        assertNotNull(trainingFacility, "Training Facility building should exist in building list for Phase 2");
        assertFalse(trainingFacility.getBuilded(), "Training Facility should not be built initially");
        assertEquals(0, trainingFacility.getLevel(), "Training Facility should start at level 0");
        assertEquals(5, trainingFacility.getMaxLevel(), "Training Facility max level should be 5");
        assertEquals(2, trainingFacility.getAmountConstructionsAllowed(), "Only 2 Training Facilities should be allowed");
    }

    @Test
    @DisplayName("Should verify Training Facility has resource costs")
    public void testTrainingFacilityHasResourceCosts() {
        // Advance to Phase 2 where Training Facility becomes available
        playerService.setLevel(2);
        
        // Test that Training Facility has resource costs
        List<Building> buildings = playerService.getBuildingList();
        Building trainingFacility = buildings.stream()
                .filter(building -> building.getConstructionTypeName().equals("Training Facility"))
                .findFirst()
                .orElse(null);
        
        assertNotNull(trainingFacility);
        assertNotNull(trainingFacility.getResourceCost(), "Training Facility should have resource costs defined");
        assertFalse(trainingFacility.getResourceCost().isEmpty(), "Training Facility should have non-empty resource costs");
    }
}
