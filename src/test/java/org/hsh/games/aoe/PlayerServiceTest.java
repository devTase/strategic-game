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
        playerService.addResource(ResourceType.WOOD, 10);
        assertEquals(10, playerService.getPlayerResources().get(0).getAmount());
    }

    @Test
    void addWorkerWhenPopulationLimitReachedDoesNotAddWorker() {
        Worker worker1 = new Worker("worker1");
        Worker worker2 = new Worker("worker2");
        Worker worker3 = new Worker("worker3");
        playerService.addWorker(worker1);
        playerService.addWorker(worker2);
        playerService.addWorker(worker3);
        assertEquals(3, playerService.getWorkers().size());
    }

    @Test
    void checkIfPlayerHasEnoughResourcesReturnsTrueWhenResourcesAreSufficient() {
        List<ResourceAmount> playerResources = Arrays.asList(
                new ResourceAmount(ResourceType.WOOD, 100),
                new ResourceAmount(ResourceType.WATER, 100)
        );
        playerService.setPlayerResources(playerResources);

        List<ResourceAmount> buildingCostResources = Arrays.asList(
                new ResourceAmount(ResourceType.WOOD, 70),
                new ResourceAmount(ResourceType.WATER, 100)
        );
        Building building = new Building(false, ConstructionType.HOUSE);
        building.setResourceCost(buildingCostResources);

        assertTrue(playerService.checkIfPlayerHasEnoughResources(building));
    }

    @Test
    void checkIfPlayerHasEnoughResourcesReturnsFalseWhenResourcesAreInsufficient() {
        List<ResourceAmount> playerResources = Arrays.asList(
                new ResourceAmount(ResourceType.WOOD, 100),
                new ResourceAmount(ResourceType.WATER, 100)
        );
        playerService.setPlayerResources(playerResources);

        List<ResourceAmount> buildingCostResources = Arrays.asList(
                new ResourceAmount(ResourceType.WOOD, 120),
                new ResourceAmount(ResourceType.WATER, 100)
        );
        Building building = new Building(false, ConstructionType.HOUSE);
        building.setResourceCost(buildingCostResources);

        assertFalse(playerService.checkIfPlayerHasEnoughResources(building));
    }

    @Test
    void checkIfBuildingAmountHasReachedReturnsTrueWhenLimitReached() {
        Building building = new Building(true, ConstructionType.HOUSE);
        building.setAmountConstructionsAllowed(1);

        playerService.addNewBuilding(building);

        assertTrue(playerService.checkIfBuildingAmountHasReached(building));
    }

    @Test
    void checkIfBuildingAmountHasReachedReturnsFalseWhenLimitReachedButIsNotBuilt() {
        Building building = new Building(false, ConstructionType.HOUSE);
        building.setAmountConstructionsAllowed(1);

        playerService.addNewBuilding(building);

        assertFalse(playerService.checkIfBuildingAmountHasReached(building));
    }

    @Test
    void checkIfBuildingAmountHasReachedReturnsFalseWhenLimitNotReached() {
        Building building = new Building(false, ConstructionType.HOUSE);
        building.setAmountConstructionsAllowed(2);

        playerService.addNewBuilding(building);

        assertFalse(playerService.checkIfBuildingAmountHasReached(building));
    }

    @Test
    void checkIfBuildingHasReachedItsMaximLevelReturnsTrueWhenMaxLevelReached() {
        Building building = new Building(false, ConstructionType.HOUSE);

        building.setMaxLevel(5);
        building.setLevel(5);

        assertTrue(playerService.checkIfBuildingHasReachedItsMaximLevel(building));
    }

    @Test
    void checkIfBuildingHasReachedItsMaximLevelReturnsFalseWhenMaxLevelNotReached() {
        Building building = new Building(false, ConstructionType.HOUSE);

        building.setMaxLevel(5);
        building.setLevel(4);

        assertFalse(playerService.checkIfBuildingHasReachedItsMaximLevel(building));
    }

    @Test
    void sendWorkersToConstructionJobAssignsWorkersToConstruction() {
        Building building = new Building(false, ConstructionType.HOUSE);
        playerService.setPlayerResources(List.of(new ResourceAmount(ResourceType.WOOD, 100)));
        playerService.sendWorkersToConstructionJob(ConstructionProcess.CREATION, building);
        boolean shouldHaveOneAvailableWorker = playerService.getWorkerAvailable().isOccupied();
        assertFalse(shouldHaveOneAvailableWorker);
    }

    @Test
    void sendWorkersToSearchJobAssignsWorkersToSearch() {
        playerService.sendWorkersToSearchJob(ResourceType.WOOD);
        assertFalse(playerService.getWorkerAvailable().isOccupied());
    }

    @Test
    void toBeDefinedThename() {

    }
}
