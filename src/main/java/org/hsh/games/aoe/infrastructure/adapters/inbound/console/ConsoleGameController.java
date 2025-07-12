package org.hsh.games.aoe.infrastructure.adapters.inbound.console;

import org.hsh.games.aoe.application.dto.PlayerDTO;
import org.hsh.games.aoe.application.dto.ResourceDTO;
import org.hsh.games.aoe.application.ports.inbound.PlayerManagementPort;
import org.hsh.games.aoe.application.ports.inbound.ResourceManagementPort;
import org.hsh.games.aoe.domain.entities.resources.ResourceType;
import org.hsh.games.aoe.domain.valueobjects.PlayerId;
import org.hsh.games.aoe.shared.utils.ConsoleUtils;
import org.hsh.games.aoe.shared.constants.ApplicationConstants;

import java.util.List;
import java.util.Scanner;

/**
 * Console-based game controller using hexagonal architecture
 * 
 * @author devTASE
 */
public class ConsoleGameController {
    
    private final PlayerManagementPort playerManagementPort;
    private final ResourceManagementPort resourceManagementPort;
    private final Scanner scanner;
    private PlayerDTO currentPlayer;
    
    public ConsoleGameController(PlayerManagementPort playerManagementPort, 
                               ResourceManagementPort resourceManagementPort) {
        this.playerManagementPort = playerManagementPort;
        this.resourceManagementPort = resourceManagementPort;
        this.scanner = new Scanner(System.in);
    }
    
    public void start() {
        createPlayer();
        ConsoleUtils.clearConsole();
        startGameLoop();
    }
    
    private void createPlayer() {
        ConsoleDisplayUtils.printGameBanner();
        
        while (currentPlayer == null) {
            System.out.println(ApplicationConstants.MESSAGE_VILLAGE_NAME_PROMPT);
            String farmName = scanner.nextLine();
            
            try {
                currentPlayer = playerManagementPort.createPlayer(farmName);
                ConsoleDisplayUtils.printSuccessMessage(ApplicationConstants.MESSAGE_VILLAGE_CREATION_SUCCESS);
                
                // Initialize with basic resources
                initializePlayerResources();
                
                Thread.sleep(ApplicationConstants.TIME_TO_SHOW_MESSAGE);
            } catch (Exception e) {
                ConsoleDisplayUtils.printErrorMessage("Error: " + e.getMessage());
                ConsoleDisplayUtils.printWarningMessage(ApplicationConstants.MESSAGE_VILLAGE_CREATION_ERROR);
            }
        }
    }
    
    private void initializePlayerResources() {
        PlayerId playerId = new PlayerId(currentPlayer.playerId());
        
        // Initialize basic resources
        resourceManagementPort.gatherResource(playerId, ResourceType.ENERGY, 100);
        resourceManagementPort.gatherResource(playerId, ResourceType.DATA, 3);
        resourceManagementPort.gatherResource(playerId, ResourceType.COMPONENTS, 50);
    }
    
