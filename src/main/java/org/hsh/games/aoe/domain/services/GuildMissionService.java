package org.hsh.games.aoe.domain.services;

import org.hsh.games.aoe.domain.entities.EraAge;
import org.hsh.games.aoe.domain.entities.TechPhase;
import org.hsh.games.aoe.domain.entities.rebelcell.Guild;
import org.hsh.games.aoe.domain.entities.rebelcell.GuildMember;
import org.hsh.games.aoe.domain.entities.rebelcell.GuildMission;
import org.hsh.games.aoe.domain.entities.rebelcell.MissionType;
import org.hsh.games.aoe.domain.entities.resources.ResourceType;
import org.hsh.games.aoe.shared.utils.ThreadUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

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
     * Generates missions for the current tech phase based on difficulty and available guilds
     * @param phase Current tech phase of the game
     * @param numberOfMissions Number of missions to generate
     * @return List of generated missions
     */
    public List<GuildMission> generateMissionsPerPhase(TechPhase phase, int numberOfMissions) {
        Objects.requireNonNull(phase, "Tech phase cannot be null");
        
        if (numberOfMissions <= 0) {
            throw new IllegalArgumentException("Number of missions must be positive");
        }
        
        List<GuildMission> generatedMissions = new ArrayList<>();
        Random random = ThreadLocalRandom.current();
        
        for (int i = 0; i < numberOfMissions; i++) {
            MissionType missionType = getRandomMissionType(random);
            String missionId = generateMissionId(phase, missionType);
            
            Map<ResourceType, Integer> rewards = calculatePhaseRewards(phase, missionType, random);
            int durationHours = calculateMissionDuration(missionType, phase);
            
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

    // Backward compatibility method
    @Deprecated(since = "2.0", forRemoval = true)
    public List<GuildMission> generateMissionsPerEra(EraAge era, int numberOfMissions) {
        TechPhase phase = TechPhase.getByLevel(era.getLevel());
        return generateMissionsPerPhase(phase, numberOfMissions);
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
                        return member != null && member.isInfiltrator();
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
                    
                    // Distribute rewards to guild vault with plunder bonuses
                    distributeRewardsWithPlunderBonus(guild, resolvedMission);
                    
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
        
        List<GuildMission> result = new ArrayList<>();
        for (String missionId : missionIds) {
            GuildMission mission = getMissionById(missionId);
            if (mission != null && (mission.getStatus().isActive() || mission.getStatus().canBeStarted())) {
                result.add(mission);
            }
        }
        return result;
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
            default -> 5; // Default to 5 minutes
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
            default -> 0.0; // No modifier for other types
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
     * Distributes mission rewards to the guild vault with plunder bonus calculations.
     * Applies skill-based loot multipliers for combat/raid missions.
     * @param guild Guild to receive rewards
     * @param mission Completed mission with rewards
     */
    private void distributeRewardsWithPlunderBonus(Guild guild, GuildMission mission) {
        Map<ResourceType, Integer> rewards = mission.getRewardMapCopy();
        
        // Apply plunder bonus for combat missions
        if (mission.isCombatMission()) {
            rewards = applyPlunderBonusToRewards(rewards, mission, guild);
        }
        
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
     * Applies plunder bonus to mission rewards based on participants' skills.
     * Stub implementation for skill-based loot multiplier integration.
     * @param baseRewards Original mission rewards
     * @param mission Mission with participant information
     * @param guild Guild executing the mission
     * @return Modified rewards with plunder bonuses applied
     */
    private Map<ResourceType, Integer> applyPlunderBonusToRewards(
            Map<ResourceType, Integer> baseRewards, 
            GuildMission mission, 
            Guild guild) {
        
        Map<ResourceType, Integer> bonusRewards = new HashMap<>(baseRewards);
        
        // Apply plunder bonus for each participant
        for (String participantId : mission.participants()) {
            // TODO: Replace with actual PlayerService lookup when available
            // For now, this is a stub implementation
            PlayerService playerService = getPlayerServiceForParticipant(participantId);
            
            if (playerService != null) {
                double lootMultiplier = playerService.getLootMultiplier();
                
                // Apply loot multiplier to combat-related resources
                for (Map.Entry<ResourceType, Integer> reward : bonusRewards.entrySet()) {
                    int baseLoot = baseRewards.get(reward.getKey());
                    int plunder = (int)(baseLoot * lootMultiplier);
                    
                    // Update the reward with plunder bonus (average across all participants)
                    int currentAmount = reward.getValue();
                    int newAmount = currentAmount + ((plunder - baseLoot) / mission.getParticipantCount());
                    reward.setValue(Math.max(baseLoot, newAmount));
                }
            }
        }
        
        return bonusRewards;
    }
    
    /**
     * Stub method to get PlayerService for a participant.
     * This should be replaced with actual player service lookup when available.
     * @param participantId Player ID to get service for
     * @return PlayerService instance or null if not available
     */
    private PlayerService getPlayerServiceForParticipant(String participantId) {
        // TODO: Implement actual player service lookup
        // This is a stub implementation for now
        // return gameContext.getPlayerService(participantId);
        return null;
    }
    
    /**
     * Calculates rewards based on tech phase and mission type
     * Uses TechPhase multipliers and ResourceType utilities for enhanced reward calculation
     * @param phase Current tech phase
     * @param missionType Type of mission
     * @param random Random generator
     * @return Map of resource rewards
     */
    private Map<ResourceType, Integer> calculatePhaseRewards(TechPhase phase, MissionType missionType, Random random) {
        Map<ResourceType, Integer> rewards = new HashMap<>();
        
        // Use phase's mission generation multiplier for more sophisticated scaling
        double phaseMultiplier = phase.getMissionGenerationMultiplier();
        
        // Base rewards scaled by phase multiplier
        switch (missionType) {
            case RESOURCE_RUN -> {
                // Include phase-appropriate resources using ResourceType utility
                List<ResourceType> phaseResources = ResourceType.getResourcesPackBasedOnCurrentPhase(phase.getLevel());
                
                // Add basic resources for resource runs
                rewards.put(ResourceType.ENERGY, (int)((50 + random.nextInt(50)) * phaseMultiplier));
                rewards.put(ResourceType.COMPONENTS, (int)((30 + random.nextInt(30)) * phaseMultiplier));
                rewards.put(ResourceType.DATA, (int)((40 + random.nextInt(40)) * phaseMultiplier));
                
                // Add phase-specific resource bonus
                if (!phaseResources.isEmpty()) {
                    ResourceType phaseSpecificResource = phaseResources.get(random.nextInt(phaseResources.size()));
                    int bonusAmount = (int)((20 + random.nextInt(30)) * phaseMultiplier);
                    rewards.put(phaseSpecificResource, bonusAmount);
                }
            }
            case TERRITORY_CONQUEST -> {
                rewards.put(ResourceType.QUANTUM_ENERGY, (int)((100 + random.nextInt(100)) * phaseMultiplier));
                rewards.put(ResourceType.CRYPTO, (int)((20 + random.nextInt(20)) * phaseMultiplier));
                
                // Higher phases get additional advanced resources
                if (phase.getLevel() >= 4) { // Drone Dominion and above
                    List<ResourceType> advancedResources = ResourceType.getResourcesPackBasedOnCurrentPhase(phase.getLevel());
                    if (!advancedResources.isEmpty()) {
                        ResourceType advancedResource = advancedResources.get(0);
                        rewards.put(advancedResource, (int)((15 + random.nextInt(25)) * phaseMultiplier));
                    }
                }
            }
            case ESPIONAGE -> {
                rewards.put(ResourceType.CRYPTO, (int)((30 + random.nextInt(30)) * phaseMultiplier));
                rewards.put(ResourceType.QUANTUM_ENERGY, (int)((50 + random.nextInt(50)) * phaseMultiplier));
                
                // Espionage missions in advanced phases yield intelligence bonuses
                if (phase.getLevel() >= 6) { // Singularity Prep and above
                    rewards.put(ResourceType.CRYPTO, rewards.get(ResourceType.CRYPTO) + (int)(10 * phaseMultiplier));
                }
            }
            case VAULT_DEFENSE -> {
                rewards.put(ResourceType.QUANTUM_ENERGY, (int)((80 + random.nextInt(80)) * phaseMultiplier));
                rewards.put(ResourceType.CIRCUITS, (int)((25 + random.nextInt(25)) * phaseMultiplier));
                
                // Vault defense gets defensive resources based on phase
                List<ResourceType> availableResources = ResourceType.getResourcesPackBasedOnCurrentPhase(phase.getLevel());
                if (availableResources.contains(ResourceType.COMPONENTS)) {
                    rewards.put(ResourceType.COMPONENTS, (int)((30 + random.nextInt(30)) * phaseMultiplier));
                }
            }
        }
        
        return rewards;
    }

    // Backward compatibility method
    @Deprecated(since = "2.0", forRemoval = true)
    private Map<ResourceType, Integer> calculateEraRewards(EraAge era, MissionType missionType, Random random) {
        TechPhase phase = TechPhase.getByLevel(era.getLevel());
        return calculatePhaseRewards(phase, missionType, random);
    }
    
    /**
     * Calculates mission duration based on type and tech phase
     * Uses TechPhase duration modifier for more sophisticated scaling
     * @param missionType Type of mission
     * @param phase Current tech phase
     * @return Duration in hours
     */
    private int calculateMissionDuration(MissionType missionType, TechPhase phase) {
        int baseDuration = switch (missionType) {
            case RESOURCE_RUN -> 2;
            case TERRITORY_CONQUEST -> 4;
            case ESPIONAGE -> 1;
            case VAULT_DEFENSE -> 3;
            default -> 2; // Default to 2 hours
        };
        
        // Use phase's mission duration modifier for more sophisticated scaling
        double phaseModifier = phase.getMissionDurationModifier();
        
        return Math.max(1, (int)(baseDuration * phaseModifier));
    }

    // Backward compatibility method
    @Deprecated(since = "2.0", forRemoval = true)
    private int calculateMissionDuration(MissionType missionType, EraAge era) {
        TechPhase phase = TechPhase.getByLevel(era.getLevel());
        return calculateMissionDuration(missionType, phase);
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
     * @param phase Current tech phase
     * @param missionType Type of mission
     * @return Generated mission ID
     */
    private String generateMissionId(TechPhase phase, MissionType missionType) {
        return String.format("mission_%s_%s_%d", 
            phase.name().toLowerCase(), 
            missionType.name().toLowerCase(), 
            System.currentTimeMillis());
    }

    // Backward compatibility method
    @Deprecated(since = "2.0", forRemoval = true)
    private String generateMissionId(EraAge era, MissionType missionType) {
        TechPhase phase = TechPhase.getByLevel(era.getLevel());
        return generateMissionId(phase, missionType);
    }
}
