package org.hsh.games.aoe.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Player guild integration functionality
 * 
 * @author devTASE
 */
public class PlayerGuildIntegrationTest {

    @Test
    public void testPlayerGuildIdReference() {
        // Given
        Player player = new Player("TestFarm");
        
        // When - Initially no guild
        // Then
        assertNull(player.getGuildId());
        assertFalse(player.isInGuild());
        
        // When - Set guild ID
        String guildId = "guild_12345";
        player.setGuildId(guildId);
        
        // Then
        assertEquals(guildId, player.getGuildId());
        assertTrue(player.isInGuild());
        
        // When - Clear guild ID
        player.setGuildId(null);
        
        // Then
        assertNull(player.getGuildId());
        assertFalse(player.isInGuild());
    }

    @Test
    public void testPlayerGuildIdBlankHandling() {
        // Given
        Player player = new Player("TestFarm");
        
        // When - Set blank guild ID
        player.setGuildId("");
        
        // Then
        assertEquals("", player.getGuildId());
        assertFalse(player.isInGuild()); // Blank should be considered not in guild
        
        // When - Set whitespace guild ID
        player.setGuildId("   ");
        
        // Then
        assertEquals("   ", player.getGuildId());
        assertFalse(player.isInGuild()); // Whitespace should be considered not in guild
    }

    @Test
    public void testPlayerEraAgeIntegration() {
        // Given
        Player player = new Player("TestFarm");
        
        // When - Check initial era
        // Then
        assertNotNull(player.getEraAge());
        assertEquals(1, player.getEraAge().getLevel());
        assertEquals(EraAge.STONE_AGE, player.getEraAge());
        
        // When - Advance to Bronze Age
        player.setEraAge(EraAge.BRONZE_AGE);
        
        // Then
        assertEquals(EraAge.BRONZE_AGE, player.getEraAge());
        assertEquals(2, player.getEraAge().getLevel());
    }
}
