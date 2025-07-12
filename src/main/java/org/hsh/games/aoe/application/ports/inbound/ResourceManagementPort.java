package org.hsh.games.aoe.application.ports.inbound;

import org.hsh.games.aoe.application.dto.ResourceDTO;
import org.hsh.games.aoe.domain.entities.resources.ResourceType;
import org.hsh.games.aoe.domain.valueobjects.PlayerId;

import java.util.List;

/**
 * Inbound port for resource management operations
 * 
 * @author devTASE
 */
public interface ResourceManagementPort {
    
    /**
     * Gathers resources for a player
     */
    void gatherResource(PlayerId playerId, ResourceType resourceType, int amount);
    
    /**
     * Consumes resources for a player
     */
    void consumeResource(PlayerId playerId, ResourceType resourceType, int amount);
    
    /**
     * Gets available resources for a player
     */
    List<ResourceDTO> getPlayerResources(PlayerId playerId);
    
    /**
     * Checks if player has enough resources
     */
    boolean hasEnoughResources(PlayerId playerId, ResourceType resourceType, int amount);
    
    /**
     * Gets available resource types for current tech phase
     */
    List<ResourceType> getAvailableResourceTypes(PlayerId playerId);
}
