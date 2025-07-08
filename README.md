# ☠️ Neon Rebellion - Cyberpunk Saga

Embark on a quest through the layers of the neon-lit underworld. Developed in Java, this game places you in charge of resources, cyber operatives, and high-tech facilities, leading a rebellion against the heartless corporate overlords dominating the world. Traverse through futuristic landscapes in quests that echo the struggle of the revolutionaries.

## 🎮 Game Features

### Era System
Advance through 9 cyberpunk tech phases:
- ⚡ **Uprising** - Basic coordination and survival
- 📡 **Augmented Streets** - Surveillance and initial enhancements
- 🧠 **Neural Nexus** - Advanced AI and neural interfaces
- 🚁 **Drone Dominion** - Mastery over drone warfare
- ⚛️ **Quantum Dawn** - Quantum computing breakthroughs
- 🤖 **Singularity Prep** - Preparing for the singularity
- 💠 **Post-Singularity** - Life after machine intelligence
- 🌌 **Hyper-Mesh** - Interconnected neural webs
- 🌐 **Exo-Reality** - A fully cybernetic world

### Resource Management
**Basic Resources:**
- 💾 Energy Cells, 🌊 Water Streams, 🍪 Data Streams, ⚙️ Tech Components
- 🤖 Cyber Operatives (determines the number of missions)

**Advanced Resources:**
- 🛠️ Neural Circuits, 🧬 Nanomaterials, 🎮 Quantum Energy, 💳 Crypto Tokens
### Building System
**Production Facilities:**
- 🏢 Command Center - Generates cyber operatives
- 🏠 Ops Base - Processes water streams
- 💾 Resource Depot - Stores tech components
- 🧬 Processing Plant - Processes data streams
- 🎛️ Tech Foundry - Develops nanomaterials
- 🔮 Neural Nexus - Enhances AI capabilities

**Military Facilities:**
- 🏴‍☠️ Training Facility - Instructs cyber operatives
- 🎯 Weapons Range - Equips combat drones
- 🚁 Drone Hangar - Deploys aerial units

**Special Facilities:**
- 📈 Trade Hub - Facilitates resource trading
- ⚒️ Cyber Forge - Manufactures advanced hardware
- 🖥️ Research Lab - Innovates future tech
- 🚀 Launch Pad - Launches deep-net probes
### Cyber Operative Management
- **Multi-threaded System** - Each operative operates independently
- **Dynamic Missions** - Resource hacking or facility construction
- **Automatic Consumption** - Operatives consume resources to survive
- **Occupation Management** - Free vs busy operative handling

### 🎁 Daily Crypto Rewards System
**Inspired by mobile MMO games:**
- **30 days of unique rewards** - Each day offers different resources
- **Streak system** - Consecutive days increase rewards
- **Tech phase multipliers** - Rewards adjusted according to current tech phase
- **Special rewards** - Days 7, 14, and 30 offer extraordinary bonuses
- **Session persistence** - Progress is maintained between games
- **Automatic reset** - Streak resets if you miss a day

### 🏛️ Rebel Cell System
**Group cooperation and strategy:**
- **👑 Cell Creation** - Found your own rebel cell or join an existing one
- **⚔️ Rank Hierarchy** - Cell Leader, Operator, Hacker, Infiltrator
- **💰 Shared Vault** - Deposit resources for common benefit
- **🗺️ Shadow Territories** - Conquer territories for bonuses
- **🎯 Cell Missions** - Cooperate on missions for epic rewards
- **📢 Real-time Events** - Broadcasting system for communication
- **🏹 Strategic Operations** - Espionage, conquest, and defense missions

## 🎨 Enhanced Interface

### New Visual Experience
- **🌃 Cyberpunk Theme** - A fully redesigned interface with neon aesthetics
- **📊 Digital Displays** - Futuristic menus styled with binary and digital motifs
- **🌈 ANSI Colors** - Colors suitable for a vibrant cyber world
- **🎮 Thematic Icons** - Enhanced icons for a fully immersive experience
- **💬 Immersive Dialogues** - Messages that reflect the cyber rebellion vibe

