package org.hsh.games.aoe;

import org.hsh.games.aoe.entities.*;
import org.hsh.games.aoe.services.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AcademyBuildingTest {

    private PlayerService playerService;
    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player("devTASE");
        playerService = new PlayerService(player);
    }

    @Test
    public void testAcademyConstructionTypeExists() {
        // Test that TRAINING_FACILITY enum exists and has correct properties (equivalent to old ACADEMY)
        ConstructionType academy = ConstructionType.TRAINING_FACILITY;
        
        assertNotNull(academy);
        assertEquals("Training Facility", academy.getName());
        assertEquals(5, academy.getMaxLevel());
        assertEquals(2, academy.getAmountConstructionsAllowed());
        // For cyber-themed academy, we expect costs for training facilities
        assertNotNull(academy.getBaseResourcesCost());
        assertNotNull(academy.getBaseResourcesProduction());
        assertNotNull(academy.getBaseProductionTime());
    }

    @Test
    public void testAcademyAvailableInPhase1() {
        // Test that Training Facility is available in Phase 1 (UPRISING)
        assertEquals(TechPhase.UPRISING, player.getTechPhase());
        
        List<Building> buildings = playerService.getBuildingList();
        boolean hasAcademy = buildings.stream()
                .anyMatch(building -> building.getConstructionTypeName().equals("Training Facility"));
        
        assertTrue(hasAcademy, "Training Facility should be available in Phase 1");
    }

    @Test
    public void testAcademyIsMandatoryInPhase1() {
        // Test that Training Facility is listed as mandatory for UPRISING phase
        TechPhase uprising = TechPhase.UPRISING;
        assertTrue(uprising.getRequirementsForNextLevel().containsKey(ConstructionType.TRAINING_FACILITY));
        assertEquals(1, uprising.getRequirementsForNextLevel().get(ConstructionType.TRAINING_FACILITY));
    }

    @Test
    public void testMandatoryBuildingValidation() {
        // Test the new mandatory building validation methods
        List<ConstructionType> missingBuildings = playerService.getMissingMandatoryBuildings();
        
        // Initially, all mandatory buildings should be missing (not built yet)
        assertTrue(missingBuildings.contains(ConstructionType.TRAINING_FACILITY));
        assertTrue(missingBuildings.contains(ConstructionType.COMMAND_CENTER));
        assertTrue(missingBuildings.contains(ConstructionType.OP_BASE));
        
        // Validation should return false since no mandatory buildings are built
        assertFalse(playerService.validateMandatoryBuildings());
    }

    @Test
    public void testAcademyCanBeBuilt() {
        // Test that Training Facility can be found in the building list and can be constructed
        List<Building> buildings = playerService.getBuildingList();
        Building academy = buildings.stream()
                .filter(building -> building.getConstructionTypeName().equals("Training Facility"))
                .findFirst()
                .orElse(null);
        
        assertNotNull(academy, "Training Facility building should exist in building list");
        assertFalse(academy.getBuilded(), "Training Facility should not be built initially");
        assertEquals(0, academy.getLevel(), "Training Facility should start at level 0");
        assertEquals(5, academy.getMaxLevel(), "Training Facility max level should be 5");
        assertEquals(2, academy.getAmountConstructionsAllowed(), "Only 2 Training Facilities should be allowed");
    }

    @Test
    public void testAcademyHasNoCosts() {
        // Test that Training Facility has resource costs
        List<Building> buildings = playerService.getBuildingList();
        Building academy = buildings.stream()
                .filter(building -> building.getConstructionTypeName().equals("Training Facility"))
                .findFirst()
                .orElse(null);
        
        assertNotNull(academy);
        assertFalse(academy.getResourceCost().isEmpty(), "Training Facility should have resource costs");
    }
}
