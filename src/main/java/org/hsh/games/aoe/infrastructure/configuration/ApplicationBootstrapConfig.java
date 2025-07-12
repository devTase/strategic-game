package org.hsh.games.aoe.infrastructure.configuration;

import org.hsh.games.aoe.application.ports.inbound.PlayerManagementPort;
import org.hsh.games.aoe.application.ports.inbound.ResourceManagementPort;
import org.hsh.games.aoe.application.ports.outbound.NotificationPort;
import org.hsh.games.aoe.application.ports.outbound.PlayerRepositoryPort;
import org.hsh.games.aoe.application.ports.outbound.ResourceRepositoryPort;
import org.hsh.games.aoe.application.usecases.CreatePlayerUseCase;
import org.hsh.games.aoe.application.usecases.ResourceManagementUseCase;
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
        
        // Initialize controllers
        this.consoleGameController = new ConsoleGameController(playerManagementPort, resourceManagementPort);
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
}
