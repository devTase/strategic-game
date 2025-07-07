package org.hsh.games.aoe.entities.achievements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AchievementCategory enum.
 * 
 * @author devTASE
 */
class AchievementCategoryTest {

    @Test
    void testAllCategoriesExist() {
        assertEquals(5, AchievementCategory.values().length);
        
        assertNotNull(AchievementCategory.CONSTRUCTION);
        assertNotNull(AchievementCategory.RESOURCES);
        assertNotNull(AchievementCategory.WORKERS);
        assertNotNull(AchievementCategory.TIME);
        assertNotNull(AchievementCategory.ERA_PROGRESS);
    }

    @Test
    void testDisplayNames() {
        assertEquals("Construction", AchievementCategory.CONSTRUCTION.getDisplayName());
        assertEquals("Resources", AchievementCategory.RESOURCES.getDisplayName());
        assertEquals("Workers", AchievementCategory.WORKERS.getDisplayName());
        assertEquals("Time", AchievementCategory.TIME.getDisplayName());
        assertEquals("Era Progress", AchievementCategory.ERA_PROGRESS.getDisplayName());
    }

    @Test
    void testDescriptions() {
        assertTrue(AchievementCategory.CONSTRUCTION.getDescription().contains("building"));
        assertTrue(AchievementCategory.RESOURCES.getDescription().contains("resource"));
        assertTrue(AchievementCategory.WORKERS.getDescription().contains("worker"));
        assertTrue(AchievementCategory.TIME.getDescription().contains("time"));
        assertTrue(AchievementCategory.ERA_PROGRESS.getDescription().contains("era"));
    }

    @Test
    void testGetByDisplayName() {
        assertEquals(AchievementCategory.CONSTRUCTION, AchievementCategory.getByDisplayName("Construction"));
        assertEquals(AchievementCategory.RESOURCES, AchievementCategory.getByDisplayName("Resources"));
        assertEquals(AchievementCategory.WORKERS, AchievementCategory.getByDisplayName("Workers"));
        assertEquals(AchievementCategory.TIME, AchievementCategory.getByDisplayName("Time"));
        assertEquals(AchievementCategory.ERA_PROGRESS, AchievementCategory.getByDisplayName("Era Progress"));
    }

    @Test
    void testGetByDisplayNameCaseInsensitive() {
        assertEquals(AchievementCategory.CONSTRUCTION, AchievementCategory.getByDisplayName("construction"));
        assertEquals(AchievementCategory.RESOURCES, AchievementCategory.getByDisplayName("RESOURCES"));
        assertEquals(AchievementCategory.WORKERS, AchievementCategory.getByDisplayName("wOrKeRs"));
    }

    @Test
    void testGetByDisplayNameNotFound() {
        assertNull(AchievementCategory.getByDisplayName("NonExistent"));
        assertNull(AchievementCategory.getByDisplayName(""));
        assertNull(AchievementCategory.getByDisplayName(null));
    }
}
