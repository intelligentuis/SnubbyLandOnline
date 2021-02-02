import java.lang.module.FindException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.*;

/*
Classes Levels, Player


*/

public class Main {

    private static final String KEY = "rmi://localhost:10101/";

    public static void main(String[] a) {

        try {
            FindPlayer f = new FindPlayer();

            LocateRegistry.createRegistry(10101);

            Naming.rebind(KEY + "Server", f);

            System.out.println("Snubby Land Server Ready...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
