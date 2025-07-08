package org.hsh.games.aoe.entities;

import org.hsh.games.aoe.utils.ThreadUtils;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Map;

public enum ConstructionType {
    COMMAND_CENTER("Command Center", 10, 2, ThreadUtils.toMilliseconds(1),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 100),
                    new ResourceAmount(ResourceType.ENERGY, 120),
                    new ResourceAmount(ResourceType.ENERGY, 140)
            ),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 1)
            ), ThreadUtils.toMilliseconds(7)
    ),
    RESOURCE_DEPOT("Resource Depot", 2, 5, ThreadUtils.toMilliseconds(2),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 50),
                    new ResourceAmount(ResourceType.ENERGY, 100)
            ),
            List.of(
                    new ResourceAmount(ResourceType.ENERGY, 5)
            ), ThreadUtils.toMilliseconds(2)
    ),
    PROCESSING_PLANT("Processing Plant", 2,5, ThreadUtils.toMilliseconds(2),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 40),
                    new ResourceAmount(ResourceType.ENERGY, 150),
                    new ResourceAmount(ResourceType.ENERGY, 80)
            ),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 5)
            ), ThreadUtils.toMilliseconds(2)
    ),
    OP_BASE("Operations Base", 2,10, ThreadUtils.toMilliseconds(1),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 30),
                    new ResourceAmount(ResourceType.ENERGY, 60)
            ),
            List.of(
                    new ResourceAmount(ResourceType.ENERGY, 5)
            ), ThreadUtils.toMilliseconds(5)
    ),
    TRAINING_FACILITY("Training Facility", 5, 2, ThreadUtils.toMilliseconds(4),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 60),
                    new ResourceAmount(ResourceType.ENERGY, 130),
                    new ResourceAmount(ResourceType.ENERGY, 120),
                    new ResourceAmount(ResourceType.COMPONENTS, 180)
            ),
            Collections.emptyList(),
            ThreadUtils.toMilliseconds(0)
    ),
    WEAPONS_RANGE("Weapons Range", 2,5, ThreadUtils.toMilliseconds(5),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 70),
                    new ResourceAmount(ResourceType.QUANTUM_ENERGY, 40),
                    new ResourceAmount(ResourceType.ENERGY, 150)
            ),
            Collections.emptyList(),
            ThreadUtils.toMilliseconds(0)
    ),
    VEHICLE_BAY("Vehicle Bay", 2,3, ThreadUtils.toMilliseconds(7),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 80),
                    new ResourceAmount(ResourceType.QUANTUM_ENERGY, 50),
                    new ResourceAmount(ResourceType.ENERGY, 180),
                    new ResourceAmount(ResourceType.CIRCUITS, 20)
            ),
            Collections.emptyList(),
            ThreadUtils.toMilliseconds(0)
    ),
    TRADE_HUB("Trade Hub", 2,3, ThreadUtils.toMilliseconds(5),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 90),
                    new ResourceAmount(ResourceType.QUANTUM_ENERGY, 60),
                    new ResourceAmount(ResourceType.ENERGY, 120),
                    new ResourceAmount(ResourceType.COMPONENTS, 30)
            ),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 0)
            ), ThreadUtils.toMilliseconds(0)
    ),
    TECH_FOUNDRY("Tech Foundry", 2,3, ThreadUtils.toMilliseconds(6),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 100),
                    new ResourceAmount(ResourceType.QUANTUM_ENERGY, 70),
                    new ResourceAmount(ResourceType.ENERGY, 100),
                    new ResourceAmount(ResourceType.CIRCUITS, 40)
            ),
            Collections.emptyList(),
            ThreadUtils.toMilliseconds(0)
    ),
    COMM_RELAY("Communications Relay", 2,1, ThreadUtils.toMilliseconds(8),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 120),
                    new ResourceAmount(ResourceType.QUANTUM_ENERGY, 80),
                    new ResourceAmount(ResourceType.ENERGY, 150),
                    new ResourceAmount(ResourceType.COMPONENTS, 50)
            ),
            Collections.emptyList(),
            ThreadUtils.toMilliseconds(0)
    ),
    RESEARCH_LAB("Research Laboratory", 2,1, ThreadUtils.toMilliseconds(10),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 150),
                    new ResourceAmount(ResourceType.QUANTUM_ENERGY, 100),
                    new ResourceAmount(ResourceType.ENERGY, 200),
                    new ResourceAmount(ResourceType.COMPONENTS, 80),
                    new ResourceAmount(ResourceType.CIRCUITS, 30)
            ),
            Collections.emptyList(),
            ThreadUtils.toMilliseconds(0)
    ),
    SUPPLY_STATION("Supply Station", 2,5, ThreadUtils.toMilliseconds(3),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 40),
                    new ResourceAmount(ResourceType.QUANTUM_ENERGY, 0),
                    new ResourceAmount(ResourceType.ENERGY, 60),
                    new ResourceAmount(ResourceType.ENERGY, 30)
            ),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 5)
            ), ThreadUtils.toMilliseconds(0)
    ),
    CRYPTO_MINE("Crypto Mining Facility", 2,2, ThreadUtils.toMilliseconds(6),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 80),
                    new ResourceAmount(ResourceType.QUANTUM_ENERGY, 40),
                    new ResourceAmount(ResourceType.ENERGY, 100),
                    new ResourceAmount(ResourceType.COMPONENTS, 50)
            ),
            List.of(
                    new ResourceAmount(ResourceType.COMPONENTS, 5)
            ), ThreadUtils.toMilliseconds(0)
    ),
    LAUNCH_PAD("Launch Pad", 2,3, ThreadUtils.toMilliseconds(5),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 70),
                    new ResourceAmount(ResourceType.QUANTUM_ENERGY, 20),
                    new ResourceAmount(ResourceType.ENERGY, 120),
                    new ResourceAmount(ResourceType.ENERGY, 80)
            ),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 5)
            ), ThreadUtils.toMilliseconds(0)
    ),
    NEURAL_NEXUS("Neural Nexus", 2,1, ThreadUtils.toMilliseconds(9),
            List.of(
                    new ResourceAmount(ResourceType.DATA, 100),
                    new ResourceAmount(ResourceType.QUANTUM_ENERGY, 60),
                    new ResourceAmount(ResourceType.ENERGY, 150),
                    new ResourceAmount(ResourceType.COMPONENTS, 40),
                    new ResourceAmount(ResourceType.CRYPTO, 30)
            ),
            List.of(
                    new ResourceAmount(ResourceType.CRYPTO, 2)
            ), ThreadUtils.toMilliseconds(0)
    ),
    TACTICAL_CENTER("Tactical Center", 5, 1, ThreadUtils.toMilliseconds(8), Collections.emptyList(), Collections.emptyList(), 0),
    ;

    // Legacy mapping for migration support
    private static final Map<String, ConstructionType> LEGACY_MAPPING = createLegacyMapping();
    
    private static Map<String, ConstructionType> createLegacyMapping() {
        Map<String, ConstructionType> mapping = new java.util.HashMap<>();
        mapping.put("TOWN_CENTER", COMMAND_CENTER);
        mapping.put("Cidade Central", COMMAND_CENTER);
        mapping.put("LUMBER_CAMP", RESOURCE_DEPOT);
        mapping.put("Madeireira", RESOURCE_DEPOT);
        mapping.put("MILL", PROCESSING_PLANT);
        mapping.put("Moinho", PROCESSING_PLANT);
        mapping.put("HOUSE", OP_BASE);
        mapping.put("Casa", OP_BASE);
        mapping.put("BARRACKS", TRAINING_FACILITY);
        mapping.put("Barracas", TRAINING_FACILITY);
        mapping.put("ARCHERY_RANGE", WEAPONS_RANGE);
        mapping.put("Archery Range", WEAPONS_RANGE);
        mapping.put("STABLE", VEHICLE_BAY);
        mapping.put("Stable", VEHICLE_BAY);
        mapping.put("MARKET", TRADE_HUB);
        mapping.put("Market", TRADE_HUB);
        mapping.put("BLACKSMITH", TECH_FOUNDRY);
        mapping.put("Blacksmith", TECH_FOUNDRY);
        mapping.put("MONASTERY", COMM_RELAY);
        mapping.put("Monastery", COMM_RELAY);
        mapping.put("UNIVERSITY", RESEARCH_LAB);
        mapping.put("University", RESEARCH_LAB);
        mapping.put("FARM", SUPPLY_STATION);
        mapping.put("Farm", SUPPLY_STATION);
        mapping.put("WINERY", CRYPTO_MINE);
        mapping.put("Vinharia", CRYPTO_MINE);
        mapping.put("DOCK", LAUNCH_PAD);
        mapping.put("Docas", LAUNCH_PAD);
        mapping.put("TEMPLE", NEURAL_NEXUS);
        mapping.put("Templo", NEURAL_NEXUS);
        mapping.put("ACADEMY", TACTICAL_CENTER);
        mapping.put("Academia", TACTICAL_CENTER);
        return mapping;
    }

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
        // First try to find by current name
        for(ConstructionType x : ConstructionType.values()) {
            if(Objects.equals(x.name, name)) return x;
        }
        
        // Then try to find by enum constant name
        try {
            return ConstructionType.valueOf(name);
        } catch (IllegalArgumentException e) {
            // Continue to legacy mapping
        }
        
        // Finally try legacy mapping for migration support
        ConstructionType legacyMapping = LEGACY_MAPPING.get(name);
        if (legacyMapping != null) {
            return legacyMapping;
        }
        
        return null;
    }
}
