package org.hsh.games.aoe.entities.guild;

import org.hsh.games.aoe.entities.ResourceType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Record representing a territory in the shadow realm that can be controlled by guilds.
 * This is an immutable data structure that contains territory information.
 * 
 * @param id             Unique identifier for the territory
 * @param name           Display name of the territory
 * @param status         Current status of the territory (neutral, claimed, under siege)
 * @param owningGuildId  ID of the guild that owns this territory (null if neutral)
 * @param resourceBonus  Map of resource types to their bonus amounts provided by this territory
 * 
 * @author devTASE
 */
public record ShadowTerritory(
        String id,
        String name,
        TerritoryStatus status,
        String owningGuildId,
        Map<ResourceType, Integer> resourceBonus
) {
    
    /**
     * Constructor with validation
     */
    public ShadowTerritory {
        Objects.requireNonNull(id, "Territory ID cannot be null");
        Objects.requireNonNull(name, "Territory name cannot be null");
        Objects.requireNonNull(status, "Territory status cannot be null");
        Objects.requireNonNull(resourceBonus, "Resource bonus map cannot be null");
        
        if (id.isBlank()) {
            throw new IllegalArgumentException("Territory ID cannot be blank");
        }
        
        if (name.isBlank()) {
            throw new IllegalArgumentException("Territory name cannot be blank");
        }
        
        // Validate ownership logic
        if (status == TerritoryStatus.NEUTRAL && owningGuildId != null) {
            throw new IllegalArgumentException("Neutral territory cannot have an owning guild");
        }
        
        if (status.isControlledByGuild() && (owningGuildId == null || owningGuildId.isBlank())) {
            throw new IllegalArgumentException("Controlled territory must have an owning guild");
        }
        
        // Create defensive copy of the resource bonus map and validate values
        resourceBonus = new HashMap<>(resourceBonus);
        for (Map.Entry<ResourceType, Integer> entry : resourceBonus.entrySet()) {
            if (entry.getValue() < 0) {
                throw new IllegalArgumentException("Resource bonus cannot be negative: " + entry.getKey());
            }
        }
    }
    
    /**
     * Creates a new neutral territory with no resource bonus
     * @param id Territory ID
     * @param name Territory name
     * @return A new ShadowTerritory instance
     */
    public static ShadowTerritory createNeutral(String id, String name) {
        return new ShadowTerritory(id, name, TerritoryStatus.NEUTRAL, null, new HashMap<>());
    }
    
    /**
     * Creates a new neutral territory with resource bonus
     * @param id Territory ID
     * @param name Territory name
     * @param resourceBonus Resource bonus map
     * @return A new ShadowTerritory instance
     */
    public static ShadowTerritory createNeutralWithBonus(String id, String name, Map<ResourceType, Integer> resourceBonus) {
        return new ShadowTerritory(id, name, TerritoryStatus.NEUTRAL, null, resourceBonus);
    }
    
    /**
     * Creates a copy of this territory claimed by a guild
     * @param guildId The ID of the guild claiming the territory
     * @return A new ShadowTerritory instance with CLAIMED_BY_GUILD status
     */
    public ShadowTerritory claimByGuild(String guildId) {
        Objects.requireNonNull(guildId, "Guild ID cannot be null");
        if (guildId.isBlank()) {
            throw new IllegalArgumentException("Guild ID cannot be blank");
        }
        
        return new ShadowTerritory(id, name, TerritoryStatus.CLAIMED_BY_GUILD, guildId, resourceBonus);
    }
    
    /**
     * Creates a copy of this territory under siege
     * @return A new ShadowTerritory instance with UNDER_SIEGE status
     */
    public ShadowTerritory putUnderSiege() {
        return new ShadowTerritory(id, name, TerritoryStatus.UNDER_SIEGE, owningGuildId, resourceBonus);
    }
    
    /**
     * Creates a copy of this territory as neutral (liberation)
     * @return A new ShadowTerritory instance with NEUTRAL status
     */
    public ShadowTerritory liberate() {
        return new ShadowTerritory(id, name, TerritoryStatus.NEUTRAL, null, resourceBonus);
    }
    
    /**
     * Creates a copy of this territory with updated resource bonus
     * @param newResourceBonus The new resource bonus map
     * @return A new ShadowTerritory instance with updated bonus
     */
    public ShadowTerritory withResourceBonus(Map<ResourceType, Integer> newResourceBonus) {
        return new ShadowTerritory(id, name, status, owningGuildId, newResourceBonus);
    }
    
    /**
     * Gets the bonus amount for a specific resource type
     * @param resourceType The resource type to check
     * @return The bonus amount, or 0 if not present
     */
    public int getResourceBonus(ResourceType resourceType) {
        return resourceBonus.getOrDefault(resourceType, 0);
    }
    
    /**
     * Checks if this territory provides any resource bonus
     * @return true if the territory has any resource bonus
     */
    public boolean hasResourceBonus() {
        return !resourceBonus.isEmpty() && resourceBonus.values().stream().anyMatch(bonus -> bonus > 0);
    }
    
    /**
     * Checks if this territory is owned by a specific guild
     * @param guildId The guild ID to check
     * @return true if the territory is owned by the specified guild
     */
    public boolean isOwnedBy(String guildId) {
        return Objects.equals(owningGuildId, guildId);
    }
    
    /**
     * Checks if this territory is available for conquest
     * @return true if the territory can be conquered
     */
    public boolean isAvailableForConquest() {
        return status.isAvailableForConquest();
    }
    
    /**
     * Checks if this territory is generating resources
     * @return true if the territory is generating resources
     */
    public boolean isGeneratingResources() {
        return status.isGeneratingResources();
    }
    
    /**
     * Checks if this territory is in conflict
     * @return true if the territory is under siege
     */
    public boolean isInConflict() {
        return status.isInConflict();
    }
    
    /**
     * Gets the total resource bonus value (sum of all bonuses)
     * @return The total bonus value
     */
    public int getTotalResourceBonus() {
        return resourceBonus.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * Gets the display name with status emoji
     * @return The territory name with status emoji
     */
    public String getDisplayNameWithStatus() {
        return status.getEmoji() + " " + name;
    }
    
    /**
     * Gets a formatted string representation of the territory
     * @return Formatted string with territory details
     */
    public String getFormattedInfo() {
        String ownerInfo = owningGuildId != null ? " (Owned by: " + owningGuildId + ")" : "";
        return String.format("%s [%s]%s - Bonus: %d", 
                name, 
                status.getDisplayNameWithEmoji(), 
                ownerInfo, 
                getTotalResourceBonus());
    }
    
    /**
     * Returns a defensive copy of the resource bonus map
     * @return A new map containing all resource bonuses
     */
    public Map<ResourceType, Integer> getResourceBonusCopy() {
        return new HashMap<>(resourceBonus);
    }
}
