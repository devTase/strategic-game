package org.hsh.games.aoe.domain.entities.rebelcell;

import org.hsh.games.aoe.domain.entities.resources.ResourceType;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the resource storage of a Rebel Cell.
 * Manages the resources with a defined capacity limit.
 * 
 * Resources are managed with a map of resource types to quantities.
 * 
 * Thread Safety: Instances of this class are immutable after creation.
 * 
 * @author devTASE
 */
public class GuildVault {
    private final int capacity;
    private final Map<ResourceType, Integer> resources;

    private GuildVault(int capacity) {
        this.capacity = capacity;
        this.resources = new HashMap<>();
    }

    public static GuildVault createEmpty(int capacity) {
        return new GuildVault(capacity);
    }

    public GuildVault depositResource(ResourceType type, int amount) {
        validateResourceOperation(type, amount);

        Map<ResourceType, Integer> newResources = new HashMap<>(resources);
        newResources.put(type, newResources.getOrDefault(type, 0) + amount);

        return new GuildVault(this.capacity, newResources);
    }

    public GuildVault withdrawResource(ResourceType type, int amount) {
        validateResourceOperation(type, -amount);

        Map<ResourceType, Integer> newResources = new HashMap<>(resources);
        newResources.put(type, newResources.getOrDefault(type, 0) - amount);

        return new GuildVault(this.capacity, newResources);
    }

    public GuildVault increaseCapacity(int additionalCapacity) {
        return new GuildVault(this.capacity + additionalCapacity, resources);
    }

    public int getTotalStoredResources() {
        return resources.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int getResourceQuantity(ResourceType type) {
        return resources.getOrDefault(type, 0);
    }

    public int capacity() {
        return capacity;
    }

    public double getCapacityUtilization() {
        return (double) getTotalStoredResources() / capacity * 100;
    }

    private void validateResourceOperation(ResourceType type, int amount) {
        if (amount + getTotalStoredResources() > capacity) {
            throw new IllegalArgumentException("Capacity exceeded");
        }

        if (getResourceQuantity(type) + amount < 0) {
            throw new IllegalArgumentException("Insufficient resources");
        }
    }

    private GuildVault(int capacity, Map<ResourceType, Integer> resources) {
        this.capacity = capacity;
        this.resources = new HashMap<>(resources);
    }
}
