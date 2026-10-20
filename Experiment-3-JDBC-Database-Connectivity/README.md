# Experiment 3 – JDBC (Java Database Connectivity)

## Aim
To connect a Java program to a MySQL database using JDBC, insert a record with a `PreparedStatement`
and read all records with a `Statement` and `ResultSet`.

## Requirements
| Software | Notes |
|----------|-------|
| Java JDK | `java -version` |
| MySQL Server | username `root`, password `root` (change in `JdbcDemo.java` if different) |
| MySQL Connector/J | JDBC driver JAR, placed in `lib/` |

## Project Structure
```
Experiment-3-JDBC-Database-Connectivity
├── src/
│   └── JdbcDemo.java      – JDBC program
├── lib/                   – put mysql-connector-j-x.x.x.jar here
├── database.sql           – creates the college database and student table
└── Lab-Record.pdf
```

## Steps to Run
1. **Create the database** – open MySQL and run `database.sql`:
   ```sql
   CREATE DATABASE college;
   USE college;
   CREATE TABLE student(id INT PRIMARY KEY, name VARCHAR(30), branch VARCHAR(20));
   ```
2. **Add the driver** – copy `mysql-connector-j-x.x.x.jar` into `lib/`.
3. **Compile and run** (from this folder):

   Windows
   ```bash
   javac -d out src/JdbcDemo.java
   java -cp "out;lib/*" JdbcDemo
   ```
   Linux / macOS
   ```bash
   javac -d out src/JdbcDemo.java
   java -cp "out:lib/*" JdbcDemo
   ```
   In **VS Code**: add the JAR under *Java Projects → Referenced Libraries* and click **Run**.

## JDBC Flow
```
Load Driver → DriverManager.getConnection() → PreparedStatement (INSERT)
→ executeUpdate() → Statement (SELECT) → executeQuery() → ResultSet → close()
```

## Output
```
Record Inserted
101 Rahul CSE
102 Amit IT
103 Neha ECE
```

## Common Errors
| Error | Fix |
|-------|-----|
| `ClassNotFoundException: com.mysql.cj.jdbc.Driver` | Add the Connector/J JAR to the classpath |
| `Communications link failure` | Start the MySQL service |
| `Access denied for user 'root'` | Use the correct MySQL username/password |
| `Unknown database 'college'` | Run `CREATE DATABASE college;` |
| `Duplicate entry '101' for key 'PRIMARY'` | Record already exists – use a different id |
