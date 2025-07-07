package org.hsh.games.aoe.entities.achievements;

import org.hsh.games.aoe.entities.EraAge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class containing static achievement definitions for the game.
 * Provides an unmodifiable list of all available achievements.
 * 
 * @author devTASE
 */
public final class AchievementDefinitions {
    
    private static final List<Achievement> ACHIEVEMENTS = createAchievements();
    
    /**
     * Private constructor to prevent instantiation.
     */
    private AchievementDefinitions() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Returns an unmodifiable list of all available achievements.
     * 
     * @return unmodifiable list of achievements
     */
    public static List<Achievement> getAllAchievements() {
        return ACHIEVEMENTS;
    }
    
    /**
     * Returns an unmodifiable list of achievements for a specific category.
     * 
     * @param category the achievement category to filter by
     * @return unmodifiable list of achievements in the specified category
     */
    public static List<Achievement> getAchievementsByCategory(AchievementCategory category) {
        return ACHIEVEMENTS.stream()
                .filter(achievement -> achievement.getCategory() == category)
                .toList();
    }
    
    /**
     * Creates and initializes all achievement definitions.
     * 
     * @return list of all achievements
     */
    private static List<Achievement> createAchievements() {
        List<Achievement> achievements = new ArrayList<>();
        
        // Construction Achievements
        achievements.add(createConstructionAchievement(
            "first_house", 
            "First House", 
            "Build your first house to shelter your people", 
            10, 
            Map.of("buildingType", "HOUSE", "count", 1)
        ));
        
        achievements.add(createConstructionAchievement(
            "city_builder", 
            "City Builder", 
            "Build a total of 10 buildings", 
            50, 
            Map.of("totalBuildings", 10)
        ));
        
        achievements.add(createConstructionAchievement(
            "architect", 
            "Architect", 
            "Build the maximum number of each building type", 
            100, 
            Map.of("maxEachBuilding", true)
        ));
        
        achievements.add(createConstructionAchievement(
            "town_founder", 
            "Town Founder", 
            "Build your first Town Center", 
            25, 
            Map.of("buildingType", "TOWN_CENTER", "count", 1)
        ));
        
        achievements.add(createConstructionAchievement(
            "master_builder", 
            "Master Builder", 
            "Build 25 buildings in total", 
            75, 
            Map.of("totalBuildings", 25)
        ));
        
        // Resource Achievements
        achievements.add(createResourceAchievement(
            "lumberjack", 
            "Lumberjack", 
            "Collect 1000 wood", 
            30, 
            Map.of("resourceType", "WOOD", "amount", 1000)
        ));
        
        achievements.add(createResourceAchievement(
            "balanced_stock", 
            "Balanced Stock", 
            "Have 500 of each resource type", 
            75, 
            Map.of("eachResourceAmount", 500)
        ));
        
        achievements.add(createResourceAchievement(
            "rock_collector", 
            "Rock Collector", 
            "Collect 1000 stone", 
            30, 
            Map.of("resourceType", "STONE", "amount", 1000)
        ));
        
        achievements.add(createResourceAchievement(
            "food_gatherer", 
            "Food Gatherer", 
            "Collect 1000 food", 
            30, 
            Map.of("resourceType", "FOOD", "amount", 1000)
        ));
        
        achievements.add(createResourceAchievement(
            "hoarder", 
            "Hoarder", 
            "Have 2000 of any single resource", 
            60, 
            Map.of("singleResourceAmount", 2000)
        ));
        
        // Worker Achievements
        achievements.add(createWorkerAchievement(
            "team_of_five", 
            "Team of Five", 
            "Have 5 workers at the same time", 
            25, 
            Map.of("simultaneousWorkers", 5)
        ));
        
        achievements.add(createWorkerAchievement(
            "task_master", 
            "Task Master", 
            "Complete 50 tasks", 
            50, 
            Map.of("completedTasks", 50)
        ));
        
        achievements.add(createWorkerAchievement(
            "workforce_manager", 
            "Workforce Manager", 
            "Have 10 workers at the same time", 
            60, 
            Map.of("simultaneousWorkers", 10)
        ));
        
        achievements.add(createWorkerAchievement(
            "efficiency_expert", 
            "Efficiency Expert", 
            "Complete 100 tasks", 
            100, 
            Map.of("completedTasks", 100)
        ));
        
        // Time Achievements
        achievements.add(createTimeAchievement(
            "one_week_veteran", 
            "One-Week Veteran", 
            "Play for 7 days", 
            50, 
            Map.of("playDays", 7)
        ));
        
        achievements.add(createTimeAchievement(
            "speed_builder", 
            "Speed Builder", 
            "Construct a building in less than 5 seconds", 
            40, 
            Map.of("constructionTime", 5)
        ));
        
        achievements.add(createTimeAchievement(
            "marathon_player", 
            "Marathon Player", 
            "Play for 30 days", 
            150, 
            Map.of("playDays", 30)
        ));
        
        achievements.add(createTimeAchievement(
            "lightning_fast", 
            "Lightning Fast", 
            "Construct a building in less than 2 seconds", 
            80, 
            Map.of("constructionTime", 2)
        ));
        
        // Era Progress Achievements (one per era)
        achievements.add(createEraAchievement(
            "stone_age_pioneer", 
            "Stone Age Pioneer", 
            "Reach the Stone Age", 
            10, 
            EraAge.STONE_AGE
        ));
        
        achievements.add(createEraAchievement(
            "bronze_age_ruler", 
            "Bronze Age Ruler", 
            "Reach the Bronze Age", 
            20, 
            EraAge.BRONZE_AGE
        ));
        
        achievements.add(createEraAchievement(
            "iron_age_warrior", 
            "Iron Age Warrior", 
            "Reach the Iron Age", 
            30, 
            EraAge.IRON_AGE
        ));
        
        achievements.add(createEraAchievement(
            "medieval_lord", 
            "Medieval Lord", 
            "Reach the Medieval Age", 
            40, 
            EraAge.MEDIEVAL_AGE
        ));
        
        achievements.add(createEraAchievement(
            "renaissance_scholar", 
            "Renaissance Scholar", 
            "Reach the Renaissance", 
            50, 
            EraAge.RENAISSANCE
        ));
        
        achievements.add(createEraAchievement(
            "industrial_magnate", 
            "Industrial Magnate", 
            "Reach the Industrial Age", 
            60, 
            EraAge.INDUSTRIAL_AGE
        ));
        
        achievements.add(createEraAchievement(
            "modern_leader", 
            "Modern Leader", 
            "Reach the Modern Age", 
            70, 
            EraAge.MODERN_AGE
        ));
        
        achievements.add(createEraAchievement(
            "information_master", 
            "Information Master", 
            "Reach the Information Age", 
            80, 
            EraAge.INFORMATION_AGE
        ));
        
        achievements.add(createEraAchievement(
            "future_visionary", 
            "Future Visionary", 
            "Reach the Future Age", 
            90, 
            EraAge.FUTURE_AGE
        ));
        
        // Special Era Achievement
        achievements.add(createEraAchievement(
            "historian", 
            "Historian", 
            "Reach Era 9 (Future Age)", 
            200, 
            EraAge.FUTURE_AGE
        ));
        
        return Collections.unmodifiableList(achievements);
    }
    
