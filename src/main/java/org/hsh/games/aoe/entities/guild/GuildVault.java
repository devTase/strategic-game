package org.hsh.games.aoe.entities.guild;

import org.hsh.games.aoe.entities.ResourceType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Record representing a guild's vault that stores resources.
 * This is an immutable data structure that contains resource storage information.
 * 
 * @param resources Map of resource types to their quantities
 * @param capacity  Maximum storage capacity of the vault
 * 
 * @author devTASE
 */
public record GuildVault(
        Map<ResourceType, Integer> resources,
        int capacity
) {
    
    /**
     * Constructor with validation
     */
    public GuildVault {
        Objects.requireNonNull(resources, "Resources map cannot be null");
        
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        
        // Create defensive copy of the resources map
        resources = new HashMap<>(resources);
        
        // Validate that no resource quantity is negative
        for (Map.Entry<ResourceType, Integer> entry : resources.entrySet()) {
            if (entry.getValue() < 0) {
                throw new IllegalArgumentException("Resource quantity cannot be negative: " + entry.getKey());
            }
        }
    }
    
    /**
     * Creates a new empty vault with the specified capacity
     * @param capacity The storage capacity
     * @return A new GuildVault instance
     */
    public static GuildVault createEmpty(int capacity) {
        return new GuildVault(new HashMap<>(), capacity);
    }
    
    /**
     * Creates a new vault with initial resources
     * @param initialResources Initial resource quantities
     * @param capacity The storage capacity
     * @return A new GuildVault instance
     */
    public static GuildVault createWithResources(Map<ResourceType, Integer> initialResources, int capacity) {
        return new GuildVault(initialResources, capacity);
    }
    
    /**
     * Deposits a resource into the vault
     * @param resourceType The type of resource to deposit
     * @param amount The amount to deposit
     * @return A new GuildVault instance with the deposited resource
     * @throws IllegalArgumentException if amount is negative or would exceed capacity
     */
    public GuildVault depositResource(ResourceType resourceType, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Deposit amount cannot be negative");
        }
        
        if (amount == 0) {
            return this; // No change needed
        }
        
        int currentAmount = resources.getOrDefault(resourceType, 0);
        int newAmount = currentAmount + amount;
        
        if (getTotalStoredResources() + amount > capacity) {
            throw new IllegalArgumentException("Deposit would exceed vault capacity");
        }
        
        Map<ResourceType, Integer> newResources = new HashMap<>(resources);
        newResources.put(resourceType, newAmount);
        
        return new GuildVault(newResources, capacity);
    }
    
    /**
     * Withdraws a resource from the vault
     * @param resourceType The type of resource to withdraw
     * @param amount The amount to withdraw
     * @return A new GuildVault instance with the withdrawn resource
     * @throws IllegalArgumentException if amount is negative or insufficient resources
     */
    public GuildVault withdrawResource(ResourceType resourceType, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Withdrawal amount cannot be negative");
        }
        
        if (amount == 0) {
            return this; // No change needed
        }
        
        int currentAmount = resources.getOrDefault(resourceType, 0);
        if (currentAmount < amount) {
            throw new IllegalArgumentException("Insufficient resources for withdrawal");
        }
        
        Map<ResourceType, Integer> newResources = new HashMap<>(resources);
        if (currentAmount == amount) {
            newResources.remove(resourceType);
        } else {
            newResources.put(resourceType, currentAmount - amount);
        }
        
        return new GuildVault(newResources, capacity);
    }
    
    /**
     * Gets the quantity of a specific resource
     * @param resourceType The resource type to check
     * @return The quantity of the resource, or 0 if not present
     */
    public int getResourceQuantity(ResourceType resourceType) {
        return resources.getOrDefault(resourceType, 0);
    }
    
    /**
     * Checks if the vault has enough of a specific resource
     * @param resourceType The resource type to check
     * @param requiredAmount The required amount
     * @return true if the vault has enough of the resource
     */
    public boolean hasEnoughResource(ResourceType resourceType, int requiredAmount) {
        return getResourceQuantity(resourceType) >= requiredAmount;
    }
    
    /**
     * Gets the total number of resources stored
     * @return The sum of all resource quantities
     */
    public int getTotalStoredResources() {
        return resources.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * Gets the remaining capacity
     * @return The available storage space
     */
    public int getRemainingCapacity() {
        return capacity - getTotalStoredResources();
    }
    
    /**
     * Checks if the vault is full
     * @return true if no more resources can be stored
     */
    public boolean isFull() {
        return getTotalStoredResources() >= capacity;
    }
    
    /**
     * Checks if the vault is empty
     * @return true if no resources are stored
     */
    public boolean isEmpty() {
        return resources.isEmpty() || getTotalStoredResources() == 0;
    }
    
    /**
     * Gets the capacity utilization percentage
     * @return The percentage of capacity used (0-100)
     */
    public double getCapacityUtilization() {
        return (double) getTotalStoredResources() / capacity * 100;
    }
    
    /**
     * Creates a copy of the vault with increased capacity
     * @param additionalCapacity The additional capacity to add
     * @return A new GuildVault instance with increased capacity
     */
    public GuildVault increaseCapacity(int additionalCapacity) {
        if (additionalCapacity <= 0) {
            throw new IllegalArgumentException("Additional capacity must be positive");
        }
        
        return new GuildVault(resources, capacity + additionalCapacity);
    }
    
    /**
     * Returns a defensive copy of the resources map
     * @return A new map containing all resources
     */
    public Map<ResourceType, Integer> getResourcesCopy() {
        return new HashMap<>(resources);
    }
}
