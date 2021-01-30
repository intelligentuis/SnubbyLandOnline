

import java.rmi.Naming;
import java.util.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Game
{
	private static final String KEY = "rmi://localhost:10101/";
	static String id = "#"+UUID.randomUUID();
	static Player p ;

	public static void main(String[] args) throws RemoteException{
		p  = new Player();
		send(p);
	}

    public static void send(Player p)
	{
		 try {
            IOnlineGames onlineGames = (IOnlineGames) Naming.lookup(KEY + "Server");
           
            String idPlayer2 = onlineGames.getPlayer2("10",id);
            System.out.println(idPlayer2);



            onlineGames.initPlayer(id);

            onlineGames.sendPlayerUpdate(50,54,id);
            
            int x = onlineGames.readX(idPlayer2), y = onlineGames.readX(idPlayer2);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
	}


}