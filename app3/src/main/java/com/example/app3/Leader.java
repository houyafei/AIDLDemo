package com.example.app3;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hou Yafei on 2017/7/12.
 */
public class Leader implements Parcelable {

    private String name ;
    private String occuption ;

    public Leader(String name, String occuption) {
        this.name = name;
        this.occuption = occuption;
    }

    protected Leader(Parcel in) {

        name = in.readString();
        occuption = in.readString();
    }

    public static final Creator<Leader> CREATOR = new Creator<Leader>() {
        @Override
        public Leader createFromParcel(Parcel in) {
            return new Leader(in);
        }

        @Override
        public Leader[] newArray(int size) {
            return new Leader[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(occuption);
    }


    @Override
    public String toString() {
        return "Leader{" +
                "name='" + name + '\'' +
                ", occuption='" + occuption + '\'' +
                '}';
    }
}
