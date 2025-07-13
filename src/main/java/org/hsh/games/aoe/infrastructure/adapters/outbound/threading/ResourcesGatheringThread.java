package org.hsh.games.aoe.infrastructure.adapters.outbound.threading;

import org.hsh.games.aoe.shared.constants.ApplicationConstants;
import org.hsh.games.aoe.domain.entities.CyberOperative;
import org.hsh.games.aoe.shared.utils.ConsoleUtils;
import org.hsh.games.aoe.domain.entities.resources.Resource;
import org.hsh.games.aoe.domain.entities.resources.ResourceAmount;

import java.util.List;

public class ResourcesGatheringThread extends Thread {

    private Resource resourceToLookFor;
    private CyberOperative operative;
    private List<ResourceAmount> playerResources;

    public ResourcesGatheringThread(Resource resourceToLookFor, List<ResourceAmount> playerResources, CyberOperative operative) {
        this.resourceToLookFor = resourceToLookFor;
        this.operative = operative;
        this.playerResources = playerResources;
    }

    @Override
    public void run() {
        operative.setOccupied(true);
        resourceToLookFor.search(playerResources, operative.getName(), operative.getCurrentMission());
        operative.setOccupied(false);

        try {
            Thread.sleep(ApplicationConstants.TIME_TO_SHOW_MESSAGE);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ConsoleUtils.clearConsole();
    }
}
