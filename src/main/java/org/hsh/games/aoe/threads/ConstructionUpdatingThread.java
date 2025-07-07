package org.hsh.games.aoe.threads;

import org.hsh.games.aoe.ApplicationConstants;
import org.hsh.games.aoe.entities.Building;
import org.hsh.games.aoe.utils.ConsoleUtils;
import org.hsh.games.aoe.entities.ResourceAmount;
import org.hsh.games.aoe.entities.Worker;

import java.util.List;

public class ConstructionUpdatingThread extends Thread {

    private Building building;
    private Worker worker;
    private List<ResourceAmount> playerResources;

    public ConstructionUpdatingThread(Building building, Worker worker, List<ResourceAmount> playerResources) {
        this.building = building;
        this.worker = worker;
        this.playerResources = playerResources;
    }

    @Override
    public void run() {
        worker.setOccupied(true);
        System.out.println("O trabalhador " + " comecou a tarefa:\n" + worker.getCurrentMission() + "\nTermina dentro de " + building.getConstructionMinutes() + " minutos.");

        this.building.upgrade();

        worker.setOccupied(false);
        System.out.printf("\nO trabalhador terminou a tarefa: %s\n", worker.getCurrentMission());

        try {
            Thread.sleep(ApplicationConstants.TIME_TO_SHOW_MESSAGE);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ConsoleUtils.clearConsole();
    }
}
