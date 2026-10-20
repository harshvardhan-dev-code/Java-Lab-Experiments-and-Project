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

*Coming soon – will be added in its own folder.*

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
└── Experiment-6-Java-Servlet/
    ├── pom.xml
    ├── src/main/java/com/servlet/   (Servlets, Filters, Listeners)
    ├── src/main/webapp/             (index.html, hello.html)
    ├── README.md
    └── Lab-Record.pdf
```

---

## Requirements
| Software | Needed for |
|----------|-----------|
| Java JDK 17+ | All experiments |
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
