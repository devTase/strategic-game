package org.hsh.games.aoe.entities;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public enum BuildingAndResourceAvailabilityPerLevel {
    LEVEL_1(1, List.of(
                    ConstructionType.COMMAND_CENTER,
                    ConstructionType.OP_BASE,
                    ConstructionType.RESOURCE_DEPOT,
                    ConstructionType.PROCESSING_PLANT,
                    ConstructionType.TACTICAL_CENTER
            ), ResourceType.getResourcesPackBasedOnCurrentEra(1)
    ),
    LEVEL_2(2,
            List.of(
                    ConstructionType.TRAINING_FACILITY,
                    ConstructionType.WEAPONS_RANGE
            ), ResourceType.getResourcesPackBasedOnCurrentEra(2)
    ),
    LEVEL_3(3,
            List.of(
                    ConstructionType.VEHICLE_BAY,
                    ConstructionType.TRADE_HUB
            ), ResourceType.getResourcesPackBasedOnCurrentEra(3)
    ),
    LEVEL_4(4,
            List.of(
                    ConstructionType.TECH_FOUNDRY,
                    ConstructionType.SUPPLY_STATION
            ), ResourceType.getResourcesPackBasedOnCurrentEra(4)
    ),
    LEVEL_5(5,
            List.of(
                    ConstructionType.CRYPTO_MINE,
                    ConstructionType.LAUNCH_PAD
            ), ResourceType.getResourcesPackBasedOnCurrentEra(5)
    ),
    LEVEL_6(6,
            List.of(
                    ConstructionType.COMM_RELAY,
                    ConstructionType.NEURAL_NEXUS
            ), ResourceType.getResourcesPackBasedOnCurrentEra(6)
    ),
    LEVEL_7(7,
            List.of(
                    ConstructionType.RESEARCH_LAB
            ), ResourceType.getResourcesPackBasedOnCurrentEra(7)
    ),
    LEVEL_8(8, new ArrayList<>(), ResourceType.getResourcesPackBasedOnCurrentEra(8)),
    LEVEL_9(9, new ArrayList<>(), ResourceType.getResourcesPackBasedOnCurrentEra(9)),
    ;

    private int level;
    private final List<ConstructionType> availableConstructions;
    private final List<ResourceType> availableResources;

    BuildingAndResourceAvailabilityPerLevel(int level, List<ConstructionType> availableConstructions, List<ResourceType> availableResources) {
        this.level = level;
        this.availableConstructions = availableConstructions;
        this.availableResources = availableResources;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public static BuildingAndResourceAvailabilityPerLevel getByLevel(int level) {
        return Arrays.stream(BuildingAndResourceAvailabilityPerLevel.values())
                .filter(it -> it.getLevel() == level)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid level"));
    }

    public List<ConstructionType> getAvailableConstructions() {
        return availableConstructions;
    }

    public List<ResourceType> getAvailableResources() {
        return availableResources;
    }
}
