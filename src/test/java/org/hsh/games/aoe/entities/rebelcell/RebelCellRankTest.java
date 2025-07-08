package org.hsh.games.aoe.entities.rebelcell;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RebelCellRank enum.
 * 
 * @author devTASE
 */
@DisplayName("RebelCellRank Tests")
class RebelCellRankTest {

    @Test
    @DisplayName("Should return correct display names for all ranks")
    void testRankDisplayName() {
        assertEquals("Rebel Hacker", RebelCellRank.HACKER.getDisplayName());
        assertEquals("Rebel Operator", RebelCellRank.OPERATOR.getDisplayName());
        assertEquals("Rebel Cell Leader", RebelCellRank.CELL_LEADER.getDisplayName());
        assertEquals("Rebel Infiltrator", RebelCellRank.INFILTRATOR.getDisplayName());
    }

    @Test
    @DisplayName("Should return correct emojis for all ranks")
    void testRankEmoji() {
        assertEquals("💻", RebelCellRank.HACKER.getEmoji()); // 💻
        assertEquals("📡", RebelCellRank.OPERATOR.getEmoji()); // 📡
        assertEquals("👨‍✈️", RebelCellRank.CELL_LEADER.getEmoji()); // 👨‍✈️
        assertEquals("🥷", RebelCellRank.INFILTRATOR.getEmoji()); // 🥷
    }

    @Test
    @DisplayName("Should correctly identify which ranks have leadership privileges")
    void testRankLeadershipPrivileges() {
        assertFalse(RebelCellRank.HACKER.hasLeadershipPrivileges());
        assertFalse(RebelCellRank.OPERATOR.hasLeadershipPrivileges());
        assertTrue(RebelCellRank.CELL_LEADER.hasLeadershipPrivileges());
        assertFalse(RebelCellRank.INFILTRATOR.hasLeadershipPrivileges());
    }

    @Test
    @DisplayName("Should correctly identify which ranks can infiltrate")
    void testRankInfiltrationAbility() {
        assertFalse(RebelCellRank.HACKER.canInfiltrate());
        assertFalse(RebelCellRank.OPERATOR.canInfiltrate());
        assertFalse(RebelCellRank.CELL_LEADER.canInfiltrate());
        assertTrue(RebelCellRank.INFILTRATOR.canInfiltrate());
    }

    @Test
    @DisplayName("Should find rank by exact display name")
    void testGetByDisplayName() {
        assertEquals(RebelCellRank.HACKER, RebelCellRank.getByDisplayName("Rebel Hacker"));
        assertEquals(RebelCellRank.OPERATOR, RebelCellRank.getByDisplayName("Rebel Operator"));
        assertEquals(RebelCellRank.CELL_LEADER, RebelCellRank.getByDisplayName("Rebel Cell Leader"));
        assertEquals(RebelCellRank.INFILTRATOR, RebelCellRank.getByDisplayName("Rebel Infiltrator"));
    }

    @Test
    @DisplayName("Should find rank by display name ignoring case")
    void testGetByDisplayNameIgnoresCase() {
        assertEquals(RebelCellRank.HACKER, RebelCellRank.getByDisplayName("rebel hacker"));
        assertEquals(RebelCellRank.OPERATOR, RebelCellRank.getByDisplayName("REBEL OPERATOR"));
        assertEquals(RebelCellRank.CELL_LEADER, RebelCellRank.getByDisplayName("ReBel CeLl LeAdEr"));
    }

    @Test
    @DisplayName("Should return null for invalid display names")
    void testGetByDisplayNameReturnsNullForInvalidName() {
        assertNull(RebelCellRank.getByDisplayName("Invalid Rank"));
        assertNull(RebelCellRank.getByDisplayName(""));
        assertNull(RebelCellRank.getByDisplayName(null));
    }

    @Test
    @DisplayName("Should return display name with emoji prefix")
    void testGetDisplayNameWithEmoji() {
        assertEquals("💻 Rebel Hacker", RebelCellRank.HACKER.getDisplayNameWithEmoji());
        assertEquals("📡 Rebel Operator", RebelCellRank.OPERATOR.getDisplayNameWithEmoji());
        assertEquals("👨‍✈️ Rebel Cell Leader", RebelCellRank.CELL_LEADER.getDisplayNameWithEmoji());
        assertEquals("🥷 Rebel Infiltrator", RebelCellRank.INFILTRATOR.getDisplayNameWithEmoji());
    }

    @Test
    @DisplayName("Should have meaningful descriptions for all ranks")
    void testRankDescriptions() {
        assertNotNull(RebelCellRank.HACKER.getDescription());
        assertNotNull(RebelCellRank.OPERATOR.getDescription());
        assertNotNull(RebelCellRank.CELL_LEADER.getDescription());
        assertNotNull(RebelCellRank.INFILTRATOR.getDescription());
        
        assertTrue(RebelCellRank.HACKER.getDescription().contains("security"));
        assertTrue(RebelCellRank.OPERATOR.getDescription().contains("logistics"));
        assertTrue(RebelCellRank.CELL_LEADER.getDescription().contains("Leader"));
        assertTrue(RebelCellRank.INFILTRATOR.getDescription().contains("covert"));
    }

    @Test
    @DisplayName("Should have all ranks with unique properties")
    void testAllRanksAreDifferent() {
        RebelCellRank[] ranks = RebelCellRank.values();
        assertEquals(4, ranks.length);
        
        for (int i = 0; i < ranks.length; i++) {
            for (int j = i + 1; j < ranks.length; j++) {
                assertNotEquals(ranks[i], ranks[j]);
                assertNotEquals(ranks[i].getRank(), ranks[j].getRank());
                assertNotEquals(ranks[i].getDisplayName(), ranks[j].getDisplayName());
            }
        }
    }
}
