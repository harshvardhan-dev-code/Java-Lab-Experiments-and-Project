# Java Lab Experiments and Project

This repository contains all **Advanced Java lab experiments** and one complete **Java project**.
Each experiment is in its own folder with full source code, a README explaining how to run it,
and the original lab record (`Lab-Record.pdf`).

| | |
|---|---|
| **Name** | Harsh Vardhan Upadhyay |
| **Roll Number** | 24EGJCS087 |
| **Language** | Java |

---

## Lab Experiments

| No. | Experiment | Folder | Key Concepts |
|:---:|------------|--------|--------------|
| 1 | GUI for a Student Registration Form using Swing | [Experiment-1-Student-Registration-Form](Experiment-1-Student-Registration-Form) | JFrame, JTextField, JRadioButton, JCheckBox, ActionListener, validation |
| 2 | Calculator using Java Swing following MVC Architecture | [Experiment-2-MVC-Calculator](Experiment-2-MVC-Calculator) | Model-View-Controller, event handling |
| 3 | JDBC (Java Database Connectivity) | [Experiment-3-JDBC-Database-Connectivity](Experiment-3-JDBC-Database-Connectivity) | MySQL, DriverManager, PreparedStatement, ResultSet |
| 4 | TCP Server and Client | [Experiment-4-TCP-Client-Server](Experiment-4-TCP-Client-Server) | Socket, ServerSocket, java.net, TCP vs UDP |
| 5 | RMI (Remote Method Invocation) | [Experiment-5-RMI-Remote-Method-Invocation](Experiment-5-RMI-Remote-Method-Invocation) | Remote interface, UnicastRemoteObject, RMI Registry |
| 6 | Java Servlet (Jakarta Servlet) | [Experiment-6-Java-Servlet](Experiment-6-Java-Servlet) | HttpServlet, life cycle, HttpSession, Cookie, Filter, Listener |

---

## Java Project

### 🎹 [Virtual Piano & Music Recorder](Virtual-Piano-Music-Recorder)

A desktop piano application built with **Java Swing** and the **Java Sound MIDI API**, following the **MVC architecture**.

![Virtual Piano](Virtual-Piano-Music-Recorder/screenshots/piano.png)

- Play with the **mouse** or the **computer keyboard** (`A S D F G H J K L` and `W E T Y U O P`)
- **16 instruments**, octave shift, volume and sustain pedal
- **Record** your music, **play it back** with the keys lighting up, and **save it as a `.mid` file**
- **Open any MIDI file** or play the built-in **demo song**
- Needs **only Java**: no external libraries, sound files or database

**Run:** double-click `run.bat` (Windows), or
```bash
cd Virtual-Piano-Music-Recorder
javac -d out -sourcepath src src/piano/Main.java
java -cp out piano.Main
```
See the [project README](Virtual-Piano-Music-Recorder/README.md) for the full details.

---

## Repository Structure
```
Java-Lab-Experiments-and-Project
├── README.md
├── .gitignore
├── Experiment-1-Student-Registration-Form/
│   ├── StudentRegistration.java
│   ├── README.md
│   └── Lab-Record.pdf
├── Experiment-2-MVC-Calculator/
│   ├── CalculatorModel.java
│   ├── CalculatorView.java
│   ├── CalculatorController.java
│   ├── Main.java
│   ├── README.md
│   └── Lab-Record.pdf
├── Experiment-3-JDBC-Database-Connectivity/
│   ├── src/JdbcDemo.java
│   ├── lib/                 (MySQL Connector/J goes here)
│   ├── database.sql
│   ├── README.md
│   └── Lab-Record.pdf
├── Experiment-4-TCP-Client-Server/
│   ├── Server.java
│   ├── Client.java
│   ├── README.md
│   └── Lab-Record.pdf
├── Experiment-5-RMI-Remote-Method-Invocation/
│   ├── MyInterface.java
│   ├── MyRemote.java
│   ├── Server.java
│   ├── Client.java
│   ├── README.md
│   └── Lab-Record.pdf
├── Experiment-6-Java-Servlet/
│   ├── pom.xml
│   ├── src/main/java/com/servlet/   (Servlets, Filters, Listeners)
│   ├── src/main/webapp/             (index.html, hello.html)
│   ├── README.md
│   └── Lab-Record.pdf
└── Virtual-Piano-Music-Recorder/     ← Java Project
    ├── src/piano/                   (Main, model/, view/, controller/)
    ├── screenshots/
    ├── run.bat / run.sh
    └── README.md
```

---

## Requirements
| Software | Needed for |
|----------|-----------|
| Java JDK 17+ | All experiments and the project (the project also runs on JDK 8+) |
| MySQL Server + MySQL Connector/J | Experiment 3 |
| Apache Maven + Apache Tomcat 11 | Experiment 6 |

## How to Run
Open a terminal inside an experiment folder and follow its README. For most experiments:
```bash
javac *.java
java <MainClassName>
```

| Experiment | Run command |
|------------|-------------|
| 1 | `java StudentRegistration` |
| 2 | `java Main` |
| 3 | `java -cp "out;lib/*" JdbcDemo` (after creating the database) |
| 4 | `java Server` then, in a second terminal, `java Client` |
| 5 | `start rmiregistry`, `java Server`, then `java Client` |
| 6 | `mvn package`, deploy `target/servlet.war` to Tomcat |
| Project | `run.bat`, or `java -cp out piano.Main` after compiling |
