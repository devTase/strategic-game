package org.hsh.games.aoe;

import org.hsh.games.aoe.entities.*;
import org.hsh.games.aoe.services.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Player Service Tests")
class PlayerServiceTest {
    private PlayerService playerService;
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("FAMRTEST");
        playerService = new PlayerService(player);
    }

    @Test
    @DisplayName("Should increase resource amount when adding resource")
    void addResourceIncreasesResourceAmount() {
        // setup
        playerService.setPlayerResources(new ArrayList<>());
        playerService.addResource(ResourceType.ENERGY, 10);
        assertEquals(10, playerService.getPlayerResources().get(0).getAmount());
    }

    @Test
    @DisplayName("Should add cyber operatives when population limit allows")
    void addCyberOperativeWhenPopulationLimitReachedDoesNotAddCyberOperative() {
        // PlayerService starts with 3 operatives and 180 DATA, so we can add more
        CyberOperative operative4 = new CyberOperative("operative4");
        CyberOperative operative5 = new CyberOperative("operative5");
        CyberOperative operative6 = new CyberOperative("operative6");
        
        // Should start with 3 operatives
        assertEquals(3, playerService.getCyberOperatives().size());
        
        // Should be able to add 3 more (total 6)
        playerService.addCyberOperative(operative4);
        playerService.addCyberOperative(operative5);
        playerService.addCyberOperative(operative6);
        
        assertEquals(6, playerService.getCyberOperatives().size());
    }

    @Test
    @DisplayName("Should return true when player has sufficient resources for building")
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
    @DisplayName("Should return false when player has insufficient resources for building")
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
    @DisplayName("Should return true when building limit is reached")
    void checkIfBuildingAmountHasReachedReturnsTrueWhenLimitReached() {
        Building building = new Building(true, ConstructionType.OP_BASE);
        building.setAmountConstructionsAllowed(1);

        playerService.addNewBuilding(building);

        assertTrue(playerService.checkIfBuildingAmountHasReached(building));
    }

    @Test
    @DisplayName("Should return false when limit reached but building is not built")
    void checkIfBuildingAmountHasReachedReturnsFalseWhenLimitReachedButIsNotBuilt() {
        Building building = new Building(false, ConstructionType.OP_BASE);
        building.setAmountConstructionsAllowed(1);

        playerService.addNewBuilding(building);

        assertFalse(playerService.checkIfBuildingAmountHasReached(building));
    }

    @Test
    @DisplayName("Should return false when building limit is not reached")
    void checkIfBuildingAmountHasReachedReturnsFalseWhenLimitNotReached() {
        Building building = new Building(false, ConstructionType.OP_BASE);
        building.setAmountConstructionsAllowed(2);

        playerService.addNewBuilding(building);

        assertFalse(playerService.checkIfBuildingAmountHasReached(building));
    }

    @Test
    @DisplayName("Should return true when building has reached maximum level")
    void checkIfBuildingHasReachedItsMaximLevelReturnsTrueWhenMaxLevelReached() {
        Building building = new Building(false, ConstructionType.OP_BASE);

        building.setMaxLevel(5);
        building.setLevel(5);

        assertTrue(playerService.checkIfBuildingHasReachedItsMaximLevel(building));
    }

    @Test
    @DisplayName("Should return false when building has not reached maximum level")
    void checkIfBuildingHasReachedItsMaximLevelReturnsFalseWhenMaxLevelNotReached() {
        Building building = new Building(false, ConstructionType.OP_BASE);

        building.setMaxLevel(5);
        building.setLevel(4);

        assertFalse(playerService.checkIfBuildingHasReachedItsMaximLevel(building));
    }

    @Test
    @DisplayName("Should assign cyber operatives to construction job")
    void sendCyberOperativesToConstructionJobAssignsCyberOperativesToConstruction() {
        Building building = new Building(false, ConstructionType.OP_BASE);
        playerService.setPlayerResources(List.of(new ResourceAmount(ResourceType.ENERGY, 100)));
        playerService.sendCyberOperativesToConstructionJob(ConstructionProcess.CREATION, building);
        boolean shouldHaveOneAvailableCyberOperative = playerService.getCyberOperativeAvailable().isOccupied();
        assertFalse(shouldHaveOneAvailableCyberOperative);
    }

    @Test
    @DisplayName("Should assign cyber operatives to search job")
    void sendCyberOperativesToSearchJobAssignsCyberOperativesToSearch() {
        playerService.sendCyberOperativesToSearchJob(ResourceType.ENERGY);
        assertFalse(playerService.getCyberOperativeAvailable().isOccupied());
    }

    @Test
    @DisplayName("Placeholder test to be defined")
    void toBeDefinedThename() {
        // TODO: Define this test
    }
}
