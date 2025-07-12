package org.hsh.games.aoe.application.ports.outbound;

import org.hsh.games.aoe.domain.entities.resources.Resource;
import org.hsh.games.aoe.domain.entities.resources.ResourceType;
import org.hsh.games.aoe.domain.valueobjects.PlayerId;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port for resource persistence operations
 * 
 * @author devTASE
 */
public interface ResourceRepositoryPort {
    
    /**
     * Saves a resource for a player
     */
    void save(PlayerId playerId, Resource resource);
    
    /**
     * Finds a specific resource for a player
     */
    Optional<Resource> findByPlayerAndType(PlayerId playerId, ResourceType resourceType);
    
    /**
     * Finds all resources for a player
     */
    List<Resource> findByPlayer(PlayerId playerId);
    
    /**
     * Deletes all resources for a player
     */
    void deleteByPlayer(PlayerId playerId);
}
