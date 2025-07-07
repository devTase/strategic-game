package org.hsh.games.aoe;

import org.hsh.games.aoe.services.PlayerService;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.*;

class GameOfStrategyTest {
    private GameOfStrategy gameOfStrategy;
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        gameOfStrategy = new GameOfStrategy();
        playerService = mock(PlayerService.class);
    }
//
//    @Test
//    void displayResourcesAvailableToSearchFor() {
//        ResourceType resourceType = ResourceType.WOOD;
//        InputStream inputStream = new ByteArrayInputStream("1\n".getBytes());
//        System.setIn(inputStream);
//
//        when(playerService.getWorkerAvailable()).thenReturn(new Worker("worker"));
//        when(playerService.getPlayer()).thenReturn(new Player("player"));
//        when(playerService.getPlayer().getEraAge().getLevel()).thenReturn(1);
//        gameOfStrategy.displayResourcesAvailableToSearchFor(playerService);
//
//        verify(playerService).sendWorkersToSearchJob(resourceType);
//    }
//
//    @Test
//    void displayBuildingTypes() {
//        Building building = new Building(false, ConstructionType.HOUSE);
//        when(playerService.getAvailableConstructionTypes()).thenReturn(Set.of("House"));
//        when(playerService.getBuildingsFromConstructionName("House")).thenReturn(List.of(building));
//        when(playerService.checkIfBuildingAmountHasReached(building)).thenReturn(false);
//
//        InputStream inputStream = new ByteArrayInputStream("1\n".getBytes());
//        System.setIn(inputStream);
//
//        gameOfStrategy.displayBuildingTypes(playerService);
//
//        verify(playerService).checkForNewEraConditions();
//        verify(playerService).processSelectedBuilding(playerService, building);
//    }
//
//    @Test
//    void processSelectedBuilding() {
//        Building building = new Building(false, ConstructionType.HOUSE);
//        ConstructionProcess process = ConstructionProcess.CREATION;
//        when(playerService.checkIfPlayerHasEnoughResources(building)).thenReturn(true);
//        when(playerService.isFirstTimeBuilding(building)).thenReturn(true);
//        when(playerService.getWorkerAvailable()).thenReturn(new Worker("worker"));
//
//        gameOfStrategy.processSelectedBuilding(playerService, building);
//
//        verify(playerService).sendWorkersToConstructionJob(process, building);
//    }
//
//    @Test
//    void processNewBuilding() {
//        Building building = new Building(false, ConstructionType.HOUSE);
//        when(playerService.checkIfPlayerHasEnoughResources(building)).thenReturn(true);
//        when(playerService.getWorkerAvailable()).thenReturn(new Worker("worker"));
//
//        gameOfStrategy.processNewBuilding(playerService, building);
//
//        verify(playerService).addNewBuilding(building);
//        verify(playerService).sendWorkersToConstructionJob(ConstructionProcess.CREATION, building);
//    }
}
