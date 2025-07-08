package org.hsh.games.aoe.demo;

import org.hsh.games.aoe.entities.ResourceType;
import org.hsh.games.aoe.entities.rebelcell.Guild;
import org.hsh.games.aoe.entities.rebelcell.RebelCellRank;
import org.hsh.games.aoe.entities.rebelcell.SpyReport;
import org.hsh.games.aoe.services.GuildService;

import java.util.concurrent.CompletableFuture;

/**
 * Demonstration class showing the new espionage and black market mechanics
 * in the GuildService.
 * 
 * @author devTASE
 */
public class EspionageAndBlackMarketDemo {
    
    public static void main(String[] args) {
        System.out.println("🎮 Strategic Game - Espionage & Black Market Demo");
        System.out.println("=================================================\n");
        
        // Initialize guild service
        GuildService guildService = new GuildService();
        
        // Create two guilds for testing
        Guild guild1 = guildService.createGuild("Shadow Legion", "spy_master", 1000);
        Guild guild2 = guildService.createGuild("Iron Merchants", "trader_boss", 800);
        
        // Add some members to make it more realistic
        guildService.invitePlayer(guild1.id(), "agent_007", "spy_master", RebelCellRank.OPERATOR);
        guildService.invitePlayer(guild2.id(), "merchant_joe", "trader_boss", RebelCellRank.HACKER);
        
        // Add resources to guild2 for trading
        guildService.depositToVault(guild2.id(), ResourceType.QUANTUM_ENERGY, 100, "trader_boss");
        guildService.depositToVault(guild2.id(), ResourceType.CIRCUITS, 200, "trader_boss");
        
        System.out.println("🏰 Created two guilds:");
        System.out.println("   • " + guild1.name() + " (Led by " + guild1.leaderPlayerId() + ")");
        System.out.println("   • " + guild2.name() + " (Led by " + guild2.leaderPlayerId() + ")");
        System.out.println();
        
        // Demonstrate espionage mechanics
        System.out.println("🕵️  ESPIONAGE DEMONSTRATION");
        System.out.println("---------------------------");
        System.out.println("Sending spy from " + guild1.name() + " to " + guild2.name() + "...");
        
        CompletableFuture<SpyReport> spyFuture = guildService.sendSpy("spy_master", guild2.id());
        
        // Handle the spy report when it completes
        spyFuture.thenAccept(report -> {
            System.out.println("\n📋 Spy Report Received:");
            System.out.println(report.getFormattedReport());
            System.out.println("\n🔍 Intelligence Type: " + report.getIntelligenceType());
            System.out.println("⏰ Report Age: " + report.getAgeInHours() + " hours");
        }).join();
        
        System.out.println("\n💸 BLACK MARKET DEMONSTRATION");
        System.out.println("-----------------------------");
        System.out.println("Attempting black market trade for " + guild2.name() + "...");
        System.out.println("Offering: CIRCUITS, Demanding: ENERGY");
        
        try {
            Guild updatedGuild = guildService.startBlackMarketTrade(guild2.id(), ResourceType.CIRCUITS, ResourceType.ENERGY);
            System.out.println("✅ Trade completed successfully!");
            System.out.println("Guild now has " + updatedGuild.members().size() + " members");
        } catch (Exception e) {
            System.out.println("❌ Trade failed: " + e.getMessage());
        }
        
        System.out.println("\n🎯 TRYING A RISKY TRADE");
        System.out.println("-----------------------");
        System.out.println("Attempting another black market trade...");
        System.out.println("Offering: QUANTUM_ENERGY, Demanding: NANOMATERIALS");
        
        try {
            Guild updatedGuild = guildService.startBlackMarketTrade(guild2.id(), ResourceType.QUANTUM_ENERGY, ResourceType.NANOMATERIALS);
            System.out.println("✅ Second trade completed!");
        } catch (Exception e) {
            System.out.println("❌ Second trade failed: " + e.getMessage());
        }
        
        System.out.println("\n🔒 TESTING SECURITY MEASURES");
        System.out.println("----------------------------");
        
        // Test spy on same guild (should fail)
        try {
            guildService.sendSpy("spy_master", guild1.id()).join();
        } catch (Exception e) {
            System.out.println("🛡️  Security check passed: Cannot spy on own guild");
        }
        
        // Test trade with same resource type (should fail)
        try {
            guildService.startBlackMarketTrade(guild1.id(), ResourceType.ENERGY, ResourceType.ENERGY);
        } catch (Exception e) {
            System.out.println("🛡️  Security check passed: Cannot trade same resource types");
        }
        
        System.out.println("\n🎉 Demo completed successfully!");
        System.out.println("Check the console output above for guild broadcasts and events.");
    }
}
