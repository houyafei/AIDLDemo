# AIDL 详细步骤(1)

## 1 提供服务的APP一侧（称作：A）

> 1) 在新建aidl文件夹，构建方式：

>> 在Project目录模式下，main-->右键新建-->new -->Directory-->输入aidl-->ok
>> 再在aidl目录下新建文件夹，（！！**文件夹目录和该项目下的Java下的目录保持一致**！！）否则会出现找不到相关类的错误
    如：项目目录：com.example.app3，则aidl下的目录应为：com.example.app3
    
> 2) 在aidl目录下的com.example.app3文件夹下创建aidl文件:

>> 该文件夹下创建以aidl接口的文件，该文件中定义方法接口：ILeaderAidlInterface.aidl
    
    // ILeaderAidlInterface.aidl
    package com.example.app3;
    
    import com.example.app3.Leader;
    // Declare any non-default types here with import statements
    
    interface ILeaderAidlInterface {
        /**
         * Demonstrates some basic types that you can use as parameters
         * and return values in AIDL.
         */
        void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
                double aDouble, String aString);
    
        /**
        * 
        * 对外预留的访问接口
        * in ：表示外界对内传入数据
        * 该Leader需要实现Parcelable接口，此处要导入该类的全称
        */
        List<Leader> getLeaderList();
        void addLeader(in Leader leader);
    }

>> 其中需要用到了外部对象：Leader，因此需要添加关于该类的声明，声明文件应该为:类名.aidl。(Leader.aidl如下)

    // Leader.aidl
    package com.example.app3;
    
    parcelable Leader ;

>> 在java目录下的com.example.app3文件夹中创建Leader类。
>> 在java的目录下创建Service,Service类主要是创建一个Binder对象，再在OnBinder方法中返回该对象即可。Binder创建方法如下：

    private  Binder binder = new ILeaderAidlInterface.Stub() {
            @Override
            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
            }
            
            //实现获取数据的接口
            @Override
            public List<Leader> getLeaderList() throws RemoteException {
                return listLeader;
            }
    
            //实现添加数据的接口
            @Override
            public void addLeader(Leader leader) throws RemoteException {
                if (leader != null) {
                    listLeader.add(leader) ;
                }
            }
        };

## 2 接受服务的客户端APP（称作：B）

> 1: 首先把APP（A）的aidl文件夹中整体复制到B中；其次要在java下创建Leader类，包名要和APP（A）中的保持一致。
> 2: 在Activity中使用绑定的方式连接APP（A），注意使用Intent显式打开APP（A）的Service，代码如下：

            Intent intent = new Intent();
            //APP(A)包名，APP(A)的Service的全名
            intent.setComponent(new ComponentName("com.example.app3","com.example.app3.MyService"));
            boolean result = bindService(intent,connection, BIND_AUTO_CREATE);
            Log.e("Main","intent 返回的结果："+result) ;

> 3: 获取ILeaderAidlInterface对象的方式，如下：

     leaderAidlInterface = ILeaderAidlInterface.Stub.asInterface(iBinder);

## 3 注意事项

> 在ILeaderAidlInterface.aidl文件中要import所有的相关的类、接口，否则无法编译。

    >> app(A)的目录结构：
    >> src---main--aidl--com.example.app3---ILeaderAildInterface.aidl,Leader.aidl
    >>        |
    >>        |----java--com.example.app3---Leader, Main2Activity.java,MyService.java
    >> app(B)的目录结构：
    >> src---main--aidl--com.example.app3---ILeaderAildInterface.aidl,Leader.aidl
    >>        |
    >>        |----java--com.example.app3---Leader
    >>              |
    >>              |----com.example.houyafei.apps---MainActivity.java, Main2Activity.java

shell@nikel:/ $ ps| grep -i -n 'com.example'

# AIDL 详细步骤(2)

这里为AIDL添加关于事件监听机制，也就是Service事件发生改变时，及时的通知服务端。这里主要是添加一个aidl的接口文件。

## 1 提供服务的APP一侧（称作：A）

> A的aidl文件添加一个提供监听的aidl文件，如下：（注意把Leader的全名导入）

    //IOnListener.aidl
    package com.example.app3;
    
    import com.example.app3.Leader ;
    // Declare any non-default types here with import statements
    
    interface IOnListener {
        /**
         * Demonstrates some basic types that you can use as parameters
         * and return values in AIDL.
         */
        void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
                double aDouble, String aString);
        void onNewLeaderListener(in Leader leader);
    }

>其次修改A的ILeaderAidlInterface.aidl文件，添加用户的注册和取消注册方法，此种的方法都是对外的接口。提供给第三方APP。
 （注意不能忘记把Leader，IOnListener的全名导入）
 
    // ILeaderAidlInterface.aidl
    package com.example.app3;
    
    import com.example.app3.Leader;
    import com.example.app3.IOnListener;
    // Declare any non-default types here with import statements
    
    interface ILeaderAidlInterface {
        /**
         * Demonstrates some basic types that you can use as parameters
         * and return values in AIDL.
         */
        void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
                double aDouble, String aString);
    
        List<Leader> getLeaderList();
        void addLeader(in Leader leader);
    
        //注册监听器
        boolean registerNewLeaderListener(in IOnListener listener);
        //取消注册监听器
        boolean unRegisterNewLeaderListener(in IOnListener listener);
    }

> aidl文件准备好后，需要对Service文件进行修改，Service文件创建Binder对象，并在OnBinder()方法返回该类:

     private  Binder binder = new ILeaderAidlInterface.Stub() {..其中实现所有的接口方法...}
     
> Service 的全部代码如下：

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
        
        /**
        *该容器具有线程同步的左右，内部通过ArrayMap实现，每个Listener的Binder作为key
        *其访问方式必须通过，beginBroadcast()和finishBroadcast()同时出现。
        */
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

## 2 客户端的APP一侧（称作：B）

> 1 将APP(A)中的aidl文件全部复制到APP(B)中。

> 2 在MainActivity中创建ServiceConnection,创建IOnListener对象。

    private ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                
                //获取ILeaderAidlInterface的实例，之后就可以使用其中的方法了
                leaderAidlInterface = ILeaderAidlInterface.Stub.asInterface(iBinder);
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
                
                Log.e("Main","\n 新纪录已经添加\n"+leader.toString()) ;
                
            }
        } ;
    
## 3 总结

> 在主APP中，创建AidlName.aidl文件，其中所有的接口均是提供给用户的，该文件引用的类都要通过import导入。
> 在主APP中，在Service中创建Binder，通过Name.aidl的静态stub()方法创建。
> 通过Service中的OnBind()方法返回创建的Binder。

> 在客户端APP中，将主APP端的所有aidl文件复制到aidl文件夹下。
> 通过绑定的方式实现创建ServiceConnection。
> 获取AidlName对象的实例，从而实现对所有接口的调用。使用Aidl.Stub.asInstance(IBinder iBinder);


> 服务端APP目录结构
> ![服务端APP目录结构](https://github.com/houyafei/AIDLDemo/blob/master/image/serverapp.png?raw=true)

> 用户端APP目录结构
> ![用户端APP目录结构](https://github.com/houyafei/AIDLDemo/blob/master/image/client.png?raw=true)

> 整个项目目录结构
> ![整个项目目录结构](https://github.com/houyafei/AIDLDemo/blob/master/image/thisproject.png?raw=true)



