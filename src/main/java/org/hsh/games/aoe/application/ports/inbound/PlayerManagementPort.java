package org.hsh.games.aoe.application.ports.inbound;

import org.hsh.games.aoe.application.dto.PlayerDTO;
import org.hsh.games.aoe.application.dto.ResourceDTO;
import org.hsh.games.aoe.domain.valueobjects.PlayerId;

import java.util.List;

/**
 * Inbound port for player management operations
 * 
 * @author devTASE
 */
public interface PlayerManagementPort {
    
    /**
     * Creates a new player
     */
    PlayerDTO createPlayer(String farmName);
    
    /**
     * Retrieves player information
     */
    PlayerDTO getPlayer(PlayerId playerId);
    
    /**
     * Gets player resources
     */
    List<ResourceDTO> getPlayerResources(PlayerId playerId);
    
    /**
     * Updates player tech phase
     */
    void advancePlayerTechPhase(PlayerId playerId);
    
    /**
     * Checks if player can advance to next phase
     */
    boolean canAdvanceToNextPhase(PlayerId playerId);
    
    /**
     * Adds player to a guild
     */
    void joinGuild(PlayerId playerId, String guildId);
    
    /**
     * Removes player from guild
     */
    void leaveGuild(PlayerId playerId);
}
