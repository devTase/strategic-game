package org.hsh.games.aoe.application.ports.outbound;

import org.hsh.games.aoe.domain.entities.Player;
import org.hsh.games.aoe.domain.valueobjects.PlayerId;

import java.util.Optional;

/**
 * Outbound port for player persistence operations
 * 
 * @author devTASE
 */
public interface PlayerRepositoryPort {
    
    /**
     * Saves a player
     */
    void save(Player player);
    
    /**
     * Finds a player by ID
     */
    Optional<Player> findById(PlayerId playerId);
    
    /**
     * Checks if player exists
     */
    boolean exists(PlayerId playerId);
    
    /**
     * Deletes a player
     */
    void delete(PlayerId playerId);
}
