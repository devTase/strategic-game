package org.hsh.games.aoe.services;

import org.hsh.games.aoe.entities.*;
import org.hsh.games.aoe.entities.skills.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SkillService functionality.
 */
class SkillServiceTest {
    
    private SkillService skillService;
    private PlayerService playerService;
    private Player player;
    
    @BeforeEach
    void setUp() {
        skillService = SkillService.getInstance();
        player = new Player("TestPlayer");
        playerService = new PlayerService(player);
        
        // Add sufficient resources for testing
        playerService.addResource(ResourceType.WOOD, 10000);
        playerService.addResource(ResourceType.FOOD, 10000);
        playerService.addResource(ResourceType.STONE, 10000);
        
        // Add Academy building
        Building academy = new Building(true, ConstructionType.ACADEMY);
        academy.setBuilt(true);
        playerService.addNewBuilding(academy);
    }
    
    @Test
    void testGetInstance_ReturnsSameInstance() {
        SkillService instance1 = SkillService.getInstance();
        SkillService instance2 = SkillService.getInstance();
        
        assertSame(instance1, instance2, "getInstance should return the same singleton instance");
    }
    
    @Test
    void testListSkills_ReturnsAllSkills() {
        Map<SkillType, Skill> skills = skillService.listSkills(playerService);
        
        assertNotNull(skills);
        assertEquals(3, skills.size(), "Should return all 3 skill types");
        assertTrue(skills.containsKey(SkillType.CONSTRUCTION_SPEED));
        assertTrue(skills.containsKey(SkillType.CONSTRUCTION_COST));
        assertTrue(skills.containsKey(SkillType.PLUNDER_BONUS));
        
        // Verify initial levels are 1
        assertEquals(1, skills.get(SkillType.CONSTRUCTION_SPEED).getLevel());
        assertEquals(1, skills.get(SkillType.CONSTRUCTION_COST).getLevel());
        assertEquals(1, skills.get(SkillType.PLUNDER_BONUS).getLevel());
    }
    
    @Test
    void testListSkills_NullPlayerService_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            skillService.listSkills(null);
        });
    }
    
    @Test
    void testGetCurrentUpgrade_NoUpgrade_ReturnsNull() {
        SkillUpgradeProcess currentUpgrade = skillService.getCurrentUpgrade(playerService);
        
        assertNull(currentUpgrade, "Should return null when no upgrade is running");
    }
    
    @Test
    void testGetCurrentUpgrade_NullPlayerService_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            skillService.getCurrentUpgrade(null);
        });
    }
    
    @Test
    void testStartUpgrade_ValidRequest_StartsUpgrade() throws InterruptedException {
        // Test starting an upgrade
        skillService.startUpgrade(playerService, SkillType.CONSTRUCTION_SPEED);
        
        // Verify upgrade process was created
        SkillUpgradeProcess currentUpgrade = skillService.getCurrentUpgrade(playerService);
        assertNotNull(currentUpgrade, "Upgrade process should be created");
        assertEquals(SkillType.CONSTRUCTION_SPEED, currentUpgrade.getSkillType());
        assertEquals(1, currentUpgrade.getFromLevel());
        assertEquals(2, currentUpgrade.getToLevel());
        assertFalse(currentUpgrade.isFinished());
        
        // Verify worker is occupied
        assertTrue(playerService.getWorkers().stream().anyMatch(Worker::isOccupied),
                  "At least one worker should be occupied");
        
        // Wait for upgrade to complete (should be quick due to test duration)
        Thread.sleep(100); // Give thread time to start
    }
    
    @Test
    void testStartUpgrade_NoAcademy_ThrowsException() {
        // Remove academy
        playerService.getBuildingList().clear();
        
        assertThrows(IllegalStateException.class, () -> {
            skillService.startUpgrade(playerService, SkillType.CONSTRUCTION_SPEED);
        });
    }
    
    @Test
    void testStartUpgrade_AnotherUpgradeRunning_ThrowsException() {
        // Start first upgrade
        skillService.startUpgrade(playerService, SkillType.CONSTRUCTION_SPEED);
        
        // Try to start second upgrade
        assertThrows(IllegalStateException.class, () -> {
            skillService.startUpgrade(playerService, SkillType.CONSTRUCTION_COST);
        });
    }
    
    @Test
    void testStartUpgrade_MaxLevel_ThrowsException() {
        // Set skill to max level
        PlayerSkills playerSkills = playerService.getPlayerSkills();
        Skill maxLevelSkill = new Skill(
            SkillType.CONSTRUCTION_SPEED, 
            10, 
            "Max level skill", 
            0.05
        );
        playerSkills.updateSkill(maxLevelSkill);
        
        assertThrows(IllegalStateException.class, () -> {
            skillService.startUpgrade(playerService, SkillType.CONSTRUCTION_SPEED);
        });
    }
    
    @Test
    void testStartUpgrade_InsufficientResources_ThrowsException() {
        // Clear resources
        playerService.getPlayerResources().clear();
        
        assertThrows(IllegalStateException.class, () -> {
            skillService.startUpgrade(playerService, SkillType.CONSTRUCTION_SPEED);
        });
    }
    
    @Test
    void testStartUpgrade_NoWorkerAvailable_ThrowsException() {
        // Occupy all workers
        playerService.getWorkers().forEach(worker -> worker.setOccupied(true));
        
        assertThrows(IllegalStateException.class, () -> {
            skillService.startUpgrade(playerService, SkillType.CONSTRUCTION_SPEED);
        });
    }
    
    @Test
    void testStartUpgrade_NullArguments_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            skillService.startUpgrade(null, SkillType.CONSTRUCTION_SPEED);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            skillService.startUpgrade(playerService, null);
        });
    }
}
