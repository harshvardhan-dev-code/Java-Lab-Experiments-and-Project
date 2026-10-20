# Experiment 2 – Calculator using Java Swing (MVC Architecture)

## Aim
To design a GUI-based calculator using Java Swing that performs basic arithmetic operations using the
**MVC (Model-View-Controller)** architecture.

## MVC Structure
```
+--------------+
|     View     |   CalculatorView.java       – Swing GUI only
+--------------+
       |
       v
+--------------+
|  Controller  |   CalculatorController.java – ActionListener, the bridge
+--------------+
       |
       v
+--------------+
|    Model     |   CalculatorModel.java      – calculations only
+--------------+
```

## Files
| File | Role |
|------|------|
| `CalculatorModel.java` | **Model** – add, subtract, multiply, divide |
| `CalculatorView.java` | **View** – JFrame, JLabel, JTextField, JButton |
| `CalculatorController.java` | **Controller** – handles button clicks |
| `Main.java` | Entry point – connects Model, View and Controller |
| `Lab-Record.pdf` | Lab record with theory and viva questions |

## How to Run
```bash
javac *.java
java Main
```

## Output
```
-----------------------------
|     MVC Calculator        |
-----------------------------
First Number   [ 25        ]
Second Number  [ 10        ]
 [+] [-] [*] [/]
Result         [ 35.0      ]
-----------------------------
```
