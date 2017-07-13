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
