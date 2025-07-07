package org.hsh.games.aoe.services;

import org.hsh.games.aoe.entities.guild.*;
import org.hsh.games.aoe.entities.ResourceType;
import org.hsh.games.aoe.entities.EraAge;
import org.hsh.games.aoe.ThreadUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Service class for managing guild missions including generation, participant assignment,
 * and mission resolution with threaded delays.
 * Uses thread-safe collections for concurrent access.
 * 
 * @author devTASE
 */
public class GuildMissionService {
    
    private final Map<String, GuildMission> missions;
    private final Map<String, Set<String>> guildMissions; // guildId -> Set of missionIds
    private final GuildService guildService;
    
    /**
     * Constructor injection for dependencies
     * @param guildService Guild service for guild operations
     */
    public GuildMissionService(GuildService guildService) {
        this.missions = new ConcurrentHashMap<>();
        this.guildMissions = new ConcurrentHashMap<>();
        this.guildService = Objects.requireNonNull(guildService, "Guild service cannot be null");
    }
    
    /**
     * Generates missions for the current era based on difficulty and available guilds
     * @param era Current era of the game
     * @param numberOfMissions Number of missions to generate
     * @return List of generated missions
     */
    public List<GuildMission> generateMissionsPerEra(EraAge era, int numberOfMissions) {
        Objects.requireNonNull(era, "Era cannot be null");
        
        if (numberOfMissions <= 0) {
            throw new IllegalArgumentException("Number of missions must be positive");
        }
        
        List<GuildMission> generatedMissions = new ArrayList<>();
        Random random = ThreadLocalRandom.current();
        
        for (int i = 0; i < numberOfMissions; i++) {
            MissionType missionType = getRandomMissionType(random);
            String missionId = generateMissionId(era, missionType);
            
            Map<ResourceType, Integer> rewards = calculateEraRewards(era, missionType, random);
            int durationHours = calculateMissionDuration(missionType, era);
            
            GuildMission mission = GuildMission.createNew(
                missionId, 
                missionType, 
                rewards, 
                LocalDateTime.now().plusHours(durationHours)
            );
            
            missions.put(missionId, mission);
            generatedMissions.add(mission);
        }
        
        return generatedMissions;
    }
    
    /**
     * Assigns participants to a mission
     * @param missionId ID of the mission
     * @param guildId ID of the guild assigning participants
     * @param participantIds Set of player IDs to assign
     * @param assignerPlayerId ID of the player making the assignment
     * @return Updated mission with participants
     * @throws IllegalArgumentException if assignment is invalid
     */
    public GuildMission assignParticipants(String missionId, String guildId, Set<String> participantIds, String assignerPlayerId) {
        Objects.requireNonNull(missionId, "Mission ID cannot be null");
        Objects.requireNonNull(guildId, "Guild ID cannot be null");
        Objects.requireNonNull(participantIds, "Participant IDs cannot be null");
        Objects.requireNonNull(assignerPlayerId, "Assigner player ID cannot be null");
        
        GuildMission mission = getMissionById(missionId);
        if (mission == null) {
            throw new IllegalArgumentException("Mission not found");
        }
        
        Guild guild = guildService.getGuildById(guildId);
        if (guild == null) {
            throw new IllegalArgumentException("Guild not found");
        }
        
        // Check if assigner has permissions
        GuildMember assigner = guild.getMember(assignerPlayerId);
        if (assigner == null) {
            throw new IllegalArgumentException("Assigner is not a member of the guild");
        }
        
        if (!assigner.hasAdminPrivileges()) {
            throw new IllegalArgumentException("Assigner does not have permission to assign mission participants");
        }
        
        // Validate all participants are guild members
        for (String participantId : participantIds) {
            if (!guild.hasMember(participantId)) {
                throw new IllegalArgumentException("Player " + participantId + " is not a member of the guild");
            }
        }
        
        // Check mission requirements
        if (mission.requiresStealth()) {
            boolean hasSpies = participantIds.stream()
                    .anyMatch(playerId -> {
                        GuildMember member = guild.getMember(playerId);
                        return member != null && member.isSpy();
                    });
            
            if (!hasSpies) {
                throw new IllegalArgumentException("Stealth missions require at least one spy participant");
            }
        }
        
        GuildMission updatedMission = mission;
        for (String participantId : participantIds) {
            updatedMission = updatedMission.addParticipant(participantId);
        }
        
        missions.put(missionId, updatedMission);
        
        // Associate mission with guild
        guildMissions.computeIfAbsent(guildId, k -> ConcurrentHashMap.newKeySet()).add(missionId);
        
        guildService.broadcastEvent(guildId, 
            String.format("Mission %s assigned with %d participants by %s", 
                mission.type().getDisplayName(), participantIds.size(), assignerPlayerId));
        
        return updatedMission;
    }
    
