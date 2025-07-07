package org.hsh.games.aoe.ui;

/**
 * Enhanced console display utilities with medieval theme and modern formatting
 * for the Strategic Game console interface.
 * 
 * @author devTASE
 */
public class ConsoleDisplayUtils {
    
    // Medieval-themed box drawing characters
    private static final String TOP_LEFT = "╔";
    private static final String TOP_RIGHT = "╗";
    private static final String BOTTOM_LEFT = "╚";
    private static final String BOTTOM_RIGHT = "╝";
    private static final String HORIZONTAL = "═";
    private static final String VERTICAL = "║";
    private static final String CROSS = "╬";
    private static final String T_DOWN = "╦";
    private static final String T_UP = "╩";
    private static final String T_LEFT = "╣";
    private static final String T_RIGHT = "╠";
    
    // ANSI Color codes for better visual experience
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BRIGHT_BLACK = "\u001B[90m";
    public static final String BRIGHT_RED = "\u001B[91m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_BLUE = "\u001B[94m";
    public static final String BRIGHT_PURPLE = "\u001B[95m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    public static final String BRIGHT_WHITE = "\u001B[97m";
    
    // Background colors
    public static final String BG_BLACK = "\u001B[40m";
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_PURPLE = "\u001B[45m";
    public static final String BG_CYAN = "\u001B[46m";
    public static final String BG_WHITE = "\u001B[47m";
    
    // Text styles
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";
    public static final String ITALIC = "\u001B[3m";
    
    /**
     * Creates a medieval-themed banner with the game title
     */
    public static void printGameBanner() {
        clearConsole();
        System.out.println(BRIGHT_YELLOW + BOLD);
        System.out.println("╔═══════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                                   ║");
        System.out.println("║     🏰 ⚔️  STRATEGIC KINGDOMS - REALM OF CONQUEST  ⚔️ 🏰                         ║");
        System.out.println("║                                                                                   ║");
        System.out.println("║         Build your empire • Gather resources • Forge your destiny                 ║");
        System.out.println("║                                                                                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println(RESET);
    }
    
    /**
     * Creates an enhanced main menu with medieval styling
     */
    public static void printMainMenu() {
        System.out.println(BRIGHT_CYAN + BOLD);
        System.out.println("╔═══════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                             🏛️  COUNCIL CHAMBER  🏛️                              ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                                   ║");
        System.out.println("║  " + BRIGHT_WHITE + "1️⃣  🗺️  " + BRIGHT_GREEN + "RESOURCE EXPEDITION" + BRIGHT_CYAN + "     │  " + BRIGHT_WHITE + "4️⃣  🎁  " + BRIGHT_YELLOW + "DAILY TRIBUTE" + BRIGHT_CYAN + "          ║");
        System.out.println("║      " + BRIGHT_BLACK + "Send scouts to gather resources" + BRIGHT_CYAN + "   │      " + BRIGHT_BLACK + "Collect your daily rewards" + BRIGHT_CYAN + "    ║");
        System.out.println("║                                     │                                     ║");
        System.out.println("║  " + BRIGHT_WHITE + "2️⃣  🏗️  " + BRIGHT_PURPLE + "CONSTRUCTION GUILD" + BRIGHT_CYAN + "      │  " + BRIGHT_WHITE + "5️⃣  📜  " + BRIGHT_BLUE + "KINGDOM STATUS" + BRIGHT_CYAN + "         ║");
        System.out.println("║      " + BRIGHT_BLACK + "Build and upgrade structures" + BRIGHT_CYAN + "    │      " + BRIGHT_BLACK + "View your realm's progress" + BRIGHT_CYAN + "    ║");
        System.out.println("║                                     │                                     ║");
        System.out.println("║  " + BRIGHT_WHITE + "3️⃣  🏛️  " + BRIGHT_PURPLE + "GUILD MANAGEMENT" + BRIGHT_CYAN + "       │  " + BRIGHT_WHITE + "6️⃣  🕯️  " + BRIGHT_RED + "SHADOW GUILDS" + BRIGHT_CYAN + "          ║");
        System.out.println("║      " + BRIGHT_BLACK + "Manage your guild affairs" + BRIGHT_CYAN + "      │      " + BRIGHT_BLACK + "Enter the shadow realm" + BRIGHT_CYAN + "       ║");
        System.out.println("║                                     │                                     ║");
        System.out.println("║                    " + BRIGHT_WHITE + "0️⃣  🚪  " + BRIGHT_RED + "ABANDON REALM" + BRIGHT_CYAN + "                             ║");
        System.out.println("║                        " + BRIGHT_BLACK + "Exit the game" + BRIGHT_CYAN + "                                ║");
        System.out.println("║                                                                                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════╝");
        System.out.print(BRIGHT_WHITE + "⚔️  " + YELLOW + "Choose your action, noble ruler: " + RESET);
    }
    
    /**
     * Creates a resource gathering menu with thematic styling
     */
    public static void printResourceMenu() {
        System.out.println(BRIGHT_GREEN + BOLD);
        System.out.println("╔═══════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                           🗺️  RESOURCE EXPEDITION  🗺️                            ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                                   ║");
        System.out.println("║  " + BRIGHT_WHITE + "Select the resource type to gather from the wilderness:" + BRIGHT_GREEN + "                    ║");
        System.out.println("║                                                                                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println(RESET);
    }
    
    /**
     * Creates a construction menu with architectural styling
     */
    public static void printConstructionMenu() {
        System.out.println(BRIGHT_PURPLE + BOLD);
        System.out.println("╔═══════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                          🏗️  CONSTRUCTION GUILD  🏗️                              ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                                   ║");
        System.out.println("║  " + BRIGHT_WHITE + "Choose the type of structure to build or upgrade:" + BRIGHT_PURPLE + "                       ║");
        System.out.println("║                                                                                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println(RESET);
    }
    
    /**
     * Creates a status display with royal theme
     */
    public static void printStatusHeader() {
        System.out.println(BRIGHT_BLUE + BOLD);
        System.out.println("╔═══════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                            📜  KINGDOM STATUS  📜                                ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                                   ║");
        System.out.println(RESET);
    }
    
    /**
     * Creates a daily rewards display with treasure theme
     */
    public static void printDailyRewardsHeader() {
        System.out.println(BRIGHT_YELLOW + BOLD);
        System.out.println("╔═══════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                             🎁  DAILY TRIBUTE  🎁                                ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                                   ║");
        System.out.println(RESET);
    }
    
    /**
     * Creates a guild management display with guild theme
     */
    public static void printGuildMenu() {
        System.out.println(BRIGHT_PURPLE + BOLD);
        System.out.println("╔═══════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                           🏛️  GUILD MANAGEMENT  🏛️                            ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                                   ║");
        System.out.println("║  " + BRIGHT_WHITE + "1️⃣  👑  " + BRIGHT_YELLOW + "CREATE GUILD" + BRIGHT_PURPLE + "           │  " + BRIGHT_WHITE + "4️⃣  🏹  " + BRIGHT_GREEN + "GUILD MISSIONS" + BRIGHT_PURPLE + "         ║");
        System.out.println("║      " + BRIGHT_BLACK + "Found your own guild" + BRIGHT_PURPLE + "           │      " + BRIGHT_BLACK + "View and manage missions" + BRIGHT_PURPLE + "     ║");
        System.out.println("║                                     │                                     ║");
        System.out.println("║  " + BRIGHT_WHITE + "2️⃣  📜  " + BRIGHT_CYAN + "GUILD INFO" + BRIGHT_PURPLE + "             │  " + BRIGHT_WHITE + "5️⃣  🗺️  " + BRIGHT_RED + "TERRITORIES" + BRIGHT_PURPLE + "           ║");
        System.out.println("║      " + BRIGHT_BLACK + "View guild information" + BRIGHT_PURPLE + "        │      " + BRIGHT_BLACK + "Shadow territory conquest" + BRIGHT_PURPLE + "     ║");
        System.out.println("║                                     │                                     ║");
        System.out.println("║  " + BRIGHT_WHITE + "3️⃣  👥  " + BRIGHT_BLUE + "MEMBER MANAGEMENT" + BRIGHT_PURPLE + "      │  " + BRIGHT_WHITE + "0️⃣  ⬅️  " + BRIGHT_WHITE + "BACK TO COUNCIL" + BRIGHT_PURPLE + "        ║");
        System.out.println("║      " + BRIGHT_BLACK + "Invite and manage members" + BRIGHT_PURPLE + "     │      " + BRIGHT_BLACK + "Return to main menu" + BRIGHT_PURPLE + "        ║");
        System.out.println("║                                     │                                     ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════╝");
        System.out.print(BRIGHT_WHITE + "🏛️  " + YELLOW + "Choose your guild action: " + RESET);
    }
    
    /**
     * Creates a shadow guilds menu with mysterious theme
     */
    public static void printShadowGuildsMenu() {
        System.out.println(BRIGHT_BLACK + BOLD);
        System.out.println("╔═══════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                           🕯️  SHADOW GUILDS  🕯️                                ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                     " + BRIGHT_RED + "⚠️  Enter the realm of shadows ⚠️" + BRIGHT_BLACK + "                      ║");
        System.out.println("║                                                                                   ║");
        System.out.println("║  " + BRIGHT_WHITE + "1️⃣  ⚔️  " + BRIGHT_PURPLE + "CREATE/JOIN GUILD" + BRIGHT_BLACK + "       │  " + BRIGHT_WHITE + "4️⃣  🗺️  " + BRIGHT_RED + "TERRITORIES" + BRIGHT_BLACK + "            ║");
        System.out.println("║      " + BRIGHT_BLACK + "Form shadow alliances" + BRIGHT_BLACK + "          │      " + BRIGHT_BLACK + "Conquer shadow domains" + BRIGHT_BLACK + "      ║");
        System.out.println("║                                     │                                     ║");
        System.out.println("║  " + BRIGHT_WHITE + "2️⃣  💰  " + BRIGHT_YELLOW + "GUILD STATUS & VAULT" + BRIGHT_BLACK + "    │  " + BRIGHT_WHITE + "5️⃣  🗡️  " + BRIGHT_RED + "ESPIONAGE/BLACK MARKET" + BRIGHT_BLACK + " ║");
        System.out.println("║      " + BRIGHT_BLACK + "Check coffers and power" + BRIGHT_BLACK + "      │      " + BRIGHT_BLACK + "Dark dealings and secrets" + BRIGHT_BLACK + "    ║");
        System.out.println("║                                     │                                     ║");
        System.out.println("║  " + BRIGHT_WHITE + "3️⃣  🎯  " + BRIGHT_GREEN + "MISSIONS" + BRIGHT_BLACK + "               │  " + BRIGHT_WHITE + "0️⃣  🌙  " + BRIGHT_BLUE + "RETURN TO LIGHT" + BRIGHT_BLACK + "        ║");
        System.out.println("║      " + BRIGHT_BLACK + "Shadow operations" + BRIGHT_BLACK + "            │      " + BRIGHT_BLACK + "Back to Council Chamber" + BRIGHT_BLACK + "     ║");
        System.out.println("║                                     │                                     ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════╝");
        System.out.print(BRIGHT_WHITE + "🕯️  " + YELLOW + "Choose your shadow action: " + RESET);
    }
    
    /**
     * Creates a formatted resource item display
     */
    public static void printResourceItem(int index, String resourceName, String icon) {
        System.out.printf("║  " + BRIGHT_WHITE + "%d. " + BRIGHT_GREEN + "%s " + BRIGHT_CYAN + "%-20s" + 
                         BRIGHT_BLACK + "│  Gather from the wilderness" + RESET + "                    ║%n", 
                         index, icon, resourceName);
    }
    
    /**
     * Creates a formatted building item display
     */
    public static void printBuildingItem(int index, String buildingName, String icon, String status) {
        System.out.printf("║  " + BRIGHT_WHITE + "%d. " + BRIGHT_PURPLE + "%s " + BRIGHT_CYAN + "%-20s" + 
                         BRIGHT_BLACK + "│  %s" + RESET + "                           ║%n", 
                         index, icon, buildingName, status);
    }
    
    /**
     * Creates a separator line
     */
    public static void printSeparator() {
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════╣");
    }
    
    /**
     * Creates a footer for menus
     */
    public static void printMenuFooter() {
        System.out.println("║                                                                                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════╝");
    }
    
    /**
     * Prints a success message with medieval styling
     */
    public static void printSuccessMessage(String message) {
        System.out.println(BRIGHT_GREEN + BOLD + "✅ " + message + RESET);
    }
    
    /**
     * Prints an error message with medieval styling
     */
    public static void printErrorMessage(String message) {
        System.out.println(BRIGHT_RED + BOLD + "❌ " + message + RESET);
    }
    
    /**
     * Prints a warning message with medieval styling
     */
    public static void printWarningMessage(String message) {
        System.out.println(BRIGHT_YELLOW + BOLD + "⚠️  " + message + RESET);
    }
    
    /**
     * Prints an info message with medieval styling
     */
    public static void printInfoMessage(String message) {
        System.out.println(BRIGHT_BLUE + BOLD + "ℹ️ " + message + RESET);
    }
    
    /**
     * Prints a wait prompt for user interaction
     */
    public static void printWaitPrompt() {
        System.out.print(BRIGHT_WHITE + "\n⏳ Press Enter to continue your journey..." + RESET);
    }
    
    /**
     * Clears the console screen
     */
    public static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception ignored) {}
    }
    
    /**
     * Creates a resource header display
     */
    public static void printResourceHeader(String villageName) {
        System.out.println(BRIGHT_CYAN + BOLD);
        System.out.println("╔═══════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                          🏰  " + villageName.toUpperCase() + "  🏰               ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println(RESET);
    }
    
    /**
     * Creates a bottom border for resource display
     */
    public static void printResourceFooter() {
        System.out.println(BRIGHT_CYAN + BOLD);
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println(RESET);
    }
    
    /**
     * Displays a medieval-themed loading message for async operations
     */
    public static void printShadowLoadingMessage() {
        System.out.println(BRIGHT_BLACK + BOLD);
        System.out.println("╔═══════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                                   ║");
        System.out.println("║                      " + BRIGHT_YELLOW + "🕯️  Scribes are recording shadows...🕯️" + BRIGHT_BLACK + "                      ║");
        System.out.println("║                                                                                   ║");
        System.out.println("║                        " + BRIGHT_WHITE + "⏳ Please await the outcome ⏳" + BRIGHT_BLACK + "                         ║");
        System.out.println("║                                                                                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println(RESET);
    }
}
