package org.hsh.games.aoe.threads;

import org.hsh.games.aoe.ApplicationConstants;
import org.hsh.games.aoe.ConsoleUtils;
import org.hsh.games.aoe.Resource;
import org.hsh.games.aoe.ResourceAmount;
import org.hsh.games.aoe.entities.Worker;

import java.util.List;

public class SearchResourcesThread extends Thread {

    private Resource resourceToLookFor;
    private Worker worker;
    private List<ResourceAmount> playerResources;

    public SearchResourcesThread(Resource resourceToLookFor, List<ResourceAmount> playerResources, Worker worker) {
        this.resourceToLookFor = resourceToLookFor;
        this.worker = worker;
        this.playerResources = playerResources;
    }

    @Override
    public void run() {
        worker.setOccupied(true);
        resourceToLookFor.search(playerResources, worker.getName(), worker.getCurrentMission());
        worker.setOccupied(false);

        try {
            Thread.sleep(ApplicationConstants.TIME_TO_SHOW_MESSAGE);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ConsoleUtils.clearConsole();
    }
}
