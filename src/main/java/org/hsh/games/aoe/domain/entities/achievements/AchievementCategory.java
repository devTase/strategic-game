package org.hsh.games.aoe.domain.entities.achievements;

/**
 * Enum representing different categories of achievements in the game.
 * 
 * @author devTASE
 */
public enum AchievementCategory {
    
    CONSTRUCTION("Construction", "Achievements related to building and construction"),
    RESOURCES("Resources", "Achievements related to resource collection and management"),
    WORKERS("Workers", "Achievements related to worker management and efficiency"),
    TIME("Time", "Achievements related to time-based challenges and milestones"),
    ERA_PROGRESS("Era Progress", "Achievements related to era progression and civilization development");
    
    private final String displayName;
    private final String description;
    
    AchievementCategory(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Gets the achievement category by display name
     * @param displayName the display name to search for
     * @return the matching AchievementCategory, or null if not found
     */
    public static AchievementCategory getByDisplayName(String displayName) {
        for (AchievementCategory category : values()) {
            if (category.getDisplayName().equalsIgnoreCase(displayName)) {
                return category;
            }
        }
        return null;
    }
}
