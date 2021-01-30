import java.lang.module.FindException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.*;



public class Server {

    private static final String KEY = "rmi://localhost:10101/";

    public static void main(String[] a) {

        try {
            OnlineGames games = new OnlineGames();

            LocateRegistry.createRegistry(10101);

            Naming.rebind(KEY + "Server", games);

            System.out.println("Server Ready...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
