package org.hsh.games.aoe;

import org.hsh.games.aoe.entities.*;
import org.hsh.games.aoe.services.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PlayerServiceTest {
    private PlayerService playerService;
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("FAMRTEST");
        playerService = new PlayerService(player);
    }

    @Test
    void addResourceIncreasesResourceAmount() {
        // setup
        playerService.setPlayerResources(new ArrayList<>());
        playerService.addResource(ResourceType.ENERGY, 10);
        assertEquals(10, playerService.getPlayerResources().get(0).getAmount());
    }

    @Test
    void addCyberOperativeWhenPopulationLimitReachedDoesNotAddCyberOperative() {
        CyberOperative operative1 = new CyberOperative("operative1");
        CyberOperative operative2 = new CyberOperative("operative2");
        CyberOperative operative3 = new CyberOperative("operative3");
        playerService.addCyberOperative(operative1);
        playerService.addCyberOperative(operative2);
        playerService.addCyberOperative(operative3);
        assertEquals(3, playerService.getCyberOperatives().size());
    }

    @Test
    void checkIfPlayerHasEnoughResourcesReturnsTrueWhenResourcesAreSufficient() {
        List<ResourceAmount> playerResources = Arrays.asList(
                new ResourceAmount(ResourceType.ENERGY, 100),
                new ResourceAmount(ResourceType.DATA, 100)
        );
        playerService.setPlayerResources(playerResources);

        List<ResourceAmount> buildingCostResources = Arrays.asList(
                new ResourceAmount(ResourceType.ENERGY, 70),
                new ResourceAmount(ResourceType.DATA, 100)
        );
        Building building = new Building(false, ConstructionType.OP_BASE);
        building.setResourceCost(buildingCostResources);

        assertTrue(playerService.checkIfPlayerHasEnoughResources(building));
    }

    @Test
    void checkIfPlayerHasEnoughResourcesReturnsFalseWhenResourcesAreInsufficient() {
        List<ResourceAmount> playerResources = Arrays.asList(
                new ResourceAmount(ResourceType.ENERGY, 100),
                new ResourceAmount(ResourceType.DATA, 100)
        );
        playerService.setPlayerResources(playerResources);

        List<ResourceAmount> buildingCostResources = Arrays.asList(
                new ResourceAmount(ResourceType.ENERGY, 120),
                new ResourceAmount(ResourceType.DATA, 100)
        );
        Building building = new Building(false, ConstructionType.OP_BASE);
        building.setResourceCost(buildingCostResources);

        assertFalse(playerService.checkIfPlayerHasEnoughResources(building));
    }

    @Test
    void checkIfBuildingAmountHasReachedReturnsTrueWhenLimitReached() {
        Building building = new Building(true, ConstructionType.OP_BASE);
        building.setAmountConstructionsAllowed(1);

        playerService.addNewBuilding(building);

        assertTrue(playerService.checkIfBuildingAmountHasReached(building));
    }

    @Test
    void checkIfBuildingAmountHasReachedReturnsFalseWhenLimitReachedButIsNotBuilt() {
        Building building = new Building(false, ConstructionType.OP_BASE);
        building.setAmountConstructionsAllowed(1);

        playerService.addNewBuilding(building);

        assertFalse(playerService.checkIfBuildingAmountHasReached(building));
    }

    @Test
    void checkIfBuildingAmountHasReachedReturnsFalseWhenLimitNotReached() {
        Building building = new Building(false, ConstructionType.OP_BASE);
        building.setAmountConstructionsAllowed(2);

        playerService.addNewBuilding(building);

        assertFalse(playerService.checkIfBuildingAmountHasReached(building));
    }

    @Test
    void checkIfBuildingHasReachedItsMaximLevelReturnsTrueWhenMaxLevelReached() {
        Building building = new Building(false, ConstructionType.OP_BASE);

        building.setMaxLevel(5);
        building.setLevel(5);

        assertTrue(playerService.checkIfBuildingHasReachedItsMaximLevel(building));
    }

    @Test
    void checkIfBuildingHasReachedItsMaximLevelReturnsFalseWhenMaxLevelNotReached() {
        Building building = new Building(false, ConstructionType.OP_BASE);

        building.setMaxLevel(5);
        building.setLevel(4);

        assertFalse(playerService.checkIfBuildingHasReachedItsMaximLevel(building));
    }

    @Test
    void sendCyberOperativesToConstructionJobAssignsCyberOperativesToConstruction() {
        Building building = new Building(false, ConstructionType.OP_BASE);
        playerService.setPlayerResources(List.of(new ResourceAmount(ResourceType.ENERGY, 100)));
        playerService.sendCyberOperativesToConstructionJob(ConstructionProcess.CREATION, building);
        boolean shouldHaveOneAvailableCyberOperative = playerService.getCyberOperativeAvailable().isOccupied();
        assertFalse(shouldHaveOneAvailableCyberOperative);
    }

    @Test
    void sendCyberOperativesToSearchJobAssignsCyberOperativesToSearch() {
        playerService.sendCyberOperativesToSearchJob(ResourceType.ENERGY);
        assertFalse(playerService.getCyberOperativeAvailable().isOccupied());
    }

    @Test
    void toBeDefinedThename() {

    }
}
