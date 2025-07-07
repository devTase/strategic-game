package org.hsh.games.aoe.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ConsoleDisplayUtils
 * Tests the enhanced console display functionality with medieval theme
 * 
 * @author devTASE
 */
class ConsoleDisplayUtilsTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testPrintGameBanner() {
        // Given & When
        ConsoleDisplayUtils.printGameBanner();
        String output = outputStream.toString();

        // Then
        assertTrue(output.contains("STRATEGIC KINGDOMS"), "Banner should contain game title");
        assertTrue(output.contains("🏰"), "Banner should contain castle emoji");
        assertTrue(output.contains("⚔️"), "Banner should contain sword emoji");
        assertTrue(output.contains("╔═══"), "Banner should use medieval-style borders");
        assertTrue(output.contains("Build your empire"), "Banner should contain motivational text");
    }

    @Test
    void testPrintMainMenu() {
        // Given & When
        ConsoleDisplayUtils.printMainMenu();
        String output = outputStream.toString();

        // Then
        assertTrue(output.contains("COUNCIL CHAMBER"), "Menu should have council chamber title");
        assertTrue(output.contains("RESOURCE EXPEDITION"), "Menu should contain resource option");
        assertTrue(output.contains("CONSTRUCTION GUILD"), "Menu should contain construction option");
        assertTrue(output.contains("DAILY TRIBUTE"), "Menu should contain daily rewards option");
        assertTrue(output.contains("KINGDOM STATUS"), "Menu should contain status option");
        assertTrue(output.contains("ABANDON REALM"), "Menu should contain exit option");
        assertTrue(output.contains("Choose your action, noble ruler"), "Menu should have proper prompt");
    }

    @Test
    void testPrintResourceMenu() {
        // Given & When
        ConsoleDisplayUtils.printResourceMenu();
        String output = outputStream.toString();

        // Then
        assertTrue(output.contains("RESOURCE EXPEDITION"), "Resource menu should have proper title");
        assertTrue(output.contains("🗺️"), "Resource menu should contain map emoji");
        assertTrue(output.contains("Select the resource type"), "Resource menu should have instruction text");
        assertTrue(output.contains("╔═══"), "Resource menu should use proper borders");
    }

    @Test
    void testPrintConstructionMenu() {
        // Given & When
        ConsoleDisplayUtils.printConstructionMenu();
        String output = outputStream.toString();

        // Then
        assertTrue(output.contains("CONSTRUCTION GUILD"), "Construction menu should have proper title");
        assertTrue(output.contains("🏗️"), "Construction menu should contain construction emoji");
        assertTrue(output.contains("Choose the type of structure"), "Construction menu should have instruction text");
    }

    @Test
    void testPrintStatusHeader() {
        // Given & When
        ConsoleDisplayUtils.printStatusHeader();
        String output = outputStream.toString();

        // Then
        assertTrue(output.contains("KINGDOM STATUS"), "Status header should have proper title");
        assertTrue(output.contains("📜"), "Status header should contain scroll emoji");
        assertTrue(output.contains("╔═══"), "Status header should use proper borders");
    }

    @Test
    void testPrintDailyRewardsHeader() {
        // Given & When
        ConsoleDisplayUtils.printDailyRewardsHeader();
        String output = outputStream.toString();

        // Then
        assertTrue(output.contains("DAILY TRIBUTE"), "Daily rewards header should have proper title");
        assertTrue(output.contains("🎁"), "Daily rewards header should contain gift emoji");
        assertTrue(output.contains("╔═══"), "Daily rewards header should use proper borders");
    }

    @Test
    void testPrintResourceItem() {
        // Given
        int index = 1;
        String resourceName = "Wood";
        String icon = "🪵";

        // When
        ConsoleDisplayUtils.printResourceItem(index, resourceName, icon);
        String output = outputStream.toString();

        // Then
        assertTrue(output.contains("1."), "Should display the index");
        assertTrue(output.contains("Wood"), "Should display the resource name");
        assertTrue(output.contains("🪵"), "Should display the resource icon");
        assertTrue(output.contains("Gather from the wilderness"), "Should display descriptive text");
        assertTrue(output.contains("║"), "Should use proper border characters");
    }

    @Test
    void testPrintBuildingItem() {
        // Given
        int index = 2;
        String buildingName = "Farm";
        String icon = "🌾";
        String status = "Level 1 - Upgradeable";

        // When
        ConsoleDisplayUtils.printBuildingItem(index, buildingName, icon, status);
        String output = outputStream.toString();

        // Then
        assertTrue(output.contains("2."), "Should display the index");
        assertTrue(output.contains("Farm"), "Should display the building name");
        assertTrue(output.contains("🌾"), "Should display the building icon");
        assertTrue(output.contains("Level 1 - Upgradeable"), "Should display the status");
        assertTrue(output.contains("║"), "Should use proper border characters");
    }

    @Test
    void testPrintSuccessMessage() {
        // Given
        String message = "Construction completed successfully!";

        // When
        ConsoleDisplayUtils.printSuccessMessage(message);
        String output = outputStream.toString();

        // Then
        assertTrue(output.contains("✅"), "Success message should contain checkmark");
        assertTrue(output.contains(message), "Success message should contain the provided text");
        assertTrue(output.contains("\u001B[92m"), "Success message should use bright green color");
    }

    @Test
    void testPrintErrorMessage() {
        // Given
        String message = "Not enough resources!";

        // When
        ConsoleDisplayUtils.printErrorMessage(message);
        String output = outputStream.toString();

        // Then
        assertTrue(output.contains("❌"), "Error message should contain X mark");
        assertTrue(output.contains(message), "Error message should contain the provided text");
        assertTrue(output.contains("\u001B[91m"), "Error message should use bright red color");
    }

    @Test
    void testPrintWarningMessage() {
        // Given
        String message = "All workers are busy!";

        // When
        ConsoleDisplayUtils.printWarningMessage(message);
        String output = outputStream.toString();

        // Then
        assertTrue(output.contains("⚠️"), "Warning message should contain warning emoji");
        assertTrue(output.contains(message), "Warning message should contain the provided text");
        assertTrue(output.contains("\u001B[93m"), "Warning message should use bright yellow color");
    }

    @Test
    void testPrintInfoMessage() {
        // Given
        String message = "Daily tribute available!";

        // When
        ConsoleDisplayUtils.printInfoMessage(message);
        String output = outputStream.toString();

        // Then
        assertTrue(output.contains("ℹ️"), "Info message should contain info emoji");
        assertTrue(output.contains(message), "Info message should contain the provided text");
        assertTrue(output.contains("\u001B[94m"), "Info message should use bright blue color");
    }

    @Test
    void testPrintWaitPrompt() {
        // Given & When
        ConsoleDisplayUtils.printWaitPrompt();
        String output = outputStream.toString();

        // Then
        assertTrue(output.contains("⏳"), "Wait prompt should contain hourglass emoji");
        assertTrue(output.contains("Press Enter to continue your journey"), "Wait prompt should have proper text");
    }

    @Test
    void testPrintResourceHeader() {
        // Given
        String villageName = "Camelot";

        // When
        ConsoleDisplayUtils.printResourceHeader(villageName);
        String output = outputStream.toString();

        // Then
        assertTrue(output.contains("CAMELOT"), "Header should display village name in uppercase");
        assertTrue(output.contains("🏰"), "Header should contain castle emoji");
        assertTrue(output.contains("╔═══"), "Header should use proper borders");
    }

    @Test
    void testPrintMenuFooter() {
        // Given & When
        ConsoleDisplayUtils.printMenuFooter();
        String output = outputStream.toString();

        // Then
        assertTrue(output.contains("║"), "Footer should contain border characters");
        assertTrue(output.contains("╚═══"), "Footer should use proper bottom border");
    }

    @Test
    void testPrintSeparator() {
        // Given & When
        ConsoleDisplayUtils.printSeparator();
        String output = outputStream.toString();

        // Then
        assertTrue(output.contains("╠═══"), "Separator should use proper horizontal line with connections");
    }

    @Test
    void testColorConstants() {
        // Test that color constants are properly defined
        assertNotNull(ConsoleDisplayUtils.RESET, "RESET color should be defined");
        assertNotNull(ConsoleDisplayUtils.BRIGHT_GREEN, "BRIGHT_GREEN color should be defined");
        assertNotNull(ConsoleDisplayUtils.BRIGHT_RED, "BRIGHT_RED color should be defined");
        assertNotNull(ConsoleDisplayUtils.BRIGHT_YELLOW, "BRIGHT_YELLOW color should be defined");
        assertNotNull(ConsoleDisplayUtils.BRIGHT_BLUE, "BRIGHT_BLUE color should be defined");
        assertNotNull(ConsoleDisplayUtils.BOLD, "BOLD style should be defined");
    }

    @Test
    void testClearConsoleWithMockedProcessBuilder() {
        // Test is more complex due to process execution, 
        // but we can test that it doesn't throw exceptions
        assertDoesNotThrow(() -> {
            ConsoleDisplayUtils.clearConsole();
        }, "clearConsole should not throw exceptions");
    }
}
