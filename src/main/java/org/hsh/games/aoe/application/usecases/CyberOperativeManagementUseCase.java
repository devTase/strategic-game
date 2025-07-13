package org.hsh.games.aoe.application.usecases;

import org.hsh.games.aoe.application.dto.ResourceDTO;
import org.hsh.games.aoe.application.ports.inbound.CyberOperativeManagementPort;
import org.hsh.games.aoe.application.ports.inbound.ResourceManagementPort;
import org.hsh.games.aoe.application.ports.outbound.NotificationPort;
import org.hsh.games.aoe.application.ports.outbound.PlayerRepositoryPort;
import org.hsh.games.aoe.domain.entities.CyberOperative;
import org.hsh.games.aoe.domain.entities.Player;
import org.hsh.games.aoe.domain.entities.resources.ResourceType;
import org.hsh.games.aoe.domain.services.PlayerService;
import org.hsh.games.aoe.domain.valueobjects.PlayerId;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Use case for cyber operative management operations
 * 
 * @author devTASE
 */
public class CyberOperativeManagementUseCase implements CyberOperativeManagementPort {
    
    private final PlayerService playerService;
    private final ResourceManagementPort resourceManagementPort;
    private final NotificationPort notificationPort;
    private final PlayerRepositoryPort playerRepository;
    
    public CyberOperativeManagementUseCase(
            PlayerService playerService,
            ResourceManagementPort resourceManagementPort,
            NotificationPort notificationPort,
            PlayerRepositoryPort playerRepository) {
        this.playerService = playerService;
        this.resourceManagementPort = resourceManagementPort;
        this.notificationPort = notificationPort;
        this.playerRepository = playerRepository;
    }
    
    @Override
    public boolean searchResource(PlayerId playerId, ResourceType resourceType) {
        // 1. Validate player exists (use repository)
        Optional<Player> playerOptional = playerRepository.findById(playerId);
        if (playerOptional.isEmpty()) {
            notificationPort.notifyError("Player not found");
            return false;
        }
        
        // 2. Ask playerService.getCyberOperativeAvailable()
        CyberOperative availableOperative = playerService.getCyberOperativeAvailable();
        if (availableOperative == null) {
            return false;
        }
        
        // 3. Check current stored amount for resourceType through resourceManagementPort.getPlayerResources and its maxCap
        List<ResourceDTO> playerResources = resourceManagementPort.getPlayerResources(playerId);
        int currentAmount = playerResources.stream()
                .filter(resource -> resource.type() == resourceType)
                .mapToInt(ResourceDTO::amount)
                .sum();
        
        int maxCap = resourceType.getMaxCap();
        
        // 4. Randomly decide amount but trim so that current + amount <= cap
        int minAmount = 1; // minimum amount to find
        int maxAmount = resourceType.getAmountMaxToBeFound();
        
        int randomAmount = ThreadLocalRandom.current().nextInt(minAmount, maxAmount + 1);
        int finalAmount = Math.min(randomAmount, maxCap - currentAmount);
        
        // If no space for resources, notify and return false
        if (finalAmount <= 0) {
            notificationPort.notifyWarning("Storage capacity reached for " + resourceType.getDescription());
            return false;
        }
        
        // 5. Call playerService.sendCyberOperativesToSearchJob(resourceType) - this starts the gathering thread
        playerService.sendCyberOperativesToSearchJob(resourceType);
        
        // After starting thread, directly book the amount to player via resourceManagementPort.gatherResource
        resourceManagementPort.gatherResource(playerId, resourceType, finalAmount);
        
        // 6. Mark operative busy is done by PlayerService (handled internally by sendCyberOperativesToSearchJob)
        
        // 7. Send notificationPort.notifyInfo() with mission started and expected amount
        notificationPort.notifyInfo(String.format("Cyber operative dispatched to search for %s. Expected to find %d units.",
                resourceType.getDescription(), finalAmount));
        
        // Return true
        return true;
    }
}
