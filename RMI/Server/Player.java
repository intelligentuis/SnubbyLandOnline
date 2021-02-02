import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.awt.Rectangle;
import java.time.LocalTime;
import java.lang.module.FindException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;




public class Player extends UnicastRemoteObject implements IPlayer {
    private String  key;
    private int x = 0,y = 0;
    protected Player(String key) throws RemoteException {
        super();
        this.key = key;
    }
    @Override
    public void setX(String key,int x) throws RemoteException
    {
        if(this.key.equals(key))
        {
            this.x= x;
        }
    }

    @Override
    public void setY(String key,int y) throws RemoteException
    {
        if(this.key.equals(key))
        {
            this.y = y;
        }
    }

    @Override
    public int getX() throws RemoteException
    {
        return x;
    }

    @Override
    public int getY() throws RemoteException
    {
        return y;
    }

    @Override
    public boolean setWin(String key) throws RemoteException
    {
        return false;
    }

}
