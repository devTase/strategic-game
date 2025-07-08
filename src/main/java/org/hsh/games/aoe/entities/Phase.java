package org.hsh.games.aoe.entities;

import java.util.Arrays;
import java.util.Map;

public enum Phase {
    EARLY_DIGITAL("Early Digital Phase", 1, createPhaseRequirements(1)),
    NEURAL_NETWORK("Neural Network Phase", 2, createPhaseRequirements(2)),
    CYBERNETIC("Cybernetic Phase", 3, createPhaseRequirements(3)),
    NANO_TECH("Nano-Tech Phase", 4, createPhaseRequirements(4)),
    QUANTUM_COMPUTING("Quantum Computing Phase", 5, createPhaseRequirements(5)),
    FUSION_TECH("Fusion Tech Phase", 6, createPhaseRequirements(6)),
    SINGULARITY("Singularity Phase", 7, createPhaseRequirements(7)),
    POST_HUMAN("Post-Human Phase", 8, createPhaseRequirements(8)),
    TRANSCENDENCE("Transcendence Phase", 9, createPhaseRequirements(9));

    private final String phaseName;
    private final int level;
    private final Map<ConstructionType, Integer> requirementsForNextLevel;

    Phase(String phaseName, int level, Map<ConstructionType, Integer> requirementsForNextLevel) {
        this.phaseName = phaseName;
        this.level = level;
        this.requirementsForNextLevel = requirementsForNextLevel;
    }
    
    private static Map<ConstructionType, Integer> createPhaseRequirements(int level) {
        Map<ConstructionType, Integer> requirements = new java.util.HashMap<>();
        
        // Base requirements for all phases
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
            requirements.put(ConstructionType.VEHICLE_BAY, level);
        }
        if (level >= 7) {
            requirements.put(ConstructionType.TRADE_HUB, level);
        }
        if (level >= 8) {
            requirements.put(ConstructionType.RESEARCH_LAB, level);
        }
        if (level >= 9) {
            requirements.put(ConstructionType.NEURAL_NEXUS, level);
        }
        
        return requirements;
    }

    public static Phase getByLevel(int level) {
        return Arrays.stream(Phase.values())
                .filter(phase -> phase.getLevel() == level)
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

    public Phase getNextLevel() {
        int nextLevel = this.level + 1;

        for (Phase phase : Phase.values()) {
            if (phase.level == nextLevel) {
                return phase;
            }
        }
        return this;
    }

    /**
     * Gets the mission generation multiplier for this phase
     * Higher phases have access to more complex and rewarding missions
     * @return Mission generation multiplier
     */
    public double getMissionGenerationMultiplier() {
        return switch (this) {
            case EARLY_DIGITAL -> 1.0;
            case NEURAL_NETWORK -> 1.3;
            case CYBERNETIC -> 1.7;
            case NANO_TECH -> 2.2;
            case QUANTUM_COMPUTING -> 2.8;
            case FUSION_TECH -> 3.5;
            case SINGULARITY -> 4.2;
            case POST_HUMAN -> 5.0;
            case TRANSCENDENCE -> 6.0;
        };
    }

    /**
     * Gets the mission difficulty modifier for this phase
     * More advanced phases face more challenging missions
     * @return Difficulty modifier (higher = more difficult)
     */
    public double getMissionDifficultyModifier() {
        return switch (this) {
            case EARLY_DIGITAL -> 0.8;
            case NEURAL_NETWORK -> 0.9;
            case CYBERNETIC -> 1.0;
            case NANO_TECH -> 1.2;
            case QUANTUM_COMPUTING -> 1.4;
            case FUSION_TECH -> 1.6;
            case SINGULARITY -> 1.8;
            case POST_HUMAN -> 2.0;
            case TRANSCENDENCE -> 2.3;
        };
    }

    /**
     * Gets the mission duration modifier for this phase
     * More advanced phases have longer, more complex missions
     * @return Duration modifier
     */
    public double getMissionDurationModifier() {
        return switch (this) {
            case EARLY_DIGITAL -> 0.7;
            case NEURAL_NETWORK -> 0.8;
            case CYBERNETIC -> 1.0;
            case NANO_TECH -> 1.2;
            case QUANTUM_COMPUTING -> 1.4;
            case FUSION_TECH -> 1.6;
            case SINGULARITY -> 1.8;
            case POST_HUMAN -> 2.0;
            case TRANSCENDENCE -> 2.3;
        };
    }

    // Legacy mapping for transition period
    private static final Map<String, Phase> LEGACY_ERA_MAPPING = Map.of(
        "STONE_AGE", EARLY_DIGITAL,
        "BRONZE_AGE", NEURAL_NETWORK,
        "IRON_AGE", CYBERNETIC,
        "MEDIEVAL_AGE", NANO_TECH,
        "RENAISSANCE", QUANTUM_COMPUTING,
        "INDUSTRIAL_AGE", FUSION_TECH,
        "MODERN_AGE", SINGULARITY,
        "INFORMATION_AGE", POST_HUMAN,
        "FUTURE_AGE", TRANSCENDENCE
    );

    /**
     * Gets the modern Phase from a legacy era name
     * Used during transition period to maintain compatibility
     * @param legacyEraName The legacy era name (e.g., "STONE_AGE", "MODERN_AGE")
     * @return The corresponding modern Phase
     * @throws IllegalArgumentException if legacy era name is not recognized
     */
    public static Phase fromLegacyEra(String legacyEraName) {
        Phase mapped = LEGACY_ERA_MAPPING.get(legacyEraName.toUpperCase());
        if (mapped == null) {
            throw new IllegalArgumentException("Unknown legacy era: " + legacyEraName);
        }
        return mapped;
    }

    /**
     * Gets the conversion map for all legacy eras
     * @return Map of legacy era names to modern Phase
     */
    public static Map<String, Phase> getLegacyEraConversionMap() {
        return Map.copyOf(LEGACY_ERA_MAPPING);
    }
}
