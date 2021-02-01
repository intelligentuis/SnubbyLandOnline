import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.awt.Rectangle;


public class OnlineGames extends UnicastRemoteObject implements IOnlineGames {

    private  Map<String,Rectangle> players = new HashMap<String,Rectangle>() ;
    private static Map<String,String> levels = new HashMap<String,String>();


    protected OnlineGames() throws RemoteException {
        super();
    }

    @Override
    public String waitOnlinePlayer(String level,String idPlayer)
    {
        System.out.println("WAITING PLAYER ~ LEVEL ["+level+"] PLAYER ["+idPlayer+"]" );
        if( levels.containsKey(level))
        {
            String id =levels.get(level);
            System.out.println("PLAYER ["+id+"] VS PLAYER ["+idPlayer+"]");
            levels.put(level,idPlayer);
            return id;
        }

        levels.put(level,idPlayer);

        while(idPlayer.equals(levels.get(level))) {
            try
            {

            Thread.sleep((long)0.2);
        }catch(Exception e){};
        };

        String id  = levels.get(level);
        levels.remove(level);

        return id;
    }


    @Override
    public boolean initPlayer(String id) throws RemoteException {
    	System.out.println("Init Player ["+id+"]");
        players.put(id,new Rectangle());
        return true;
    }

    @Override
    public boolean sendPlayerUpdate(int x,int y,String id) throws RemoteException
    {
        players.get(id).x = x;
        players.get(id).y = y;

        return true;
    }

    @Override
    public int readX(String id) throws RemoteException
    {
        if(!players.containsKey(id)) return -1;
        return players.get(id).x;
    }

    @Override
    public int readY(String id) throws RemoteException
    {
        if(!players.containsKey(id)) return -1;
        return players.get(id).y;
    }

    @Override
    public void sendWin(String id) throws RemoteException
    {
        System.out.print("Player["+id+"] WIN");
        players.remove(id);
    }

    @Override
    public void sendLoss(String id) throws RemoteException
    {
        System.out.print("Player["+id+"] Loss");
        players.remove(id);
    }

}
