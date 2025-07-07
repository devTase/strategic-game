package org.hsh.games.aoe.entities;

import org.hsh.games.aoe.utils.ThreadUtils;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public enum ConstructionType {
    TOWN_CENTER("Cidade Central", 10, 2, ThreadUtils.toMilliseconds(1),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 100),
                    new ResourceAmount(ResourceType.WATER, 120),
                    new ResourceAmount(ResourceType.WOOD, 140)
            ),
            List.of(
                    new ResourceAmount(ResourceType.POPULATION, 1)
            ), ThreadUtils.toMilliseconds(7)
    ),
    LUMBER_CAMP("Madeireira", 2, 5, ThreadUtils.toMilliseconds(2),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 50),
                    new ResourceAmount(ResourceType.WOOD, 100)
            ),
            List.of(
                    new ResourceAmount(ResourceType.WOOD, 5)
            ), ThreadUtils.toMilliseconds(2)
    ),
    MILL("Moinho", 2,5, ThreadUtils.toMilliseconds(2),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 40),
                    new ResourceAmount(ResourceType.WATER, 150),
                    new ResourceAmount(ResourceType.WOOD, 80)
            ),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 5)
            ), ThreadUtils.toMilliseconds(2)
    ),
    HOUSE("Casa", 2,10, ThreadUtils.toMilliseconds(1),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 30),
                    new ResourceAmount(ResourceType.WOOD, 60)
            ),
            List.of(
                    new ResourceAmount(ResourceType.WATER, 5)
            ), ThreadUtils.toMilliseconds(5)
    ),
    BARRACKS("Barracas", 2,5, ThreadUtils.toMilliseconds(4),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 60),
                    new ResourceAmount(ResourceType.WATER, 130),
                    new ResourceAmount(ResourceType.WOOD, 120),
                    new ResourceAmount(ResourceType.STONE, 180)
            ),
            Collections.emptyList(),
            ThreadUtils.toMilliseconds(0)
    ),
    ARCHERY_RANGE("Archery Range", 2,5, ThreadUtils.toMilliseconds(5),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 70),
                    new ResourceAmount(ResourceType.GOLD, 40),
                    new ResourceAmount(ResourceType.WOOD, 150)
            ),
            Collections.emptyList(),
            ThreadUtils.toMilliseconds(0)
    ),
    STABLE("Stable", 2,3, ThreadUtils.toMilliseconds(7),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 80),
                    new ResourceAmount(ResourceType.GOLD, 50),
                    new ResourceAmount(ResourceType.WOOD, 180),
                    new ResourceAmount(ResourceType.IRON, 20)
            ),
            Collections.emptyList(),
            ThreadUtils.toMilliseconds(0)
    ),
    MARKET("Market", 2,3, ThreadUtils.toMilliseconds(5),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 90),
                    new ResourceAmount(ResourceType.GOLD, 60),
                    new ResourceAmount(ResourceType.WOOD, 120),
                    new ResourceAmount(ResourceType.STONE, 30)
            ),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 0)
            ), ThreadUtils.toMilliseconds(0)
    ),
    BLACKSMITH("Blacksmith", 2,3, ThreadUtils.toMilliseconds(6),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 100),
                    new ResourceAmount(ResourceType.GOLD, 70),
                    new ResourceAmount(ResourceType.WOOD, 100),
                    new ResourceAmount(ResourceType.IRON, 40)
            ),
            Collections.emptyList(),
            ThreadUtils.toMilliseconds(0)
    ),
    MONASTERY("Monastery", 2,1, ThreadUtils.toMilliseconds(8),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 120),
                    new ResourceAmount(ResourceType.GOLD, 80),
                    new ResourceAmount(ResourceType.WOOD, 150),
                    new ResourceAmount(ResourceType.STONE, 50)
            ),
            Collections.emptyList(),
            ThreadUtils.toMilliseconds(0)
    ),
    UNIVERSITY("University", 2,1, ThreadUtils.toMilliseconds(10),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 150),
                    new ResourceAmount(ResourceType.GOLD, 100),
                    new ResourceAmount(ResourceType.WOOD, 200),
                    new ResourceAmount(ResourceType.STONE, 80),
                    new ResourceAmount(ResourceType.IRON, 30)
            ),
            Collections.emptyList(),
            ThreadUtils.toMilliseconds(0)
    ),
    FARM("Farm", 2,5, ThreadUtils.toMilliseconds(3),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 40),
                    new ResourceAmount(ResourceType.GOLD, 0),
                    new ResourceAmount(ResourceType.WOOD, 60),
                    new ResourceAmount(ResourceType.WATER, 30)
            ),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 5)
            ), ThreadUtils.toMilliseconds(0)
    ),
    WINERY("Vinharia", 2,2, ThreadUtils.toMilliseconds(6),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 80),
                    new ResourceAmount(ResourceType.GOLD, 40),
                    new ResourceAmount(ResourceType.WOOD, 100),
                    new ResourceAmount(ResourceType.GRAPES, 50)
            ),
            List.of(
                    new ResourceAmount(ResourceType.GRAPES, 5)
            ), ThreadUtils.toMilliseconds(0)
    ),
    DOCK("Docas", 2,3, ThreadUtils.toMilliseconds(5),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 70),
                    new ResourceAmount(ResourceType.GOLD, 20),
                    new ResourceAmount(ResourceType.WOOD, 120),
                    new ResourceAmount(ResourceType.WATER, 80)
            ),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 5)
            ), ThreadUtils.toMilliseconds(0)
    ),
    TEMPLE("Templo", 2,1, ThreadUtils.toMilliseconds(9),
            List.of(
                    new ResourceAmount(ResourceType.FOOD, 100),
                    new ResourceAmount(ResourceType.GOLD, 60),
                    new ResourceAmount(ResourceType.WOOD, 150),
                    new ResourceAmount(ResourceType.STONE, 40),
                    new ResourceAmount(ResourceType.FAVOR, 30)
            ),
            List.of(
                    new ResourceAmount(ResourceType.FAVOR, 2)
            ), ThreadUtils.toMilliseconds(0)
    ),
    ;

    private String name;
    private int amountConstructionsAllowed;
    private final int maxLevel;
    private final int baseConstructionTime;
    private List<ResourceAmount> baseResourcesCost;
    private List<ResourceAmount> baseResourcesProduction;
    private int baseProductionTime;

    ConstructionType(
            String name,
            int maxLevel,
            int amountConstructionsAllowed,
            int baseConstructionTime,
            List<ResourceAmount> baseResourcesCost,
            List<ResourceAmount> baseResourcesProduction,
            int baseProductionTime
    ) {
        this.name = name;
        this.maxLevel = maxLevel;
        this.amountConstructionsAllowed = amountConstructionsAllowed;
        this.baseConstructionTime = baseConstructionTime;
        this.baseResourcesCost = baseResourcesCost;
        this.baseResourcesProduction = baseResourcesProduction;
        this.baseProductionTime = baseProductionTime;
    }

    public void setBaseResourcesCost(List<ResourceAmount> baseResourcesCost) {
        this.baseResourcesCost = baseResourcesCost;
    }

    public void setBaseResourcesProduction(List<ResourceAmount> baseResourcesProduction) {
        this.baseResourcesProduction = baseResourcesProduction;
    }

    public int getBaseProductionTime() {
        return baseProductionTime;
    }

    public void setBaseProductionTime(int baseProductionTime) {
        this.baseProductionTime = baseProductionTime;
    }

    public String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public List<ResourceAmount> getBaseResourcesCost() {
        return baseResourcesCost;
    }

    public int getAmountConstructionsAllowed() {
        return amountConstructionsAllowed;
    }

    public void setAmountConstructionsAllowed(int amountConstructionsAllowed) {
        this.amountConstructionsAllowed = amountConstructionsAllowed;
    }

    public int getBaseConstructionTime() {
        return baseConstructionTime;
    }

    public List<ResourceAmount> getBaseResourcesProduction() {
        return baseResourcesProduction;
    }

    public static ConstructionType getEnumFromConstant(String name) {
        for(ConstructionType x : ConstructionType.values()) {
            if(Objects.equals(x.name, name)) return x;
        }
        return null;
    }
}
