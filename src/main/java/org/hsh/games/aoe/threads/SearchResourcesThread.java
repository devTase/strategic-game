package org.hsh.games.aoe.threads;

import org.hsh.games.aoe.ApplicationConstants;
import org.hsh.games.aoe.utils.ConsoleUtils;
import org.hsh.games.aoe.entities.Resource;
import org.hsh.games.aoe.entities.ResourceAmount;
import org.hsh.games.aoe.entities.CyberOperative;

import java.util.List;

public class SearchResourcesThread extends Thread {

    private Resource resourceToLookFor;
    private CyberOperative operative;
    private List<ResourceAmount> playerResources;

    public SearchResourcesThread(Resource resourceToLookFor, List<ResourceAmount> playerResources, CyberOperative operative) {
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
