import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IFindPlayer extends Remote {
    public String initPlayer(String key)  throws RemoteException;
	public String waitOnlinePlayer(String level,String idPlayer) throws RemoteException;
}
