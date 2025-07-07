package org.hsh.games.aoe.entities.achievements;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AchievementProgress.
 * 
 * These tests verify the tracking of achievements including the logic for unlocking
 * and updating the progress.
 * 
 * @author devTASE
 */
class AchievementProgressTest {

    private Achievement achievement;
    private AchievementProgress progress;

    @BeforeEach
    void setUp() {
        Map<String, Object> unlockConditions = new HashMap<>();
        unlockConditions.put("tasksCompleted", 10);
        
        achievement = new Achievement("ach1", "Complete Tasks", "Complete 10 tasks to unlock.",
                                      AchievementCategory.WORKERS, 50, unlockConditions);
        progress = new AchievementProgress(achievement, null);
    }

    @Test
    void testAchievementProgressInitialization() {
        assertNotNull(progress);
        assertEquals(achievement, progress.getAchievement());
        assertFalse(progress.isUnlocked());
        assertEquals(0, progress.getCurrentProgress());
    }

    @Test
    void testSetCurrentProgress() {
        progress.setCurrentProgress(5);
        assertEquals(5, progress.getCurrentProgress());
        assertFalse(progress.isUnlocked());

        progress.setCurrentProgress(50);
        assertTrue(progress.isUnlocked());
        assertNotNull(progress.getUnlockDate());
    }

    @Test
    void testUnlockChecking() {
        progress.setCurrentProgress(49);
        assertFalse(progress.isUnlocked());

        progress.setCurrentProgress(50);
        assertTrue(progress.isUnlocked());
    }

    @Test
    void testEqualsAndHashCode() {
        AchievementProgress sameProgress = new AchievementProgress(achievement, null);
        assertEquals(progress, sameProgress);
        assertEquals(progress.hashCode(), sameProgress.hashCode());

        Achievement differentAchievement = new Achievement("ach2", "Different Task", "Complete different tasks.",
                                      AchievementCategory.WORKERS, 30, null);
        AchievementProgress differentProgress = new AchievementProgress(differentAchievement, null);
        assertNotEquals(progress, differentProgress);
    }

    @Test
    void testToString() {
        String result = progress.toString();
        assertTrue(result.contains("ach1"));
        assertTrue(result.contains("Complete Tasks"));
    }
}

