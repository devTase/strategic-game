package org.hsh.games.aoe;

import org.hsh.games.aoe.entities.BuildingAndResourceAvailabilityPerLevel;
import org.hsh.games.aoe.entities.ConstructionType;
import org.hsh.games.aoe.entities.Player;
import org.hsh.games.aoe.entities.ResourceType;
import org.hsh.games.aoe.ui.ConsoleDisplayUtils;

import javax.security.auth.Subject;
import java.util.*;

public class GameOfStrategy {
    private static final Scanner scanner = new Scanner(System.in);
    private PlayerService playerService;

    public PlayerService getPlayerService() {
        return playerService;
    }

    public void start() {
        PlayerService playerService = createPlayer();
        ConsoleUtils.clearConsole();
        startGame(playerService);
    }

    public PlayerService createPlayer() {
        Player player = null;
        
        ConsoleDisplayUtils.printGameBanner();
        
        while (player == null) {
            System.out.println(ApplicationConstants.MESSAGE_VILLAGE_NAME_PROMPT);
            String farmName = scanner.nextLine();
            
            try {
                player = new Player(farmName);
                ConsoleDisplayUtils.printSuccessMessage(ApplicationConstants.MESSAGE_VILLAGE_CREATION_SUCCESS);
                try {
                    Thread.sleep(ApplicationConstants.TIME_TO_SHOW_MESSAGE);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } catch (IllegalArgumentException e) {
                ConsoleDisplayUtils.printErrorMessage("Error: " + e.getMessage());
                ConsoleDisplayUtils.printWarningMessage(ApplicationConstants.MESSAGE_VILLAGE_CREATION_ERROR);
            }
        }
        
        return new PlayerService(player);
    }

    private void startGame(PlayerService playerService) {
        while (!playerService.getWorkers().isEmpty()) {
            try {
                Thread.sleep(ApplicationConstants.TIME_TO_SHOW_QUICK_MESSAGE);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ConsoleUtils.clearConsole();
            showMenu(playerService);
            int choice = scanner.nextInt();
            scanner.nextLine();

            ConsoleUtils.clearConsole();
            playerService.showResourcesHeader();

            switch (choice) {
                case 1:
                    displayResourcesAvailableToSearchFor(playerService);
                    break;
                case 2:
                    displayBuildingTypes(playerService);
                    break;
                case 3:
                    handleDailyRewards(playerService);
                    break;
                case 4:
                    displayPlayerStatus(playerService);
                    break;
                case 0:
                    ConsoleDisplayUtils.printInfoMessage(ApplicationConstants.MESSAGE_FAREWELL);
                    System.exit(0);
                default:
                    ConsoleDisplayUtils.printErrorMessage(ApplicationConstants.MESSAGE_WRONG_OPTION_TRY_AGAIN);
            }
        }
        ConsoleDisplayUtils.printErrorMessage(ApplicationConstants.MESSAGE_GAME_OVER);
    }

    private void showMenu(PlayerService playerService) {
        playerService.showResourcesHeader();
        ConsoleDisplayUtils.printMainMenu();
    }
    

    private void displayResourcesAvailableToSearchFor(PlayerService playerService) {
        ConsoleDisplayUtils.printResourceMenu();

        int index = 1;
        List<ResourceType> availableResources = BuildingAndResourceAvailabilityPerLevel
            .getByLevel(playerService.getPlayer().getEraAge().getLevel()).getAvailableResources();
        
        for (ResourceType resourceType : availableResources) {
            String icon = getResourceIcon(resourceType);
            ConsoleDisplayUtils.printResourceItem(index, resourceType.getDescription(), icon);
            index++;
        }
        
        ConsoleDisplayUtils.printMenuFooter();
        System.out.print("\n⚔️  Choose the resource to gather: ");

        int input = scanner.nextInt();
        if (input < 1 || input > availableResources.size()) {
            ConsoleDisplayUtils.printErrorMessage(ApplicationConstants.MESSAGE_WRONG_OPTION_TRY_AGAIN);
            return;
        }
        if (playerService.getWorkerAvailable() != null) {
            playerService.sendWorkersToSearchJob(availableResources.get(input - 1));
            ConsoleDisplayUtils.printSuccessMessage(ApplicationConstants.MESSAGE_WORKER_SENT_TO_GATHER);
        } else {
            ConsoleDisplayUtils.printWarningMessage(ApplicationConstants.MESSAGE_NO_WORKERS_AVAILABLE);
        }
    }

    /**
     * Helper method to get appropriate icon for resource types
     */
    private String getResourceIcon(ResourceType resourceType) {
        switch (resourceType.name().toUpperCase()) {
            case "WOOD": return ApplicationConstants.ICON_WOOD;
            case "STONE": return ApplicationConstants.ICON_STONE;
            case "GOLD": return ApplicationConstants.ICON_GOLD;
            case "FOOD": return ApplicationConstants.ICON_FOOD;
            case "IRON": return ApplicationConstants.ICON_IRON;
            default: return "📦"; // Generic package icon
        }
    }
    
    /**
     * Helper method to get appropriate icon for building types
     */
    private String getBuildingIcon(String buildingType) {
        switch (buildingType.toUpperCase()) {
            case "HOUSE": return ApplicationConstants.ICON_HOUSE;
            case "FARM": return ApplicationConstants.ICON_FARM;
            case "MINE": return ApplicationConstants.ICON_MINE;
            case "BARRACKS": return ApplicationConstants.ICON_BARRACKS;
            case "MARKET": return ApplicationConstants.ICON_MARKET;
            case "TEMPLE": return ApplicationConstants.ICON_TEMPLE;
            default: return "🏗️"; // Generic construction icon
        }
    }

    private void displayBuildingTypes(PlayerService playerService) {
        ConsoleDisplayUtils.printConstructionMenu();
        
        List<String> constructionTypes = new ArrayList<>(playerService.getAvailableConstructionTypes());
        int index = 1;
        for (String constructionType : constructionTypes) {
            String icon = getBuildingIcon(constructionType);
            ConsoleDisplayUtils.printBuildingItem(index, constructionType, icon, "Available for construction");
            index++;
        }
        
        ConsoleDisplayUtils.printMenuFooter();
        System.out.print("\n🏗️  Choose the building type: ");

        int typeOption = scanner.nextInt();
        scanner.nextLine();

        ConsoleUtils.clearConsole();
        playerService.showResourcesHeader();

        if (typeOption > 0 && typeOption <= constructionTypes.size()) {
            displayBuildingsOfType(playerService, constructionTypes.get(typeOption - 1));
        } else {
            ConsoleDisplayUtils.printErrorMessage(ApplicationConstants.MESSAGE_WRONG_OPTION_TRY_AGAIN);
        }
        playerService.checkForNewEraConditions();
    }

    private void displayBuildingsOfType(PlayerService playerService, String selectedType) {
        List<Building> buildings = playerService.getBuildingsFromConstructionName(selectedType);
        for (int i = 0; i < buildings.size(); i++) {
            System.out.printf("%d. ", i + 1);
            buildings.get(i).showDetails();
            if (!playerService.checkIfBuildingAmountHasReached(buildings.get(i))) {
                System.out.printf("%d Cria novo edificio: %s\n", i + 2, buildings.get(i).getConstructionTypeName());
            }
        }

        int option = getOptionFromUser();
        if (option > 0 && option <= buildings.size()) {
            processSelectedBuilding(playerService, buildings.get(option - 1));
        } else if (option == buildings.size() + 1) {
            processNewBuilding(playerService,
                    new Building(false, Objects.requireNonNull(ConstructionType.getEnumFromConstant(selectedType))));
        } else {
            ConsoleDisplayUtils.printErrorMessage(ApplicationConstants.MESSAGE_WRONG_OPTION_TRY_AGAIN);
        }
    }

    private int getOptionFromUser() {
        System.out.println(ApplicationConstants.MESSAGE_CHOOSE_OPTION);
        int option = scanner.nextInt();
        scanner.nextLine();
        return option;
    }

    private void processSelectedBuilding(PlayerService playerService, Building building) {
        if (playerService.checkIfPlayerHasEnoughResources(building)) {
            ConstructionProcess process = playerService.isFirstTimeBuilding(building) ? ConstructionProcess.CREATION
                : ConstructionProcess.UPDATE;
            if (playerService.getWorkerAvailable() != null) {
                playerService.sendWorkersToConstructionJob(process, building);
                if(process == ConstructionProcess.UPDATE) playerService.checkForNewEraConditions();
            }
        } else {
            ConsoleDisplayUtils.printErrorMessage(ApplicationConstants.MESSAGE_NOT_ENOUGH_RESOURCES);
        }
    }

    private void processNewBuilding(PlayerService playerService, Building building) {
        if (playerService.checkIfPlayerHasEnoughResources(building)) {
            playerService.addNewBuilding(building);
            playerService.sendWorkersToConstructionJob(ConstructionProcess.CREATION, building);
        } else {
            ConsoleDisplayUtils.printErrorMessage(ApplicationConstants.MESSAGE_NOT_ENOUGH_RESOURCES);
        }
    }

    private void handleDailyRewards(PlayerService playerService) {
        ConsoleDisplayUtils.printDailyRewardsHeader();
        
        try {
            playerService.claimDailyReward();
            ConsoleDisplayUtils.printSuccessMessage(ApplicationConstants.MESSAGE_DAILY_REWARD_CLAIMED);
        } catch (IllegalStateException e) {
            ConsoleDisplayUtils.printWarningMessage(e.getMessage());
        }
        
        // Show next reward preview
        ConsoleDisplayUtils.printInfoMessage("\n🔮 Next tribute: " + 
            playerService.getDailyRewardService().getNextReward(playerService.getPlayer().getFarmName()).toString());
        
        ConsoleDisplayUtils.printMenuFooter();
        ConsoleDisplayUtils.printWaitPrompt();
        scanner.nextLine();
    }

    private void displayPlayerStatus(PlayerService playerService) {
        ConsoleDisplayUtils.printStatusHeader();
        
        System.out.println("🏰 Kingdom: " + playerService.getPlayer().getFarmName());
        System.out.println("🎯 Current Era: " + playerService.getPlayer().getEraAge().getEraName());
        System.out.println("👥 Population: " + playerService.getWorkers().size() + " workers");
        System.out.println("🏠 Structures: " + playerService.getBuildingList().size() + " buildings");
        
        ConsoleDisplayUtils.printSeparator();
        
        // Daily reward status
        String playerId = playerService.getPlayer().getFarmName();
        if (playerService.getDailyRewardService().hasClaimedToday(playerId)) {
            ConsoleDisplayUtils.printSuccessMessage("Daily tribute already collected today!");
        } else {
            ConsoleDisplayUtils.printInfoMessage("Daily tribute available for collection!");
        }
        System.out.println("🔥 Current streak: " + playerService.getDailyRewardService().getCurrentStreak(playerId) + " days");
        
        ConsoleDisplayUtils.printMenuFooter();
        ConsoleDisplayUtils.printWaitPrompt();
        scanner.nextLine();
    }
}
