
---

# SDG 4 — Quality Education Quiz & Learning Application

## 🌿 Project Overview

This application is an Object-Oriented Programming (OOP) Java-based desktop application developed for the **TMF 2954 Java Programming** course. It aligns directly with the United Nations **Sustainable Development Goal 4 (SDG 4): Quality Education**, aiming to provide inclusive learning modules alongside interactive gamified quiz elements to maximize student engagement.

The user interface is intentionally structured within a **smartphone-responsive display resolution (360x640)** while operating purely as a native desktop client.

---

## 🚀 Core Application Features

* **User Session Management:** Validates and dynamically logs new or returning student profile histories via robust custom exception handling pipelines.
* **Learning Module:** Delivers comprehensive, multi-screen educational content focused on SDG 4 targets, tracking metrics, and practical real-world application examples.
* **Bidirectional Quiz Modules:** Features both Multiple Choice Question (MCQ) and True/False tracks backed by a 30-second active thread countdown timer. Includes a backward-tracking navigation matrix to alter selections on the fly without state or data loss.
* **JTable-Driven Analytics:** Replaced legacy unformatted layouts with sophisticated, cell-rendered `JTable` architectures to render real-time global leaderboards and user-specific attempt archives.
* **Gamification Badges System:** Calculates score percentages to dynamically allocate visual achievement awards ranging from *🌱 Beginner* to *🏆 Quiz Champion*.

---

## 📊 Component Distribution Matrix

Every project team member designed and fully developed a dedicated architectural component consisting of a concrete class paired with an independent Java Interface contract:

| Assigned Developer | Core Module Class | Structural Interface | Role & Responsibility Description |
| --- | --- | --- | --- |
| **Qhairunnisa** | `Home` & `QuizResultPage` | `IUser` & `IResultDisplay` | Built the core controller, state trackers, user text-saving layers, and the badge reward screen. |
| **Putra Akmal** | `LeaderBoard` & `HistoryPage` | `ILeaderBoard` & `IHistoryDisplay` | Implemented the `JTable` rendering frameworks, file data stream parsers, and merge conflict sanitizers. |
| **Abdul Rahim** | `MCQModule` & `TrueFalseModule` | `IQuizModule` *(Abstract Interface)* | Engineered the algorithmic quiz question matrices, background timer threads, and bidirectional selection caches. |
| **Wan Adam** | `MenuQuiz` | `IMenuQuiz` | Created the primary application dashboard entry hooks and the interactive instruction alert overlays. |

---

## 💾 Persistent Storage Schemas

The program avoids any hardcoding of grades or user names, operating completely out of local relative flat-file databases:

1. **`users.txt`**: Manages volatile profile metadata and logs the cumulative high scores computed during initial session authentication routines.
2. **`QuizScores.txt`**: Serves as the active database grid recording user metrics. Entries are cleanly appended using sequential pipe separations:
```text
User: Wan Adam | Type: MCQ | Score: 28/30
User: Qhairunnisa | Type: True/False | Score: 30/30

```



---

## ⚙️ Compilation & Execution Commands

As required by the assignment guidelines, compilation and execution must be performed entirely through the command line terminal environment:

1. **Navigate to the root directory containing your source code:**
```bash
cd "C:\Users\User\OneDrive\Documents\The Code"

```


2. **Compile all Java files cleanly:**
```bash
javac *.java

```


3. **Run the main application launcher:**
```bash
java Home

```



---

## 🛠️ Testing, Quality Assurance, and Debugging

* **Git Version Control Logs:** Leveraged split-pane `git diff` trace comparisons to actively perform code refactoring on thread loops and resolve synchronization issues before production deployment.
* **Upstream Conflict Sanitization:** Programmed explicit check statements (`!line.startsWith("<")`) inside file parsing filters to automatically isolate and ignore raw Git merge conflict flags, preventing data corruption or app crashes.
* **Input Validation Exceptions:** Enforced error handling via custom throwable exceptions (`InvalidInputException`) to prevent empty name strings from corrupting user text indexes.
