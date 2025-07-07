# Skill Integration Example

This document demonstrates how the skill modifiers have been integrated with construction and plunder calculations.

## Building Construction with Skills

### Before Integration
```java
// Old way - fixed construction time and cost
public void build(List<ResourceAmount> playerResources, List<Worker> playerWorkersList) {
    sleep(getConstructionTimeInMils());  // Fixed time
    // Resource deduction without skill modifiers
}
```

### After Integration
```java
// New way - skill-modified time and cost
public void build(List<ResourceAmount> playerResources, List<Worker> playerWorkersList, PlayerService playerService) {
    // Apply skill-modified construction time
    int skillModifiedTime = (int)(constructionMinutes * playerService.getConstructionTimeMultiplier());
    sleep(skillModifiedTime);
    
    // Apply skill-modified resource cost
    deductResourcesWithSkillModifier(playerResources, playerService.getConstructionCostMultiplier());
}
```

## PlayerService Helper Methods

The PlayerService now exposes skill multipliers while keeping Building decoupled from the skills package:

```java
/**
 * Gets the time multiplier from player skills for construction speed.
 * @return Time multiplier (less than 1.0 means faster construction)
 */
public double getConstructionTimeMultiplier() {
    return playerSkills.getTimeMultiplier();
}

/**
 * Gets the cost multiplier from player skills for construction cost.
 * @return Cost multiplier (less than 1.0 means cheaper construction)
 */
public double getConstructionCostMultiplier() {
    return playerSkills.getCostMultiplier();
}

/**
 * Gets the loot multiplier from player skills for plunder bonus.
 * @return Loot multiplier (greater than 1.0 means more loot)
 */
public double getLootMultiplier() {
    return playerSkills.getLootMultiplier();
}
```

## Plunder Hook in Combat/Raid

The GuildMissionService now includes a plunder hook for combat missions:

```java
/**
 * Applies plunder bonus to mission rewards based on participants' skills.
 */
private Map<ResourceType, Integer> applyPlunderBonusToRewards(
        Map<ResourceType, Integer> baseRewards, 
        GuildMission mission, 
        Guild guild) {
    
    for (String participantId : mission.participants()) {
        PlayerService playerService = getPlayerServiceForParticipant(participantId);
        
        if (playerService != null) {
            double lootMultiplier = playerService.getLootMultiplier();
            
            for (Map.Entry<ResourceType, Integer> reward : bonusRewards.entrySet()) {
                int baseLoot = baseRewards.get(reward.getKey());
                int plunder = (int)(baseLoot * lootMultiplier);  // <-- Plunder calculation
                
                // Apply bonus to final rewards
                reward.setValue(Math.max(baseLoot, newAmount));
            }
        }
    }
    
    return bonusRewards;
}
```

## Skill Multiplier Examples

With PlayerSkills at level 5:
- **Construction Speed**: 5% per level = 25% faster = 0.75 time multiplier
- **Construction Cost**: 3% per level = 15% cheaper = 0.85 cost multiplier  
- **Plunder Bonus**: 4% per level = 20% more loot = 1.20 loot multiplier

### Example Calculation:
```java
// Original construction time: 60 minutes
// With level 5 construction speed skill: 60 * 0.75 = 45 minutes

// Original resource cost: 100 wood
// With level 5 construction cost skill: 100 * 0.85 = 85 wood

// Original loot: 50 gold
// With level 5 plunder bonus skill: 50 * 1.20 = 60 gold
```

## Backward Compatibility

The old method signatures are still available but marked as deprecated:

```java
@Deprecated
public void build(List<ResourceAmount> playerResources, List<Worker> playerWorkersList) {
    // Old implementation without skill modifiers
}

@Deprecated  
public void upgrade() {
    // Old implementation without skill modifiers
}
```

This ensures existing code continues to work while encouraging migration to the new skill-aware methods.
