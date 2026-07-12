# ⌨️ TypeSpeed — Java Swing Typing Speed Test & Performance Analytics App

A desktop typing speed test application built in **Java (Swing)**, featuring real-time
words-per-minute (WPM) tracking, live character-level accuracy feedback, a persistent
global leaderboard, per-user history, and a role-based free/premium account system.

Built as an individual object-oriented design project to apply core software engineering
principles — inheritance, polymorphism, interfaces, generics, and the Observer pattern —
in a real, working application rather than isolated exercises.

<!-- Add 1-2 screenshots or a short GIF of the app running here, e.g.: -->
<!-- ![Typing Test Screenshot](docs/screenshot-typing-test.png) -->

---

## ✨ Features

- **Live typing test** — real-time WPM, accuracy, and mistake tracking as you type, with
  inline color-coded feedback (correct characters in blue, incorrect in red) rendered via
  `JTextPane`/`StyledDocument`
- **Difficulty levels** — Easy / Medium / Hard text pools loaded from external resource files
- **Account system** — registration and login with **SHA-256 password hashing** (no
  plaintext passwords stored or compared)
- **Free vs. Premium tiers** — Premium users unlock difficulty selection and history export;
  Free users get a streamlined default experience
- **Global leaderboard** — top performers ranked by WPM, built on a generic,
  type-safe `Leaderboard<T extends Result>` class
- **Personal history** — per-user performance history with sorting
- **Export to file** — Premium users can export their typing history to a `.txt` report
- **Persistent storage** — user accounts and results persist across sessions via Java
  object serialization

## 🧠 Concepts demonstrated

| Concept | Where |
|---|---|
| Abstraction & inheritance | `User` (abstract) → `FreeUser`, `PremiumUser` |
| Interfaces / polymorphism | `TypingBehavior` interface implemented per user type |
| Generics | `Leaderboard<T extends Result>` |
| Observer / listener pattern | `TypingTestGUI.TypingTestListener` decouples the GUI from scoring logic |
| Event-driven programming | Swing `KeyListener`, `ActionListener`, `Timer` for live stats |
| File I/O & serialization | `ObjectInputStream`/`ObjectOutputStream` for persistence, `BufferedReader` for text pools |
| Security basics | SHA-256 password hashing via `MessageDigest` |

## 🛠️ Tech stack

- **Language:** Java (JDK 8+)
- **UI:** Java Swing (`JFrame`, `JTextPane`, `StyledDocument`, `Timer`)
- **Persistence:** Java Object Serialization (file-based)
- **Design:** Object-Oriented Programming, Observer pattern, role-based access control

## 📂 Project structure

```
typing-speed-analyzer/
├── src/com/typingapp/       # All source files (package com.typingapp)
│   ├── TypingApp.java       # Entry point
│   ├── LoginScreen.java     # Auth UI
│   ├── DashboardScreen.java # Main menu
│   ├── TypingTestGUI.java   # Live typing test UI
│   ├── TypingTest.java      # Test orchestration logic
│   ├── FeedbackAnalyzer.java# WPM / accuracy / mistake calculation
│   ├── Leaderboard.java     # Generic leaderboard + persistence
│   ├── User.java / FreeUser.java / PremiumUser.java
│   └── ...
├── resources/                # Text pools for each difficulty
│   ├── easy.txt
│   ├── medium.txt
│   └── hard.txt
├── LICENSE
└── README.md
```

## 🚀 Getting started

**Requirements:** JDK 8 or later

```bash
# Clone the repo
git clone https://github.com/<your-username>/typing-speed-analyzer.git
cd typing-speed-analyzer

# Compile
javac -d out $(find src -name "*.java")

# Run (from the project root, so it can find the resources/ folder)
java -cp out com.typingapp.TypingApp
```

On first run the app creates `users.dat` and `results.dat` in the working directory to
persist accounts and scores.

## 🗺️ Known limitations & possible extensions

Being transparent about scope, since this was built as a learning project:

- Data is stored via Java serialization in flat files rather than a database — fine for a
  single-user desktop demo, not for concurrent/multi-user use
- No password reset flow or input sanitization beyond basic empty-field checks
- UI is desktop-only (Swing); a web version would need a full client-server rewrite,
  not just a "frontend swap"

Natural next steps if extended further: SQLite/JDBC-backed persistence, JUnit test
coverage for `FeedbackAnalyzer`, and a settings screen for custom test duration.

## 📄 License

MIT — see [LICENSE](LICENSE).