    /**
     * Resolves a mission with ThreadUtils delays to simulate mission execution
     * @param missionId ID of the mission to resolve
     * @param guildId ID of the guild executing the mission
     * @return CompletableFuture that completes when mission is resolved
     */
    public CompletableFuture<GuildMission> resolveMission(String missionId, String guildId) {
        Objects.requireNonNull(missionId, "Mission ID cannot be null");
        Objects.requireNonNull(guildId, "Guild ID cannot be null");
        
        GuildMission mission = getMissionById(missionId);
        if (mission == null) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Mission not found"));
        }
        
        Guild guild = guildService.getGuildById(guildId);
        if (guild == null) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Guild not found"));
        }
        
        if (mission.getParticipantCount() == 0) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Cannot resolve mission without participants"));
        }
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Start mission
                GuildMission startedMission = mission.start();
                missions.put(missionId, startedMission);
                
                guildService.broadcastEvent(guildId, 
                    String.format("Mission %s has started with %d participants!", 
                        startedMission.type().getDisplayName(), startedMission.getParticipantCount()));
                
                // Calculate mission execution time with ThreadUtils
                int executionTimeMinutes = calculateExecutionTime(startedMission);
                int executionTimeMillis = ThreadUtils.toMilliseconds(executionTimeMinutes);
                
                // Simulate mission execution delay
                Thread.sleep(executionTimeMillis);
                
                // Determine mission outcome
                boolean success = calculateMissionSuccess(startedMission, guild);
                GuildMission resolvedMission;
                
                if (success) {
                    resolvedMission = startedMission.complete();
                    
                    // Distribute rewards to guild vault
                    distributeRewards(guild, resolvedMission);
                    
                    guildService.broadcastEvent(guildId, 
                        String.format("Mission %s completed successfully! Rewards distributed to guild vault.", 
                            resolvedMission.type().getDisplayName()));
                } else {
                    resolvedMission = startedMission.fail();
                    
                    guildService.broadcastEvent(guildId, 
                        String.format("Mission %s failed. Better luck next time!", 
                            resolvedMission.type().getDisplayName()));
                }
                
                missions.put(missionId, resolvedMission);
                return resolvedMission;
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                
                // Mark mission as failed due to interruption
                GuildMission failedMission = mission.fail();
                missions.put(missionId, failedMission);
                
                guildService.broadcastEvent(guildId, 
                    String.format("Mission %s was interrupted and failed!", 
                        failedMission.type().getDisplayName()));
                
                return failedMission;
            }
        });
    }
    
    /**
     * Gets all active missions for a guild
     * @param guildId ID of the guild
     * @return List of active missions
     */
    public List<GuildMission> getGuildMissions(String guildId) {
        Set<String> missionIds = guildMissions.getOrDefault(guildId, Collections.emptySet());
        
        return missionIds.stream()
                .map(this::getMissionById)
                .filter(Objects::nonNull)
                .filter(mission -> mission.status().isActive() || mission.status().canBeStarted())
                .collect(Collectors.toList());
    }
    
    /**
     * Gets a mission by its ID
     * @param missionId ID of the mission
     * @return Mission instance or null if not found
     */
    public GuildMission getMissionById(String missionId) {
        return missions.get(missionId);
    }
    
    /**
     * Gets all missions
     * @return Collection of all missions
     */
    public Collection<GuildMission> getAllMissions() {
        return missions.values();
    }
    
    /**
     * Cancels a mission
     * @param missionId ID of the mission to cancel
     * @param guildId ID of the guild canceling the mission
     * @param cancellerPlayerId ID of the player canceling the mission
     * @return Cancelled mission
     * @throws IllegalArgumentException if cancellation is invalid
     */
    public GuildMission cancelMission(String missionId, String guildId, String cancellerPlayerId) {
        Objects.requireNonNull(missionId, "Mission ID cannot be null");
        Objects.requireNonNull(guildId, "Guild ID cannot be null");
        Objects.requireNonNull(cancellerPlayerId, "Canceller player ID cannot be null");
        
        GuildMission mission = getMissionById(missionId);
        if (mission == null) {
            throw new IllegalArgumentException("Mission not found");
        }
        
        Guild guild = guildService.getGuildById(guildId);
        if (guild == null) {
            throw new IllegalArgumentException("Guild not found");
        }
        
        // Check if canceller has permissions
        GuildMember canceller = guild.getMember(cancellerPlayerId);
        if (canceller == null) {
            throw new IllegalArgumentException("Canceller is not a member of the guild");
        }
        
        if (!canceller.hasAdminPrivileges()) {
            throw new IllegalArgumentException("Canceller does not have permission to cancel missions");
        }
        
        GuildMission cancelledMission = mission.cancel();
        missions.put(missionId, cancelledMission);
        
        guildService.broadcastEvent(guildId, 
            String.format("Mission %s was cancelled by %s", 
                mission.type().getDisplayName(), cancellerPlayerId));
        
        return cancelledMission;
    }
    
    /**
     * Calculates mission execution time based on type and participants
     * @param mission Mission to calculate time for
     * @return Execution time in minutes
     */
    private int calculateExecutionTime(GuildMission mission) {
        int baseTime = switch (mission.type()) {
            case RESOURCE_RUN -> 5; // 5 minutes
            case TERRITORY_CONQUEST -> 10; // 10 minutes
            case ESPIONAGE -> 3; // 3 minutes
            case VAULT_DEFENSE -> 8; // 8 minutes
        };
        
        // Reduce time based on number of participants (max 50% reduction)
        int participants = mission.getParticipantCount();
        double reduction = Math.min(0.5, (participants - 1) * 0.1);
        
        return (int) (baseTime * (1 - reduction));
    }
    
    /**
     * Calculates mission success chance and determines outcome
     * Uses EraAge difficulty modifiers for era-appropriate challenge scaling
     * @param mission Mission to evaluate
     * @param guild Guild executing the mission
     * @return true if mission succeeds
     */
    private boolean calculateMissionSuccess(GuildMission mission, Guild guild) {
        double baseSuccessRate = 0.7; // 70% base success rate
        
        // Mission type modifiers
        double typeModifier = switch (mission.type()) {
            case RESOURCE_RUN -> 0.1; // +10% (easier)
            case TERRITORY_CONQUEST -> -0.2; // -20% (harder)
            case ESPIONAGE -> 0.0; // no modifier
            case VAULT_DEFENSE -> 0.15; // +15% (defending own territory)
        };
        
        // Participant count bonus (more people = higher success chance)
        double participantBonus = Math.min(0.2, mission.getParticipantCount() * 0.05);
        
        // Guild experience bonus (more territories = more experienced)
        double experienceBonus = Math.min(0.1, guild.getTerritoryCount() * 0.02);
        
        // Era difficulty modifier - higher eras face more challenging missions
        // We need to calculate average era level of participants
        // For now, assume guild leader's era as approximation
        EraAge approximateEra = EraAge.getByLevel(Math.min(9, Math.max(1, guild.getTerritoryCount() + 1)));
        double eraDifficultyPenalty = (approximateEra.getMissionDifficultyModifier() - 1.0) * 0.5; // Scale down the impact
        
        double totalSuccessRate = baseSuccessRate + typeModifier + participantBonus + experienceBonus - eraDifficultyPenalty;
        totalSuccessRate = Math.max(0.1, Math.min(0.95, totalSuccessRate)); // Clamp between 10% and 95%
        
        return ThreadLocalRandom.current().nextDouble() < totalSuccessRate;
    }
    
    /**
     * Distributes mission rewards to the guild vault
     * @param guild Guild to receive rewards
     * @param mission Completed mission with rewards
     */
    private void distributeRewards(Guild guild, GuildMission mission) {
        Map<ResourceType, Integer> rewards = mission.getRewardMapCopy();
        
        for (Map.Entry<ResourceType, Integer> reward : rewards.entrySet()) {
            try {
                guildService.depositToVault(
                    guild.id(), 
                    reward.getKey(), 
                    reward.getValue(), 
                    "MISSION_SYSTEM"
                );
            } catch (Exception e) {
                // Log error but continue with other rewards
                System.err.printf("Failed to distribute reward %s: %s%n", reward.getKey(), e.getMessage());
            }
        }
    }
    
    /**
     * Calculates rewards based on era and mission type
     * Uses EraAge multipliers and ResourceType utilities for enhanced reward calculation
     * @param era Current era
     * @param missionType Type of mission
     * @param random Random generator
     * @return Map of resource rewards
     */
    private Map<ResourceType, Integer> calculateEraRewards(EraAge era, MissionType missionType, Random random) {
        Map<ResourceType, Integer> rewards = new HashMap<>();
        
        // Use era's mission generation multiplier for more sophisticated scaling
        double eraMultiplier = era.getMissionGenerationMultiplier();
        
        // Base rewards scaled by era multiplier
        switch (missionType) {
            case RESOURCE_RUN -> {
                // Include era-appropriate resources using ResourceType utility
                List<ResourceType> eraResources = ResourceType.getResourcesPackBasedOnCurrentEra(era.getLevel());
                
                // Add basic resources for resource runs
                rewards.put(ResourceType.WOOD, (int)((50 + random.nextInt(50)) * eraMultiplier));
                rewards.put(ResourceType.STONE, (int)((30 + random.nextInt(30)) * eraMultiplier));
                rewards.put(ResourceType.FOOD, (int)((40 + random.nextInt(40)) * eraMultiplier));
                
                // Add era-specific resource bonus
                if (!eraResources.isEmpty()) {
                    ResourceType eraSpecificResource = eraResources.get(random.nextInt(eraResources.size()));
                    int bonusAmount = (int)((20 + random.nextInt(30)) * eraMultiplier);
                    rewards.put(eraSpecificResource, bonusAmount);
                }
            }
            case TERRITORY_CONQUEST -> {
                rewards.put(ResourceType.GOLD, (int)((100 + random.nextInt(100)) * eraMultiplier));
                rewards.put(ResourceType.FAVOR, (int)((20 + random.nextInt(20)) * eraMultiplier));
                
                // Higher eras get additional advanced resources
                if (era.getLevel() >= 4) { // Medieval age and above
                    List<ResourceType> advancedResources = ResourceType.getResourcesPackBasedOnCurrentEra(era.getLevel());
                    if (!advancedResources.isEmpty()) {
                        ResourceType advancedResource = advancedResources.get(0);
                        rewards.put(advancedResource, (int)((15 + random.nextInt(25)) * eraMultiplier));
                    }
                }
            }
            case ESPIONAGE -> {
                rewards.put(ResourceType.FAVOR, (int)((30 + random.nextInt(30)) * eraMultiplier));
                rewards.put(ResourceType.GOLD, (int)((50 + random.nextInt(50)) * eraMultiplier));
                
                // Espionage missions in advanced eras yield intelligence bonuses
                if (era.getLevel() >= 6) { // Industrial age and above
                    rewards.put(ResourceType.FAVOR, rewards.get(ResourceType.FAVOR) + (int)(10 * eraMultiplier));
                }
            }
            case VAULT_DEFENSE -> {
                rewards.put(ResourceType.GOLD, (int)((80 + random.nextInt(80)) * eraMultiplier));
                rewards.put(ResourceType.IRON, (int)((25 + random.nextInt(25)) * eraMultiplier));
                
                // Vault defense gets defensive resources based on era
                List<ResourceType> availableResources = ResourceType.getResourcesPackBasedOnCurrentEra(era.getLevel());
                if (availableResources.contains(ResourceType.STONE)) {
                    rewards.put(ResourceType.STONE, (int)((30 + random.nextInt(30)) * eraMultiplier));
                }
            }
        }
        
        return rewards;
    }
    
    /**
     * Calculates mission duration based on type and era
     * Uses EraAge duration modifier for more sophisticated scaling
     * @param missionType Type of mission
     * @param era Current era
     * @return Duration in hours
     */
    private int calculateMissionDuration(MissionType missionType, EraAge era) {
        int baseDuration = switch (missionType) {
            case RESOURCE_RUN -> 2;
            case TERRITORY_CONQUEST -> 4;
            case ESPIONAGE -> 1;
            case VAULT_DEFENSE -> 3;
        };
        
        // Use era's mission duration modifier for more sophisticated scaling
        double eraModifier = era.getMissionDurationModifier();
        
        return Math.max(1, (int)(baseDuration * eraModifier));
    }
    
    /**
     * Gets a random mission type
     * @param random Random generator
     * @return Random mission type
     */
    private MissionType getRandomMissionType(Random random) {
        MissionType[] types = MissionType.values();
        return types[random.nextInt(types.length)];
    }
    
    /**
     * Generates a unique mission ID
     * @param era Current era
     * @param missionType Type of mission
     * @return Generated mission ID
     */
    private String generateMissionId(EraAge era, MissionType missionType) {
        return String.format("mission_%s_%s_%d", 
            era.name().toLowerCase(), 
            missionType.name().toLowerCase(), 
            System.currentTimeMillis());
    }
}
