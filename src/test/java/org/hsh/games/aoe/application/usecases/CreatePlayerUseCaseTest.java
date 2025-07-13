package org.hsh.games.aoe.application.usecases;

import org.hsh.games.aoe.application.dto.PlayerDTO;
import org.hsh.games.aoe.application.ports.outbound.NotificationPort;
import org.hsh.games.aoe.application.ports.outbound.PlayerRepositoryPort;
import org.hsh.games.aoe.domain.entities.Player;
import org.hsh.games.aoe.domain.entities.TechPhase;
import org.hsh.games.aoe.domain.exceptions.InvalidPlayerException;
import org.hsh.games.aoe.domain.valueobjects.PlayerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test class for CreatePlayerUseCase
 * 
 * @author devTASE
 */
@DisplayName("CreatePlayerUseCase Tests")
@ExtendWith(MockitoExtension.class)
class CreatePlayerUseCaseTest {

    @Mock
    private PlayerRepositoryPort playerRepository;

    @Mock
    private NotificationPort notificationPort;

    private CreatePlayerUseCase createPlayerUseCase;

    @BeforeEach
    void setUp() {
        createPlayerUseCase = new CreatePlayerUseCase(playerRepository, notificationPort);
    }

    @Test
    @DisplayName("Should create player successfully")
    void shouldCreatePlayerSuccessfully() {
        // Given
        String farmName = "devTASE Village";
        when(playerRepository.exists(any(PlayerId.class))).thenReturn(false);

        // When
        PlayerDTO result = createPlayerUseCase.createPlayer(farmName);

        // Then
        assertNotNull(result);
        assertEquals(farmName, result.farmName());
        assertEquals(TechPhase.UPRISING, result.techPhase());
        assertFalse(result.isInGuild());
        assertNull(result.guildId());

        verify(playerRepository).save(any(Player.class));
        verify(notificationPort).notifySuccess(anyString());
    }

    @Test
    @DisplayName("Should throw exception when player already exists")
    void shouldThrowExceptionWhenPlayerAlreadyExists() {
        // Given
        String farmName = "Existing Village";
        when(playerRepository.exists(any(PlayerId.class))).thenReturn(true);

        // When & Then
        assertThrows(InvalidPlayerException.class, 
            () -> createPlayerUseCase.createPlayer(farmName));

        verify(playerRepository, never()).save(any(Player.class));
        verify(notificationPort).notifyError(anyString());
    }

    @Test
    @DisplayName("Should get player by ID")
    void shouldGetPlayerById() {
        // Given
        PlayerId playerId = new PlayerId("test-id");
        Player player = new Player("Test Village");
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        // When
        PlayerDTO result = createPlayerUseCase.getPlayer(playerId);

        // Then
        assertNotNull(result);
        assertEquals("Test Village", result.farmName());
        assertEquals(TechPhase.UPRISING, result.techPhase());
    }

    @Test
    @DisplayName("Should throw exception when player not found")
    void shouldThrowExceptionWhenPlayerNotFound() {
        // Given
        PlayerId playerId = new PlayerId("non-existent-id");
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(InvalidPlayerException.class, 
            () -> createPlayerUseCase.getPlayer(playerId));
    }

    @Test
    @DisplayName("Should advance player tech phase")
    void shouldAdvancePlayerTechPhase() {
        // Given
        PlayerId playerId = new PlayerId("test-id");
        Player player = new Player("Test Village");
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        // When
        createPlayerUseCase.advancePlayerTechPhase(playerId);

        // Then
        verify(playerRepository).save(player);
        verify(notificationPort).notifySuccess(anyString());
    }

    @Test
    @DisplayName("Should check if player can advance to next phase")
    void shouldCheckIfPlayerCanAdvanceToNextPhase() {
        // Given
        PlayerId playerId = new PlayerId("test-id");
        Player player = new Player("Test Village");
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        // When
        boolean canAdvance = createPlayerUseCase.canAdvanceToNextPhase(playerId);

        // Then
        assertTrue(canAdvance); // New player should be able to advance from UPRISING
    }

    @Test
    @DisplayName("Should join guild successfully")
    void shouldJoinGuildSuccessfully() {
        // Given
        PlayerId playerId = new PlayerId("test-id");
        Player player = new Player("Test Village");
        String guildId = "guild-123";
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        // When
        createPlayerUseCase.joinGuild(playerId, guildId);

        // Then
        verify(playerRepository).save(player);
        verify(notificationPort).notifySuccess(anyString());
    }

    @Test
    @DisplayName("Should leave guild successfully")
    void shouldLeaveGuildSuccessfully() {
        // Given
        PlayerId playerId = new PlayerId("test-id");
        Player player = new Player("Test Village");
        player.joinGuild("guild-123"); // First join a guild
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        // When
        createPlayerUseCase.leaveGuild(playerId);

        // Then
        verify(playerRepository).save(player);
        verify(notificationPort).notifySuccess(anyString());
    }
}
