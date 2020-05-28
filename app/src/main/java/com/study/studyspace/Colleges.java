package com.study.studyspace;

import android.os.Parcel;
import android.os.Parcelable;

public class Colleges implements Parcelable {
    public int id;
    public String collegename;

    public Colleges(int id, String collegename)
    {
        this.id=id;
        this.collegename= collegename;
    }

    public Colleges() {

    }

    protected Colleges(Parcel in) {
        id = in.readInt();
        collegename = in.readString();
    }

    public static final Creator<Colleges> CREATOR = new Creator<Colleges>() {
        @Override
        public Colleges createFromParcel(Parcel in) {
            return new Colleges(in);
        }

        @Override
        public Colleges[] newArray(int size) {
            return new Colleges[size];
        }
    };

    public String getName()
    {
        return collegename;
    }
    public void setName(String s)
    {
         this.collegename = s;
    }
    public  void setId(int i)
    {
        id = i;
    }


    public int getId()
    {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(collegename);
    }
}