    private void startGameLoop() {
        while (true) {
            try {
                Thread.sleep(ApplicationConstants.TIME_TO_SHOW_QUICK_MESSAGE);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            
            ConsoleUtils.clearConsole();
            showEnhancedDashboard();
            showMainMenu();
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            ConsoleUtils.clearConsole();
            showEnhancedDashboard();
            
            switch (choice) {
                case 1:
                    handleResourceGathering();
                    break;
                case 2:
                    handleConstruction();
                    break;
                case 3:
                    handleGuildManagement();
                    break;
                case 4:
                    handleDailyRewards();
                    break;
                case 5:
                    handlePlayerStatus();
                    break;
                case 6:
                    handleShadowGuilds();
                    break;
                case 0:
                    ConsoleDisplayUtils.printInfoMessage(ApplicationConstants.MESSAGE_FAREWELL);
                    System.exit(0);
                    break;
                default:
                    ConsoleDisplayUtils.printErrorMessage(ApplicationConstants.MESSAGE_WRONG_OPTION_TRY_AGAIN);
            }
        }
    }
    
    private void showEnhancedDashboard() {
        PlayerId playerId = new PlayerId(currentPlayer.playerId());
        List<ResourceDTO> resources = resourceManagementPort.getPlayerResources(playerId);
        
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                    🏛️  STRATEGIC GAME  🏛️                                    ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        // Player Info
        System.out.printf("║ 🏘️  Village: %-25s │ %s Phase: %-25s ║%n", 
            currentPlayer.farmName(), getPhaseIcon(), currentPlayer.techPhase().getPhaseName());
        
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        // Resources Section
        System.out.println("║                                        📦 RESOURCES 📦                                       ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        for (ResourceDTO resource : resources) {
            String icon = getResourceIcon(resource.type());
            System.out.printf("║ %s %-12s: %-8d                                                        ║%n", 
                icon, resource.description(), resource.amount());
        }
        
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private void showMainMenu() {
        ConsoleDisplayUtils.printMainMenu();
    }
    
    private void handleResourceGathering() {
        PlayerId playerId = new PlayerId(currentPlayer.playerId());
        List<ResourceType> availableResources = resourceManagementPort.getAvailableResourceTypes(playerId);
        
        ConsoleDisplayUtils.printResourceMenu();
        
        int index = 1;
        for (ResourceType resourceType : availableResources) {
            String icon = getResourceIcon(resourceType);
            ConsoleDisplayUtils.printResourceItem(index, resourceType.getDescription(), icon);
            index++;
        }
        
        ConsoleDisplayUtils.printMenuFooter();
        System.out.print("\n⚔️  Choose the resource to gather: ");
        
        int input = scanner.nextInt();
        scanner.nextLine();
        
        if (input < 1 || input > availableResources.size()) {
            ConsoleDisplayUtils.printErrorMessage(ApplicationConstants.MESSAGE_WRONG_OPTION_TRY_AGAIN);
            return;
        }
        
        ResourceType selectedResource = availableResources.get(input - 1);
        
        try {
            // Simulate resource gathering
            int gatherAmount = 10 + (int) (Math.random() * 20);
            resourceManagementPort.gatherResource(playerId, selectedResource, gatherAmount);
            
            ConsoleDisplayUtils.printSuccessMessage("Successfully gathered " + gatherAmount + " " + selectedResource.getDescription());
        } catch (Exception e) {
            ConsoleDisplayUtils.printErrorMessage("Failed to gather resource: " + e.getMessage());
        }
        
        waitForUser();
    }
    
    private void handleConstruction() {
        ConsoleDisplayUtils.printWarningMessage("Construction system not yet implemented in hexagonal architecture");
        waitForUser();
    }
    
    private void handleGuildManagement() {
        ConsoleDisplayUtils.printWarningMessage("Guild management not yet implemented in hexagonal architecture");
        waitForUser();
    }
    
    private void handleDailyRewards() {
        ConsoleDisplayUtils.printWarningMessage("Daily rewards not yet implemented in hexagonal architecture");
        waitForUser();
    }
    
    private void handlePlayerStatus() {
        PlayerId playerId = new PlayerId(currentPlayer.playerId());
        
        ConsoleDisplayUtils.printStatusHeader();
        
        System.out.println("🏰 Kingdom: " + currentPlayer.farmName());
        System.out.println("🎯 Current Era: " + currentPlayer.techPhase().getPhaseName());
        System.out.println("🏛️ Guild: " + (currentPlayer.isInGuild() ? currentPlayer.guildId() : "None"));
        
        List<ResourceDTO> resources = resourceManagementPort.getPlayerResources(playerId);
        System.out.println("\n📦 Resources:");
        for (ResourceDTO resource : resources) {
            String icon = getResourceIcon(resource.type());
            System.out.printf("  %s %s: %d%n", icon, resource.description(), resource.amount());
        }
        
        ConsoleDisplayUtils.printMenuFooter();
        waitForUser();
    }
    
    private void handleShadowGuilds() {
        ConsoleDisplayUtils.printWarningMessage("Shadow guilds not yet implemented in hexagonal architecture");
        waitForUser();
    }
    
    private String getPhaseIcon() {
        return switch (currentPlayer.techPhase()) {
            case UPRISING -> "⚡";
            case AUGMENTED_STREETS -> "🤖";
            case NEURAL_NEXUS -> "🧠";
            case DRONE_DOMINION -> "🚁";
            case QUANTUM_DAWN -> "⚛️";
            case SINGULARITY_PREP -> "🔮";
            case POST_SINGULARITY -> "🌌";
            case HYPER_MESH -> "🕸️";
            case EXO_REALITY -> "👁️";
        };
    }
    
    private String getResourceIcon(ResourceType resourceType) {
        return switch (resourceType) {
            case ENERGY -> "⚡";
            case DATA -> "💾";
            case COMPONENTS -> "🔧";
            case CIRCUITS -> "🖲️";
            case NANOMATERIALS -> "⚛️";
            case QUANTUM_ENERGY -> "🌟";
            case CRYPTO -> "💰";
        };
    }
    
    private void waitForUser() {
        ConsoleDisplayUtils.printWaitPrompt();
        scanner.nextLine();
    }
}
