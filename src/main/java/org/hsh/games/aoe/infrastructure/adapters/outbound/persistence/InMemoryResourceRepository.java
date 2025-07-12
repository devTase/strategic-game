package org.hsh.games.aoe.infrastructure.adapters.outbound.persistence;

import org.hsh.games.aoe.application.ports.outbound.ResourceRepositoryPort;
import org.hsh.games.aoe.domain.entities.resources.Resource;
import org.hsh.games.aoe.domain.entities.resources.ResourceType;
import org.hsh.games.aoe.domain.valueobjects.PlayerId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of ResourceRepositoryPort
 * 
 * @author devTASE
 */
public class InMemoryResourceRepository implements ResourceRepositoryPort {
    
    private final ConcurrentHashMap<String, Resource> resources = new ConcurrentHashMap<>();
    
    @Override
    public void save(PlayerId playerId, Resource resource) {
        String key = generateKey(playerId, resource.getType());
        resources.put(key, resource);
    }
    
    @Override
    public Optional<Resource> findByPlayerAndType(PlayerId playerId, ResourceType resourceType) {
        String key = generateKey(playerId, resourceType);
        return Optional.ofNullable(resources.get(key));
    }
    
    @Override
    public List<Resource> findByPlayer(PlayerId playerId) {
        String playerPrefix = playerId.value() + "_";
        return resources.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(playerPrefix))
            .map(Map.Entry::getValue)
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteByPlayer(PlayerId playerId) {
        String playerPrefix = playerId.value() + "_";
        resources.entrySet().removeIf(entry -> entry.getKey().startsWith(playerPrefix));
    }
    
    private String generateKey(PlayerId playerId, ResourceType resourceType) {
        return playerId.value() + "_" + resourceType.name();
    }

    public int size() {
        return resources.size();
    }
}
