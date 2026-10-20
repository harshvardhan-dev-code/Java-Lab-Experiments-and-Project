# Experiment 1 – Student Registration Form (Java Swing)

## Aim
To create a graphical user interface (GUI) for a student registration form using the Java Swing framework.

## Description
The form collects the **student name, roll number, gender, branch** and **acceptance of terms & conditions**.
On **Submit**, every field is validated and a success dialog shows the entered details. **Reset** clears the form.

## Swing Components Used
`JFrame`, `JLabel`, `JTextField`, `JRadioButton`, `ButtonGroup`, `JCheckBox`, `JButton`, `JOptionPane`, `ActionListener`

## Files
| File | Description |
|------|-------------|
| `StudentRegistration.java` | Complete source code |
| `Lab-Record.pdf` | Lab record with line-by-line explanation |

## How to Run
```bash
javac StudentRegistration.java
java StudentRegistration
```

## Output
A 450×450 window titled **Student Registration Form**. After filling all fields and clicking **Submit**:

```
Registration Successful

Name : Harsh Vardhan Upadhyay
Roll Number : 24EGJCS087
Gender : Male
Branch : CSE
```
