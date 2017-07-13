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
