# Experiment 5 – RMI (Remote Method Invocation)

## Aim
To develop a simple Java RMI application in which a client invokes a method of a remote object
to perform addition of two numbers.

## RMI Architecture
```
                 RMI REGISTRY
              (name "Add" → MyRemote)
                 ^           ^
          lookup |           | rebind
                 |           |
   CLIENT JVM ---+           +--- SERVER JVM
   Client.java                    Server.java
   obj.add(10,20)                 MyRemote.add()
        |                               ^
        +--- Stub ---- Network ---------+
                Result = 30
```

## Files
| File | Description |
|------|-------------|
| `MyInterface.java` | Remote interface – extends `java.rmi.Remote` |
| `MyRemote.java` | Remote object – extends `UnicastRemoteObject` |
| `Server.java` | Creates the remote object and registers it as `Add` |
| `Client.java` | Looks up `Add` and calls `add(10, 20)` |
| `Lab-Record.pdf` | Lab record with theory and viva questions |

## How to Run
Open the terminal in this folder.

**Step 1 – Compile**
```bash
javac *.java
```
**Step 2 – Start the RMI Registry** (Windows opens it in a new window)
```bash
start rmiregistry
```
On Linux / macOS use `rmiregistry &`.

**Step 3 – Start the Server** (new terminal)
```bash
java Server
```
**Step 4 – Run the Client** (another terminal)
```bash
java Client
```

## Output
Server:
```
Server is ready...
```
Client:
```
Result = 30
```

## Important Methods
| Method | Used by | Purpose |
|--------|---------|---------|
| `Naming.rebind()` | Server | Register (or replace) a remote object by name |
| `Naming.lookup()` | Client | Find a remote object by name |
