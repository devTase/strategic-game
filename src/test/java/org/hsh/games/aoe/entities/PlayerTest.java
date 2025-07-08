package org.hsh.games.aoe.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void createPlayerWithValidNameShouldSucceed() {
        String validName = "devTASE";
        Player player = new Player(validName);
        assertEquals(validName, player.getFarmName());
        assertEquals(TechPhase.UPRISING, player.getTechPhase());
    }

    @Test
    void createPlayerWithMinimumValidNameShouldSucceed() {
        String validName = "ABC";
        Player player = new Player(validName);
        assertEquals(validName, player.getFarmName());
    }

    @Test
    void createPlayerWithNullNameShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Player(null)
        );
        assertEquals("O nome da aldeia não pode estar vazio", exception.getMessage());
    }

    @Test
    void createPlayerWithEmptyNameShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Player("")
        );
        assertEquals("O nome da aldeia não pode estar vazio", exception.getMessage());
    }

    @Test
    void createPlayerWithWhitespaceOnlyNameShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Player("   ")
        );
        assertEquals("O nome da aldeia não pode estar vazio", exception.getMessage());
    }

    @Test
    void createPlayerWithTooShortNameShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Player("AB")
        );
        assertEquals("O nome da aldeia deve ter pelo menos 3 caracteres", exception.getMessage());
    }

    @Test
    void createPlayerWithTooShortNameAfterTrimmingShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Player("  AB  ")
        );
        assertEquals("O nome da aldeia deve ter pelo menos 3 caracteres", exception.getMessage());
    }

    @Test
    void setFarmNameWithValidNameShouldSucceed() {
        Player player = new Player("devTASE");
        String newName = "NewFarmName";
        player.setFarmName(newName);
        assertEquals(newName, player.getFarmName());
    }

    @Test
    void setFarmNameWithInvalidNameShouldThrowException() {
        Player player = new Player("devTASE");
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> player.setFarmName("AB")
        );
        assertEquals("O nome da aldeia deve ter pelo menos 3 caracteres", exception.getMessage());
    }

    @Test
    void setFarmNameWithNullShouldThrowException() {
        Player player = new Player("devTASE");
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> player.setFarmName(null)
        );
        assertEquals("O nome da aldeia não pode estar vazio", exception.getMessage());
    }

    @Test
    void setEraAgeShouldUpdateEraAge() {
        Player player = new Player("devTASE");
        EraAge newEra = EraAge.BRONZE_AGE;
        player.setEraAge(newEra);
        assertEquals(newEra, player.getEraAge());
    }
}
