package com.example.houyafei.apps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.app3.ILeaderAidlInterface;
import com.example.app3.IOnListener;
import com.example.app3.Leader;


import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private List<Leader> leaders = null ;

    private ILeaderAidlInterface leaderAidlInterface ;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            leaderAidlInterface = ILeaderAidlInterface.Stub.asInterface(iBinder);

            //注册监听器
            try {
                boolean isRegist = leaderAidlInterface.registerNewLeaderListener(listener);
                Log.e("Main2Activity","isRegist = "+isRegist) ;
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                leaders = leaderAidlInterface.getLeaderList();
                for (Leader l :
                        leaders) {
                    Log.e("Main2Activity",l.toString()) ;
                }


            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("MainActivity","error") ;
        }

    };

    private IOnListener listener = new IOnListener.Stub() {


        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void onNewLeaderListener(final Leader leader) throws RemoteException {

            if (textViewShowMsg!=null &&
                    leader!=null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewShowMsg.append("\n 新纪录已经添加\n"+leader.toString());
                    }
                });

            }

        }


    } ;

    private Button btn,btn2 ;
    private TextView textViewShowMsg ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        openRemoteService();

        textViewShowMsg = (TextView) findViewById(R.id.msg);
        btn = (Button) findViewById(R.id.btngetMsg);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showResult();

            }

        });

        btn2 = (Button) findViewById(R.id.addedmsg);
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(leaderAidlInterface!=null){
                    try {
                        leaderAidlInterface.addLeader(new Leader("LiQingzhao","传媒总监"));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void openRemoteService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.app3","com.example.app3.MyService"));
        intent.setAction("com.hyf.app3.myservice");

        boolean result = bindService(intent,connection, BIND_AUTO_CREATE);
        Log.e("Main","intent 返回的结果："+result) ;
    }

    private void showResult() {

        List<Leader> leaders = null ;

        try {
            leaders = leaderAidlInterface.getLeaderList();
            Log.e("Main","leaders==null 返回的结果："+(leaders==null)) ;
            Log.e("Main","textViewShowMsg==null 返回的结果："+(textViewShowMsg==null)) ;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (leaders != null && (textViewShowMsg!=null)) {
            textViewShowMsg.setText("Service Datas :\n");
            for (Leader l :
                    leaders) {
                textViewShowMsg.append(l.toString()+"\n");
            }
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Main","返回的结果：1") ;
        if (connection!=null){
            Log.e("Main","返回的结果：2") ;
            unbindService(connection);
            try {
                boolean isUnRegis = leaderAidlInterface.unRegisterNewLeaderListener(listener);
                Log.e("Main","isUnRegis 返回的结果："+(isUnRegis)) ;

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }
}
