package org.hsh.games.aoe.entities.achievements;

import org.hsh.games.aoe.entities.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PlayerAchievements using constructor injection.
 * 
 * @author devTASE
 */
class PlayerAchievementsTest {

    private Player player;
    private PlayerAchievements playerAchievements;
    private Achievement achievement1;
    private Achievement achievement2;
    private AchievementProgress progress1;
    private AchievementProgress progress2;

    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer");
        playerAchievements = new PlayerAchievements(player);

        Map<String, Object> unlockConditions1 = new HashMap<>();
        unlockConditions1.put("buildings", 5);
        achievement1 = new Achievement("ach1", "Builder", "Build 5 buildings", 
                                     AchievementCategory.CONSTRUCTION, 100, unlockConditions1);

        Map<String, Object> unlockConditions2 = new HashMap<>();
        unlockConditions2.put("resources", 1000);
        achievement2 = new Achievement("ach2", "Collector", "Collect 1000 resources", 
                                     AchievementCategory.RESOURCES, 50, unlockConditions2);

        progress1 = new AchievementProgress(achievement1, null);
        progress2 = new AchievementProgress(achievement2, null);
    }

    @Test
    void testConstructorWithValidPlayer() {
        assertNotNull(playerAchievements);
        assertEquals(player, playerAchievements.getPlayer());
        assertEquals(0, playerAchievements.getTotalAchievementCount());
        assertEquals(0, playerAchievements.getTotalPoints());
    }

    @Test
    void testConstructorWithNullPlayerThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new PlayerAchievements(null);
        });
    }

    @Test
    void testAddAchievementProgress() {
        playerAchievements.addAchievementProgress(progress1);
        assertEquals(1, playerAchievements.getTotalAchievementCount());
        assertEquals(progress1, playerAchievements.getAchievementProgress("ach1"));
    }

    @Test
    void testAddNullAchievementProgressThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            playerAchievements.addAchievementProgress(null);
        });
    }

    @Test
    void testGetAchievementProgressWithNullId() {
        assertNull(playerAchievements.getAchievementProgress(null));
    }

    @Test
    void testGetAchievementProgressNotFound() {
        assertNull(playerAchievements.getAchievementProgress("nonexistent"));
    }

    @Test
    void testGetAllAchievementProgress() {
        playerAchievements.addAchievementProgress(progress1);
        playerAchievements.addAchievementProgress(progress2);

        Map<String, AchievementProgress> allProgress = playerAchievements.getAllAchievementProgress();
        assertEquals(2, allProgress.size());
        assertTrue(allProgress.containsKey("ach1"));
        assertTrue(allProgress.containsKey("ach2"));

        // Test immutability
        assertThrows(UnsupportedOperationException.class, () -> {
            allProgress.put("new_key", progress1);
        });
    }

    @Test
    void testGetTotalPointsWithNoUnlockedAchievements() {
        playerAchievements.addAchievementProgress(progress1);
        playerAchievements.addAchievementProgress(progress2);
        assertEquals(0, playerAchievements.getTotalPoints());
    }

    @Test
    void testGetTotalPointsWithUnlockedAchievements() {
        progress1.setCurrentProgress(100); // This should unlock achievement1 (100 points)
        progress2.setCurrentProgress(25);  // This should NOT unlock achievement2 (needs 50 points)

        playerAchievements.addAchievementProgress(progress1);
        playerAchievements.addAchievementProgress(progress2);

        assertEquals(100, playerAchievements.getTotalPoints());
    }

    @Test
    void testGetUnlockedAchievementCount() {
        progress1.setCurrentProgress(100); // unlocks
        progress2.setCurrentProgress(25);  // doesn't unlock

        playerAchievements.addAchievementProgress(progress1);
        playerAchievements.addAchievementProgress(progress2);

        assertEquals(1, playerAchievements.getUnlockedAchievementCount());
    }

    @Test
    void testIsAchievementUnlocked() {
        progress1.setCurrentProgress(100); // unlocks
        playerAchievements.addAchievementProgress(progress1);

        assertTrue(playerAchievements.isAchievementUnlocked("ach1"));
        assertFalse(playerAchievements.isAchievementUnlocked("ach2"));
        assertFalse(playerAchievements.isAchievementUnlocked("nonexistent"));
    }

    @Test
    void testSerializeStub() {
        String result = playerAchievements.serialize();
        assertEquals("{}", result);
    }

    @Test
    void testDeserializeStub() {
        // Should not throw exception
        assertDoesNotThrow(() -> {
            playerAchievements.deserialize("{}");
        });
    }

    @Test
    void testEqualsAndHashCode() {
        PlayerAchievements other = new PlayerAchievements(player);
        assertEquals(playerAchievements, other);
        assertEquals(playerAchievements.hashCode(), other.hashCode());

        Player differentPlayer = new Player("DifferentPlayer");
        PlayerAchievements differentPlayerAchievements = new PlayerAchievements(differentPlayer);
        assertNotEquals(playerAchievements, differentPlayerAchievements);
    }

    @Test
    void testToString() {
        playerAchievements.addAchievementProgress(progress1);
        String result = playerAchievements.toString();
        assertTrue(result.contains("PlayerAchievements"));
        assertTrue(result.contains("totalPoints=0"));
        assertTrue(result.contains("unlockedCount=0"));
        assertTrue(result.contains("totalCount=1"));
    }
}
