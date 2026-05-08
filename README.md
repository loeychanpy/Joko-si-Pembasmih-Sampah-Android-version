# 🗑️ Joko Si Pembasmih Sampah
 
> **A 2D educational platformer game built with libGDX** — where players collect trash by solving math problems to clean up the environment!
 
---
 
## 📖 About The Game
 
**Joko Si Pembasmih Sampah** (Joko The Trash Exterminator) is a 2D side-scrolling platformer game with an educational twist. Players control **Joko**, a character on a mission to clean up the environment by collecting scattered trash — but each piece of trash is locked behind a **math problem** that must be solved to pick it up. Collect enough trash to unlock the gate and advance to the next level!
 
This project was developed as a **Game Project** for my final project in Object-oriented Programming course to demonstrate cross-platform game development using Java and the libGDX framework.
 
---
 
## 🎮 Gameplay
 
- **Move** left and right across platformer stages
- **Jump** over obstacles and spikes
- **Approach** trash objects to reveal a math problem (addition or subtraction)
- **Answer** the problem correctly to collect the trash
- **Collect enough trash** to unlock the exit gate and complete the level
- **3 lives** per run — avoid spikes and don't run out of time!
---
 
## ✨ Features
 
- 🧮 **Educational Math Integration** — every trash pickup requires solving a randomized arithmetic problem
- 🏃 **Smooth Platformer Physics** — gravity, jumping with fall multiplier, friction, and collision detection
- 🎨 **Animated Player Character** — walk, idle, and jump animations with sprite flipping
- 🚧 **Varied Obstacles** — spikes, platforms, textured blocks, and timed gates
- 📱 **Mobile-Ready Controls** — on-screen touch buttons (left, right, jump, enter answer)
- ❤️ **Lives & Timer System** — adds urgency and challenge to each level
- 🔊 **Level Progression** — multiple levels with increasing difficulty
- 📱 **Android-First** — designed and optimized for mobile touch controls
---
 
## 🛠️ Tech Stack
 
| Technology | Purpose |
|---|---|
| **Java** | Core game logic |
| **libGDX** | Game framework (rendering, input, audio) |
| **LWJGL3** | Desktop backend |
| **Android SDK** | Mobile backend |
| **Gradle** | Build system & dependency management |
| **AssetManager** | Efficient texture & asset loading |
 
---
 
## 🏗️ Architecture
 
The project follows a clean modular structure:
 
```
Joko-Si-Pembasmih-Sampah/
├── core/                   # Platform-agnostic game logic
│   └── src/main/java/
│       ├── entities/       # Game objects (Player, Trash, Platform, Block, Gate, Spike, Heart)
│       ├── screens/        # Screen management (StartScreen, GameScreen, ScreenManager)
│       └── utils/          # Constants, AssetManager, LevelData
├── android/                # Android launcher & configuration
├── lwjgl3/                 # Desktop launcher & configuration
└── assets/                 # Textures, sprites, fonts
```
 
Key design decisions:
- **Screen Manager pattern** for clean screen transitions
- **Centralized AssetManager** (`Assets.java`) for memory-efficient resource loading
- **LevelData abstraction** to separate level configuration from game logic
- **CollisionResult inner classes** in Platform and Block for descriptive collision handling
---
 
## 🚀 Getting Started
 
### Prerequisites
 
- **Java JDK 17+**
- **Android Studio** (recommended)
- **Android SDK** (API 21+)
- **Git**
### Build & Install Android APK
 
```bash
git clone https://github.com/loeychanpy/joko-si-pembasmih-sampah.git
cd joko-si-pembasmih-sampah
./gradlew android:assembleDebug
```
 
The APK will be located at `android/build/outputs/apk/debug/`. Install it on your Android device or emulator.
 
> ⚠️ **Note:** This game is designed and optimized for **Android** with touch controls. Desktop (LWJGL3) is available as part of the libGDX setup but controls and UI are not tailored for desktop use.
 
---
 
## 📱 Platform Support
 
| Platform | Status |
|---|---|
| 📱 Android (API 21+) | ✅ Primary Target |
| 🖥️ Windows | ⚠️ Runs via LWJGL3 but not optimized for desktop |
| 🍎 macOS | ⚠️ Runs via LWJGL3 but not optimized for desktop |
| 🐧 Linux | ⚠️ Runs via LWJGL3 but not optimized for desktop |
 
---
 
## 🎯 Game Controls
 
### Desktop (Keyboard)
| Key | Action |
|---|---|
| `←` / `→` Arrow Keys | Move left / right |
| `Space` | Jump |
| Touch ENT button | Submit math answer |
 
### Mobile (Touch)
| Button | Action |
|---|---|
| Left Arrow | Move left |
| Right Arrow | Move right |
| Jump Button | Jump |
| ENT Button | Open number input for math answer |
 
---
 
## 🗺️ Development Roadmap
 
- [x] Core platformer movement & physics
- [x] Math problem integration
- [x] Level 1 with full layout
- [x] Android touch controls
- [x] Gate & progression system
- [ ] Level 2 & 3 full designs
- [ ] Sound effects & background music
- [ ] High score & leaderboard
- [ ] More math operators (multiplication, division)
- [ ] Enemy characters
---
 
## 👤 Developer
 
**Janisha** — Computer Science student 
 
- 🐙 GitHub: [@loeychanpy](https://github.com/loeychanpy)
- 💼 LinkedIn: [Janisha Jaya](https://linkedin.com/in/janishajaya)
---

Made with ☕ Java & ❤️ libGDX 
 
