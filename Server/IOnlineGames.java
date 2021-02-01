import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IOnlineGames extends Remote {
	public String waitOnlinePlayer(String level,String idPlayer) throws RemoteException;
    public boolean initPlayer(String id) throws RemoteException;
    public boolean sendPlayerUpdate(int x,int y,String id) throws RemoteException;
    public int readX(String id) throws RemoteException;
    public int readY(String id) throws RemoteException;
    public void sendWin(String id) throws RemoteException;
    public void sendLoss(String id) throws RemoteException;
}
