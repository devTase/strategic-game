package org.hsh.games.aoe.entities.achievements;

import org.hsh.games.aoe.entities.ResourceType;

import java.util.List;
import java.util.Objects;

/**
 * Achievement reward entity representing rewards given for completing achievements.
 * 
 * @author devTASE
 */
public class AchievementReward {
    
    private String id;
    private List<ResourceReward> resources;
    private int experiencePoints;
    private String specialReward;
    
    public AchievementReward() {
        this.experiencePoints = 0;
    }
    
    public AchievementReward(String id, List<ResourceReward> resources, int experiencePoints) {
        this.id = id;
        this.resources = resources;
        this.experiencePoints = experiencePoints;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public List<ResourceReward> getResources() {
        return resources;
    }
    
    public void setResources(List<ResourceReward> resources) {
        this.resources = resources;
    }
    
    public int getExperiencePoints() {
        return experiencePoints;
    }
    
    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }
    
    public String getSpecialReward() {
        return specialReward;
    }
    
    public void setSpecialReward(String specialReward) {
        this.specialReward = specialReward;
    }
    
    /**
     * Checks if the reward has any resources
     * @return true if there are resource rewards
     */
    public boolean hasResourceRewards() {
        return resources != null && !resources.isEmpty();
    }
    
    /**
     * Checks if the reward has experience points
     * @return true if there are experience point rewards
     */
    public boolean hasExperienceReward() {
        return experiencePoints > 0;
    }
    
    /**
     * Checks if the reward has special rewards
     * @return true if there is a special reward
     */
    public boolean hasSpecialReward() {
        return specialReward != null && !specialReward.trim().isEmpty();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AchievementReward that = (AchievementReward) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "AchievementReward{" +
                "id='" + id + '\'' +
                ", resources=" + resources +
                ", experiencePoints=" + experiencePoints +
                ", specialReward='" + specialReward + '\'' +
                '}';
    }
    
    /**
     * Inner class representing a resource reward
     */
    public static class ResourceReward {
        private ResourceType resourceType;
        private int amount;
        
        public ResourceReward() {}
        
        public ResourceReward(ResourceType resourceType, int amount) {
            this.resourceType = resourceType;
            this.amount = amount;
        }
        
        public ResourceType getResourceType() {
            return resourceType;
        }
        
        public void setResourceType(ResourceType resourceType) {
            this.resourceType = resourceType;
        }
        
        public int getAmount() {
            return amount;
        }
        
        public void setAmount(int amount) {
            this.amount = amount;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ResourceReward that = (ResourceReward) o;
            return amount == that.amount && resourceType == that.resourceType;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(resourceType, amount);
        }
        
        @Override
        public String toString() {
            return "ResourceReward{" +
                    "resourceType=" + resourceType +
                    ", amount=" + amount +
                    '}';
        }
    }
}
