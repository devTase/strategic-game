package org.hsh.games.aoe;

import org.hsh.games.aoe.entities.BuildingAndResourceAvailabilityPerLevel;
import org.hsh.games.aoe.entities.ConstructionType;
import org.hsh.games.aoe.entities.Player;
import org.hsh.games.aoe.entities.ResourceType;
import org.hsh.games.aoe.entities.guild.*;
import org.hsh.games.aoe.services.*;
import org.hsh.games.aoe.ui.ConsoleDisplayUtils;

import javax.security.auth.Subject;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class GameOfStrategy {
    private static final Scanner scanner = new Scanner(System.in);
    private PlayerService playerService;
    private GuildService guildService;
    private GuildMissionService guildMissionService;
    private ShadowTerritoryService shadowTerritoryService;

    public GameOfStrategy() {
        // Initialize guild services with constructor injection
        this.guildService = new GuildService();
        this.guildMissionService = new GuildMissionService(guildService);
        this.shadowTerritoryService = new ShadowTerritoryService(guildService);
    }

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
                    handleGuildManagement(playerService);
                    break;
                case 4:
                    handleDailyRewards(playerService);
                    break;
                case 5:
                    displayPlayerStatus(playerService);
                    break;
                case 6:
                    handleShadowGuilds(playerService);
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
    
    private void handleGuildManagement(PlayerService playerService) {
        while (true) {
            ConsoleUtils.clearConsole();
            playerService.showResourcesHeader();
            ConsoleDisplayUtils.printGuildMenu();
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            ConsoleUtils.clearConsole();
            playerService.showResourcesHeader();
            
            switch (choice) {
                case 1:
                    handleCreateGuild(playerService);
                    break;
                case 2:
                    handleGuildInfo(playerService);
                    break;
                case 3:
                    handleMemberManagement(playerService);
                    break;
                case 4:
                    handleGuildMissions(playerService);
                    break;
                case 5:
                    handleTerritories(playerService);
                    break;
                case 0:
                    return; // Back to main menu
                default:
                    ConsoleDisplayUtils.printErrorMessage(ApplicationConstants.MESSAGE_WRONG_OPTION_TRY_AGAIN);
            }
            
            if (choice != 0) {
                ConsoleDisplayUtils.printWaitPrompt();
                scanner.nextLine();
            }
        }
    }
    
    private void handleCreateGuild(PlayerService playerService) {
        String playerId = playerService.getPlayer().getFarmName();
        
        if (guildService.isPlayerInGuild(playerId)) {
            ConsoleDisplayUtils.printWarningMessage("You are already a member of a guild!");
            return;
        }
        
        System.out.print("🏛️ Enter guild name: ");
        String guildName = scanner.nextLine();
        
        if (guildName.trim().isEmpty()) {
            ConsoleDisplayUtils.printErrorMessage("Guild name cannot be empty!");
            return;
        }
        
        try {
            Guild guild = guildService.createGuild(guildName, playerId, 1000);
            ConsoleDisplayUtils.printSuccessMessage("Guild '" + guild.name() + "' created successfully!");
            ConsoleDisplayUtils.printInfoMessage("You are now the leader of this guild.");
        } catch (Exception e) {
            ConsoleDisplayUtils.printErrorMessage("Failed to create guild: " + e.getMessage());
        }
    }
    
    private void handleGuildInfo(PlayerService playerService) {
        String playerId = playerService.getPlayer().getFarmName();
        Guild guild = guildService.getPlayerGuild(playerId);
        
        if (guild == null) {
            ConsoleDisplayUtils.printWarningMessage("You are not a member of any guild.");
            ConsoleDisplayUtils.printInfoMessage("Create a guild or ask to be invited to one!");
            return;
        }
        
        System.out.println(guild.getFormattedInfo());
        System.out.println();
        
        // Show recent events
        var recentEvents = guild.getRecentEvents(5);
        if (!recentEvents.isEmpty()) {
            ConsoleDisplayUtils.printInfoMessage("📰 Recent Guild Events:");
            for (var event : recentEvents) {
                System.out.println("  • " + event.getSummary());
            }
        }
    }
    
    private void handleMemberManagement(PlayerService playerService) {
        String playerId = playerService.getPlayer().getFarmName();
        Guild guild = guildService.getPlayerGuild(playerId);
        
        if (guild == null) {
            ConsoleDisplayUtils.printWarningMessage("You must be in a guild to manage members.");
            return;
        }
        
        GuildMember member = guild.getMember(playerId);
        if (!member.hasAdminPrivileges()) {
            ConsoleDisplayUtils.printWarningMessage("Only officers and leaders can manage members.");
            return;
        }
        
        System.out.println("👥 Guild Members:");
        for (var guildMember : guild.getMembersCopy()) {
            System.out.println("  " + guildMember.getFormattedInfo());
        }
        
        System.out.println();
        System.out.println("1. Invite Player");
        System.out.println("2. Change Player Rank");
        System.out.println("3. Remove Player");
        System.out.print("Choose action: ");
        
        int action = scanner.nextInt();
        scanner.nextLine();
        
        switch (action) {
            case 1:
                System.out.print("Enter player ID to invite: ");
                String invitePlayerId = scanner.nextLine();
                try {
                    guildService.invitePlayer(guild.id(), invitePlayerId, playerId, GuildRank.RECRUIT);
                    ConsoleDisplayUtils.printSuccessMessage("Player invited successfully!");
                } catch (Exception e) {
                    ConsoleDisplayUtils.printErrorMessage("Failed to invite player: " + e.getMessage());
                }
                break;
            case 2:
                System.out.print("Enter player ID to promote: ");
                String promotePlayerId = scanner.nextLine();
                System.out.println("Available ranks: MEMBER, OFFICER, SPY");
                System.out.print("Enter new rank: ");
                String rankStr = scanner.nextLine();
                try {
                    GuildRank newRank = GuildRank.valueOf(rankStr.toUpperCase());
                    guildService.changeRank(guild.id(), promotePlayerId, newRank, playerId);
                    ConsoleDisplayUtils.printSuccessMessage("Player rank changed successfully!");
                } catch (Exception e) {
                    ConsoleDisplayUtils.printErrorMessage("Failed to change rank: " + e.getMessage());
                }
                break;
            case 3:
                System.out.print("Enter player ID to remove: ");
                String removePlayerId = scanner.nextLine();
                try {
                    guildService.removePlayerFromGuild(removePlayerId, playerId);
                    ConsoleDisplayUtils.printSuccessMessage("Player removed successfully!");
                } catch (Exception e) {
                    ConsoleDisplayUtils.printErrorMessage("Failed to remove player: " + e.getMessage());
                }
                break;
        }
    }
    
    private void handleGuildMissions(PlayerService playerService) {
        String playerId = playerService.getPlayer().getFarmName();
        Guild guild = guildService.getPlayerGuild(playerId);
        
        if (guild == null) {
            ConsoleDisplayUtils.printWarningMessage("You must be in a guild to access missions.");
            return;
        }
        
        var missions = guildMissionService.getGuildMissions(guild.id());
        
        if (missions.isEmpty()) {
            ConsoleDisplayUtils.printInfoMessage("No active missions available.");
            System.out.println();
            System.out.println("1. Generate New Missions");
            System.out.print("Choose action: ");
            
            int action = scanner.nextInt();
            scanner.nextLine();
            
            if (action == 1) {
                var newMissions = guildMissionService.generateMissionsPerEra(playerService.getPlayer().getEraAge(), 3);
                ConsoleDisplayUtils.printSuccessMessage("Generated " + newMissions.size() + " new missions!");
                
                for (var mission : newMissions) {
                    System.out.println("📋 " + mission.getSummary());
                }
            }
        } else {
            System.out.println("🎯 Available Guild Missions:");
            for (int i = 0; i < missions.size(); i++) {
                var mission = missions.get(i);
                System.out.printf("%d. %s\n", i + 1, mission.getFormattedInfo());
            }
            
            System.out.println();
            System.out.print("Select mission to manage (0 to cancel): ");
            int missionChoice = scanner.nextInt();
            scanner.nextLine();
            
            if (missionChoice > 0 && missionChoice <= missions.size()) {
                var selectedMission = missions.get(missionChoice - 1);
                handleMissionManagement(guild, selectedMission, playerId);
            }
        }
    }
    
    private void handleMissionManagement(Guild guild, GuildMission mission, String playerId) {
        GuildMember member = guild.getMember(playerId);
        
        System.out.println("📋 Mission: " + mission.type().getDisplayName());
        System.out.println("Status: " + mission.status().getDisplayNameWithEmoji());
        System.out.println("Participants: " + mission.getParticipantCount());
        System.out.println();
        
        if (member.hasAdminPrivileges()) {
            System.out.println("1. Assign Participants");
            System.out.println("2. Start Mission");
            System.out.println("3. Cancel Mission");
            System.out.print("Choose action: ");
            
            int action = scanner.nextInt();
            scanner.nextLine();
            
            switch (action) {
                case 1:
                    System.out.print("Enter participant player IDs (comma separated): ");
                    String participantsStr = scanner.nextLine();
                    Set<String> participants = new HashSet<>(Arrays.asList(participantsStr.split(",")));
                    
                    try {
                        guildMissionService.assignParticipants(mission.id(), guild.id(), participants, playerId);
                        ConsoleDisplayUtils.printSuccessMessage("Participants assigned successfully!");
                    } catch (Exception e) {
                        ConsoleDisplayUtils.printErrorMessage("Failed to assign participants: " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        ConsoleDisplayUtils.printInfoMessage("🚀 Starting mission...");
                        var future = guildMissionService.resolveMission(mission.id(), guild.id());
                        var result = future.get(); // Wait for mission completion
                        
                        if (result.status() == MissionStatus.COMPLETED) {
                            ConsoleDisplayUtils.printSuccessMessage("Mission completed successfully!");
                        } else {
                            ConsoleDisplayUtils.printWarningMessage("Mission failed.");
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        ConsoleDisplayUtils.printErrorMessage("Mission execution failed: " + e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        guildMissionService.cancelMission(mission.id(), guild.id(), playerId);
                        ConsoleDisplayUtils.printSuccessMessage("Mission cancelled.");
                    } catch (Exception e) {
                        ConsoleDisplayUtils.printErrorMessage("Failed to cancel mission: " + e.getMessage());
                    }
                    break;
            }
        } else {
            ConsoleDisplayUtils.printWarningMessage("Only officers and leaders can manage missions.");
        }
    }
    
    private void handleTerritories(PlayerService playerService) {
        String playerId = playerService.getPlayer().getFarmName();
        Guild guild = guildService.getPlayerGuild(playerId);
        
        if (guild == null) {
            ConsoleDisplayUtils.printWarningMessage("You must be in a guild to access territories.");
            return;
        }
        
        var territories = shadowTerritoryService.listTerritories();
        
        if (territories.isEmpty()) {
            ConsoleDisplayUtils.printInfoMessage("No territories available for conquest.");
            ConsoleDisplayUtils.printInfoMessage("🗺️ The shadow realm awaits brave adventurers...");
        } else {
            System.out.println("🗺️ Shadow Territories:");
            for (var territory : territories) {
                System.out.println("  " + territory.getFormattedInfo());
            }
        }
        
        System.out.println();
        ConsoleDisplayUtils.printInfoMessage("🏰 Your guild controls " + guild.getTerritoryCount() + " territories.");
        
        if (!guild.territories().isEmpty()) {
            System.out.println("Controlled territories:");
            for (String territoryId : guild.territories()) {
                var territory = shadowTerritoryService.getTerritoryById(territoryId);
                if (territory != null) {
                    System.out.println("  " + territory.getFormattedInfo());
                }
            }
        }
    }
    
    private void handleShadowGuilds(PlayerService playerService) {
        while (true) {
            ConsoleUtils.clearConsole();
            playerService.showResourcesHeader();
            ConsoleDisplayUtils.printShadowGuildsMenu();
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            ConsoleUtils.clearConsole();
            playerService.showResourcesHeader();
            
            switch (choice) {
                case 1:
                    handleShadowCreateJoinGuild(playerService);
                    break;
                case 2:
                    handleShadowGuildStatusVault(playerService);
                    break;
                case 3:
                    handleShadowMissions(playerService);
                    break;
                case 4:
                    handleShadowTerritories(playerService);
                    break;
                case 5:
                    handleEspionageBlackMarket(playerService);
                    break;
                case 0:
                    return; // Back to main menu
                default:
                    ConsoleDisplayUtils.printErrorMessage(ApplicationConstants.MESSAGE_WRONG_OPTION_TRY_AGAIN);
            }
            
            if (choice != 0) {
                ConsoleDisplayUtils.printWaitPrompt();
                scanner.nextLine();
            }
        }
    }
    
    private void handleShadowCreateJoinGuild(PlayerService playerService) {
        ConsoleDisplayUtils.printInfoMessage("🕯️ Shadow Guild Creation/Joining");
        ConsoleDisplayUtils.printShadowLoadingMessage();
        
        try {
            Thread.sleep(2000); // Simulate async operation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        ConsoleDisplayUtils.printWarningMessage("Shadow guild functionality is still shrouded in mystery...");
        ConsoleDisplayUtils.printInfoMessage("The shadow realm requires further exploration by our scribes.");
    }
    
    private void handleShadowGuildStatusVault(PlayerService playerService) {
        ConsoleDisplayUtils.printInfoMessage("💰 Shadow Guild Status & Vault");
        ConsoleDisplayUtils.printShadowLoadingMessage();
        
        try {
            Thread.sleep(1500); // Simulate async operation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        ConsoleDisplayUtils.printWarningMessage("The shadow vault remains sealed...");
        ConsoleDisplayUtils.printInfoMessage("Your shadow guild's status is hidden in the darkness.");
    }
    
    private void handleShadowMissions(PlayerService playerService) {
        ConsoleDisplayUtils.printInfoMessage("🎯 Shadow Operations");
        ConsoleDisplayUtils.printShadowLoadingMessage();
        
        try {
            Thread.sleep(2500); // Simulate async operation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        ConsoleDisplayUtils.printWarningMessage("Shadow missions are whispered only in the darkest corners...");
        ConsoleDisplayUtils.printInfoMessage("The shadow operatives are still gathering intelligence.");
    }
    
    private void handleShadowTerritories(PlayerService playerService) {
        ConsoleDisplayUtils.printInfoMessage("🗺️ Shadow Territory Conquest");
        ConsoleDisplayUtils.printShadowLoadingMessage();
        
        try {
            Thread.sleep(2000); // Simulate async operation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        ConsoleDisplayUtils.printWarningMessage("The shadow territories shift and change with the twilight...");
        ConsoleDisplayUtils.printInfoMessage("Territory maps are being drawn by shadow cartographers.");
    }
    
    private void handleEspionageBlackMarket(PlayerService playerService) {
        ConsoleDisplayUtils.printInfoMessage("🗡️ Espionage & Black Market");
        ConsoleDisplayUtils.printShadowLoadingMessage();
        
        try {
            Thread.sleep(3000); // Simulate async operation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        ConsoleDisplayUtils.printWarningMessage("The black market deals are conducted in absolute secrecy...");
        ConsoleDisplayUtils.printInfoMessage("Our spies are infiltrating the underground networks.");
    }
}
