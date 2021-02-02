import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.awt.Rectangle;
import java.time.LocalTime;
import java.lang.module.FindException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;




public class FindPlayer extends UnicastRemoteObject implements IFindPlayer {

    private static Map<String,String> levels = new HashMap<String,String>();
    
    private static final String KEY = "rmi://localhost:10101/";

    protected FindPlayer() throws RemoteException {
        super();
    }

    @Override
    public String waitOnlinePlayer(String level,String idPlayer)
    {
        System.out.println(LocalTime.now()+"# WAITING PLAYER ~ LEVEL ["+level+"] PLAYER ["+idPlayer+"]" );
        if( levels.containsKey(level))
        {
            String id =levels.get(level);
            System.out.println(LocalTime.now() +"# PLAYER ["+id+"] VS PLAYER ["+idPlayer+"]");
            levels.put(level,idPlayer);
            return id;
        }

        levels.put(level,idPlayer);

        while(idPlayer.equals(levels.get(level))) {
            try
            {

            Thread.sleep(30);
        }catch(Exception e){};
        };

        String id  = levels.get(level);
        levels.remove(level);

        return id;
    }


    @Override
    public String initPlayer(String key) throws RemoteException {
    	String id = generateID(4);
        System.out.println(LocalTime.now()+"# Init Player ["+id+"]");

        try{

            // LocateRegistry.createRegistry(10101);
            Naming.rebind(KEY + "Players/"+id, new Player(key));

        }catch(Exception e){
            e.printStackTrace();
        };
        
        return id;
    }

    private static String generateID(int length) {
      String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
      String numbers = "1234567890";
      String combinedChars = capitalCaseLetters + lowerCaseLetters  + numbers;
      Random random = new Random();
      char[] password = new char[length];

      for(int i = 0; i< length ; i++) {
         password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
      }
      return String.valueOf(password);
     }

}
