package org.hsh.games.aoe.services;

import org.hsh.games.aoe.entities.ResourceType;
import org.hsh.games.aoe.entities.EraAge;
import org.hsh.games.aoe.entities.guild.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GuildMissionService
 * 
 * @author devTASE
 */
class GuildMissionServiceTest {

    private GuildService guildService;
    private GuildMissionService missionService;

    @BeforeEach
    void setUp() {
        guildService = new GuildService();
        missionService = new GuildMissionService(guildService);
    }

    @Test
    void generateMissionsPerEraCreatesCorrectNumberOfMissions() {
        // Given
        int expectedMissions = 5;
        EraAge era = EraAge.getByLevel(1);

        // When
        var missions = missionService.generateMissionsPerEra(era, expectedMissions);

        // Then
        assertEquals(expectedMissions, missions.size());
    }

    @Test
    void assignParticipantsSuccessfullyAddsParticipantsToMission() {
        // Given
        Guild guild = guildService.createGuild("Test Guild", "leader", 1000);
        Set<String> participants = new HashSet<>();
        participants.add("player1");
        guildService.invitePlayer(guild.id(), "player1", "leader", GuildRank.SPY); // Use SPY rank for safety

        var missions = missionService.generateMissionsPerEra(EraAge.getByLevel(1), 5);
        
        // Find a non-espionage mission or create one that works
        GuildMission mission = missions.stream()
                .filter(m -> !m.requiresStealth())
                .findFirst()
                .orElse(missions.get(0)); // If all require stealth, use first one (SPY rank will handle it)

        // When
        GuildMission updatedMission = missionService.assignParticipants(
            mission.id(), guild.id(), participants, "leader");

        // Then
        assertEquals(1, updatedMission.getParticipantCount());
    }

    @Test
    void resolveMissionReturnsCompletableFuture() {
        // Given
        Guild guild = guildService.createGuild("Test Guild", "leader", 1000);
        guildService.invitePlayer(guild.id(), "player1", "leader", GuildRank.SPY); // Use SPY rank for safety

        var missions = missionService.generateMissionsPerEra(EraAge.getByLevel(1), 5);
        
        // Find a non-espionage mission or create one that works
        GuildMission mission = missions.stream()
                .filter(m -> !m.requiresStealth())
                .findFirst()
                .orElse(missions.get(0)); // If all require stealth, use first one (SPY rank will handle it)
                
        missionService.assignParticipants(mission.id(), guild.id(), Collections.singleton("player1"), "leader");

        // When
        var future = missionService.resolveMission(mission.id(), guild.id());

        // Then
        assertNotNull(future);
        assertFalse(future.isDone()); // Should not be completed immediately
    }

    @Test
    void resolveMissionFailsWithNoParticipants() {
        // Given
        Guild guild = guildService.createGuild("Test Guild", "leader", 1000);

        GuildMission mission = missionService.generateMissionsPerEra(EraAge.getByLevel(1), 1).get(0);

        // When
        var completableFuture = missionService.resolveMission(mission.id(), guild.id());

        // Then
        assertThrows(ExecutionException.class, completableFuture::get);
    }

}
