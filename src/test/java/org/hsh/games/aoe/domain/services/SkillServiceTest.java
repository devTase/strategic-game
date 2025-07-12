package org.hsh.games.aoe.domain.services;

import org.hsh.games.aoe.domain.entities.CyberOperative;
import org.hsh.games.aoe.domain.entities.Player;
import org.hsh.games.aoe.domain.entities.buildings.Building;
import org.hsh.games.aoe.domain.entities.buildings.ConstructionType;
import org.hsh.games.aoe.domain.entities.resources.ResourceAmount;
import org.hsh.games.aoe.domain.entities.resources.ResourceType;
import org.hsh.games.aoe.domain.entities.skills.PlayerSkills;
import org.hsh.games.aoe.domain.entities.skills.Skill;
import org.hsh.games.aoe.domain.entities.skills.SkillType;
import org.hsh.games.aoe.domain.entities.skills.SkillUpgradeProcess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for SkillService
 * 
 * @author devTASE
 */
@DisplayName("SkillService Tests")
class SkillServiceTest {

    private SkillService skillService;
    
    @Mock
    private PlayerService playerService;
    
    @Mock
    private PlayerSkills playerSkills;
    
    @Mock
    private Skill mockSkill;
    
    @Mock
    private CyberOperative cyberOperative;
    
