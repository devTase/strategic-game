package org.hsh.games.aoe.entities;

import java.util.Arrays;
import java.util.Map;

public enum TechPhase {
    UPRISING("Uprising", 1, createTechPhaseRequirements(1)),
    AUGMENTED_STREETS("Augmented Streets", 2, createTechPhaseRequirements(2)),
    NEURAL_NEXUS("Neural Nexus", 3, createTechPhaseRequirements(3)),
    DRONE_DOMINION("Drone Dominion", 4, createTechPhaseRequirements(4)),
    QUANTUM_DAWN("Quantum Dawn", 5, createTechPhaseRequirements(5)),
    SINGULARITY_PREP("Singularity Prep", 6, createTechPhaseRequirements(6)),
    POST_SINGULARITY("Post-Singularity", 7, createTechPhaseRequirements(7)),
    HYPER_MESH("Hyper-Mesh", 8, createTechPhaseRequirements(8)),
    EXO_REALITY("Exo-Reality", 9, createTechPhaseRequirements(9));

    private final String phaseName;
    private final int level;
    private final Map<ConstructionType, Integer> requirementsForNextLevel;

    TechPhase(String phaseName, int level, Map<ConstructionType, Integer> requirementsForNextLevel) {
        this.phaseName = phaseName;
        this.level = level;
        this.requirementsForNextLevel = requirementsForNextLevel;
    }
    
    private static Map<ConstructionType, Integer> createTechPhaseRequirements(int level) {
        Map<ConstructionType, Integer> requirements = new java.util.HashMap<>();
        
        // Base requirements for all tech phases
        requirements.put(ConstructionType.COMMAND_CENTER, level);
        requirements.put(ConstructionType.OP_BASE, level);
        
        // Progressive requirements based on level
        if (level >= 2) {
            requirements.put(ConstructionType.RESOURCE_DEPOT, level);
        }
        if (level >= 3) {
            requirements.put(ConstructionType.PROCESSING_PLANT, level);
        }
        if (level >= 4) {
            requirements.put(ConstructionType.TRAINING_FACILITY, level);
        }
        if (level >= 5) {
            requirements.put(ConstructionType.WEAPONS_RANGE, level);
        }
        if (level >= 6) {
            requirements.put(ConstructionType.TECH_FOUNDRY, level);
        }
        if (level >= 7) {
            requirements.put(ConstructionType.COMM_RELAY, level);
        }
        if (level >= 8) {
            requirements.put(ConstructionType.RESEARCH_LAB, level);
        }
        if (level >= 9) {
            requirements.put(ConstructionType.NEURAL_NEXUS, level);
        }
        
        return requirements;
    }

    public static TechPhase getByLevel(int level) {
        return Arrays.stream(TechPhase.values())
                .filter(techPhase -> techPhase.getLevel() == level)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid level"));
    }

    public Map<ConstructionType, Integer> getRequirementsForNextLevel() {
        return requirementsForNextLevel;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public int getLevel() {
        return this.level;
    }

    public TechPhase getNextPhase() {
        int nextLevel = this.level + 1;

        for (TechPhase techPhase : TechPhase.values()) {
            if (techPhase.level == nextLevel) {
                return techPhase;
            }
        }
        return this;
    }

    /**
     * Gets the mission generation multiplier for this tech phase
     * Higher phases have access to more complex and rewarding missions
     * @return Mission generation multiplier
     */
    public double getMissionGenerationMultiplier() {
        return switch (this) {
            case UPRISING -> 1.0;
            case AUGMENTED_STREETS -> 1.3;
            case NEURAL_NEXUS -> 1.7;
            case DRONE_DOMINION -> 2.2;
            case QUANTUM_DAWN -> 2.8;
            case SINGULARITY_PREP -> 3.5;
            case POST_SINGULARITY -> 4.3;
            case HYPER_MESH -> 5.2;
            case EXO_REALITY -> 6.4;
        };
    }

    /**
     * Gets the mission difficulty modifier for this tech phase
     * More advanced phases face more challenging missions
     * @return Difficulty modifier (higher = more difficult)
     */
    public double getMissionDifficultyModifier() {
        return switch (this) {
            case UPRISING -> 0.7;
            case AUGMENTED_STREETS -> 0.85;
            case NEURAL_NEXUS -> 1.0;
            case DRONE_DOMINION -> 1.15;
            case QUANTUM_DAWN -> 1.3;
            case SINGULARITY_PREP -> 1.5;
            case POST_SINGULARITY -> 1.7;
            case HYPER_MESH -> 1.9;
            case EXO_REALITY -> 2.2;
        };
    }

    /**
     * Gets the mission duration modifier for this tech phase
     * More advanced phases have longer, more complex missions
     * @return Duration modifier
     */
    public double getMissionDurationModifier() {
        return switch (this) {
            case UPRISING -> 0.6;
            case AUGMENTED_STREETS -> 0.75;
            case NEURAL_NEXUS -> 1.0;
            case DRONE_DOMINION -> 1.2;
            case QUANTUM_DAWN -> 1.4;
            case SINGULARITY_PREP -> 1.6;
            case POST_SINGULARITY -> 1.8;
            case HYPER_MESH -> 2.1;
            case EXO_REALITY -> 2.5;
        };
    }
}