    /**
     * Helper method to create construction achievements.
     */
    private static Achievement createConstructionAchievement(String id, String title, String description, 
                                                           int points, Map<String, Object> conditions) {
        return new Achievement(id, title, description, AchievementCategory.CONSTRUCTION, points, conditions);
    }
    
    /**
     * Helper method to create resource achievements.
     */
    private static Achievement createResourceAchievement(String id, String title, String description, 
                                                       int points, Map<String, Object> conditions) {
        return new Achievement(id, title, description, AchievementCategory.RESOURCES, points, conditions);
    }
    
    /**
     * Helper method to create worker achievements.
     */
    private static Achievement createWorkerAchievement(String id, String title, String description, 
                                                     int points, Map<String, Object> conditions) {
        return new Achievement(id, title, description, AchievementCategory.WORKERS, points, conditions);
    }
    
    /**
     * Helper method to create time-based achievements.
     */
    private static Achievement createTimeAchievement(String id, String title, String description, 
                                                   int points, Map<String, Object> conditions) {
        return new Achievement(id, title, description, AchievementCategory.TIME, points, conditions);
    }
    
    /**
     * Helper method to create era progress achievements.
     */
    private static Achievement createEraAchievement(String id, String title, String description, 
                                                  int points, EraAge targetEra) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("targetEra", targetEra.name());
        conditions.put("eraLevel", targetEra.getLevel());
        
        return new Achievement(id, title, description, AchievementCategory.ERA_PROGRESS, points, conditions);
    }
}
