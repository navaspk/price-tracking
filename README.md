# 📈 StockTracker

StockTracker is an Android application for tracking **real-time stock prices** and managing a watchlist.  
Built with **Kotlin**, **Jetpack Compose**, and **Clean Architecture**, it offers a modern and responsive stock monitoring experience.

![StockTracker Demo](https://github.com/user-attachments/assets/d6e29db9-2025-4248-b098-251e4606200f)

---

## ✨ Features

- 📊 **Real-time stock prices** — Powered by WebSocket for instant updates.
- 🔍 **Stock details view** — See detailed price changes and history.
- 🛠 **Watchlist management** — Track only the stocks you care about.
- ⚡ **Toggle live updates** — Control when you want to stream data.
- 🎨 **Modern UI** — Built entirely with Jetpack Compose.

---

## 🛠 Tech Stack

- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Architecture:** MVI + Clean Architecture
- **Dependency Injection:** [Koin](https://insert-koin.io/)
- **Networking:** [Ktor](https://ktor.io/) (WebSocket + HTTP)
- **Build System:** [Gradle](https://gradle.org/)

---

## 📂 Project Structure

StockTracker/
│
├── base/ # Base module — shared utilities (BaseViewModel, etc.)
├── network/ # Networking module — Ktor client setup for WebSocket
├── app/
│ ├── src/main/kotlin/ # Application source code
│ ├── src/main/res/ # Resources (drawables, layouts, values)
│ └── AndroidManifest.xml # App manifest


---

## 🚀 Getting Started

### Prerequisites
- [Android Studio](https://developer.android.com/studio) **2025.1.1** or later
- **JDK 17+**
- Android device or emulator

### Setup & Run
```bash
# Clone the repository
git clone https://github.com/navaspk/price-trackin
cd StockTracker

# Open the project in Android Studio
# Build the project
# Run on an emulator or connected device



https://github.com/user-attachments/assets/d6e29db9-2025-4248-b098-251e4606200f