    @Mock
    private Building building;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        skillService = SkillService.getInstance();
    }

    @Test
    @DisplayName("Should return singleton instance")
    void shouldReturnSingletonInstance() {
        // When
        SkillService instance1 = SkillService.getInstance();
        SkillService instance2 = SkillService.getInstance();

        // Then
        assertSame(instance1, instance2);
        assertNotNull(instance1);
    }

    @Test
    @DisplayName("Should list skills from player service")
    void shouldListSkillsFromPlayerService() {
        // Given
        Map<SkillType, Skill> mockSkills = Map.of(
            SkillType.CONSTRUCTION_SPEED, mockSkill,
            SkillType.CONSTRUCTION_COST, mockSkill
        );
        when(playerService.getPlayerSkills()).thenReturn(playerSkills);
        when(playerSkills.getSkills()).thenReturn(mockSkills);

        // When
        Map<SkillType, Skill> skills = skillService.listSkills(playerService);

        // Then
        assertEquals(mockSkills, skills);
        verify(playerService).getPlayerSkills();
        verify(playerSkills).getSkills();
    }

    @Test
    @DisplayName("Should throw exception when listing skills with null player service")
    void shouldThrowExceptionWhenListingSkillsWithNullPlayerService() {
        // When & Then
        assertThrows(IllegalArgumentException.class, 
            () -> skillService.listSkills(null));
    }

    @Test
    @DisplayName("Should get current upgrade process")
    void shouldGetCurrentUpgradeProcess() {
        // Given
        SkillUpgradeProcess mockProcess = mock(SkillUpgradeProcess.class);
        when(playerService.getPlayerSkills()).thenReturn(playerSkills);
        when(playerSkills.getCurrentUpgradeProcess()).thenReturn(mockProcess);

        // When
        SkillUpgradeProcess process = skillService.getCurrentUpgrade(playerService);

        // Then
        assertEquals(mockProcess, process);
        verify(playerService).getPlayerSkills();
        verify(playerSkills).getCurrentUpgradeProcess();
    }

    @Test
    @DisplayName("Should throw exception when getting current upgrade with null player service")
    void shouldThrowExceptionWhenGettingCurrentUpgradeWithNullPlayerService() {
        // When & Then
        assertThrows(IllegalArgumentException.class, 
            () -> skillService.getCurrentUpgrade(null));
    }

    @Test
    @DisplayName("Should throw exception when starting upgrade with null player service")
    void shouldThrowExceptionWhenStartingUpgradeWithNullPlayerService() {
        // When & Then
        assertThrows(IllegalArgumentException.class, 
            () -> skillService.startUpgrade(null, SkillType.CONSTRUCTION_SPEED));
    }

    @Test
    @DisplayName("Should throw exception when starting upgrade with null skill type")
    void shouldThrowExceptionWhenStartingUpgradeWithNullSkillType() {
        // When & Then
        assertThrows(IllegalArgumentException.class, 
            () -> skillService.startUpgrade(playerService, null));
    }

    @Test
    @DisplayName("Should throw exception when academy not built")
    void shouldThrowExceptionWhenAcademyNotBuilt() {
        // Given
        when(playerService.getBuildingList()).thenReturn(List.of());

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> skillService.startUpgrade(playerService, SkillType.CONSTRUCTION_SPEED));
        assertTrue(exception.getMessage().contains("Training Facility must be built"));
    }

    @Test
    @DisplayName("Should throw exception when another upgrade is running")
    void shouldThrowExceptionWhenAnotherUpgradeIsRunning() {
        // Given
        setupAcademyBuilt();
        SkillUpgradeProcess runningProcess = mock(SkillUpgradeProcess.class);
        when(runningProcess.isUpgradeComplete()).thenReturn(false);
        when(runningProcess.getSkillType()).thenReturn(SkillType.CONSTRUCTION_SPEED);
        when(playerService.getPlayerSkills()).thenReturn(playerSkills);
        when(playerSkills.getCurrentUpgradeProcess()).thenReturn(runningProcess);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> skillService.startUpgrade(playerService, SkillType.CONSTRUCTION_COST));
        assertTrue(exception.getMessage().contains("Another skill upgrade is already in progress"));
    }

    @Test
    @DisplayName("Should throw exception when skill is at max level")
    void shouldThrowExceptionWhenSkillIsAtMaxLevel() {
        // Given
        setupAcademyBuilt();
        setupNoCurrentUpgrade();
        when(playerService.getPlayerSkills()).thenReturn(playerSkills);
        when(playerSkills.getSkill(SkillType.CONSTRUCTION_SPEED)).thenReturn(mockSkill);
        when(mockSkill.getLevel()).thenReturn(10);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> skillService.startUpgrade(playerService, SkillType.CONSTRUCTION_SPEED));
        assertTrue(exception.getMessage().contains("already at maximum level"));
    }

    @Test
    @DisplayName("Should throw exception when insufficient resources")
    void shouldThrowExceptionWhenInsufficientResources() {
        // Given
        setupAcademyBuilt();
        setupNoCurrentUpgrade();
        setupSkillAtLevel(1);
        when(playerService.getPlayerResources()).thenReturn(List.of(
            new ResourceAmount(ResourceType.ENERGY, 10) // Not enough for upgrade
        ));

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> skillService.startUpgrade(playerService, SkillType.CONSTRUCTION_SPEED));
        assertTrue(exception.getMessage().contains("Insufficient resources"));
    }

    @Test
    @DisplayName("Should throw exception when no operative available")
    void shouldThrowExceptionWhenNoOperativeAvailable() {
        // Given
        setupAcademyBuilt();
        setupNoCurrentUpgrade();
        setupSkillAtLevel(1);
        setupSufficientResources();
        when(playerService.getCyberOperativeAvailable()).thenReturn(null);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> skillService.startUpgrade(playerService, SkillType.CONSTRUCTION_SPEED));
        assertTrue(exception.getMessage().contains("No available cyber operatives"));
    }

    @Test
    @DisplayName("Should successfully start upgrade when all conditions met")
    void shouldSuccessfullyStartUpgradeWhenAllConditionsMet() {
        // Given
        setupAcademyBuilt();
        setupNoCurrentUpgrade();
        setupSkillAtLevel(1);
        setupSufficientResources();
        when(cyberOperative.getName()).thenReturn("TestOperative");
        when(playerService.getCyberOperativeAvailable()).thenReturn(cyberOperative);

        // When
        assertDoesNotThrow(() -> skillService.startUpgrade(playerService, SkillType.CONSTRUCTION_SPEED));

        // Then
        verify(cyberOperative).setOccupied(true);
        verify(playerSkills).setCurrentUpgradeProcess(any(SkillUpgradeProcess.class));
    }

    @Test
    @DisplayName("Should handle completed upgrade process correctly")
    void shouldHandleCompletedUpgradeProcessCorrectly() {
        // Given
        setupAcademyBuilt();
        SkillUpgradeProcess completedProcess = mock(SkillUpgradeProcess.class);
        when(completedProcess.isUpgradeComplete()).thenReturn(true);
        when(playerService.getPlayerSkills()).thenReturn(playerSkills);
        when(playerSkills.getCurrentUpgradeProcess()).thenReturn(completedProcess);
        when(playerSkills.getSkill(SkillType.CONSTRUCTION_SPEED)).thenReturn(mockSkill);
        when(mockSkill.getLevel()).thenReturn(1);
        setupSufficientResources();
        when(cyberOperative.getName()).thenReturn("TestOperative");
        when(playerService.getCyberOperativeAvailable()).thenReturn(cyberOperative);

        // When & Then
        assertDoesNotThrow(() -> skillService.startUpgrade(playerService, SkillType.CONSTRUCTION_SPEED));
    }

    @Test
    @DisplayName("Should throw exception when skill not found")
    void shouldThrowExceptionWhenSkillNotFound() {
        // Given
        setupAcademyBuilt();
        setupNoCurrentUpgrade();
        when(playerService.getPlayerSkills()).thenReturn(playerSkills);
        when(playerSkills.getSkill(SkillType.CONSTRUCTION_SPEED)).thenReturn(null);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> skillService.startUpgrade(playerService, SkillType.CONSTRUCTION_SPEED));
        assertTrue(exception.getMessage().contains("Skill not found"));
    }

    // Helper methods
    private void setupAcademyBuilt() {
        when(building.getConstructionTypeName()).thenReturn(ConstructionType.TRAINING_FACILITY.getName());
        when(building.getBuilded()).thenReturn(true);
        when(playerService.getBuildingList()).thenReturn(List.of(building));
    }

    private void setupNoCurrentUpgrade() {
        when(playerService.getPlayerSkills()).thenReturn(playerSkills);
        when(playerSkills.getCurrentUpgradeProcess()).thenReturn(null);
    }

    private void setupSkillAtLevel(int level) {
        when(playerService.getPlayerSkills()).thenReturn(playerSkills);
        when(playerSkills.getSkill(SkillType.CONSTRUCTION_SPEED)).thenReturn(mockSkill);
        when(mockSkill.getLevel()).thenReturn(level);
    }

    private void setupSufficientResources() {
        // Level 2 upgrade costs: 100*4=400 ENERGY, 80*4=320 DATA, 50*4=200 COMPONENTS
        when(playerService.getPlayerResources()).thenReturn(List.of(
            new ResourceAmount(ResourceType.ENERGY, 500),
            new ResourceAmount(ResourceType.DATA, 400),
            new ResourceAmount(ResourceType.COMPONENTS, 300)
        ));
    }
}
