# Experiment 4 – TCP Client-Server Communication (Socket Programming)

## Aim
To implement communication between a Client and a Server over **TCP** using `Socket` and `ServerSocket`
from the `java.net` package.

## Concepts
- **Server** – waits on port **5000** for a client (`ServerSocket.accept()` is blocking).
- **Client** – connects to `localhost:5000`, sends a message and reads the reply.
- **Socket** – an endpoint for communication between the two programs.
- **TCP** – connection-oriented, reliable, ordered, error-controlled.

| TCP | UDP |
|-----|-----|
| Connection-oriented | Connectionless |
| Reliable | Less reliable |
| Ordered data | No guarantee of order |
| `Socket` / `ServerSocket` | `DatagramSocket` / `DatagramPacket` |
| More overhead | Less overhead |

## Communication Flow
```
CLIENT                              SERVER
  | ------ connection accepted ------> |
  | ------ "Hello Server" ------------> |
  | <----- "Hello Client" ------------- |
  | ------ close ---------------------> |
```

## Files
| File | Description |
|------|-------------|
| `Server.java` | TCP server on port 5000 |
| `Client.java` | TCP client |
| `Lab-Record.pdf` | Lab record with theory |

## How to Run
Compile both:
```bash
javac Server.java Client.java
```
**Terminal 1** – start the server first:
```bash
java Server
```
**Terminal 2** – then run the client:
```bash
java Client
```

## Output
Server terminal:
```
Server started...
Waiting for client...
Client connected.
Client says: Hello Server
Connection closed.
```
Client terminal:
```
Connected to server.
Server says: Hello Client
Connection closed.
```
