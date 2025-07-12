package org.hsh.games.aoe.domain.entities.achievements;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable Achievement entity representing a player achievement in the game.
 * 
 * @author devTASE
 */
public final class Achievement {
    
    private final String id;
    private final String title;
    private final String description;
    private final AchievementCategory category;
    private final int points;
    private final Map<String, Object> unlockConditions;
    
    /**
     * Creates a new Achievement instance.
     * 
     * @param id the unique identifier for the achievement
     * @param title the display title of the achievement
     * @param description the detailed description of the achievement
     * @param category the category this achievement belongs to
     * @param points the points awarded for this achievement
     * @param unlockConditions the conditions required to unlock this achievement
     * @throws IllegalArgumentException if any parameter is null or points is negative
     */
    public Achievement(String id, String title, String description, AchievementCategory category, 
                      int points, Map<String, Object> unlockConditions) {
        validateParameters(id, title, description, category, points, unlockConditions);
        
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.points = points;
        this.unlockConditions = unlockConditions != null ? 
            Collections.unmodifiableMap(unlockConditions) : Collections.emptyMap();
    }
    
    /**
     * Validates constructor parameters.
     * 
     * @param id the achievement id
     * @param title the achievement title
     * @param description the achievement description
     * @param category the achievement category
     * @param points the achievement points
     * @param unlockConditions the unlock conditions
     * @throws IllegalArgumentException if any parameter is invalid
     */
    private void validateParameters(String id, String title, String description, 
                                   AchievementCategory category, int points, 
                                   Map<String, Object> unlockConditions) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Achievement ID cannot be null or empty");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Achievement title cannot be null or empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Achievement description cannot be null or empty");
        }
        if (category == null) {
            throw new IllegalArgumentException("Achievement category cannot be null");
        }
        if (points < 0) {
            throw new IllegalArgumentException("Achievement points cannot be negative");
        }
    }
    
    public String getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public AchievementCategory getCategory() {
        return category;
    }
    
    public int getPoints() {
        return points;
    }
    
    public Map<String, Object> getUnlockConditions() {
        return unlockConditions;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Achievement that = (Achievement) o;
        return points == that.points &&
               Objects.equals(id, that.id) &&
               Objects.equals(title, that.title) &&
               Objects.equals(description, that.description) &&
               category == that.category &&
               Objects.equals(unlockConditions, that.unlockConditions);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, category, points, unlockConditions);
    }
    
    @Override
    public String toString() {
        return "Achievement{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", points=" + points +
                ", unlockConditions=" + unlockConditions +
                '}';
    }
}
