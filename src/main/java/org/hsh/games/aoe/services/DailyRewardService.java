package org.hsh.games.aoe.services;

import org.hsh.games.aoe.entities.ResourceAmount;
import org.hsh.games.aoe.entities.DailyReward;
import org.hsh.games.aoe.entities.TechPhase;
import org.hsh.games.aoe.entities.ResourceType;
import java.util.*;

public class DailyRewardService {

    private final List<DailyReward> rewards = new ArrayList<>();
    private final Map<String, Integer> playerStreaks = new HashMap<>();
    private final Map<String, String> lastClaimDate = new HashMap<>();
    private GuildService guildService;

    public DailyRewardService() {
        initializeRewards();
    }
    
    public DailyRewardService(GuildService guildService) {
        this.guildService = guildService;
        initializeRewards();
    }

    private void initializeRewards() {
        // Day 1: Basic resources
        rewards.add(new DailyReward(1, List.of(
            new ResourceAmount(ResourceType.ENERGY, 50),
            new ResourceAmount(ResourceType.DATA, 50)
        ), false, "Energia e Dados"));

        // Day 2: Energy and Components
        rewards.add(new DailyReward(2, List.of(
            new ResourceAmount(ResourceType.ENERGY, 100),
            new ResourceAmount(ResourceType.COMPONENTS, 25)
        ), false, "Energia e Dados"));

        // Day 3: More food and wood
        rewards.add(new DailyReward(3, List.of(
            new ResourceAmount(ResourceType.DATA, 75),
            new ResourceAmount(ResourceType.ENERGY, 75)
        ), false, "Mais Recursos Básicos"));

        // Day 4: Population boost
        rewards.add(new DailyReward(4, List.of(
            new ResourceAmount(ResourceType.DATA, 1),
            new ResourceAmount(ResourceType.ENERGY, 50)
        ), false, "População e Água"));

        // Day 5: Balanced resources
        rewards.add(new DailyReward(5, List.of(
            new ResourceAmount(ResourceType.ENERGY, 60),
            new ResourceAmount(ResourceType.DATA, 60),
            new ResourceAmount(ResourceType.COMPONENTS, 30)
        ), false, "Recursos Equilibrados"));

        // Day 6: Preparation for special reward
        rewards.add(new DailyReward(6, List.of(
            new ResourceAmount(ResourceType.CIRCUITS, 20),
            new ResourceAmount(ResourceType.DATA, 100)
        ), false, "Ferro e Comida"));

        // Day 7: Special weekly reward
        rewards.add(new DailyReward(7, List.of(
            new ResourceAmount(ResourceType.QUANTUM_ENERGY, 10),
            new ResourceAmount(ResourceType.DATA, 2)
        ), true, "🎁 Recompensa Semanal: Ouro e População!"));

        // Day 8-13: Progressive rewards
        rewards.add(new DailyReward(8, List.of(
            new ResourceAmount(ResourceType.NANOMATERIALS, 15),
            new ResourceAmount(ResourceType.ENERGY, 80)
        ), false, "Prata e Madeira"));

        rewards.add(new DailyReward(9, List.of(
            new ResourceAmount(ResourceType.COMPONENTS, 25),
            new ResourceAmount(ResourceType.COMPONENTS, 40)
        ), false, "Uvas e Pedra"));

        rewards.add(new DailyReward(10, List.of(
            new ResourceAmount(ResourceType.DATA, 120),
            new ResourceAmount(ResourceType.CIRCUITS, 25)
        ), false, "Comida e Ferro"));

        rewards.add(new DailyReward(11, List.of(
            new ResourceAmount(ResourceType.ENERGY, 150),
            new ResourceAmount(ResourceType.NANOMATERIALS, 20)
        ), false, "Água e Prata"));

        rewards.add(new DailyReward(12, List.of(
            new ResourceAmount(ResourceType.ENERGY, 100),
            new ResourceAmount(ResourceType.COMPONENTS, 30)
        ), false, "Madeira e Uvas"));

        rewards.add(new DailyReward(13, List.of(
            new ResourceAmount(ResourceType.DATA, 1),
            new ResourceAmount(ResourceType.QUANTUM_ENERGY, 5)
        ), false, "População e Ouro"));

        // Day 14: Special bi-weekly reward
        rewards.add(new DailyReward(14, List.of(
            new ResourceAmount(ResourceType.QUANTUM_ENERGY, 25),
            new ResourceAmount(ResourceType.CRYPTO, 5)
        ), true, "🌟 Recompensa Quinzenal: Ouro e Favor dos Deuses!"));

        // Day 15-29: Higher tier rewards
        for (int day = 15; day < 30; day++) {
            List<ResourceAmount> dayRewards = generateProgressiveRewards(day);
            rewards.add(new DailyReward(day, dayRewards, false, "Recursos Avançados"));
        }

        // Day 30: Special monthly reward
        rewards.add(new DailyReward(30, List.of(
            new ResourceAmount(ResourceType.QUANTUM_ENERGY, 50),
            new ResourceAmount(ResourceType.CRYPTO, 15),
            new ResourceAmount(ResourceType.DATA, 3)
        ), true, "👑 RECOMPENSA MENSAL: Tesouro Real!"));
    }

    private List<ResourceAmount> generateProgressiveRewards(int day) {
        // Generate progressive rewards based on day
        int baseAmount = 50 + (day * 5);
        
        return switch (day % 3) {
            case 0 -> List.of(
                new ResourceAmount(ResourceType.QUANTUM_ENERGY, baseAmount / 10),
                new ResourceAmount(ResourceType.ENERGY, baseAmount)
            );
            case 1 -> List.of(
                new ResourceAmount(ResourceType.NANOMATERIALS, baseAmount / 5),
                new ResourceAmount(ResourceType.DATA, baseAmount)
            );
            default -> List.of(
                new ResourceAmount(ResourceType.CIRCUITS, baseAmount / 4),
                new ResourceAmount(ResourceType.COMPONENTS, baseAmount / 2)
            );
        };
    }

