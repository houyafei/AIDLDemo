package com.example.app3;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyService extends Service {

    private CopyOnWriteArrayList<Leader> listLeader = new CopyOnWriteArrayList<>();

    //private CopyOnWriteArrayList<IOnListener> listListener = new CopyOnWriteArrayList<>() ;

    private RemoteCallbackList<IOnListener> listListener = new RemoteCallbackList<>();

    private  Binder binder = new ILeaderAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public List<Leader> getLeaderList() throws RemoteException {
            return listLeader;
        }

        @Override
        public void addLeader(Leader leader) throws RemoteException {
            if (leader != null) {

                listLeader.add(leader) ;
                int size = listListener.beginBroadcast();
                for (int i = 0; i < size; i++) {
                    listListener.getBroadcastItem(i).onNewLeaderListener(leader);
                }
                listListener.finishBroadcast();

            }
        }

        @Override
        public boolean registerNewLeaderListener(IOnListener listener) throws RemoteException {


             return   listListener.register(listener);


        }

        @Override
        public boolean unRegisterNewLeaderListener(IOnListener listener) throws RemoteException {

            return listListener.unregister(listener) ;



        }
    };

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Main","Service is running") ;
        listLeader.add(new Leader("HouYafei","CEO"));
        listLeader.add(new Leader("RenQiang","CTO"));
        listLeader.add(new Leader("Zengye","CFO"));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;

    }
}
