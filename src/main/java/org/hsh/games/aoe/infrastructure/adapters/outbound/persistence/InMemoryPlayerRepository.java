package org.hsh.games.aoe.infrastructure.adapters.outbound.persistence;

import org.hsh.games.aoe.application.ports.outbound.PlayerRepositoryPort;
import org.hsh.games.aoe.domain.entities.Player;
import org.hsh.games.aoe.domain.valueobjects.PlayerId;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of PlayerRepositoryPort
 * 
 * @author devTASE
 */
public class InMemoryPlayerRepository implements PlayerRepositoryPort {
    
    private final ConcurrentHashMap<PlayerId, Player> players = new ConcurrentHashMap<>();
    
    @Override
    public void save(Player player) {
        players.put(player.getPlayerId(), player);
    }
    
    @Override
    public Optional<Player> findById(PlayerId playerId) {
        return Optional.ofNullable(players.get(playerId));
    }
    
    @Override
    public boolean exists(PlayerId playerId) {
        return players.containsKey(playerId);
    }
    
    @Override
    public void delete(PlayerId playerId) {
        players.remove(playerId);
    }

    public int size() {
        return players.size();
    }
}
