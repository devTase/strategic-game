package org.hsh.games.aoe.application.usecases;

import org.hsh.games.aoe.application.dto.ResourceDTO;
import org.hsh.games.aoe.application.ports.inbound.ResourceManagementPort;
import org.hsh.games.aoe.application.ports.outbound.NotificationPort;
import org.hsh.games.aoe.application.ports.outbound.PlayerRepositoryPort;
import org.hsh.games.aoe.domain.entities.CyberOperative;
import org.hsh.games.aoe.domain.entities.Player;
import org.hsh.games.aoe.domain.entities.resources.ResourceType;
import org.hsh.games.aoe.domain.services.PlayerService;
import org.hsh.games.aoe.domain.valueobjects.PlayerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CyberOperativeManagementUseCase
 * 
 * @author devTASE
 */
@DisplayName("CyberOperativeManagementUseCase Tests")
@ExtendWith(MockitoExtension.class)
class CyberOperativeManagementUseCaseTest {

    @Mock
    private PlayerService playerService;
    
    @Mock
    private ResourceManagementPort resourceManagementPort;
    
    @Mock
    private NotificationPort notificationPort;
    
    @Mock
    private PlayerRepositoryPort playerRepository;
    
    @Mock
    private Player player;
    
    @Mock
    private CyberOperative cyberOperative;
    
    private CyberOperativeManagementUseCase useCase;
    private PlayerId playerId;
    
    @BeforeEach
    void setUp() {
        useCase = new CyberOperativeManagementUseCase(
            playerService,
            resourceManagementPort,
            notificationPort,
            playerRepository
        );
        playerId = new PlayerId("test-player-123");
    }
    
    @Test
    @DisplayName("Should return false when player does not exist")
    void shouldReturnFalseWhenPlayerDoesNotExist() {
        // Given
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());
        
        // When
        boolean result = useCase.searchResource(playerId, ResourceType.ENERGY);
        
        // Then
        assertFalse(result);
        verify(notificationPort).notifyError("Player not found");
        verify(playerService, never()).getCyberOperativeAvailable();
    }
    
    @Test
    @DisplayName("Should return false when all operatives are busy")
    void shouldReturnFalseWhenAllOperativesAreBusy() {
        // Given
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(playerService.getCyberOperativeAvailable()).thenReturn(null);
        
        // When
        boolean result = useCase.searchResource(playerId, ResourceType.ENERGY);
        
        // Then
        assertFalse(result);
        verify(resourceManagementPort, never()).getPlayerResources(any());
    }
    
    @Test
    @DisplayName("Should return false when storage capacity is reached")
    void shouldReturnFalseWhenStorageCapacityReached() {
        // Given
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(playerService.getCyberOperativeAvailable()).thenReturn(cyberOperative);
        
        ResourceDTO resourceDTO = new ResourceDTO(ResourceType.ENERGY, 1000, "Energy Cells");
        when(resourceManagementPort.getPlayerResources(playerId)).thenReturn(List.of(resourceDTO));
        
        // When
        boolean result = useCase.searchResource(playerId, ResourceType.ENERGY);
        
        // Then
        assertFalse(result);
        verify(notificationPort).notifyWarning("Storage capacity reached for Energy Cells");
        verify(playerService, never()).sendCyberOperativesToSearchJob(any());
    }
    
    @Test
    @DisplayName("Should successfully dispatch cyber operative when all conditions are met")
    void shouldSuccessfullyDispatchCyberOperative() {
        // Given
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(playerService.getCyberOperativeAvailable()).thenReturn(cyberOperative);
        
        ResourceDTO resourceDTO = new ResourceDTO(ResourceType.ENERGY, 500, "Energy Cells");
        when(resourceManagementPort.getPlayerResources(playerId)).thenReturn(List.of(resourceDTO));
        
        // When
        boolean result = useCase.searchResource(playerId, ResourceType.ENERGY);
        
        // Then
        assertTrue(result);
        verify(playerService).sendCyberOperativesToSearchJob(ResourceType.ENERGY);
        verify(resourceManagementPort).gatherResource(eq(playerId), eq(ResourceType.ENERGY), any(Integer.class));
        verify(notificationPort).notifyInfo(contains("Cyber operative dispatched to search for Energy Cells"));
    }
    
    @Test
    @DisplayName("Should handle resource amount correctly when approaching max capacity")
    void shouldHandleResourceAmountCorrectlyWhenApproachingMaxCapacity() {
        // Given
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(playerService.getCyberOperativeAvailable()).thenReturn(cyberOperative);
        
        // Current amount is 990, maxCap is 1000, so only 10 units can be added
        ResourceDTO resourceDTO = new ResourceDTO(ResourceType.ENERGY, 990, "Energy Cells");
        when(resourceManagementPort.getPlayerResources(playerId)).thenReturn(List.of(resourceDTO));
        
        // When
        boolean result = useCase.searchResource(playerId, ResourceType.ENERGY);
        
        // Then
        assertTrue(result);
        verify(playerService).sendCyberOperativesToSearchJob(ResourceType.ENERGY);
        verify(resourceManagementPort).gatherResource(eq(playerId), eq(ResourceType.ENERGY), intThat(amount -> amount <= 10));
        verify(notificationPort).notifyInfo(contains("Cyber operative dispatched to search for Energy Cells"));
    }
    
    @Test
    @DisplayName("Should handle player with no existing resources")
    void shouldHandlePlayerWithNoExistingResources() {
        // Given
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(playerService.getCyberOperativeAvailable()).thenReturn(cyberOperative);
        
        when(resourceManagementPort.getPlayerResources(playerId)).thenReturn(List.of());
        
        // When
        boolean result = useCase.searchResource(playerId, ResourceType.ENERGY);
        
        // Then
        assertTrue(result);
        verify(playerService).sendCyberOperativesToSearchJob(ResourceType.ENERGY);
        verify(resourceManagementPort).gatherResource(eq(playerId), eq(ResourceType.ENERGY), any(Integer.class));
        verify(notificationPort).notifyInfo(contains("Cyber operative dispatched to search for Energy Cells"));
    }
}
