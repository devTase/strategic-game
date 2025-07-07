package org.hsh.games.aoe.entities;

import org.hsh.games.aoe.utils.ThreadUtils;

import java.util.List;
import java.util.Random;

public class Resource {
    ResourceType type;
    String name;
    Difficulty hardToGet;
    int timeLimitForSearch;
    int amountMaxToBeFound;
    int pricePerUnit;

    public Resource(ResourceType resourceType) {
        this.type = resourceType;
        this.name = resourceType.getDescription();
        this.hardToGet = resourceType.getHardToGet();
        this.timeLimitForSearch = resourceType.getTimeLimitForSearch();
        this.amountMaxToBeFound = resourceType.getAmountMaxToBeFound();
        this.pricePerUnit = resourceType.getPricePerUnit();
    }

    public void search(List<ResourceAmount> playerResources, String workerName, String currentMission) {
        Random random = new Random();
        int lowestSearchTime = random.nextInt(ThreadUtils.toMilliseconds(1), timeLimitForSearch/2);
        int totalSearchTime = random.nextInt(lowestSearchTime, timeLimitForSearch);
        int amountMax = amountMaxToBeFound/2;
        if(amountMax < 2) amountMax = 3;
        int lowestAmountToBeFound = random.nextInt(1, amountMax);

        System.out.printf("O trabalhador começou a tarefa de %s\nTermina dentro de %d minutos.\n", currentMission, ThreadUtils.toMinutes(totalSearchTime));

        try {

            int splittedTime = totalSearchTime/3;
            Thread.sleep(splittedTime);
            isPLayerWorkerInjured();
            if(isPLayerWorkerKilled()) return;
            Thread.sleep(totalSearchTime-splittedTime);
            ResourceAmount resourceFound = new ResourceAmount(type, random.nextInt(lowestAmountToBeFound, amountMaxToBeFound));
            addToPlayerResources(playerResources, resourceFound);
            System.out.printf("\nO trabalhador voltou para casa com %d de %s\n", resourceFound.getAmount(), resourceFound.getResource().getDescription());
        } catch (java.lang.InterruptedException e) {
            System.out.println("O trabalhador aleijou-se enquanto procurava resources!!");
        }
    }

    public boolean isPLayerWorkerKilled() {
        if(hardToGet.equals(Difficulty.EASY)) return false;
        if(hardToGet.equals(Difficulty.MEDIUM)) {
            Random random = new Random();
            int killed = random.nextInt(1, 100);
            if(killed > 80) {
                System.out.println("O trabalhador foi atacado por um animal selvagem e foi morto");
                return true;
            }
            return false;
        }

        if(hardToGet.equals(Difficulty.HARD) || hardToGet.equals(Difficulty.EXTREME)) {
            Random random = new Random();
            int killed = random.nextInt(1, 100);
            if(killed > 70) {
                System.out.println("O trabalhador foi atacado por um animal selvagem e foi morto");
                return true;
            }
            return false;
        }
        return false;
    }

    public void isPLayerWorkerInjured() {
        if(hardToGet.equals(Difficulty.EASY)) return;
        if(hardToGet.equals(Difficulty.MEDIUM)) {
            Random random = new Random();
            int injured = random.nextInt(1, 100);
            if(injured > 80) {
                System.out.println("\nO trabalhador foi atacado por um animal selvagem e voltou para casa com menos recursos");
            }
            return;
        }
        if(hardToGet.equals(Difficulty.HARD) || hardToGet.equals(Difficulty.EXTREME)) {
            Random random = new Random();
            int injured = random.nextInt(1, 100);
            if(injured > 60) {
                System.out.println("\nO trabalhador foi atacado por um animal selvagem e voltou para casa com menos recursos");
            }
            return;
        }
        Random random = new Random();
        int injured = random.nextInt(1, 100);
        if(injured > 50) {
            System.out.println("\nO trabalhador foi atacado por um animal selvagem e voltou para casa com menos recursos");
        }
    }

    public void addToPlayerResources(List<ResourceAmount> playerResources, ResourceAmount resourcesToAdd) {
        for(ResourceAmount x : playerResources) {
            if(x.getResource().equals(resourcesToAdd.getResource())) {
                x.setAmount(x.getAmount() + resourcesToAdd.getAmount());
            }
        }
    }
}
