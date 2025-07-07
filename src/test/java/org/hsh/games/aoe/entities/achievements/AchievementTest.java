package org.hsh.games.aoe.entities.achievements;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Achievement entity.
 * 
 * @author devTASE
 */
class AchievementTest {
    
    private Achievement achievement;
    private Map<String, Object> unlockConditions;
    
    @BeforeEach
    void setUp() {
        unlockConditions = new HashMap<>();
        unlockConditions.put("buildings_built", 10);
        unlockConditions.put("resources_collected", 500);
        
        achievement = new Achievement("test-id", "Test Achievement", "Test Description", 
                                    AchievementCategory.CONSTRUCTION, 100, unlockConditions);
    }
    
    @Test
    void testValidAchievementCreation() {
        assertNotNull(achievement);
        assertEquals("test-id", achievement.getId());
        assertEquals("Test Achievement", achievement.getTitle());
        assertEquals("Test Description", achievement.getDescription());
        assertEquals(AchievementCategory.CONSTRUCTION, achievement.getCategory());
        assertEquals(100, achievement.getPoints());
        assertEquals(unlockConditions, achievement.getUnlockConditions());
    }
    
    @Test
    void testNullIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Achievement(null, "Title", "Description", AchievementCategory.CONSTRUCTION, 100, unlockConditions);
        });
    }
    
    @Test
    void testEmptyIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Achievement("", "Title", "Description", AchievementCategory.CONSTRUCTION, 100, unlockConditions);
        });
    }
    
    @Test
    void testNullTitleThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Achievement("id", null, "Description", AchievementCategory.CONSTRUCTION, 100, unlockConditions);
        });
    }
    
    @Test
    void testEmptyTitleThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Achievement("id", "", "Description", AchievementCategory.CONSTRUCTION, 100, unlockConditions);
        });
    }
    
    @Test
    void testNullDescriptionThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Achievement("id", "Title", null, AchievementCategory.CONSTRUCTION, 100, unlockConditions);
        });
    }
    
    @Test
    void testEmptyDescriptionThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Achievement("id", "Title", "", AchievementCategory.CONSTRUCTION, 100, unlockConditions);
        });
    }
    
    @Test
    void testNullCategoryThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Achievement("id", "Title", "Description", null, 100, unlockConditions);
        });
    }
    
    @Test
    void testNegativePointsThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Achievement("id", "Title", "Description", AchievementCategory.CONSTRUCTION, -1, unlockConditions);
        });
    }
    
    @Test
    void testZeroPointsIsValid() {
        assertDoesNotThrow(() -> {
            new Achievement("id", "Title", "Description", AchievementCategory.CONSTRUCTION, 0, unlockConditions);
        });
    }
    
    @Test
    void testNullUnlockConditionsCreatesEmptyMap() {
        Achievement achievementWithNullConditions = new Achievement(
            "id", "Title", "Description", AchievementCategory.CONSTRUCTION, 100, null);
        assertNotNull(achievementWithNullConditions.getUnlockConditions());
        assertTrue(achievementWithNullConditions.getUnlockConditions().isEmpty());
    }
    
    @Test
    void testUnlockConditionsAreImmutable() {
        Map<String, Object> conditions = achievement.getUnlockConditions();
        assertThrows(UnsupportedOperationException.class, () -> {
            conditions.put("new_condition", 50);
        });
    }
    
    @Test
    void testEqualsAndHashCode() {
        Achievement achievement1 = new Achievement("id", "Title", "Description", 
                                                 AchievementCategory.CONSTRUCTION, 100, unlockConditions);
        Achievement achievement2 = new Achievement("id", "Title", "Description", 
                                                 AchievementCategory.CONSTRUCTION, 100, unlockConditions);
        Achievement achievement3 = new Achievement("different-id", "Title", "Description", 
                                                 AchievementCategory.CONSTRUCTION, 100, unlockConditions);
        
        assertEquals(achievement1, achievement2);
        assertEquals(achievement1.hashCode(), achievement2.hashCode());
        assertNotEquals(achievement1, achievement3);
    }
    
    @Test
    void testToString() {
        String result = achievement.toString();
        assertTrue(result.contains("test-id"));
        assertTrue(result.contains("Test Achievement"));
        assertTrue(result.contains("CONSTRUCTION"));
    }
}
