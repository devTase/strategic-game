# Medieval References Comprehensive Audit Report

## Overview
This document contains a complete audit of all medieval-themed references found across the `src/main` and `src/test` directories of the Strategic Game codebase. The search focused on resources, buildings, workers, guilds, eras, icons, messages, and UI strings.

---

## 1. **Main Application Constants** (`ApplicationConstants.java`)

### Medieval-themed Messages:
- `MESSAGE_WRONG_OPTION_TRY_AGAIN`: "⚠️ That command is not recognized, noble ruler. Please choose wisely."
- `MESSAGE_CHOOSE_OPTION`: "Choose your action, noble ruler: "
- `MESSAGE_NOT_ENOUGH_RESOURCES`: "💰 Your coffers lack the required resources for this endeavor, my lord!"
- `MESSAGE_VILLAGE_NAME_PROMPT`: "🏰 What shall we name your noble realm, my lord?"
- `MESSAGE_VILLAGE_CREATION_ERROR`: "❌ A realm must have a proper name! Please try again."
- `MESSAGE_VILLAGE_CREATION_SUCCESS`: "🎉 Welcome to your new kingdom! May it prosper and grow mighty!"
- `MESSAGE_GAME_OVER`: "💀 Your kingdom has fallen! All workers have abandoned the realm..."
- `MESSAGE_FAREWELL`: "⚔️ Farewell, noble ruler! May your legend live on!"
- `MESSAGE_NO_WORKERS_AVAILABLE`: "👥 All your workers are currently occupied with other tasks, my lord!"
- `MESSAGE_WORKER_SENT_TO_GATHER`: "🗺️ Your scout has been dispatched to gather resources from the wilderness!"
- `MESSAGE_WORKER_SENT_TO_BUILD`: "🏗️ Your craftsman has begun construction work on the selected structure!"
- `MESSAGE_RESOURCES_GATHERED`: "✅ Your scouts have returned with valuable resources!"
- `MESSAGE_DAILY_REWARD_CLAIMED`: "🎁 You have claimed your daily tribute from the realm!"
- `MESSAGE_DAILY_REWARD_ALREADY_CLAIMED`: "⏰ You have already claimed your tribute for today, my lord!"
- `MESSAGE_DAILY_REWARD_NEXT`: "🔮 Your next tribute will be ready tomorrow!"
- `MESSAGE_KINGDOM_PROSPERITY`: "🌟 Your kingdom prospers under your wise rule!"
- `MESSAGE_ERA_ADVANCEMENT`: "🎯 Your realm has advanced to a new era of prosperity!"

### Medieval Icons:
- `ICON_BARRACKS`: "🏰" (Castle emoji)
- `ICON_HOUSE`: "🏠"
- `ICON_FARM`: "🌾"
- `ICON_MINE`: "⛏️"
- `ICON_MARKET`: "🏪"
- `ICON_TEMPLE`: "⛪"
- `ICON_WOOD`: "🪵"
- `ICON_STONE`: "🪨"
- `ICON_GOLD`: "💰"
- `ICON_FOOD`: "🌾"
- `ICON_IRON`: "⚒️"

---

## 2. **Era System** (`EraAge.java`)

### Medieval Era Definitions:
- `STONE_AGE`: "Idade da Pedra" (Stone Age)
- `BRONZE_AGE`: "Idade do Bronze" (Bronze Age)
- `IRON_AGE`: "Idade do Ferro" (Iron Age)
- **`MEDIEVAL_AGE`**: "Idade Medieval" (Medieval Age) - **PRIMARY MEDIEVAL REFERENCE**
- `RENAISSANCE`: "Idade do Renascimento" (Renaissance Age)
- `INDUSTRIAL_AGE`: "Idade Industrial" (Industrial Age)
- `MODERN_AGE`: "Idade Moderna" (Modern Age)
- `INFORMATION_AGE`: "Idade da Informação" (Information Age)
- `FUTURE_AGE`: "Idade Futura" (Future Age)

### Medieval Progression System:
- Mission generation multipliers per era
- Mission difficulty modifiers per era
- Mission duration modifiers per era

---

## 3. **Construction System** (`ConstructionType.java`)

