package org.hsh.games.aoe.shared.constants;

/**
 * Application constants with cyberpunk-themed messages
 * Enhanced for immersive experience in the Neon Corporate Wars game
 * 
 * @author devTASE
 */
public class ApplicationConstants {
    
    // Core game messages with cyberpunk theme
    public static final String MESSAGE_WRONG_OPTION_TRY_AGAIN = "⚡ Command not recognized, cyber-runner. Check your neural interface.";
    public static final String MESSAGE_CHOOSE_OPTION = "Select your next hack, netrunner: ";
    public static final String MESSAGE_NOT_ENOUGH_RESOURCES = "💾 Insufficient data credits for this operation, runner!";
    
    // Timing constants
    public static final Integer TIME_TO_SHOW_MESSAGE = 3000;
    public static final Integer TIME_TO_SHOW_QUICK_MESSAGE = 500;
    
    // Village creation messages
    public static final String MESSAGE_VILLAGE_NAME_PROMPT = "🛰️ What designation shall we assign to your data fortress, runner?";
    public static final String MESSAGE_VILLAGE_CREATION_ERROR = "❌ A fortress needs a proper designation! Retry initialization.";
    public static final String MESSAGE_VILLAGE_CREATION_SUCCESS = "🎉 Welcome to your new cyber-domain! May your servers never crash!";
    
    // Game over messages
    public static final String MESSAGE_GAME_OVER = "💀 System failure! All your AI units have gone offline...";
    public static final String MESSAGE_FAREWELL = "⚡ Logging out, cyber-runner! Your digital legacy remains!";
    
    // CyberOperative messages
    public static final String MESSAGE_NO_WORKERS_AVAILABLE = "🤖 All your cyber operatives are currently processing other tasks, runner!";
    public static final String MESSAGE_WORKER_SENT_TO_GATHER = "🛰️ Your cyber operative has been deployed to harvest resources from the net!";
    public static final String MESSAGE_WORKER_SENT_TO_BUILD = "🏢️ Your cyber operative has initiated assembly of the selected module!";
    
    // CyberOperative limits
    public static final int INITIAL_CYBER_OPERATIVES = 3;
    public static final int MAX_CYBER_OPERATIVES_PER_TECH_PHASE = 2;
    public static final String MESSAGE_CYBER_OPERATIVE_LIMIT_REACHED = "🚫 You've reached the maximum number of cyber operatives for your current tech phase!";
    public static final String MESSAGE_CYBER_OPERATIVE_RECRUITED = "✅ New cyber operative recruited to your network!";
    public static final String MESSAGE_INSUFFICIENT_RESOURCES_FOR_OPERATIVE = "💾 Insufficient resources to recruit a new cyber operative!";
    
    // Resource messages
    public static final String MESSAGE_RESOURCES_GATHERED = "✅ Your data-miners have returned with valuable intel!";
    public static final String MESSAGE_CONSTRUCTION_COMPLETE = "🏗️ Module assembly has been completed successfully!";
    public static final String MESSAGE_UPGRADE_COMPLETE = "⬆️ Your system has been upgraded to the next version!";
    
    // Daily rewards messages
    public static final String MESSAGE_DAILY_REWARD_CLAIMED = "🎁 You have claimed your daily crypto-reward from the network!";
    public static final String MESSAGE_DAILY_REWARD_ALREADY_CLAIMED = "⏰ You have already claimed your rewards for today, runner!";
    public static final String MESSAGE_DAILY_REWARD_NEXT = "🔮 Your next crypto-drop will be available tomorrow!";
    
    // Status messages
    public static final String MESSAGE_KINGDOM_PROSPERITY = "🌟 Your cyber-domain thrives under your digital dominion!";
    public static final String MESSAGE_ERA_ADVANCEMENT = "🎯 Your network has evolved to a new technological era!";
    public static final String MESSAGE_BUILDING_LIMIT_REACHED = "🏗️ You have reached the maximum number of this module type!";
    
    // Input prompts
    public static final String PROMPT_CONTINUE = "Press Enter to continue your hack...";
    public static final String PROMPT_RETURN_TO_MENU = "Press Enter to return to the Command Terminal...";
    
    // Resource type icons - for better visual representation
    public static final String ICON_WOOD = "💾"; // Data storage
    public static final String ICON_STONE = "⚙️"; // Hardware components
    public static final String ICON_GOLD = "💰"; // Crypto currency
    public static final String ICON_FOOD = "⚡"; // Energy/power
    public static final String ICON_IRON = "🔧"; // Tech parts
    
    // Building type icons
    public static final String ICON_HOUSE = "🏠"; // Residential pod
    public static final String ICON_FARM = "⚡"; // Power plant
    public static final String ICON_MINE = "💾"; // Data center
    public static final String ICON_BARRACKS = "🛰️"; // Security hub
    public static final String ICON_MARKET = "🏪"; // Trade terminal
    public static final String ICON_TEMPLE = "🖥️"; // AI shrine
    
    // Game mechanics
    public static final String MESSAGE_BUILDING_REQUIREMENTS = "📋 System requirements: ";
    public static final String MESSAGE_BUILDING_BENEFITS = "✨ Network benefits: ";
    public static final String MESSAGE_TIME_REMAINING = "⏳ Processing time: ";
    
}
