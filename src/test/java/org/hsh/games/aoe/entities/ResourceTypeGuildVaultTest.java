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
        assertEquals(2, stoneAgeResources.size());
        assertTrue(stoneAgeResources.contains(ResourceType.ENERGY));
        assertTrue(stoneAgeResources.contains(ResourceType.DATA));
        
        assertEquals(1, bronzeAgeResources.size());
        assertTrue(bronzeAgeResources.contains(ResourceType.COMPONENTS));
        
        assertEquals(1, modernAgeResources.size());
        assertTrue(modernAgeResources.contains(ResourceType.CRYPTO));
    }

    @Test
    public void testGuildVaultTransferValidation() {
        // When/Then - Valid transfers should not throw
        assertDoesNotThrow(() -> ResourceType.validateGuildVaultTransfer(ResourceType.ENERGY, 100));
        assertDoesNotThrow(() -> ResourceType.validateGuildVaultTransfer(ResourceType.QUANTUM_ENERGY, 1));
        
        // When/Then - Invalid transfers should throw
        assertThrows(IllegalArgumentException.class, 
            () -> ResourceType.validateGuildVaultTransfer(null, 100));
        assertThrows(IllegalArgumentException.class, 
            () -> ResourceType.validateGuildVaultTransfer(ResourceType.ENERGY, 0));
        assertThrows(IllegalArgumentException.class, 
            () -> ResourceType.validateGuildVaultTransfer(ResourceType.ENERGY, -5));
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
        assertEquals(0.6, ResourceType.CRYPTO.getGuildVaultTransferMultiplier(), 0.001);
        assertEquals(0.9, ResourceType.DATA.getGuildVaultTransferMultiplier(), 0.001);
        assertEquals(1.0, ResourceType.ENERGY.getGuildVaultTransferMultiplier(), 0.001);
        assertEquals(0.7, ResourceType.QUANTUM_ENERGY.getGuildVaultTransferMultiplier(), 0.001);
        assertEquals(1.0, ResourceType.CIRCUITS.getGuildVaultTransferMultiplier(), 0.001);
        
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
        
        // When - Apply CRYPTO transfer multiplier
        double cryptoMultiplier = ResourceType.CRYPTO.getGuildVaultTransferMultiplier();
        int cryptoTransferAmount = (int)(originalAmount * cryptoMultiplier);
        
        // Then
        assertEquals(60, cryptoTransferAmount);
        
        // When - Apply DATA transfer multiplier
        double dataMultiplier = ResourceType.DATA.getGuildVaultTransferMultiplier();
        int dataTransferAmount = (int)(originalAmount * dataMultiplier);
        
        // Then
        assertEquals(90, dataTransferAmount);
        
        // When - Apply ENERGY transfer multiplier (should be full amount)
        double energyMultiplier = ResourceType.ENERGY.getGuildVaultTransferMultiplier();
        int energyTransferAmount = (int)(originalAmount * energyMultiplier);
        
        // Then
        assertEquals(100, energyTransferAmount);
    }
}
