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
    public void testPlayerTechPhaseIntegration() {
        // Given
        Player player = new Player("TestFarm");
        
        // When - Check initial tech phase
        // Then
        assertNotNull(player.getTechPhase());
        assertEquals(1, player.getTechPhase().getLevel());
        assertEquals(TechPhase.UPRISING, player.getTechPhase());
        
        // When - Advance to Augmented Streets
        player.setTechPhase(TechPhase.AUGMENTED_STREETS);
        
        // Then
        assertEquals(TechPhase.AUGMENTED_STREETS, player.getTechPhase());
        assertEquals(2, player.getTechPhase().getLevel());
    }
}
