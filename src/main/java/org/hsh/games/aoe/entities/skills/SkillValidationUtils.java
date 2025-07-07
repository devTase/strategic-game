package org.hsh.games.aoe.entities.skills;

public final class SkillValidationUtils {
    
    public static final int MIN_SKILL_LEVEL = 1;
    public static final int MAX_SKILL_LEVEL = 10;
    
    private SkillValidationUtils() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Validates that a skill level is within the allowed range (1-10).
     * 
     * @param level the skill level to validate
     * @throws IllegalArgumentException if the level is outside the valid range
     */
    public static void validateSkillLevel(int level) {
        if (level < MIN_SKILL_LEVEL || level > MAX_SKILL_LEVEL) {
            throw new IllegalArgumentException(
                String.format("Skill level must be between %d and %d, got: %d", 
                    MIN_SKILL_LEVEL, MAX_SKILL_LEVEL, level)
            );
        }
    }
    
    /**
     * Validates that a skill level is within the allowed range (1-10).
     * 
     * @param level the skill level to validate
     * @param parameterName the name of the parameter being validated (for error messages)
     * @throws IllegalArgumentException if the level is outside the valid range
     */
    public static void validateSkillLevel(int level, String parameterName) {
        if (level < MIN_SKILL_LEVEL || level > MAX_SKILL_LEVEL) {
            throw new IllegalArgumentException(
                String.format("%s must be between %d and %d, got: %d", 
                    parameterName, MIN_SKILL_LEVEL, MAX_SKILL_LEVEL, level)
            );
        }
    }
    
    /**
     * Validates that a skill upgrade transition is valid (toLevel = fromLevel + 1).
     * 
     * @param fromLevel the current skill level
     * @param toLevel the target skill level
     * @throws IllegalArgumentException if the upgrade is invalid
     */
    public static void validateSkillUpgrade(int fromLevel, int toLevel) {
        validateSkillLevel(fromLevel, "From level");
        validateSkillLevel(toLevel, "To level");
        
        if (toLevel != fromLevel + 1) {
            throw new IllegalArgumentException(
                String.format("Skills can only be upgraded by one level at a time. From: %d, To: %d", 
                    fromLevel, toLevel)
            );
        }
    }
    
    /**
     * Validates that an object is not null.
     * 
     * @param obj the object to validate
     * @param parameterName the name of the parameter being validated (for error messages)
     * @throws IllegalArgumentException if the object is null
     */
    public static void validateNotNull(Object obj, String parameterName) {
        if (obj == null) {
            throw new IllegalArgumentException(parameterName + " cannot be null");
        }
    }
    
    /**
     * Validates that a string is not null or empty.
     * 
     * @param str the string to validate
     * @param parameterName the name of the parameter being validated (for error messages)
     * @throws IllegalArgumentException if the string is null or empty
     */
    public static void validateNotNullOrEmpty(String str, String parameterName) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException(parameterName + " cannot be null or empty");
        }
    }
    
    /**
     * Checks if a skill level is valid without throwing an exception.
     * 
     * @param level the skill level to check
     * @return true if the level is valid, false otherwise
     */
    public static boolean isValidSkillLevel(int level) {
        return level >= MIN_SKILL_LEVEL && level <= MAX_SKILL_LEVEL;
    }
    
    /**
     * Checks if a skill upgrade is valid without throwing an exception.
     * 
     * @param fromLevel the current skill level
     * @param toLevel the target skill level
     * @return true if the upgrade is valid, false otherwise
     */
    public static boolean isValidSkillUpgrade(int fromLevel, int toLevel) {
        return isValidSkillLevel(fromLevel) && 
               isValidSkillLevel(toLevel) && 
               toLevel == fromLevel + 1;
    }
}
