package org.hsh.games.aoe.entities.guild;

import org.hsh.games.aoe.entities.ResourceType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Record representing a guild mission that members can participate in.
 * This is an immutable data structure that contains mission information.
 * 
 * @param id           Unique identifier for the mission
 * @param type         The type/category of mission
 * @param participants Set of player IDs participating in the mission
 * @param rewardMap    Map of resource types to their reward amounts
 * @param status       Current status of the mission
 * @param deadline     When the mission must be completed by
 * 
 * @author devTASE
 */
public record GuildMission(
        String id,
        MissionType type,
        Set<String> participants,
        Map<ResourceType, Integer> rewardMap,
        MissionStatus status,
        LocalDateTime deadline
) {
    
    /**
     * Constructor with validation
     */
    public GuildMission {
        Objects.requireNonNull(id, "Mission ID cannot be null");
        Objects.requireNonNull(type, "Mission type cannot be null");
        Objects.requireNonNull(participants, "Participants set cannot be null");
        Objects.requireNonNull(rewardMap, "Reward map cannot be null");
        Objects.requireNonNull(status, "Mission status cannot be null");
        Objects.requireNonNull(deadline, "Mission deadline cannot be null");
        
        if (id.isBlank()) {
            throw new IllegalArgumentException("Mission ID cannot be blank");
        }
        
        if (deadline.isBefore(LocalDateTime.now()) && status.canBeStarted()) {
            throw new IllegalArgumentException("Mission deadline cannot be in the past for pending missions");
        }
        
        // Create defensive copies
        participants = new HashSet<>(participants);
        rewardMap = new HashMap<>(rewardMap);
        
        // Validate participant IDs
        for (String participantId : participants) {
            if (participantId == null || participantId.isBlank()) {
                throw new IllegalArgumentException("Participant ID cannot be null or blank");
            }
        }
        
        // Validate reward amounts
        for (Map.Entry<ResourceType, Integer> entry : rewardMap.entrySet()) {
            if (entry.getValue() < 0) {
                throw new IllegalArgumentException("Reward amount cannot be negative: " + entry.getKey());
            }
        }
    }
    
    /**
     * Creates a new pending mission with the specified parameters
     * @param id Mission ID
     * @param type Mission type
     * @param rewardMap Reward mapping
     * @param deadline Mission deadline
     * @return A new GuildMission instance
     */
    public static GuildMission createNew(String id, MissionType type, Map<ResourceType, Integer> rewardMap, LocalDateTime deadline) {
        return new GuildMission(id, type, new HashSet<>(), rewardMap, MissionStatus.PENDING, deadline);
    }
    
    /**
     * Creates a new mission with default 24-hour deadline
     * @param id Mission ID
     * @param type Mission type
     * @param rewardMap Reward mapping
     * @return A new GuildMission instance with 24-hour deadline
     */
    public static GuildMission createWithDefaultDeadline(String id, MissionType type, Map<ResourceType, Integer> rewardMap) {
        return createNew(id, type, rewardMap, LocalDateTime.now().plusHours(24));
    }
    
    /**
     * Creates a resource run mission
     * @param id Mission ID
     * @param resourceReward Amount of random resources to reward
     * @param hoursToComplete Hours until deadline
     * @return A new resource run mission
     */
    public static GuildMission createResourceRun(String id, int resourceReward, int hoursToComplete) {
        Map<ResourceType, Integer> rewards = new HashMap<>();
        rewards.put(ResourceType.WOOD, resourceReward / 2);
        rewards.put(ResourceType.STONE, resourceReward / 3);
        rewards.put(ResourceType.FOOD, resourceReward / 4);
        
        return createNew(id, MissionType.RESOURCE_RUN, rewards, LocalDateTime.now().plusHours(hoursToComplete));
    }
    
    /**
     * Creates a territory conquest mission
     * @param id Mission ID
     * @param goldReward Gold reward amount
     * @param hoursToComplete Hours until deadline
     * @return A new territory conquest mission
     */
    public static GuildMission createTerritoryConquest(String id, int goldReward, int hoursToComplete) {
        Map<ResourceType, Integer> rewards = new HashMap<>();
        rewards.put(ResourceType.GOLD, goldReward);
        rewards.put(ResourceType.FAVOR, goldReward / 10); // Bonus favor
        
        return createNew(id, MissionType.TERRITORY_CONQUEST, rewards, LocalDateTime.now().plusHours(hoursToComplete));
    }
    
    /**
     * Adds a participant to the mission
     * @param playerId The player ID to add
     * @return A new GuildMission instance with the added participant
     * @throws IllegalArgumentException if mission cannot accept new participants
     */
    public GuildMission addParticipant(String playerId) {
        Objects.requireNonNull(playerId, "Player ID cannot be null");
        if (playerId.isBlank()) {
            throw new IllegalArgumentException("Player ID cannot be blank");
        }
        
        if (!status.canBeStarted() && status != MissionStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Cannot add participants to a mission with status: " + status);
        }
        
        if (participants.contains(playerId)) {
            return this; // Already participating
        }
        
        Set<String> newParticipants = new HashSet<>(participants);
        newParticipants.add(playerId);
        
        return new GuildMission(id, type, newParticipants, rewardMap, status, deadline);
    }
    
    /**
     * Removes a participant from the mission
     * @param playerId The player ID to remove
     * @return A new GuildMission instance with the removed participant
     */
    public GuildMission removeParticipant(String playerId) {
        if (!participants.contains(playerId)) {
            return this; // Not participating
        }
        
        Set<String> newParticipants = new HashSet<>(participants);
        newParticipants.remove(playerId);
        
        return new GuildMission(id, type, newParticipants, rewardMap, status, deadline);
    }
    
    /**
     * Starts the mission (changes status to IN_PROGRESS)
     * @return A new GuildMission instance with IN_PROGRESS status
     * @throws IllegalArgumentException if mission cannot be started
     */
    public GuildMission start() {
        if (!status.canBeStarted()) {
            throw new IllegalArgumentException("Mission cannot be started with status: " + status);
        }
        
        if (participants.isEmpty()) {
            throw new IllegalArgumentException("Cannot start mission without participants");
        }
        
        if (deadline.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot start mission past deadline");
        }
        
        return new GuildMission(id, type, participants, rewardMap, MissionStatus.IN_PROGRESS, deadline);
    }
    
    /**
     * Completes the mission successfully
     * @return A new GuildMission instance with COMPLETED status
     * @throws IllegalArgumentException if mission cannot be completed
     */
    public GuildMission complete() {
        if (!status.isActive()) {
            throw new IllegalArgumentException("Only active missions can be completed");
        }
        
        return new GuildMission(id, type, participants, rewardMap, MissionStatus.COMPLETED, deadline);
    }
    
    /**
     * Fails the mission
     * @return A new GuildMission instance with FAILED status
     * @throws IllegalArgumentException if mission cannot be failed
     */
    public GuildMission fail() {
        if (!status.isActive()) {
            throw new IllegalArgumentException("Only active missions can be failed");
        }
        
        return new GuildMission(id, type, participants, rewardMap, MissionStatus.FAILED, deadline);
    }
    
    /**
     * Cancels the mission
     * @return A new GuildMission instance with CANCELLED status
     * @throws IllegalArgumentException if mission cannot be cancelled
     */
    public GuildMission cancel() {
        if (status.isFinished()) {
            throw new IllegalArgumentException("Cannot cancel finished mission");
        }
        
        return new GuildMission(id, type, participants, rewardMap, MissionStatus.CANCELLED, deadline);
    }
    
    /**
     * Creates a copy of the mission with updated rewards
     * @param newRewardMap The new reward mapping
     * @return A new GuildMission instance with updated rewards
     */
    public GuildMission withRewards(Map<ResourceType, Integer> newRewardMap) {
        return new GuildMission(id, type, participants, newRewardMap, status, deadline);
    }
    
    /**
     * Creates a copy of the mission with extended deadline
     * @param additionalHours Hours to add to the deadline
     * @return A new GuildMission instance with extended deadline
     */
    public GuildMission extendDeadline(int additionalHours) {
        if (additionalHours <= 0) {
            throw new IllegalArgumentException("Additional hours must be positive");
        }
        
        return new GuildMission(id, type, participants, rewardMap, status, deadline.plusHours(additionalHours));
    }
    
    /**
     * Checks if a specific player is participating in this mission
     * @param playerId The player ID to check
     * @return true if the player is participating
     */
    public boolean hasParticipant(String playerId) {
        return participants.contains(playerId);
    }
    
    /**
     * Gets the number of participants
     * @return The participant count
     */
    public int getParticipantCount() {
        return participants.size();
    }
    
    /**
     * Checks if the mission is overdue
     * @return true if the current time is past the deadline
     */
    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(deadline);
    }
    
    /**
     * Gets the remaining time until deadline in hours
     * @return Hours remaining, or negative if overdue
     */
    public long getRemainingHours() {
        return java.time.Duration.between(LocalDateTime.now(), deadline).toHours();
    }
    
    /**
     * Gets the total reward value (sum of all resource rewards)
     * @return Total reward value
     */
    public int getTotalRewardValue() {
        return rewardMap.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * Gets the reward amount for a specific resource
     * @param resourceType The resource type to check
     * @return The reward amount, or 0 if not present
     */
    public int getRewardAmount(ResourceType resourceType) {
        return rewardMap.getOrDefault(resourceType, 0);
    }
    
    /**
     * Checks if the mission requires stealth
     * @return true if the mission type requires stealth
     */
    public boolean requiresStealth() {
        return type.requiresStealth();
    }
    
    /**
     * Checks if the mission is combat-oriented
     * @return true if the mission type involves combat
     */
    public boolean isCombatMission() {
        return type.isCombatMission();
    }
    
    /**
     * Checks if the mission is resource-oriented
     * @return true if the mission type involves resource gathering
     */
    public boolean isResourceMission() {
        return type.isResourceMission();
    }
    
    /**
     * Gets a formatted string representation of the mission
     * @return Formatted string with mission details
     */
    public String getFormattedInfo() {
        return String.format("%s [%s] - %s\n" +
                           "Participants: %d | Status: %s\n" +
                           "Deadline: %s | Total Reward: %d",
                           type.getDisplayNameWithEmoji(), id, type.getDescription(),
                           getParticipantCount(), status.getDisplayNameWithEmoji(),
                           deadline.toLocalDate(), getTotalRewardValue());
    }
    
    /**
     * Gets a summary string for display purposes
     * @return Short summary of the mission
     */
    public String getSummary() {
        return String.format("%s (%d participants) - %s", 
                           type.getDisplayNameWithEmoji(),
                           getParticipantCount(),
                           status.getDisplayNameWithEmoji());
    }
    
    /**
     * Returns defensive copies of the collections
     */
    public Set<String> getParticipantsCopy() {
        return new HashSet<>(participants);
    }
    
    public Map<ResourceType, Integer> getRewardMapCopy() {
        return new HashMap<>(rewardMap);
    }
}
