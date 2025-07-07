package org.hsh.games.aoe.entities;

import java.util.List;

public class DailyReward {
    private final int day;
    private final List<ResourceAmount> baseRewards;
    private final boolean isSpecialReward;
    private final String description;

    public DailyReward(int day, List<ResourceAmount> baseRewards, boolean isSpecialReward, String description) {
        this.day = day;
        this.baseRewards = baseRewards;
        this.isSpecialReward = isSpecialReward;
        this.description = description;
    }

    public int getDay() {
        return day;
    }

    public List<ResourceAmount> getBaseRewards() {
        return baseRewards;
    }

    public boolean isSpecialReward() {
        return isSpecialReward;
    }

    public String getDescription() {
        return description;
    }

    public List<ResourceAmount> getAdjustedRewards(EraAge currentEra) {
        double multiplier = calculateEraMultiplier(currentEra);
        return baseRewards.stream()
                .map(reward -> new ResourceAmount(
                        reward.getResource(),
                        (int) Math.round(reward.getAmount() * multiplier)
                ))
                .toList();
    }

    private double calculateEraMultiplier(EraAge era) {
        return switch (era) {
            case STONE_AGE -> 1.0;
            case BRONZE_AGE -> 1.2;
            case IRON_AGE -> 1.4;
            case MEDIEVAL_AGE -> 1.6;
            case RENAISSANCE -> 1.8;
            case INDUSTRIAL_AGE -> 2.0;
            case MODERN_AGE -> 2.2;
            case INFORMATION_AGE -> 2.4;
            case FUTURE_AGE -> 2.6;
        };
    }

    @Override
    public String toString() {
        return String.format("Dia %d: %s", day, description);
    }
}
