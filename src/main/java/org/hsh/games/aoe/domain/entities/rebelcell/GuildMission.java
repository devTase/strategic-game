package org.hsh.games.aoe.domain.entities.rebelcell;

import org.hsh.games.aoe.domain.entities.resources.ResourceType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a mission that can be undertaken by a Rebel Cell.
 * Missions have participants, rewards, and a deadline.
 * 
 * @author devTASE
 */
public class GuildMission {
    private final String missionId;
    private final MissionType type;
    private final Map<ResourceType, Integer> rewards;
    private final LocalDateTime deadline;
    private final Set<String> participants;
    private final MissionStatus status;

    private GuildMission(String missionId, MissionType type, Map<ResourceType, Integer> rewards, 
                         LocalDateTime deadline, Set<String> participants, MissionStatus status) {
        this.missionId = missionId;
        this.type = type;
        this.rewards = new HashMap<>(rewards);
        this.deadline = deadline;
        this.participants = new HashSet<>(participants);
        this.status = status;
    }

    public static GuildMission createNew(String missionId, MissionType type, 
                                        Map<ResourceType, Integer> rewards, LocalDateTime deadline) {
        return new GuildMission(missionId, type, rewards, deadline, new HashSet<>(), MissionStatus.PENDING);
    }

    public GuildMission addParticipant(String participantId) {
        Set<String> newParticipants = new HashSet<>(participants);
        newParticipants.add(participantId);
        return new GuildMission(missionId, type, rewards, deadline, newParticipants, status);
    }

    public GuildMission start() {
        return new GuildMission(missionId, type, rewards, deadline, participants, MissionStatus.IN_PROGRESS);
    }

    public GuildMission complete() {
        return new GuildMission(missionId, type, rewards, deadline, participants, MissionStatus.COMPLETED);
    }

    public GuildMission fail() {
        return new GuildMission(missionId, type, rewards, deadline, participants, MissionStatus.FAILED);
    }

    public boolean requiresStealth() {
        return type == MissionType.INFILTRATION || type == MissionType.SABOTAGE;
    }

    public int getParticipantCount() {
        return participants.size();
    }

    public String getMissionId() {
        return missionId;
    }

    public MissionType type() {
        return type;
    }

    public Map<ResourceType, Integer> getRewards() {
        return new HashMap<>(rewards);
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public Set<String> getParticipants() {
        return new HashSet<>(participants);
    }

    public MissionStatus getStatus() {
        return status;
    }

    /**
     * Gets a summary of the mission
     * @return Mission summary
     */
    public String getSummary() {
        return String.format("Mission: %s, Type: %s, Status: %s", 
                missionId, type, status);
    }

    /**
     * Gets a copy of the reward map
     * @return Copy of reward map
     */
    public Map<ResourceType, Integer> getRewardMapCopy() {
        return new HashMap<>(rewards);
    }

    /**
     * Cancels the mission
     * @return Updated mission with CANCELED status
     */
    public GuildMission cancel() {
        return new GuildMission(missionId, type, rewards, deadline, participants, MissionStatus.CANCELED);
    }

    /**
     * Checks if the mission is a combat mission
     * @return True if the mission is combat-related
     */
    public boolean isCombatMission() {
        return type == MissionType.BATTLE || type == MissionType.DEFENSE;
    }

    /**
     * Gets mission ID
     * @return mission ID
     */
    public String id() {
        return missionId;
    }

    /**
     * Gets mission status  
     * @return mission status
     */
    public MissionStatus status() {
        return status;
    }

    /**
     * Gets participants as a Set
     * @return Set of participant IDs
     */
    public Set<String> participants() {
        return new HashSet<>(participants);
    }

    /**
     * Gets formatted mission information
     * @return Formatted mission details
     */
    public String getFormattedInfo() {
        return String.format("Mission: %s [%s]\nType: %s\nStatus: %s\nDeadline: %s\nParticipants: %d\nRewards: %s", 
                missionId, type, type.name(), status, deadline, participants.size(), rewards);
    }

}
