package org.hsh.games.aoe.entities.achievements;

import org.hsh.games.aoe.entities.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class managing achievements for a specific player.
 * 
 * This class is injected with a Player instance and maintains a map of 
 * achievement progress, providing methods to calculate total points,
 * add achievements, and perform lookups.
 * 
 * @author devTASE
 */
public class PlayerAchievements {
    
    private final Player player;
    private final Map<String, AchievementProgress> achievementProgressMap;
    
    /**
     * Constructor for PlayerAchievements.
     * 
     * @param player the player this achievement manager belongs to
     * @throws IllegalArgumentException if player is null
     */
    public PlayerAchievements(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        this.player = player;
        this.achievementProgressMap = new HashMap<>();
    }
    
    /**
     * Gets the player associated with this achievement manager.
     * 
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Adds an achievement progress to the player's collection.
     * 
     * @param achievementProgress the achievement progress to add
     * @throws IllegalArgumentException if achievementProgress is null
     */
    public void addAchievementProgress(AchievementProgress achievementProgress) {
        if (achievementProgress == null) {
            throw new IllegalArgumentException("Achievement progress cannot be null");
        }
        achievementProgressMap.put(achievementProgress.getAchievement().getId(), achievementProgress);
    }
    
    /**
     * Looks up an achievement progress by its ID.
     * 
     * @param achievementId the ID of the achievement to look up
     * @return the achievement progress, or null if not found
     */
    public AchievementProgress getAchievementProgress(String achievementId) {
        if (achievementId == null) {
            return null;
        }
        return achievementProgressMap.get(achievementId);
    }
    
    /**
     * Gets all achievement progress entries.
     * 
     * @return an unmodifiable map of achievement progress entries
     */
    public Map<String, AchievementProgress> getAllAchievementProgress() {
        return Collections.unmodifiableMap(achievementProgressMap);
    }
    
    /**
     * Calculates the total points earned by the player from unlocked achievements.
     * 
     * @return the total points
     */
    public int getTotalPoints() {
        return achievementProgressMap.values().stream()
                .filter(AchievementProgress::isUnlocked)
                .mapToInt(progress -> progress.getAchievement().getPoints())
                .sum();
    }
    
    /**
     * Gets the number of unlocked achievements.
     * 
     * @return the count of unlocked achievements
     */
    public int getUnlockedAchievementCount() {
        return (int) achievementProgressMap.values().stream()
                .filter(AchievementProgress::isUnlocked)
                .count();
    }
    
    /**
     * Gets the total number of achievements tracked for this player.
     * 
     * @return the total count of achievements
     */
    public int getTotalAchievementCount() {
        return achievementProgressMap.size();
    }
    
    /**
     * Checks if a specific achievement is unlocked.
     * 
     * @param achievementId the ID of the achievement to check
     * @return true if the achievement is unlocked, false otherwise
     */
    public boolean isAchievementUnlocked(String achievementId) {
        AchievementProgress progress = getAchievementProgress(achievementId);
        return progress != null && progress.isUnlocked();
    }
    
    // Serialization stubs for future persistence
    
    /**
     * Stub method for serializing player achievements data.
     * This will be implemented in future versions for data persistence.
     * 
     * @return serialized data representation
     */
    public String serialize() {
        // TODO: Implement serialization logic for persistence
        return "{}";
    }
    
    /**
     * Stub method for deserializing player achievements data.
     * This will be implemented in future versions for data persistence.
     * 
     * @param data the serialized data to deserialize
     */
    public void deserialize(String data) {
        // TODO: Implement deserialization logic for persistence
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerAchievements that = (PlayerAchievements) o;
        return Objects.equals(player, that.player);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(player);
    }
    
    @Override
    public String toString() {
        return "PlayerAchievements{" +
                "player=" + player +
                ", totalPoints=" + getTotalPoints() +
                ", unlockedCount=" + getUnlockedAchievementCount() +
                ", totalCount=" + getTotalAchievementCount() +
                '}';
    }
}