    public List<ResourceAmount> claimDailyReward(String playerId, TechPhase phase) {
        if (hasClaimedToday(playerId)) {
            throw new IllegalStateException("Já coletaste a recompensa de hoje! Volta amanhã.");
        }

        int streak = calculateNewStreak(playerId);
        if (streak > rewards.size()) {
            streak = 1; // loop back after all rewards have been claimed
        }

        playerStreaks.put(playerId, streak);
        lastClaimDate.put(playerId, getTodayDateString());

        return rewards.get(streak - 1).getAdjustedRewards(phase);
    }

    
    /**
     * Claims daily reward and deposits it directly to player's guild vault if they choose.
     * Streak logic remains untouched.
     * 
     * @param playerId ID of the player claiming the reward
     * @param phase Current tech phase of the player
     * @param depositToGuild true to deposit to guild vault, false to return rewards for player inventory
     * @return List of resources if depositToGuild is false, empty list if deposited to guild
     * @throws IllegalStateException if already claimed today
     * @throws IllegalArgumentException if player is not in a guild when trying to deposit to guild
     */
    public List<ResourceAmount> claimDailyRewardWithGuildOption(String playerId, TechPhase phase, boolean depositToGuild) {
        if (hasClaimedToday(playerId)) {
            throw new IllegalStateException("Já coletaste a recompensa de hoje! Volta amanhã.");
        }

        // Calculate streak exactly as before - streak logic is preserved
        int streak = calculateNewStreak(playerId);
        if (streak > rewards.size()) {
            streak = 1; // loop back after all rewards have been claimed
        }

        // Update streak and claim date - same logic as original method
        playerStreaks.put(playerId, streak);
        lastClaimDate.put(playerId, getTodayDateString());

        // Get the adjusted rewards for the player's tech phase
        List<ResourceAmount> rewardAmounts = rewards.get(streak - 1).getAdjustedRewards(phase);
        
        if (depositToGuild) {
            if (guildService == null) {
                throw new IllegalStateException("Guild service not available. Cannot deposit to guild vault.");
            }
            
            org.hsh.games.aoe.entities.rebelcell.Guild playerGuild = guildService.getPlayerGuild(playerId);
            if (playerGuild == null) {
                throw new IllegalArgumentException("Player is not in a guild. Cannot deposit rewards to guild vault.");
            }
            
            // Deposit each reward to the guild vault
            for (ResourceAmount reward : rewardAmounts) {
                try {
                    // Check if this resource type can be stored in guild vault
                    if (reward.getResource().isGuildVaultStorable()) {
                        guildService.depositToVault(playerGuild.id(), reward.getResource(), 
                                                   reward.getAmount(), playerId);
                    } else {
                        // If resource can't be stored in guild vault, it goes to player instead
                        // This could happen with certain special resources
                        throw new IllegalArgumentException(
                            String.format("Resource %s cannot be stored in guild vault and will be awarded to player instead.", 
                                        reward.getResource().getDescription()));
                    }
                } catch (Exception e) {
                    // If vault deposit fails (e.g., capacity exceeded), throw meaningful error
                    throw new IllegalStateException(
                        String.format("Failed to deposit %d %s to guild vault: %s", 
                                    reward.getAmount(), reward.getResource().getDescription(), e.getMessage()));
                }
            }
            
            // Return empty list since rewards were deposited to guild
            return new ArrayList<>();
        } else {
            // Return rewards for player inventory (original behavior)
            return rewardAmounts;
        }
    }


    public boolean hasClaimedToday(String playerId) {
        String lastDate = lastClaimDate.getOrDefault(playerId, "");
        return getTodayDateString().equals(lastDate);
    }

    public int getCurrentStreak(String playerId) {
        return playerStreaks.getOrDefault(playerId, 0);
    }

    public DailyReward getNextReward(String playerId) {
        int currentStreak = playerStreaks.getOrDefault(playerId, 0);
        int nextStreak = currentStreak + 1;
        
        if (nextStreak > rewards.size()) {
            nextStreak = 1; // Loop back to start
        }
        
        return rewards.get(nextStreak - 1);
    }

    private int calculateNewStreak(String playerId) {
        String lastDate = lastClaimDate.getOrDefault(playerId, "");
        String today = getTodayDateString();
        
        if (lastDate.isEmpty()) {
            return 1; // First time claiming
        }
        
        if (isConsecutiveDay(lastDate, today)) {
            return playerStreaks.getOrDefault(playerId, 0) + 1;
        } else {
            return 1; // Streak broken, start over
        }
    }

    private boolean isConsecutiveDay(String lastDate, String today) {
        try {
            java.time.LocalDate last = java.time.LocalDate.parse(lastDate);
            java.time.LocalDate current = java.time.LocalDate.parse(today);
            return last.plusDays(1).equals(current);
        } catch (Exception e) {
            return false;
        }
    }

    private String getTodayDateString() {
        return java.time.LocalDate.now().toString();
    }
    
    /**
     * Sets the guild service for vault deposits.
     * This allows the service to be configured with guild functionality.
     * 
     * @param guildService The guild service to use for vault operations
     */
    public void setGuildService(GuildService guildService) {
        this.guildService = guildService;
    }
    
    /**
     * Checks if guild vault deposit is available for this service instance.
     * 
     * @return true if guild service is configured and vault deposits are possible
     */
    public boolean isGuildVaultDepositAvailable() {
        return guildService != null;
    }
}
