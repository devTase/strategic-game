package org.hsh.games.aoe.application.dto;

import org.hsh.games.aoe.domain.entities.resources.ResourceType;

/**
 * Data Transfer Object for Resource information
 * 
 * @author devTASE
 */
public record ResourceDTO(
    ResourceType type,
    int amount,
    String description
) {
    
    public static ResourceDTO from(org.hsh.games.aoe.domain.entities.resources.ResourceAmount resourceAmount) {
        return new ResourceDTO(
            resourceAmount.getResource(),
            resourceAmount.getAmount(),
            resourceAmount.getResource().getDescription()
        );
    }
}
