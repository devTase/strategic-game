package org.hsh.games.aoe.application.dto;

import org.hsh.games.aoe.domain.entities.TechPhase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for PlayerDTO
 * 
 * @author devTASE
 */
@DisplayName("PlayerDTO Tests")
class PlayerDTOTest {

    @Test
    @DisplayName("Should create PlayerDTO with correct data")
    void shouldCreatePlayerDTOWithCorrectData() {
        // Given
        String playerId = "player-123";
        String farmName = "devTASE Village";
        TechPhase techPhase = TechPhase.UPRISING;
        String guildId = "guild-456";
        boolean isInGuild = true;

        // When
        PlayerDTO playerDTO = new PlayerDTO(playerId, farmName, techPhase, guildId, isInGuild);

        // Then
        assertEquals(playerId, playerDTO.playerId());
        assertEquals(farmName, playerDTO.farmName());
        assertEquals(techPhase, playerDTO.techPhase());
        assertEquals(guildId, playerDTO.guildId());
        assertTrue(playerDTO.isInGuild());
    }

    @Test
    @DisplayName("Should create PlayerDTO without guild")
    void shouldCreatePlayerDTOWithoutGuild() {
        // Given
        String playerId = "player-789";
        String farmName = "Solo Village";
        TechPhase techPhase = TechPhase.NEURAL_NEXUS;

        // When
        PlayerDTO playerDTO = new PlayerDTO(playerId, farmName, techPhase, null, false);

        // Then
        assertEquals(playerId, playerDTO.playerId());
        assertEquals(farmName, playerDTO.farmName());
        assertEquals(techPhase, playerDTO.techPhase());
        assertNull(playerDTO.guildId());
        assertFalse(playerDTO.isInGuild());
    }
}
