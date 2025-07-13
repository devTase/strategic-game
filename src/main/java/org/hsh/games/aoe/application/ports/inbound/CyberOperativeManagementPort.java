package org.hsh.games.aoe.application.ports.inbound;

import org.hsh.games.aoe.domain.entities.resources.ResourceType;
import org.hsh.games.aoe.domain.valueobjects.PlayerId;

/**
 * Inbound port for cyber operative management operations
 * 
 * @author devTASE
 */
public interface CyberOperativeManagementPort {
    
    /**
     * Searches for a resource using a cyber operative
     * Returns true when a Cyber-Operative was dispatched, false when no operative is free
     * 
     * @param playerId the ID of the player requesting the search
     * @param resourceType the type of resource to search for
     * @return true if a cyber operative was dispatched, false if no operative is available
     */
    boolean searchResource(PlayerId playerId, ResourceType resourceType);
}
