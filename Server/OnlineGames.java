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
    public String getPlayer2(String level,String idPlayer)
    {
        System.out.println("NEW PLAYER: Level@"+level+" ID@"+idPlayer);
        if( levels.containsKey(level))
        {
            String id =levels.get(level);
            System.out.println("PLAYER1 ID "+id+" VS PLAYER 2 ID"+idPlayer);
            levels.put(level,idPlayer);
            return id;
        }

        System.out.println("PLAYER WAITING ...");

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
    	System.out.println("Init Player Of ID : "+id);
        players.put(id,new Rectangle());
        return true;
    }

    @Override
    public boolean sendPlayerUpdate(int x,int y,String id) throws RemoteException
    {
        System.out.println("Update Player Of ID : "+id);
        players.get(id).x = x;
        players.get(id).y = y;

        return true;
    }

    @Override
    public int readX(String id) throws RemoteException
    {
        System.out.println("Read X Of Player ID : "+id);
        return players.get(id).x;
    }

    @Override
    public int readY(String id) throws RemoteException
    {
        System.out.println("Read Y Of Player ID : "+id);
        return players.get(id).y;
    }

}
