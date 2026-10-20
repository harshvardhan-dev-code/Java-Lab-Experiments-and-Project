import java.rmi.Remote;
import java.rmi.RemoteException;

// Remote Interface: declares the methods a client can call remotely
public interface MyInterface extends Remote {
    int add(int a, int b) throws RemoteException;
}
