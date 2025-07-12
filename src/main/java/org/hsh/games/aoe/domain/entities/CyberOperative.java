package org.hsh.games.aoe.domain.entities;

import org.hsh.games.aoe.domain.entities.buildings.Building;
import org.hsh.games.aoe.domain.entities.buildings.ConstructionProcess;
import org.hsh.games.aoe.domain.entities.resources.Resource;
import org.hsh.games.aoe.domain.entities.resources.ResourceAmount;
import org.hsh.games.aoe.domain.entities.resources.ResourceType;
import org.hsh.games.aoe.infrastructure.adapters.outbound.threading.ConstructionBuildingThread;
import org.hsh.games.aoe.infrastructure.adapters.outbound.threading.ConstructionUpdatingThread;
import org.hsh.games.aoe.infrastructure.adapters.outbound.threading.SearchResourcesThread;
import org.hsh.games.aoe.shared.utils.ThreadUtils;

import java.util.List;

public class CyberOperative extends Consumer<ResourceAmount> {
    private String name;
    private boolean isOccupied;
    private ResourceAmount resourceConsumption;
    private String currentMission;
    private final int TIME_BETWEEN_CONSUMPTIONS = ThreadUtils.toMilliseconds(1);

    public CyberOperative(String name) {
        this.name = name;
        this.isOccupied = false;
        this.resourceConsumption = new ResourceAmount(ResourceType.fromLegacyName("FOOD"), 50);
    }

    @Override
    public List<ResourceAmount> getConsumptionType() {
        return List.of(new ResourceAmount(ResourceType.fromLegacyName("WATER"), 5));
    }

    @Override
    public int getTimeBetweenConsumptions() {
        return this.TIME_BETWEEN_CONSUMPTIONS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public ResourceAmount getResourceConsumption() {
        return resourceConsumption;
    }

    public String getCurrentMission() {
        return currentMission;
    }

    public void setCurrentMission(String currentMission) {
        this.currentMission = currentMission;
    }

    public String getStringWithName() {
        return String.format("CyberOperative %s", name);
    }

    public void setResourceConsumption(ResourceAmount resourceConsumption) {
        this.resourceConsumption = resourceConsumption;
    }

    public void searchResources(ResourceType resourceType, List<ResourceAmount> playerResources) {
        currentMission = "Procurar Recursos: " + resourceType.getDescription();
        new SearchResourcesThread(new Resource(resourceType), playerResources, this).start();
    }

    public void makeConstruction(ConstructionProcess process, Building building, List<ResourceAmount> playerResources, List<CyberOperative> operativeList) {
        currentMission = process.getProcess() + " de Edifício: " + building.getConstructionTypeName();
        Thread constructionThread = process == ConstructionProcess.CREATION
                ? new ConstructionBuildingThread(building, this, playerResources, operativeList)
                : new ConstructionUpdatingThread(building, this, playerResources);
        constructionThread.start();
    }

}
