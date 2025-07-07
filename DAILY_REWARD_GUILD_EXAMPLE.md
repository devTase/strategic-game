# Daily Reward Guild Vault Integration

## Overview

The DailyRewardService has been enhanced to allow players to deposit their daily bonuses directly to their guild's vault instead of their personal inventory. The streak logic remains completely unchanged.

## Key Features

### ✅ Streak Logic Preserved
- All streak calculations work exactly as before
- Daily claim restrictions remain intact
- Consecutive day logic unchanged
- Reward progression stays the same

### 🏦 Guild Vault Option
- Players can choose to deposit rewards to guild vault
- Resources are validated for guild vault compatibility
- Proper error handling for vault capacity issues
- Fallback to player inventory when needed

## Usage Examples

### Basic Usage - Player Inventory (Original Behavior)
```java
// Traditional way - deposits to player inventory
playerService.claimDailyReward();

// New way - explicit player inventory
playerService.claimDailyRewardWithGuildOption(false);
```

### Guild Vault Deposit
```java
// Setup guild service
GuildService guildService = new GuildService();
Guild guild = guildService.createGuild("My Guild", "playerName", 1000);
playerService.setGuildService(guildService);

// Claim reward to guild vault
playerService.claimDailyRewardWithGuildOption(true);
```

### Service Level API
```java
DailyRewardService dailyRewardService = new DailyRewardService(guildService);

// Returns empty list when deposited to guild
List<ResourceAmount> rewards = dailyRewardService.claimDailyRewardWithGuildOption(
    "playerId", EraAge.STONE_AGE, true);

// Returns reward list for player inventory
List<ResourceAmount> rewards = dailyRewardService.claimDailyRewardWithGuildOption(
    "playerId", EraAge.STONE_AGE, false);
```

## Error Handling

### Player Not in Guild
```java
try {
    dailyRewardService.claimDailyRewardWithGuildOption("playerId", era, true);
} catch (IllegalArgumentException e) {
    // "Player is not in a guild. Cannot deposit rewards to guild vault."
    // Fallback to player inventory
}
```

### Guild Service Not Available
```java
try {
    dailyRewardService.claimDailyRewardWithGuildOption("playerId", era, true);
} catch (IllegalStateException e) {
    // "Guild service not available. Cannot deposit to guild vault."
    // Configure guild service first
}
```

### Vault Capacity Issues
```java
try {
    dailyRewardService.claimDailyRewardWithGuildOption("playerId", era, true);
} catch (IllegalStateException e) {
    // "Failed to deposit X Wood to guild vault: Deposit would exceed vault capacity"
    // Consider expanding vault or using player inventory
}
```

## Configuration

### Setting Up Guild Service
```java
// In PlayerService constructor or setup method
public void configureGuildSupport(GuildService guildService) {
    this.setGuildService(guildService);
}

// Check if guild vault deposits are available
if (dailyRewardService.isGuildVaultDepositAvailable()) {
    // Guild vault option is available
}
```

### Constructor Injection (Recommended)
```java
// Using constructor injection for better dependency management
DailyRewardService dailyRewardService = new DailyRewardService(guildService);
```

## Technical Implementation Details

### Streak Logic Preservation
- The `calculateNewStreak()` method is called identically in both scenarios
- `playerStreaks` and `lastClaimDate` maps are updated the same way
- All validation (already claimed today) happens before any processing

### Resource Validation
- Resources are checked for guild vault compatibility using `ResourceType.isGuildVaultStorable()`
- Transfer multipliers are applied via `ResourceType.getGuildVaultTransferMultiplier()`
- Vault capacity limits are enforced through `GuildVault.depositResource()`

### Backwards Compatibility
- Original `claimDailyReward()` method remains unchanged
- Existing code continues to work without modifications
- New functionality is additive, not replacing

## Testing

All existing tests pass, plus new tests verify:
- ✅ Guild vault deposits work correctly
- ✅ Streak logic is identical for both options
- ✅ Error handling for invalid scenarios
- ✅ Service availability checks
- ✅ Resource validation and transfers

## Benefits

1. **Guild Collaboration**: Players can contribute daily rewards to guild resources
2. **Strategic Planning**: Guilds can accumulate resources for large projects
3. **Player Choice**: Flexible option without forcing guild participation
4. **Error Resilience**: Graceful fallbacks when guild options aren't available
5. **Maintained Compatibility**: Zero impact on existing functionality

## Author

Implementation by devTASE following constructor injection and comprehensive testing practices.
