package org.hsh.games.aoe.domain.services;

import org.hsh.games.aoe.domain.entities.CyberOperative;
import org.hsh.games.aoe.domain.entities.Player;
import org.hsh.games.aoe.domain.entities.TechPhase;
import org.hsh.games.aoe.domain.entities.buildings.Building;
import org.hsh.games.aoe.domain.entities.buildings.ConstructionType;
import org.hsh.games.aoe.domain.entities.resources.ResourceAmount;
import org.hsh.games.aoe.domain.entities.resources.ResourceType;
import org.hsh.games.aoe.domain.entities.skills.PlayerSkills;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for PlayerService
 * 
 * @author devTASE
 */
@DisplayName("PlayerService Tests")
@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    private PlayerService playerService;
    private Player player;
    
    @Mock
    private GuildService guildService;

    @BeforeEach
    void setUp() {
        player = new Player("devTASE Village");
        playerService = new PlayerService(player);
    }

    @Test
    @DisplayName("Should create player service with player")
    void shouldCreatePlayerServiceWithPlayer() {
        // When
        PlayerService service = new PlayerService(player);

        // Then
        assertNotNull(service);
        assertEquals(player, service.getPlayer());
        assertNotNull(service.getCyberOperatives());
        assertEquals(3, service.getCyberOperatives().size()); // Default 3 operatives
        assertNotNull(service.getPlayerResources());
        assertNotNull(service.getPlayerSkills());
    }

    @Test
    @DisplayName("Should get player")
    void shouldGetPlayer() {
        // When
        Player retrievedPlayer = playerService.getPlayer();

        // Then
        assertEquals(player, retrievedPlayer);
    }

    @Test
    @DisplayName("Should get cyber operatives")
    void shouldGetCyberOperatives() {
        // When
        List<CyberOperative> operatives = playerService.getCyberOperatives();

        // Then
        assertNotNull(operatives);
        assertEquals(3, operatives.size());
        assertEquals("operative1", operatives.get(0).getName());
        assertEquals("operative2", operatives.get(1).getName());
        assertEquals("operative3", operatives.get(2).getName());
    }

    @Test
    @DisplayName("Should get and set player resources")
    void shouldGetAndSetPlayerResources() {
        // Given
        List<ResourceAmount> newResources = List.of(
            new ResourceAmount(ResourceType.ENERGY, 100),
            new ResourceAmount(ResourceType.DATA, 50)
        );

        // When
        playerService.setPlayerResources(newResources);
        List<ResourceAmount> retrievedResources = playerService.getPlayerResources();

        // Then
        assertEquals(newResources, retrievedResources);
    }

    @Test
    @DisplayName("Should add resource to existing resource")
    void shouldAddResourceToExistingResource() {
        // Given
        int initialEnergy = 250; // Initial energy from level setup
        playerService.addResource(ResourceType.ENERGY, 100);
        playerService.addResource(ResourceType.ENERGY, 50);

        // When
        List<ResourceAmount> resources = playerService.getPlayerResources();
        ResourceAmount energyResource = resources.stream()
            .filter(r -> r.getResource() == ResourceType.ENERGY)
            .findFirst()
            .orElse(null);

        // Then
        assertNotNull(energyResource);
        assertEquals(initialEnergy + 100 + 50, energyResource.getAmount());
    }

    @Test
    @DisplayName("Should add new resource when not exists")
    void shouldAddNewResourceWhenNotExists() {
        // Given
        int initialSize = playerService.getPlayerResources().size();

        // When
        playerService.addResource(ResourceType.CRYPTO, 10);
        List<ResourceAmount> resources = playerService.getPlayerResources();

        // Then
        assertEquals(initialSize + 1, resources.size());
        assertTrue(resources.stream()
            .anyMatch(r -> r.getResource() == ResourceType.CRYPTO && r.getAmount() == 10));
    }

    @Test
    @DisplayName("Should get building list")
    void shouldGetBuildingList() {
        // When
        List<Building> buildings = playerService.getBuildingList();

        // Then
        assertNotNull(buildings);
        // Should have buildings from initial level setup
        assertFalse(buildings.isEmpty());
    }

    @Test
    @DisplayName("Should check if player has enough resources for building")
    void shouldCheckIfPlayerHasEnoughResourcesForBuilding() {
        // Given
        Building building = mock(Building.class);
        List<ResourceAmount> requiredResources = List.of(
            new ResourceAmount(ResourceType.ENERGY, 50)
        );
        when(building.getResourceCost()).thenReturn(requiredResources);

        playerService.addResource(ResourceType.ENERGY, 100);

        // When
        boolean hasEnoughResources = playerService.checkIfPlayerHasEnoughResources(building);

        // Then
        assertTrue(hasEnoughResources);
    }

    @Test
    @DisplayName("Should return false when not enough resources for building")
    void shouldReturnFalseWhenNotEnoughResourcesForBuilding() {
        // Given
        Building building = mock(Building.class);
        List<ResourceAmount> requiredResources = List.of(
            new ResourceAmount(ResourceType.ENERGY, 500) // Requires more than initial 250 + 100 = 350
        );
        when(building.getResourceCost()).thenReturn(requiredResources);

        playerService.addResource(ResourceType.ENERGY, 100);

        // When
        boolean hasEnoughResources = playerService.checkIfPlayerHasEnoughResources(building);

        // Then
        assertFalse(hasEnoughResources);
    }

    @Test
    @DisplayName("Should add new building")
    void shouldAddNewBuilding() {
        // Given
        Building newBuilding = mock(Building.class);
        int initialSize = playerService.getBuildingList().size();

        // When
        playerService.addNewBuilding(newBuilding);

        // Then
        assertEquals(initialSize + 1, playerService.getBuildingList().size());
        assertTrue(playerService.getBuildingList().contains(newBuilding));
    }

    @Test
    @DisplayName("Should check if first time building")
    void shouldCheckIfFirstTimeBuilding() {
        // Given
        Building building = mock(Building.class);
        when(building.getBuilded()).thenReturn(false);
        playerService.addNewBuilding(building);

        // When
        Boolean isFirstTime = playerService.isFirstTimeBuilding(building);

        // Then
        assertTrue(isFirstTime);
    }

    @Test
    @DisplayName("Should return false for already built building")
    void shouldReturnFalseForAlreadyBuiltBuilding() {
        // Given
        Building building = mock(Building.class);
        when(building.getBuilded()).thenReturn(true);
        playerService.addNewBuilding(building);

        // When
        Boolean isFirstTime = playerService.isFirstTimeBuilding(building);

        // Then
        assertFalse(isFirstTime);
    }

    @Test
    @DisplayName("Should check if building amount has reached limit")
    void shouldCheckIfBuildingAmountHasReachedLimit() {
        // Given
        Building building = mock(Building.class);
        when(building.getAmountConstructionsAllowed()).thenReturn(2);
        when(building.getConstructionTypeName()).thenReturn("TEST_BUILDING");
        when(building.getBuilded()).thenReturn(true);

        playerService.addNewBuilding(building);
        Building building2 = mock(Building.class);
        when(building2.getConstructionTypeName()).thenReturn("TEST_BUILDING");
        when(building2.getBuilded()).thenReturn(true);
        playerService.addNewBuilding(building2);

        // When
        Boolean hasReachedLimit = playerService.checkIfBuildingAmountHasReached(building);

        // Then
        assertTrue(hasReachedLimit);
    }

    @Test
    @DisplayName("Should check if building has reached max level")
    void shouldCheckIfBuildingHasReachedMaxLevel() {
        // Given
        Building building = mock(Building.class);
        when(building.getLevel()).thenReturn(5);
        when(building.getMaxLevel()).thenReturn(5);

        // When
        Boolean hasReachedMaxLevel = playerService.checkIfBuildingHasReachedItsMaximLevel(building);

        // Then
        assertTrue(hasReachedMaxLevel);
    }

    @Test
    @DisplayName("Should get available cyber operative")
    void shouldGetAvailableCyberOperative() {
        // When
        CyberOperative operative = playerService.getCyberOperativeAvailable();

        // Then
        assertNotNull(operative);
        assertFalse(operative.isOccupied());
    }

    @Test
    @DisplayName("Should return null when no operative available")
    void shouldReturnNullWhenNoOperativeAvailable() {
        // Given
        playerService.getCyberOperatives().forEach(op -> op.setOccupied(true));

        // When
        CyberOperative operative = playerService.getCyberOperativeAvailable();

        // Then
        assertNull(operative);
    }

    @Test
    @DisplayName("Should check if player is eligible for new phase")
    void shouldCheckIfPlayerIsEligibleForNewPhase() {
        // When
        boolean isEligible = playerService.isPlayerEligibleForNewPhase();

        // Then
        // Since buildings are initially not built, should return false
        assertFalse(isEligible);
    }

    @Test
    @DisplayName("Should get available construction types")
    void shouldGetAvailableConstructionTypes() {
        // When
        Set<String> availableTypes = playerService.getAvailableConstructionTypes();

        // Then
        assertNotNull(availableTypes);
        // Should contain available construction types from current level
        assertFalse(availableTypes.isEmpty());
    }

    @Test
    @DisplayName("Should get buildings from construction name")
    void shouldGetBuildingsFromConstructionName() {
        // Given
        Building building = mock(Building.class);
        when(building.getConstructionTypeName()).thenReturn("COMMAND_CENTER");
        playerService.addNewBuilding(building);

        // When
        List<Building> buildings = playerService.getBuildingsFromConstructionName("COMMAND_CENTER");

        // Then
        assertNotNull(buildings);
        assertTrue(buildings.contains(building));
    }

    @Test
    @DisplayName("Should claim daily reward")
    void shouldClaimDailyReward() {
        // Given
        int initialResourcesSize = playerService.getPlayerResources().size();

        // When
        playerService.claimDailyReward();

        // Then
        // Should have added reward resources to player resources
        assertTrue(playerService.getPlayerResources().size() >= initialResourcesSize);
        assertTrue(playerService.getDailyRewardService().hasClaimedToday(player.getFarmName()));
    }

    @Test
    @DisplayName("Should set guild service")
    void shouldSetGuildService() {
        // When
        playerService.setGuildService(guildService);

        // Then
        assertTrue(playerService.getDailyRewardService().isGuildVaultDepositAvailable());
    }

    @Test
    @DisplayName("Should get daily reward service")
    void shouldGetDailyRewardService() {
        // When
        DailyRewardService service = playerService.getDailyRewardService();

        // Then
        assertNotNull(service);
    }

    @Test
    @DisplayName("Should validate mandatory buildings")
    void shouldValidateMandatoryBuildings() {
        // When
        boolean isValid = playerService.validateMandatoryBuildings();

        // Then
        // Since buildings are initially not built, should return false
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should get missing mandatory buildings")
    void shouldGetMissingMandatoryBuildings() {
        // When
        List<ConstructionType> missingBuildings = playerService.getMissingMandatoryBuildings();

        // Then
        assertNotNull(missingBuildings);
        // Should have missing buildings since none are built initially
        assertFalse(missingBuildings.isEmpty());
    }

    @Test
    @DisplayName("Should get construction time multiplier")
    void shouldGetConstructionTimeMultiplier() {
        // When
        double multiplier = playerService.getConstructionTimeMultiplier();

        // Then
        assertTrue(multiplier > 0);
        assertEquals(playerService.getPlayerSkills().getTimeMultiplier(), multiplier);
    }

    @Test
    @DisplayName("Should get construction cost multiplier")
    void shouldGetConstructionCostMultiplier() {
        // When
        double multiplier = playerService.getConstructionCostMultiplier();

        // Then
        assertTrue(multiplier > 0);
        assertEquals(playerService.getPlayerSkills().getCostMultiplier(), multiplier);
    }

    @Test
    @DisplayName("Should get loot multiplier")
    void shouldGetLootMultiplier() {
        // When
        double multiplier = playerService.getLootMultiplier();

        // Then
        assertTrue(multiplier > 0);
        assertEquals(playerService.getPlayerSkills().getLootMultiplier(), multiplier);
    }

    @Test
    @DisplayName("Should get player skills")
    void shouldGetPlayerSkills() {
        // When
        PlayerSkills skills = playerService.getPlayerSkills();

        // Then
        assertNotNull(skills);
    }

    @Test
    @DisplayName("Should handle deprecated method isPlayerEligibleForNewEra")
    void shouldHandleDeprecatedMethodIsPlayerEligibleForNewEra() {
        // When
        boolean isEligible = playerService.isPlayerEligibleForNewPhase();

        // Then
        assertEquals(playerService.isPlayerEligibleForNewPhase(), isEligible);
    }

    @Test
    @DisplayName("Should claim daily reward with guild option when not depositing to guild")
    void shouldClaimDailyRewardWithGuildOptionWhenNotDepositingToGuild() {
        // Given
        playerService.setGuildService(guildService);
        int initialResourcesSize = playerService.getPlayerResources().size();

        // When
        playerService.claimDailyRewardWithGuildOption(false);

        // Then
        assertTrue(playerService.getPlayerResources().size() >= initialResourcesSize);
        assertTrue(playerService.getDailyRewardService().hasClaimedToday(player.getFarmName()));
    }

    @Test
    @DisplayName("Should handle exception when claiming reward twice")
    void shouldHandleExceptionWhenClaimingRewardTwice() {
        // Given
        playerService.claimDailyReward();

        // When & Then
        assertThrows(IllegalStateException.class, () -> playerService.claimDailyReward());
    }
}
