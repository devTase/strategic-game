package org.hsh.games.aoe.entities;

import java.util.Arrays;
import java.util.Map;

public enum EraAge {
    STONE_AGE("Idade da Pedra", 1, Map.of(ConstructionType.TOWN_CENTER, 1, ConstructionType.HOUSE, 1)),
    BRONZE_AGE("Idade do Bronze", 2, Map.of(ConstructionType.TOWN_CENTER, 2, ConstructionType.HOUSE, 2, ConstructionType.LUMBER_CAMP, 2)),
    IRON_AGE("Idade do Ferro", 3, Map.of(ConstructionType.TOWN_CENTER, 3, ConstructionType.HOUSE, 3, ConstructionType.LUMBER_CAMP, 3, ConstructionType.MILL, 3)),
    MEDIEVAL_AGE("Idade Medával", 4, Map.of(ConstructionType.TOWN_CENTER, 4, ConstructionType.HOUSE, 4, ConstructionType.LUMBER_CAMP, 4, ConstructionType.MILL, 4, ConstructionType.BARRACKS, 4)),
    RENAISSANCE("Idade do Renascimento", 5, Map.of(ConstructionType.TOWN_CENTER, 5, ConstructionType.HOUSE, 5, ConstructionType.LUMBER_CAMP, 5, ConstructionType.MILL, 5, ConstructionType.BARRACKS, 5, ConstructionType.ARCHERY_RANGE, 5)),
    INDUSTRIAL_AGE("Idade Industrial", 6, Map.of(ConstructionType.TOWN_CENTER, 6, ConstructionType.HOUSE, 6)),
    MODERN_AGE("Idade Moderna", 7, Map.of(ConstructionType.TOWN_CENTER, 7, ConstructionType.HOUSE, 7)),
    INFORMATION_AGE("Idade da Informação", 8, Map.of(ConstructionType.TOWN_CENTER, 8, ConstructionType.HOUSE, 8)),
    FUTURE_AGE("Idade Futura", 9, Map.of(ConstructionType.TOWN_CENTER, 9, ConstructionType.HOUSE, 9));

    private final String eraName;
    private final int level;
    private final Map<ConstructionType, Integer> requirementsForNextLevel;

    EraAge(String eraName, int level, Map<ConstructionType, Integer> requirementsForNextLevel) {
        this.eraName = eraName;
        this.level = level;
        this.requirementsForNextLevel = requirementsForNextLevel;
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
