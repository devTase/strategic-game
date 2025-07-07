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
        // Test that ACADEMY enum exists and has correct properties
        ConstructionType academy = ConstructionType.ACADEMY;
        
        assertNotNull(academy);
        assertEquals("Academia", academy.getName());
        assertEquals(5, academy.getMaxLevel());
        assertEquals(1, academy.getAmountConstructionsAllowed());
        assertTrue(academy.getBaseResourcesCost().isEmpty()); // Empty cost list
        assertTrue(academy.getBaseResourcesProduction().isEmpty()); // No resource production
        assertEquals(0, academy.getBaseProductionTime()); // No production time
    }

    @Test
    public void testAcademyAvailableInEra1() {
        // Test that Academy is available in Era 1 (STONE_AGE)
        assertEquals(EraAge.STONE_AGE, player.getEraAge());
        
        List<Building> buildings = playerService.getBuildingList();
        boolean hasAcademy = buildings.stream()
                .anyMatch(building -> building.getConstructionTypeName().equals("Academia"));
        
        assertTrue(hasAcademy, "Academy should be available in Era 1");
    }

    @Test
    public void testAcademyIsMandatoryInEra1() {
        // Test that Academy is listed as mandatory for STONE_AGE era
        EraAge stoneAge = EraAge.STONE_AGE;
        assertTrue(stoneAge.getRequirementsForNextLevel().containsKey(ConstructionType.ACADEMY));
        assertEquals(1, stoneAge.getRequirementsForNextLevel().get(ConstructionType.ACADEMY));
    }

    @Test
    public void testMandatoryBuildingValidation() {
        // Test the new mandatory building validation methods
        List<ConstructionType> missingBuildings = playerService.getMissingMandatoryBuildings();
        
        // Initially, all mandatory buildings should be missing (not built yet)
        assertTrue(missingBuildings.contains(ConstructionType.ACADEMY));
        assertTrue(missingBuildings.contains(ConstructionType.TOWN_CENTER));
        assertTrue(missingBuildings.contains(ConstructionType.HOUSE));
        
        // Validation should return false since no mandatory buildings are built
        assertFalse(playerService.validateMandatoryBuildings());
    }

    @Test
    public void testAcademyCanBeBuilt() {
        // Test that Academy can be found in the building list and can be constructed
        List<Building> buildings = playerService.getBuildingList();
        Building academy = buildings.stream()
                .filter(building -> building.getConstructionTypeName().equals("Academia"))
                .findFirst()
                .orElse(null);
        
        assertNotNull(academy, "Academy building should exist in building list");
        assertFalse(academy.getBuilded(), "Academy should not be built initially");
        assertEquals(0, academy.getLevel(), "Academy should start at level 0");
        assertEquals(5, academy.getMaxLevel(), "Academy max level should be 5");
        assertEquals(1, academy.getAmountConstructionsAllowed(), "Only 1 Academy should be allowed");
    }

    @Test
    public void testAcademyHasNoCosts() {
        // Test that Academy has no resource costs
        List<Building> buildings = playerService.getBuildingList();
        Building academy = buildings.stream()
                .filter(building -> building.getConstructionTypeName().equals("Academia"))
                .findFirst()
                .orElse(null);
        
        assertNotNull(academy);
        assertTrue(academy.getResourceCost().isEmpty(), "Academy should have no resource costs");
    }
}
