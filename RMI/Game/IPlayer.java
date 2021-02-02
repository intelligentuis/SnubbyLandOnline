import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPlayer extends Remote {
    public int getX() throws RemoteException;
    public int getY() throws RemoteException;
    public void setX(String key,int x) throws RemoteException;
    public void setY(String key,int y) throws RemoteException;

}
