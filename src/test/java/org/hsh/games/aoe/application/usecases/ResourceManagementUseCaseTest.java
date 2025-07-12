package org.hsh.games.aoe.application.usecases;

import org.hsh.games.aoe.application.dto.ResourceDTO;
import org.hsh.games.aoe.application.ports.outbound.NotificationPort;
import org.hsh.games.aoe.application.ports.outbound.PlayerRepositoryPort;
import org.hsh.games.aoe.application.ports.outbound.ResourceRepositoryPort;
import org.hsh.games.aoe.domain.entities.Player;
import org.hsh.games.aoe.domain.entities.TechPhase;
import org.hsh.games.aoe.domain.entities.resources.Resource;
import org.hsh.games.aoe.domain.entities.resources.ResourceType;
import org.hsh.games.aoe.domain.exceptions.InsufficientResourcesException;
import org.hsh.games.aoe.domain.exceptions.InvalidPlayerException;
import org.hsh.games.aoe.domain.valueobjects.PlayerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test class for ResourceManagementUseCase
 * 
 * @author devTASE
 */
@DisplayName("ResourceManagementUseCase Tests")
class ResourceManagementUseCaseTest {

    @Mock
    private ResourceRepositoryPort resourceRepository;

    @Mock
    private PlayerRepositoryPort playerRepository;

    @Mock
    private NotificationPort notificationPort;

    private ResourceManagementUseCase resourceManagementUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        resourceManagementUseCase = new ResourceManagementUseCase(
            resourceRepository, playerRepository, notificationPort);
    }

    @Test
    @DisplayName("Should gather resource successfully")
    void shouldGatherResourceSuccessfully() {
        // Given
        PlayerId playerId = new PlayerId("test-player");
        ResourceType resourceType = ResourceType.ENERGY;
        int amount = 100;
        
        when(playerRepository.exists(playerId)).thenReturn(true);
        when(resourceRepository.findByPlayerAndType(playerId, resourceType))
            .thenReturn(Optional.of(new Resource(resourceType)));

        // When
        resourceManagementUseCase.gatherResource(playerId, resourceType, amount);

        // Then
        verify(resourceRepository).save(eq(playerId), any(Resource.class));
        verify(notificationPort).notifySuccess(anyString());
    }

    @Test
    @DisplayName("Should throw exception when gathering with invalid amount")
    void shouldThrowExceptionWhenGatheringWithInvalidAmount() {
        // Given
        PlayerId playerId = new PlayerId("test-player");
        ResourceType resourceType = ResourceType.ENERGY;
        int invalidAmount = -10;

        when(playerRepository.exists(playerId)).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, 
            () -> resourceManagementUseCase.gatherResource(playerId, resourceType, invalidAmount));
    }

    @Test
    @DisplayName("Should consume resource successfully")
    void shouldConsumeResourceSuccessfully() {
        // Given
        PlayerId playerId = new PlayerId("test-player");
        ResourceType resourceType = ResourceType.ENERGY;
        int amount = 50;
        
        Resource resource = new Resource(resourceType);
        when(playerRepository.exists(playerId)).thenReturn(true);
        when(resourceRepository.findByPlayerAndType(playerId, resourceType))
            .thenReturn(Optional.of(resource));

        // When
        resourceManagementUseCase.consumeResource(playerId, resourceType, amount);

        // Then
        verify(resourceRepository).save(eq(playerId), any(Resource.class));
        verify(notificationPort).notifyInfo(anyString());
    }

    @Test
    @DisplayName("Should throw exception when consuming non-existent resource")
    void shouldThrowExceptionWhenConsumingNonExistentResource() {
        // Given
        PlayerId playerId = new PlayerId("test-player");
        ResourceType resourceType = ResourceType.ENERGY;
        int amount = 50;

        when(playerRepository.exists(playerId)).thenReturn(true);
        when(resourceRepository.findByPlayerAndType(playerId, resourceType))
            .thenReturn(Optional.empty());

        // When & Then
        assertThrows(InsufficientResourcesException.class, 
            () -> resourceManagementUseCase.consumeResource(playerId, resourceType, amount));
    }

    @Test
    @DisplayName("Should get player resources")
    void shouldGetPlayerResources() {
        // Given
        PlayerId playerId = new PlayerId("test-player");
        List<Resource> resources = List.of(
            new Resource(ResourceType.ENERGY),
            new Resource(ResourceType.DATA)
        );

        when(playerRepository.exists(playerId)).thenReturn(true);
        when(resourceRepository.findByPlayer(playerId)).thenReturn(resources);

        // When
        List<ResourceDTO> result = resourceManagementUseCase.getPlayerResources(playerId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(ResourceType.ENERGY, result.get(0).type());
        assertEquals(ResourceType.DATA, result.get(1).type());
    }

    @Test
    @DisplayName("Should check if player has enough resources")
    void shouldCheckIfPlayerHasEnoughResources() {
        // Given
        PlayerId playerId = new PlayerId("test-player");
        ResourceType resourceType = ResourceType.ENERGY;
        int amount = 50;
        
        Resource resource = new Resource(resourceType);
        when(playerRepository.exists(playerId)).thenReturn(true);
        when(resourceRepository.findByPlayerAndType(playerId, resourceType))
            .thenReturn(Optional.of(resource));

        // When
        boolean hasEnough = resourceManagementUseCase.hasEnoughResources(playerId, resourceType, amount);

        // Then
        assertTrue(hasEnough);
    }

    @Test
    @DisplayName("Should return false when player doesn't have enough resources")
    void shouldReturnFalseWhenPlayerDoesntHaveEnoughResources() {
        // Given
        PlayerId playerId = new PlayerId("test-player");
        ResourceType resourceType = ResourceType.ENERGY;
        int amount = 50;

        when(playerRepository.exists(playerId)).thenReturn(true);
        when(resourceRepository.findByPlayerAndType(playerId, resourceType))
            .thenReturn(Optional.empty());

        // When
        boolean hasEnough = resourceManagementUseCase.hasEnoughResources(playerId, resourceType, amount);

        // Then
        assertFalse(hasEnough);
    }

    @Test
    @DisplayName("Should get available resource types")
    void shouldGetAvailableResourceTypes() {
        // Given
        PlayerId playerId = new PlayerId("test-player");
        Player player = new Player("Test Village");
        player.setTechPhase(TechPhase.UPRISING);

        when(playerRepository.exists(playerId)).thenReturn(true);
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        // When
        List<ResourceType> availableTypes = resourceManagementUseCase.getAvailableResourceTypes(playerId);

        // Then
        assertNotNull(availableTypes);
        assertFalse(availableTypes.isEmpty());
    }

    @Test
    @DisplayName("Should throw exception when validating non-existent player")
    void shouldThrowExceptionWhenValidatingNonExistentPlayer() {
        // Given
        PlayerId playerId = new PlayerId("non-existent-player");
        when(playerRepository.exists(playerId)).thenReturn(false);

        // When & Then
        assertThrows(InvalidPlayerException.class, 
            () -> resourceManagementUseCase.gatherResource(playerId, ResourceType.ENERGY, 100));
    }
}
