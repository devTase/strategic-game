package org.hsh.games.aoe.infrastructure.configuration;

import org.hsh.games.aoe.application.ports.inbound.CyberOperativeManagementPort;
import org.hsh.games.aoe.application.ports.inbound.PlayerManagementPort;
import org.hsh.games.aoe.application.ports.inbound.ResourceManagementPort;
import org.hsh.games.aoe.application.ports.outbound.NotificationPort;
import org.hsh.games.aoe.application.ports.outbound.PlayerRepositoryPort;
import org.hsh.games.aoe.application.ports.outbound.ResourceRepositoryPort;
import org.hsh.games.aoe.application.usecases.CreatePlayerUseCase;
import org.hsh.games.aoe.application.usecases.CyberOperativeManagementUseCase;
import org.hsh.games.aoe.application.usecases.ResourceManagementUseCase;
import org.hsh.games.aoe.domain.entities.Player;
import org.hsh.games.aoe.domain.services.PlayerService;
import org.hsh.games.aoe.infrastructure.adapters.inbound.console.ConsoleGameController;
import org.hsh.games.aoe.infrastructure.adapters.outbound.notification.ConsoleNotificationAdapter;
import org.hsh.games.aoe.infrastructure.adapters.outbound.persistence.InMemoryPlayerRepository;
import org.hsh.games.aoe.infrastructure.adapters.outbound.persistence.InMemoryResourceRepository;

/**
 * Application configuration for dependency injection
 * 
 * @author devTASE
 */
public class ApplicationBootstrapConfig {
    
    // Repositories
    private final PlayerRepositoryPort playerRepository;
    private final ResourceRepositoryPort resourceRepository;
    
    // Adapters
    private final NotificationPort notificationPort;
    
    // Use Cases
    private final PlayerManagementPort playerManagementPort;
    private final ResourceManagementPort resourceManagementPort;
    private final CyberOperativeManagementPort cyberOperativeManagementPort;
    
    // Controllers
    private final ConsoleGameController consoleGameController;
    
    public ApplicationBootstrapConfig() {
        // Initialize repositories
        this.playerRepository = new InMemoryPlayerRepository();
        this.resourceRepository = new InMemoryResourceRepository();
        
        // Initialize adapters
        this.notificationPort = new ConsoleNotificationAdapter();
        
        // Initialize use cases with constructor injection
        this.playerManagementPort = new CreatePlayerUseCase(playerRepository, notificationPort);
        this.resourceManagementPort = new ResourceManagementUseCase(resourceRepository, playerRepository, notificationPort);
        
        // Create a default player for PlayerService instantiation
        // Note: In a real application, PlayerService should be created per player instance
        Player defaultPlayer = new Player("DefaultPlayer");
        playerRepository.save(defaultPlayer);
        
        // Initialize PlayerService right after player creation is moved to repository
        PlayerService playerService = new PlayerService(defaultPlayer);
        
        // Initialize CyberOperativeManagementUseCase with dependencies and expose as CyberOperativeManagementPort
        this.cyberOperativeManagementPort = new CyberOperativeManagementUseCase(
            playerService,
            resourceManagementPort,
            notificationPort,
            playerRepository
        );
        
        // Initialize controllers
        this.consoleGameController = new ConsoleGameController(playerManagementPort, resourceManagementPort, cyberOperativeManagementPort);
    }
    
    public ConsoleGameController getConsoleGameController() {
        return consoleGameController;
    }
    
    public PlayerRepositoryPort getPlayerRepository() {
        return playerRepository;
    }
    
    public ResourceRepositoryPort getResourceRepository() {
        return resourceRepository;
    }
    
    public NotificationPort getNotificationPort() {
        return notificationPort;
    }
    
    public PlayerManagementPort getPlayerManagementPort() {
        return playerManagementPort;
    }
    
    public ResourceManagementPort getResourceManagementPort() {
        return resourceManagementPort;
    }
    
    public CyberOperativeManagementPort getCyberOperativeManagementPort() {
        return cyberOperativeManagementPort;
    }
}