### Interface Features
- **Command Terminal** - Main menu styled as a futuristic control center
- **Resource Hack** - Resource menu with cyber-hacking theme
- **Construction Bay** - Building interface with high-tech architecture style
- **Daily Crypto Drop** - Reward system as digital currency distributions
- **Network Status** - System reports with holographic displays

## 🚀 How to Play

### Requirements
- Java 21+
- Maven 3.6+

### Installation and Execution

```bash
# Clone the repository
git clone https://github.com/devTASE/strategic-game.git
cd strategic-game

# Compile the project
mvn clean compile

# Run the game
mvn exec:java -Dexec.mainClass="org.hsh.games.aoe.Game"

# Or compile and run JAR
mvn clean package
java -jar target/StrategyGame-1.0-SNAPSHOT.jar
```

### Controls
1. **Resource Hacking** - Deploy cyber operatives to gather specific resources
2. **Build/Upgrade** - Construct new facilities or upgrade existing ones
3. **🎁 Daily Crypto Rewards** - Collect daily rewards and check your streak
4. **View Status** - Check current network state and progression

### Strategy Tips
- 🎯 **Balance resources** - Maintain balanced production
- 👥 **Manage operatives** - More operatives = more productivity
- 🏢 **Build strategically** - Each facility has specific requirements
- ⏰ **Manage time** - Construction and hacking take real time
- 📈 **Advance through tech phases** - Meet requirements to unlock new technologies

## 🔧 Technical Architecture

### Project Structure
```
src/main/java/org/hsh/games/aoe/
├── entities/          # Game entities (Player, CyberOperative, Building, etc.)
│   └── rebelcell/    # Entities specific to rebel cell system
├── services/         # Service layer with dependency injection
├── threads/          # Threads for asynchronous operations
├── ui/              # Enhanced console interface utilities
├── visual/          # Interface components (future JavaFX)
├── Game.java        # Entry point
├── GameOfStrategy.java # Main game loop
├── ApplicationConstants.java # Constants and thematic messages
└── ConsoleUtils.java # Basic console utilities
```

### Technical Features
- **Concurrent Programming** - Multiple threads for realistic simulation
- **Strategy Pattern** - Different types of construction and resources
- **Dependency Injection** - Constructor injection for services
- **Thread-Safe Collections** - ConcurrentHashMap for concurrent access
- **Robust Validation** - Input and state verification
- **Modular Architecture** - Easy extension and maintenance

### Technologies
- **Java 21** - Main language
- **Maven** - Dependency management and build
- **JUnit 5** - Unit testing
- **Mockito** - Mocking for tests
- **JavaFX** - Graphical interface (future preparation)

## 🧪 Tests

```bash
# Run all tests
mvn test

# Run specific tests
mvn test -Dtest=PlayerTest
mvn test -Dtest=PlayerServiceTest
```

**Test Coverage:**
- ✅ Player name validation
- ✅ Resource management
- ✅ Construction system
- ✅ Cyber operative logic
- ✅ Daily rewards system
- ✅ Tech phase multipliers
- ✅ Streak management
- ✅ Enhanced console interface (ConsoleDisplayUtils)
- ✅ Thematic messages and ANSI colors
- ✅ Cyberpunk menu formatting
- ✅ **Rebel Cell System** - Creation, member management, ranks
- ✅ **Cell Missions** - Assignment, resolution with delays
- ✅ **Shadow Territories** - Conquest and status updates

## 🎯 Future Features

- [ ] Graphical interface with JavaFX
- [ ] Combat system
- [ ] Multiplayer
- [ ] Progress saving
- [ ] Random events
- [ ] Achievement system
- [ ] Campaign mode

## 🤝 Contribution

1. Fork the project
2. Create a branch for your feature (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -m 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Open a Pull Request

## 👨‍💻 Developer

**devTASE** - [GitHub](https://github.com/devTASE)

## 📄 License

This project is under the MIT license. See the `LICENSE` file for more details.

---

"Lead the rebellion, phase by phase, resource by hack." 🌐
