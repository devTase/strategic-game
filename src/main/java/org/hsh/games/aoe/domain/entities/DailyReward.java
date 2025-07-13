package org.hsh.games.aoe.domain.entities;

import org.hsh.games.aoe.domain.entities.resources.ResourceAmount;

import java.util.List;

public record DailyReward(int day, List<ResourceAmount> baseRewards, boolean isSpecialReward, String description) {

    public List<ResourceAmount> getAdjustedRewards(TechPhase currentPhase) {
        double multiplier = calculatePhaseMultiplier(currentPhase);
        return baseRewards.stream()
                .map(reward -> new ResourceAmount(
                        reward.getResource(),
                        (int) Math.round(reward.getAmount() * multiplier)
                ))
                .toList();
    }

    private double calculatePhaseMultiplier(TechPhase phase) {
        return switch (phase) {
            case UPRISING -> 1.0;
            case AUGMENTED_STREETS -> 1.3;
            case NEURAL_NEXUS -> 1.6;
            case DRONE_DOMINION -> 2.0;
            case QUANTUM_DAWN -> 2.4;
            case SINGULARITY_PREP -> 2.9;
            case POST_SINGULARITY -> 3.5;
            case HYPER_MESH -> 4.2;
            case EXO_REALITY -> 5.0;
        };
    }

    @Override
    public String toString() {
        return String.format("Dia %d: %s", day, description);
    }
}
