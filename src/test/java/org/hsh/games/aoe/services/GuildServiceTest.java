package org.hsh.games.aoe.services;

import org.hsh.games.aoe.entities.guild.*;
import org.hsh.games.aoe.entities.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GuildService
 * 
 * @author devTASE
 */
class GuildServiceTest {
    
    private GuildService guildService;
    
    @BeforeEach
    void setUp() {
        guildService = new GuildService();
    }
    
    @Test
    void createGuildSuccessfullyCreatesNewGuild() {
        // Given
        String guildName = "Test Guild";
        String founderId = "player1";
        int vaultCapacity = 1000;
        
        // When
        Guild guild = guildService.createGuild(guildName, founderId, vaultCapacity);
        
        // Then
        assertNotNull(guild);
        assertEquals(guildName, guild.name());
        assertEquals(founderId, guild.leaderPlayerId());
        assertTrue(guild.hasMember(founderId));
        assertEquals(1, guild.getMemberCount());
        assertEquals(vaultCapacity, guild.vault().capacity());
    }
    
    @Test
    void createGuildWithNullNameThrowsException() {
        assertThrows(NullPointerException.class, () -> 
            guildService.createGuild(null, "player1", 1000));
    }
    
    @Test
    void createGuildWithBlankNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            guildService.createGuild("", "player1", 1000));
    }
    
    @Test
    void createGuildWithNegativeVaultCapacityThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            guildService.createGuild("Test Guild", "player1", -1));
    }
    
    @Test
    void createGuildWithDuplicateNameThrowsException() {
        // Given
        guildService.createGuild("Test Guild", "player1", 1000);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            guildService.createGuild("Test Guild", "player2", 1000));
    }
    
    @Test
    void createGuildWithFounderAlreadyInGuildThrowsException() {
        // Given
        guildService.createGuild("First Guild", "player1", 1000);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            guildService.createGuild("Second Guild", "player1", 1000));
    }
    
    @Test
    void invitePlayerSuccessfullyAddsNewMember() {
        // Given
        Guild guild = guildService.createGuild("Test Guild", "leader", 1000);
        String newPlayerId = "player2";
        
        // When
        Guild updatedGuild = guildService.invitePlayer(
            guild.id(), newPlayerId, "leader", GuildRank.RECRUIT);
        
        // Then
        assertTrue(updatedGuild.hasMember(newPlayerId));
        assertEquals(2, updatedGuild.getMemberCount());
        assertEquals(GuildRank.RECRUIT, updatedGuild.getMember(newPlayerId).rank());
    }
    
    @Test
    void invitePlayerWithInvalidGuildThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            guildService.invitePlayer("invalid-guild", "player1", "leader", GuildRank.RECRUIT));
    }
    
    @Test
    void invitePlayerWithNonMemberInviterThrowsException() {
        // Given
        Guild guild = guildService.createGuild("Test Guild", "leader", 1000);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            guildService.invitePlayer(guild.id(), "player2", "nonmember", GuildRank.RECRUIT));
    }
    
    @Test
    void invitePlayerAlreadyInGuildThrowsException() {
        // Given
        Guild guild1 = guildService.createGuild("Guild 1", "leader1", 1000);
        Guild guild2 = guildService.createGuild("Guild 2", "leader2", 1000);
        guildService.invitePlayer(guild1.id(), "player1", "leader1", GuildRank.RECRUIT);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            guildService.invitePlayer(guild2.id(), "player1", "leader2", GuildRank.RECRUIT));
    }
    
    @Test
    void changeRankSuccessfullyPromotesMember() {
        // Given
        Guild guild = guildService.createGuild("Test Guild", "leader", 1000);
        guildService.invitePlayer(guild.id(), "player1", "leader", GuildRank.RECRUIT);
        
        // When
        Guild updatedGuild = guildService.changeRank(
            guild.id(), "player1", GuildRank.MEMBER, "leader");
        
        // Then
        assertEquals(GuildRank.MEMBER, updatedGuild.getMember("player1").rank());
    }
    
    @Test
    void changeRankOfLeaderThrowsException() {
        // Given
        Guild guild = guildService.createGuild("Test Guild", "leader", 1000);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            guildService.changeRank(guild.id(), "leader", GuildRank.MEMBER, "leader"));
    }
    
    @Test
    void changeRankWithoutPermissionThrowsException() {
        // Given
        Guild guild = guildService.createGuild("Test Guild", "leader", 1000);
        guildService.invitePlayer(guild.id(), "player1", "leader", GuildRank.RECRUIT);
        guildService.invitePlayer(guild.id(), "player2", "leader", GuildRank.RECRUIT);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            guildService.changeRank(guild.id(), "player2", GuildRank.MEMBER, "player1"));
    }
    
    @Test
    void depositToVaultSuccessfullyAddsResources() {
        // Given
        Guild guild = guildService.createGuild("Test Guild", "leader", 1000);
        
        // When
        Guild updatedGuild = guildService.depositToVault(
            guild.id(), ResourceType.WOOD, 100, "leader");
        
        // Then
        assertEquals(100, updatedGuild.vault().getResourceQuantity(ResourceType.WOOD));
    }
    
    @Test
    void depositToVaultWithNonMemberThrowsException() {
        // Given
        Guild guild = guildService.createGuild("Test Guild", "leader", 1000);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            guildService.depositToVault(guild.id(), ResourceType.WOOD, 100, "nonmember"));
    }
    
    @Test
    void depositToVaultWithNegativeAmountThrowsException() {
        // Given
        Guild guild = guildService.createGuild("Test Guild", "leader", 1000);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            guildService.depositToVault(guild.id(), ResourceType.WOOD, -10, "leader"));
    }
    
    @Test
    void removePlayerFromGuildSuccessfullyRemovesMember() {
        // Given
        Guild guild = guildService.createGuild("Test Guild", "leader", 1000);
        guildService.invitePlayer(guild.id(), "player1", "leader", GuildRank.RECRUIT);
        
        // When
        Guild updatedGuild = guildService.removePlayerFromGuild("player1", "leader");
        
        // Then
        assertFalse(updatedGuild.hasMember("player1"));
        assertEquals(1, updatedGuild.getMemberCount());
        assertFalse(guildService.isPlayerInGuild("player1"));
    }
    
    @Test
    void removePlayerFromGuildAllowsSelfRemoval() {
        // Given
        Guild guild = guildService.createGuild("Test Guild", "leader", 1000);
        guildService.invitePlayer(guild.id(), "player1", "leader", GuildRank.RECRUIT);
        
        // When
        Guild updatedGuild = guildService.removePlayerFromGuild("player1", "player1");
        
        // Then
        assertFalse(updatedGuild.hasMember("player1"));
        assertFalse(guildService.isPlayerInGuild("player1"));
    }
    
    @Test
    void removePlayerWithoutPermissionThrowsException() {
        // Given
        Guild guild = guildService.createGuild("Test Guild", "leader", 1000);
        guildService.invitePlayer(guild.id(), "player1", "leader", GuildRank.RECRUIT);
        guildService.invitePlayer(guild.id(), "player2", "leader", GuildRank.RECRUIT);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            guildService.removePlayerFromGuild("player2", "player1"));
    }
    
    @Test
    void getGuildByIdReturnsCorrectGuild() {
        // Given
        Guild guild = guildService.createGuild("Test Guild", "leader", 1000);
        
        // When
        Guild retrievedGuild = guildService.getGuildById(guild.id());
        
        // Then
        assertNotNull(retrievedGuild);
        assertEquals(guild.id(), retrievedGuild.id());
        assertEquals(guild.name(), retrievedGuild.name());
    }
    
    @Test
    void getGuildByIdWithInvalidIdReturnsNull() {
        // When
        Guild guild = guildService.getGuildById("invalid-id");
        
        // Then
        assertNull(guild);
    }
    
    @Test
    void getPlayerGuildReturnsCorrectGuild() {
        // Given
        Guild guild = guildService.createGuild("Test Guild", "leader", 1000);
        
        // When
        Guild playerGuild = guildService.getPlayerGuild("leader");
        
        // Then
        assertNotNull(playerGuild);
        assertEquals(guild.id(), playerGuild.id());
    }
    
    @Test
    void getPlayerGuildWithPlayerNotInGuildReturnsNull() {
        // When
        Guild guild = guildService.getPlayerGuild("nonmember");
        
        // Then
        assertNull(guild);
    }
    
    @Test
    void isPlayerInGuildReturnsTrueForGuildMember() {
        // Given
        guildService.createGuild("Test Guild", "leader", 1000);
        
        // When & Then
        assertTrue(guildService.isPlayerInGuild("leader"));
    }
    
    @Test
    void isPlayerInGuildReturnsFalseForNonMember() {
        // When & Then
        assertFalse(guildService.isPlayerInGuild("nonmember"));
    }
    
    @Test
    void getGuildCountReturnsCorrectCount() {
        // Given
        assertEquals(0, guildService.getGuildCount());
        
        guildService.createGuild("Guild 1", "leader1", 1000);
        assertEquals(1, guildService.getGuildCount());
        
        guildService.createGuild("Guild 2", "leader2", 1000);
        assertEquals(2, guildService.getGuildCount());
    }
    
    @Test
    void getAllGuildIdsReturnsAllGuildIds() {
        // Given
        Guild guild1 = guildService.createGuild("Guild 1", "leader1", 1000);
        Guild guild2 = guildService.createGuild("Guild 2", "leader2", 1000);
        
        // When
        var guildIds = guildService.getAllGuildIds();
        
        // Then
        assertEquals(2, guildIds.size());
        assertTrue(guildIds.contains(guild1.id()));
        assertTrue(guildIds.contains(guild2.id()));
    }
    
    @Test
    void broadcastEventDoesNotThrowWithValidParameters() {
        // Given
        Guild guild = guildService.createGuild("Test Guild", "leader", 1000);
        
        // When & Then
        assertDoesNotThrow(() -> 
            guildService.broadcastEvent(guild.id(), "Test message"));
    }
    
    @Test
    void broadcastEventWithNullMessageThrowsException() {
        // Given
        Guild guild = guildService.createGuild("Test Guild", "leader", 1000);
        
        // When & Then
        assertThrows(NullPointerException.class, () -> 
            guildService.broadcastEvent(guild.id(), null));
    }
    
    @Test
    void broadcastEventWithBlankMessageThrowsException() {
        // Given
        Guild guild = guildService.createGuild("Test Guild", "leader", 1000);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            guildService.broadcastEvent(guild.id(), ""));
    }
}
