package org.hsh.games.aoe.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Test class for ResourceType guild vault integration functionality
 * 
 * @author devTASE
 */
public class ResourceTypeGuildVaultTest {

    @Test
    public void testGetResourcesPackBasedOnCurrentEraIsPublic() {
        // Given - Era levels
        int stoneAge = 1;
        int bronzeAge = 2;
        int modernAge = 7;
        
        // When - Get resources for different eras
        List<ResourceType> stoneAgeResources = ResourceType.getResourcesPackBasedOnCurrentEra(stoneAge);
        List<ResourceType> bronzeAgeResources = ResourceType.getResourcesPackBasedOnCurrentEra(bronzeAge);
        List<ResourceType> modernAgeResources = ResourceType.getResourcesPackBasedOnCurrentEra(modernAge);
        
        // Then - Verify expected resources
        assertEquals(5, stoneAgeResources.size());
        assertTrue(stoneAgeResources.contains(ResourceType.WOOD));
        assertTrue(stoneAgeResources.contains(ResourceType.STONE));
        assertTrue(stoneAgeResources.contains(ResourceType.FOOD));
        
        assertEquals(1, bronzeAgeResources.size());
        assertTrue(bronzeAgeResources.contains(ResourceType.IRON));
        
        assertEquals(1, modernAgeResources.size());
        assertTrue(modernAgeResources.contains(ResourceType.FAVOR));
    }

    @Test
    public void testGuildVaultTransferValidation() {
        // When/Then - Valid transfers should not throw
        assertDoesNotThrow(() -> ResourceType.validateGuildVaultTransfer(ResourceType.WOOD, 100));
        assertDoesNotThrow(() -> ResourceType.validateGuildVaultTransfer(ResourceType.GOLD, 1));
        
        // When/Then - Invalid transfers should throw
        assertThrows(IllegalArgumentException.class, 
            () -> ResourceType.validateGuildVaultTransfer(null, 100));
        assertThrows(IllegalArgumentException.class, 
            () -> ResourceType.validateGuildVaultTransfer(ResourceType.WOOD, 0));
        assertThrows(IllegalArgumentException.class, 
            () -> ResourceType.validateGuildVaultTransfer(ResourceType.WOOD, -5));
    }

    @Test
    public void testResourceTypeGuildVaultStorability() {
        // When/Then - All resource types should be storable in guild vaults
        for (ResourceType resource : ResourceType.values()) {
            assertTrue(resource.isGuildVaultStorable(), 
                "Resource " + resource + " should be storable in guild vaults");
        }
    }

    @Test
    public void testResourceTypeTransferMultipliers() {
        // When/Then - Check specific multipliers
        assertEquals(0.5, ResourceType.FAVOR.getGuildVaultTransferMultiplier(), 0.001);
        assertEquals(0.8, ResourceType.POPULATION.getGuildVaultTransferMultiplier(), 0.001);
        assertEquals(1.0, ResourceType.WOOD.getGuildVaultTransferMultiplier(), 0.001);
        assertEquals(1.0, ResourceType.GOLD.getGuildVaultTransferMultiplier(), 0.001);
        assertEquals(1.0, ResourceType.IRON.getGuildVaultTransferMultiplier(), 0.001);
        
        // When/Then - All multipliers should be positive
        for (ResourceType resource : ResourceType.values()) {
            assertTrue(resource.getGuildVaultTransferMultiplier() > 0, 
                "Transfer multiplier for " + resource + " should be positive");
            assertTrue(resource.getGuildVaultTransferMultiplier() <= 1.0, 
                "Transfer multiplier for " + resource + " should not exceed 1.0");
        }
    }

    @Test
    public void testTransferMultiplierLogic() {
        // Given
        int originalAmount = 100;
        
        // When - Apply FAVOR transfer multiplier
        double favorMultiplier = ResourceType.FAVOR.getGuildVaultTransferMultiplier();
        int favorTransferAmount = (int)(originalAmount * favorMultiplier);
        
        // Then
        assertEquals(50, favorTransferAmount);
        
        // When - Apply POPULATION transfer multiplier
        double populationMultiplier = ResourceType.POPULATION.getGuildVaultTransferMultiplier();
        int populationTransferAmount = (int)(originalAmount * populationMultiplier);
        
        // Then
        assertEquals(80, populationTransferAmount);
        
        // When - Apply WOOD transfer multiplier (should be full amount)
        double woodMultiplier = ResourceType.WOOD.getGuildVaultTransferMultiplier();
        int woodTransferAmount = (int)(originalAmount * woodMultiplier);
        
        // Then
        assertEquals(100, woodTransferAmount);
    }
}
