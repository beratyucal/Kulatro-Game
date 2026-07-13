\# Kulatro



Kulatro is a Java desktop card game inspired by \*Balatro\*, reimagined with a science-themed twist. Instead of traditional playing cards, you build hands using elements, quantum particles, or alchemical forces — each with unique scoring mechanics and special-card synergies.



\## Overview



Players log in, choose a deck theme and difficulty, and compete across 4 rounds to beat target scores. Each round, you draw cards, select a hand, and submit it for scoring. Special cards can transform, boost, or duplicate values to help you reach higher scores.



\## Deck Themes



\- \*\*Element\*\* — Hydrogen, Oxygen, Nitrogen, Carbon Dioxide

&#x20; - Special cards: Periodic Boost, Noble Gas, Isotope Decay, Electron Bond

\- \*\*Quantum\*\* — Quark, Boson, Gluon, Photon

&#x20; - Special cards: Quantum Entanglement, Superposition, Gluon Bind, Photon Burst

\- \*\*Alchemy\*\* — Fire, Water, Earth, Air

&#x20; - Special cards: Philosopher's Stone, Transmutation, Elemental Fusion, Catalyst



\## Gameplay



\- Each round the player draws 4 cards and submits a hand (up to 4 cards) for scoring.

\- Difficulty determines the target score thresholds per round:

&#x20; - \*\*Easy:\*\* 40 / 55 / 70 / 85

&#x20; - \*\*Normal:\*\* 50 / 65 / 80 / 95

&#x20; - \*\*Hard:\*\* 60 / 75 / 90 / 105

\- The game is won if the player's average score across all 4 rounds meets or exceeds the average target score.

\- Special cards apply unique scoring rules — for example, treating matching types as a "four of a kind," transforming card values, or letting the player pick the better of two scoring patterns.



\## Features



\- Player login and account management

\- Save/load system for ongoing game sessions

\- Game history and event logging

\- Custom Swing-based GUI (login, main menu, game board, and rules screens)



\## Tech Stack



\- \*\*Language:\*\* Java 21

\- \*\*GUI:\*\* Java Swing

\- \*\*Build:\*\* Eclipse project (`.project` / `.classpath` included)



\## Project Structure

KulatroGame/

├── src/

│   ├── kulatro/

│   │   ├── element/       # Element deck and special cards

│   │   ├── quantum/       # Quantum deck and special cards

│   │   ├── alchemy/       # Alchemy deck and special cards

│   │   ├── engine/        # Game and GameEngine logic

│   │   ├── gui/           # Swing UI frames

│   │   ├── saveload/      # Save/load, player, and logging systems

│   │   ├── Card.java

│   │   ├── Hand.java

│   │   ├── Round.java

│   │   ├── Player.java

│   │   └── ScoreManager.java

│   └── cards/              # Card artwork

├── saves/                   # Saved game sessions

└── cards/                   # Special card definitions



\## Running the Game



1\. Open the project in Eclipse (or any Java 21–compatible IDE).

2\. Run `src/kulatro/main/Main.java`.

3\. Log in or create a player profile, choose a deck theme and difficulty, and start playing.



\## Author



Berat Yücal