### Medieval Building Types:
- `TOWN_CENTER`: "Cidade Central" (Central City/Town Hall)
- `LUMBER_CAMP`: "Madeireira" (Lumber Camp)
- `MILL`: "Moinho" (Mill)
- `HOUSE`: "Casa" (House)
- **`BARRACKS`**: "Barracas" (Barracks) - **MILITARY MEDIEVAL BUILDING**
- `ARCHERY_RANGE`: "Archery Range" - **MEDIEVAL MILITARY BUILDING**
- `STABLE`: "Stable" - **MEDIEVAL CAVALRY BUILDING**
- `MARKET`: "Market" - **MEDIEVAL TRADE BUILDING**
- **`BLACKSMITH`**: "Blacksmith" - **MEDIEVAL CRAFTING BUILDING**
- **`MONASTERY`**: "Monastery" - **MEDIEVAL RELIGIOUS BUILDING**
- `UNIVERSITY`: "University"
- `FARM`: "Farm" - **MEDIEVAL AGRICULTURAL BUILDING**
- `WINERY`: "Vinharia" (Winery)
- `DOCK`: "Docas" (Docks)
- **`TEMPLE`**: "Templo" (Temple) - **MEDIEVAL RELIGIOUS BUILDING**
- `ACADEMY`: "Academia" (Academy)

---

## 4. **Resource System** (`ResourceType.java`)

### Medieval Resource Types:
- `WOOD`: "Madeira" (Wood) - **MEDIEVAL RESOURCE**
- `STONE`: "Pedra" (Stone) - **MEDIEVAL RESOURCE**
- `IRON`: "Ferro" (Iron) - **MEDIEVAL RESOURCE**
- `GOLD`: "Ouro" (Gold) - **MEDIEVAL PRECIOUS RESOURCE**
- `FOOD`: "Comida" (Food) - **MEDIEVAL RESOURCE**
- `WATER`: "Água" (Water) - **BASIC MEDIEVAL RESOURCE**
- `SILVER`: "Prata" (Silver) - **MEDIEVAL PRECIOUS RESOURCE**
- **`FAVOR`**: "Favor aos Deuses" (Favor to the Gods) - **MEDIEVAL DIVINE RESOURCE**
- `GRAPES`: "Uvas" (Grapes) - **MEDIEVAL AGRICULTURAL RESOURCE**

### Era-based Resource Availability:
- Stone Age: Basic resources (food, water, wood, stone)
- Bronze Age: Iron introduction
- Iron Age: Silver introduction  
- **Medieval Age: Grapes introduction**
- Industrial Age: Gold introduction
- Modern Age: Divine favor introduction

---

## 5. **Guild System**

### Guild Event Types (`GuildEventType.java`):
- `CREATED`: "Guild Created" with "🏛️" icon
- `MEMBER_JOINED`: "Member Joined Guild" with "👥" icon
- `MISSION_COMPLETED`: "Mission Completed" with "✅" icon
- **`TERRITORY_CONQUERED`**: "Territory Conquered" with "🏰" icon - **MEDIEVAL CONQUEST THEME**
- `SPY_REPORT`: "Spy Report Received" with "📊" icon

### Guild Mission Types (`MissionType.java`):
- `RESOURCE_RUN`: "Resource Gathering Mission" with "⛏️" icon
- **`TERRITORY_CONQUEST`**: "Territory Conquest Mission" with "🏰" icon - **MEDIEVAL CONQUEST**
- `ESPIONAGE`: "Espionage Mission" with "🕵️‍♂️" icon - **MEDIEVAL SPY THEME**
- `VAULT_DEFENSE`: "Vault Defense Mission" with "🛡️" icon - **MEDIEVAL DEFENSE**

### Guild Ranks (`GuildRank.java`):
- **`LEADER`**: "Guild Leader" with "👑" icon - **MEDIEVAL ROYALTY**
- **`OFFICER`**: "Guild Officer" with "⚔️" icon - **MEDIEVAL MILITARY**
- **`MEMBER`**: "Guild Member" with "🛡️" icon - **MEDIEVAL WARRIOR**
- `RECRUIT`: "Guild Recruit" with "🎯" icon
- **`SPY`**: "Guild Spy" with "🕵️" icon - **MEDIEVAL ESPIONAGE**

---

## 6. **User Interface** (`ConsoleDisplayUtils.java`)

### Medieval-themed UI Elements:
- Game Banner: "🏰 ⚔️ STRATEGIC KINGDOMS - REALM OF CONQUEST ⚔️ 🏰"
- Subtitle: "Build your empire • Gather resources • Forge your destiny"
- Main Menu: "🏛️ COUNCIL CHAMBER 🏛️"
- Menu Options:
  - "🗺️ RESOURCE EXPEDITION"
  - "🏗️ CONSTRUCTION GUILD"
  - "🏛️ GUILD MANAGEMENT"
  - "🎁 DAILY TRIBUTE"
  - "📜 KINGDOM STATUS"
  - "🕯️ SHADOW GUILDS"
  - "🚪 ABANDON REALM"

