package org.hsh.games.aoe.application.usecases;

import org.hsh.games.aoe.application.dto.ResourceDTO;
import org.hsh.games.aoe.application.ports.inbound.ResourceManagementPort;
import org.hsh.games.aoe.application.ports.outbound.ResourceRepositoryPort;
import org.hsh.games.aoe.application.ports.outbound.PlayerRepositoryPort;
import org.hsh.games.aoe.application.ports.outbound.NotificationPort;
import org.hsh.games.aoe.domain.entities.buildings.BuildingAndResourceAvailabilityPerLevel;
import org.hsh.games.aoe.domain.entities.resources.Resource;
import org.hsh.games.aoe.domain.entities.resources.ResourceType;
import org.hsh.games.aoe.domain.valueobjects.PlayerId;
import org.hsh.games.aoe.domain.valueobjects.ResourceQuantity;
import org.hsh.games.aoe.domain.exceptions.InvalidPlayerException;
import org.hsh.games.aoe.domain.exceptions.InsufficientResourcesException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Use case for managing player resources
 * 
 * @author devTASE
 */
public class ResourceManagementUseCase implements ResourceManagementPort {
    
    private final ResourceRepositoryPort resourceRepository;
    private final PlayerRepositoryPort playerRepository;
    private final NotificationPort notificationPort;
    
    public ResourceManagementUseCase(ResourceRepositoryPort resourceRepository, 
                                   PlayerRepositoryPort playerRepository, 
                                   NotificationPort notificationPort) {
        this.resourceRepository = resourceRepository;
        this.playerRepository = playerRepository;
        this.notificationPort = notificationPort;
    }
    
    @Override
    public void gatherResource(PlayerId playerId, ResourceType resourceType, int amount) {
        validatePlayer(playerId);
        
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        Resource resource = resourceRepository.findByPlayerAndType(playerId, resourceType)
            .orElse(new Resource(resourceType));
        
        resource.addToPlayerResources(new ResourceQuantity(resourceType, amount));
        resourceRepository.save(playerId, resource);
        
        notificationPort.notifySuccess("Gathered " + amount + " " + resourceType.getDescription());
    }
    
    @Override
    public void consumeResource(PlayerId playerId, ResourceType resourceType, int amount) {
        validatePlayer(playerId);
        
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        Resource resource = resourceRepository.findByPlayerAndType(playerId, resourceType)
            .orElseThrow(() -> new InsufficientResourcesException("No " + resourceType.getDescription() + " available"));
        
        try {
            resource.subtract(new ResourceQuantity(resourceType, amount));
            resourceRepository.save(playerId, resource);
            
            notificationPort.notifyInfo("Consumed " + amount + " " + resourceType.getDescription());
        } catch (InsufficientResourcesException e) {
            notificationPort.notifyError(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<ResourceDTO> getPlayerResources(PlayerId playerId) {
        validatePlayer(playerId);
        
        return resourceRepository.findByPlayer(playerId)
            .stream()
            .map(resource -> new ResourceDTO(
                resource.getType(),
                resource.getAmount(),
                resource.getType().getDescription()
            ))
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean hasEnoughResources(PlayerId playerId, ResourceType resourceType, int amount) {
        validatePlayer(playerId);
        
        return resourceRepository.findByPlayerAndType(playerId, resourceType)
            .map(resource -> resource.hasEnough(new ResourceQuantity(resourceType, amount)))
            .orElse(false);
    }
    
    @Override
    public List<ResourceType> getAvailableResourceTypes(PlayerId playerId) {
        var player = playerRepository.findById(playerId)
            .orElseThrow(() -> new InvalidPlayerException("Player not found: " + playerId));
        
        var legacyResourceTypes = BuildingAndResourceAvailabilityPerLevel
            .getByLevel(player.getTechPhase().getLevel())
            .getAvailableResources();
        
        return legacyResourceTypes.stream()
            .map(legacyType -> ResourceType.valueOf(legacyType.name()))
            .collect(Collectors.toList());
    }
    
    private void validatePlayer(PlayerId playerId) {
        if (!playerRepository.exists(playerId)) {
            throw new InvalidPlayerException("Player not found: " + playerId);
        }
    }
}
