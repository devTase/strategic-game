package org.hsh.games.aoe.domain.entities;

import org.hsh.games.aoe.domain.valueobjects.PlayerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Player entity
 * 
 * @author devTASE
 */
@DisplayName("Player Entity Tests")
class PlayerTest {

    @Test
    @DisplayName("Should create player with valid farm name")
    void shouldCreatePlayerWithValidFarmName() {
        // Given
        String farmName = "devTASE Village";

        // When
        Player player = new Player(farmName);

        // Then
        assertNotNull(player);
        assertEquals(farmName, player.getFarmName());
        assertEquals(TechPhase.UPRISING, player.getTechPhase());
        assertNotNull(player.getPlayerId());
        assertFalse(player.isInGuild());
        assertNull(player.getGuildId());
    }

    @Test
    @DisplayName("Should throw exception when creating player with null farm name")
    void shouldThrowExceptionWhenCreatingPlayerWithNullFarmName() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new Player(null));
    }

    @Test
    @DisplayName("Should throw exception when creating player with empty farm name")
    void shouldThrowExceptionWhenCreatingPlayerWithEmptyFarmName() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new Player(""));
    }

    @Test
    @DisplayName("Should throw exception when creating player with short farm name")
    void shouldThrowExceptionWhenCreatingPlayerWithShortFarmName() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new Player("ab"));
    }

    @Test
    @DisplayName("Should advance to next tech phase")
    void shouldAdvanceToNextTechPhase() {
        // Given
        Player player = new Player("Test Village");
        TechPhase initialPhase = player.getTechPhase();

        // When
        player.advanceToNextPhase();

        // Then
        assertNotEquals(initialPhase, player.getTechPhase());
        assertEquals(TechPhase.AUGMENTED_STREETS, player.getTechPhase());
    }

    @Test
    @DisplayName("Should check if can advance to next phase")
    void shouldCheckIfCanAdvanceToNextPhase() {
        // Given
        Player player = new Player("Test Village");

        // When
        boolean canAdvance = player.canAdvanceToNextPhase();

        // Then
        assertTrue(canAdvance);
    }

    @Test
    @DisplayName("Should not advance beyond maximum tech phase")
    void shouldNotAdvanceBeyondMaximumTechPhase() {
        // Given
        Player player = new Player("Test Village");
        player.setTechPhase(TechPhase.EXO_REALITY); // Set to max phase

        // When
        boolean canAdvance = player.canAdvanceToNextPhase();

        // Then
        assertFalse(canAdvance);
    }

    @Test
    @DisplayName("Should join guild successfully")
    void shouldJoinGuildSuccessfully() {
        // Given
        Player player = new Player("Test Village");
        String guildId = "guild-123";

        // When
        player.joinGuild(guildId);

        // Then
        assertTrue(player.isInGuild());
        assertEquals(guildId, player.getGuildId());
    }

    @Test
    @DisplayName("Should throw exception when joining guild with null ID")
    void shouldThrowExceptionWhenJoiningGuildWithNullId() {
        // Given
        Player player = new Player("Test Village");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> player.joinGuild(null));
    }

    @Test
    @DisplayName("Should throw exception when joining guild with empty ID")
    void shouldThrowExceptionWhenJoiningGuildWithEmptyId() {
        // Given
        Player player = new Player("Test Village");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> player.joinGuild(""));
    }

    @Test
    @DisplayName("Should throw exception when already in guild")
    void shouldThrowExceptionWhenAlreadyInGuild() {
        // Given
        Player player = new Player("Test Village");
        player.joinGuild("guild-123");

        // When & Then
        assertThrows(IllegalStateException.class, () -> player.joinGuild("guild-456"));
    }

    @Test
    @DisplayName("Should leave guild successfully")
    void shouldLeaveGuildSuccessfully() {
        // Given
        Player player = new Player("Test Village");
        player.joinGuild("guild-123");

        // When
        player.leaveGuild();

        // Then
        assertFalse(player.isInGuild());
        assertNull(player.getGuildId());
    }

    @Test
    @DisplayName("Should throw exception when leaving guild but not in guild")
    void shouldThrowExceptionWhenLeavingGuildButNotInGuild() {
        // Given
        Player player = new Player("Test Village");

        // When & Then
        assertThrows(IllegalStateException.class, player::leaveGuild);
    }

    @Test
    @DisplayName("Should update farm name successfully")
    void shouldUpdateFarmNameSuccessfully() {
        // Given
        Player player = new Player("Old Village");
        String newFarmName = "New Village";

        // When
        player.setFarmName(newFarmName);

        // Then
        assertEquals(newFarmName, player.getFarmName());
    }

    @Test
    @DisplayName("Should throw exception when setting invalid farm name")
    void shouldThrowExceptionWhenSettingInvalidFarmName() {
        // Given
        Player player = new Player("Test Village");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> player.setFarmName("ab"));
    }

    @Test
    @DisplayName("Should set and get player ID")
    void shouldSetAndGetPlayerId() {
        // Given
        Player player = new Player("Test Village");
        PlayerId newPlayerId = new PlayerId("new-id");

        // When
        player.setPlayerId(newPlayerId);

        // Then
        assertEquals(newPlayerId, player.getPlayerId());
    }

    @Test
    @DisplayName("Should set tech phase")
    void shouldSetTechPhase() {
        // Given
        Player player = new Player("Test Village");
        TechPhase newPhase = TechPhase.QUANTUM_DAWN;

        // When
        player.setTechPhase(newPhase);

        // Then
        assertEquals(newPhase, player.getTechPhase());
    }

    @Test
    @DisplayName("Should handle deprecated era age methods")
    void shouldHandleDeprecatedEraAgeMethods() {
        // Given
        Player player = new Player("Test Village");
        EraAge eraAge = EraAge.BRONZE_AGE;

        // When
        player.setEraAge(eraAge);
        EraAge retrievedEra = player.getEraAge();

        // Then
        assertNotNull(retrievedEra);
        assertEquals(eraAge.getLevel(), retrievedEra.getLevel());
    }
}