### Medieval Box Drawing Characters:
- Extensive use of medieval-style borders: "╔", "╗", "╚", "╝", "═", "║"

### Medieval-themed Prompts:
- "⚔️ Choose your action, noble ruler:"
- "🏰 Kingdom:" (status display)
- "🎯 Current Era:" (era display)
- "⏳ Press Enter to continue your journey..."

---

## 7. **Achievement System** (`AchievementDefinitions.java`)

### Medieval-themed Achievements:
- **`medieval_lord`**: "Medieval Lord" - "Reach the Medieval Age"
- `bronze_age_ruler`: "Bronze Age Ruler"
- `iron_age_warrior`: "Iron Age Warrior"
- `renaissance_scholar`: "Renaissance Scholar"
- `city_builder`: "City Builder"
- `architect`: "Architect"
- `town_founder`: "Town Founder"
- `master_builder`: "Master Builder"

---

## 8. **Game Logic** (`GameOfStrategy.java`)

### Medieval Terminology in Code:
- Farm/Village naming: "🏰 What shall we name your noble realm, my lord?"
- Kingdom status display: "🏰 Kingdom: " + playerName
- Medieval prompts throughout gameplay
- Guild creation: "🏛️ Enter guild name:"
- Territory conquest: "🗺️ Shadow Territories:"
- Mission management with medieval themes

---

## 9. **Test Assertions** 

### UI Test Assertions (`ConsoleDisplayUtilsTest.java`):
- Tests for castle emoji: `assertTrue(output.contains("🏰"))`
- Tests for sword emoji: `assertTrue(output.contains("⚔️"))`
- Tests for "STRATEGIC KINGDOMS" title
- Tests for "COUNCIL CHAMBER" menu
- Tests for "noble ruler" prompts
- Tests for medieval-style borders
- Tests for kingdom/realm terminology

### Era Test (`EraAgeMissionMultipliersTest.java`):
- Tests for all era progression including Medieval Age
- Tests for mission multipliers per era
- Tests for difficulty modifiers per era

---

## 10. **Additional Medieval References**

### Worker System:
- Workers referred to as "scouts", "craftsmen", "workers"
- Medieval job assignments (gathering, construction, etc.)

### Shadow Territory System:
- Territory conquest mechanics
- Guild-based territorial control
- Medieval-style conflict resolution

### Daily Rewards:
- Referred to as "tribute" (medieval tax/offering concept)
- "Daily tribute from the realm"

---

## **Summary of Affected Files**

### Main Source Files (`src/main`):
1. `ApplicationConstants.java` - **Extensive medieval messaging and icons**
2. `EraAge.java` - **Medieval Age definition and progression**
3. `ConstructionType.java` - **Medieval building types**
4. `ResourceType.java` - **Medieval resources and era availability**
5. `ConsoleDisplayUtils.java` - **Complete medieval UI theme**
6. `GameOfStrategy.java` - **Medieval terminology throughout**
7. `GuildEventType.java` - **Medieval guild events with castle emoji**
8. `MissionType.java` - **Medieval mission types with appropriate icons**
9. `GuildRank.java` - **Medieval hierarchy system**
10. `AchievementDefinitions.java` - **Medieval-themed achievements**

### Test Files (`src/test`):
1. `ConsoleDisplayUtilsTest.java` - **Tests for medieval UI elements**
2. `EraAgeMissionMultipliersTest.java` - **Tests for medieval era progression**
3. Various other test files with medieval terminology validation

---

## **Key Medieval Icons Used**
- 🏰 (Castle) - **Primary medieval identifier, appears 14+ times**
- ⚔️ (Crossed Swords) - Military/combat theme
- 🏛️ (Classical Building) - Guild/government buildings
- 👑 (Crown) - Leadership/royalty
- 🛡️ (Shield) - Defense/protection
- ⛏️ (Pick) - Resource gathering
- 🗺️ (World Map) - Exploration/territory
- 🕵️ (Detective/Spy) - Espionage theme
- ⛪ (Church) - Religious buildings
- 🪵 (Wood), 🪨 (Rock), ⚒️ (Hammer and Pick) - Medieval resources

---

## **Conclusion**
The codebase contains extensive medieval theming throughout all major systems:
- **59+ medieval-themed constants and messages**
- **15+ building types with medieval names**
- **10+ resource types fitting medieval economy**
- **Complete medieval guild hierarchy system**
- **Medieval-themed UI with appropriate icons and terminology**
- **Era progression system with Medieval Age as core period**
- **Achievement system celebrating medieval milestones**

The castle emoji (🏰) appears as the primary medieval identifier in **14+ locations** across the codebase, making it the most prominent medieval symbol used.
