package org.hsh.games.aoe.domain.entities;

import org.hsh.games.aoe.domain.entities.buildings.ConstructionType;

import java.util.Arrays;
import java.util.Map;

public enum EraAge {
    STONE_AGE("Idade da Pedra", 1, createEraRequirements(1)),
    BRONZE_AGE("Idade do Bronze", 2, createEraRequirements(2)),
    IRON_AGE("Idade do Ferro", 3, createEraRequirements(3)),
    MEDIEVAL_AGE("Idade Medával", 4, createEraRequirements(4)),
    RENAISSANCE("Idade do Renascimento", 5, createEraRequirements(5)),
    INDUSTRIAL_AGE("Idade Industrial", 6, createEraRequirements(6)),
    MODERN_AGE("Idade Moderna", 7, createEraRequirements(7)),
    INFORMATION_AGE("Idade da Informação", 8, createEraRequirements(8)),
    FUTURE_AGE("Idade Futura", 9, createEraRequirements(9));

    private final String eraName;
    private final int level;
    private final Map<ConstructionType, Integer> requirementsForNextLevel;

    EraAge(String eraName, int level, Map<ConstructionType, Integer> requirementsForNextLevel) {
        this.eraName = eraName;
        this.level = level;
        this.requirementsForNextLevel = requirementsForNextLevel;
    }
    
    private static Map<ConstructionType, Integer> createEraRequirements(int level) {
        Map<ConstructionType, Integer> requirements = new java.util.HashMap<>();
        
        // Base requirements for all eras
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

    public static EraAge getByLevel(int level) {
        return Arrays.stream(EraAge.values())
                .filter(eraAge -> eraAge.getLevel() == level)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid level"));
    }

    public Map<ConstructionType, Integer> getRequirementsForNextLevel() {
        return requirementsForNextLevel;
    }

    public String getEraName() {
        return eraName;
    }

    public int getLevel() {
        return this.level;
    }

    public EraAge getNextLevel() {
        int nextLevel = this.level + 1;

        for (EraAge eraAge : EraAge.values()) {
            if (eraAge.level == nextLevel) {
                return eraAge;
            }
        }
        return this;
    }

    /**
     * Gets the mission generation multiplier for this era
     * Higher eras have access to more complex and rewarding missions
     * @return Mission generation multiplier
     */
    public double getMissionGenerationMultiplier() {
        return switch (this) {
            case STONE_AGE -> 1.0;
            case BRONZE_AGE -> 1.2;
            case IRON_AGE -> 1.5;
            case MEDIEVAL_AGE -> 1.8;
            case RENAISSANCE -> 2.1;
            case INDUSTRIAL_AGE -> 2.5;
            case MODERN_AGE -> 3.0;
            case INFORMATION_AGE -> 3.5;
            case FUTURE_AGE -> 4.0;
        };
    }

    /**
     * Gets the mission difficulty modifier for this era
     * More advanced eras face more challenging missions
     * @return Difficulty modifier (higher = more difficult)
     */
    public double getMissionDifficultyModifier() {
        return switch (this) {
            case STONE_AGE -> 0.8;
            case BRONZE_AGE -> 0.9;
            case IRON_AGE -> 1.0;
            case MEDIEVAL_AGE -> 1.1;
            case RENAISSANCE -> 1.2;
            case INDUSTRIAL_AGE -> 1.3;
            case MODERN_AGE -> 1.4;
            case INFORMATION_AGE -> 1.5;
            case FUTURE_AGE -> 1.6;
        };
    }

    /**
     * Gets the mission duration modifier for this era
     * More advanced eras have longer, more complex missions
     * @return Duration modifier
     */
    public double getMissionDurationModifier() {
        return switch (this) {
            case STONE_AGE -> 0.7;
            case BRONZE_AGE -> 0.8;
            case IRON_AGE -> 1.0;
            case MEDIEVAL_AGE -> 1.1;
            case RENAISSANCE -> 1.2;
            case INDUSTRIAL_AGE -> 1.3;
            case MODERN_AGE -> 1.4;
            case INFORMATION_AGE -> 1.5;
            case FUTURE_AGE -> 1.7;
        };
    }
}
