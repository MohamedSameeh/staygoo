package com.phptravelsnative.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 25/11/2016.
 */

public class Hotel_data  implements Parcelable{

    private String from;
    private String to;
    private String adult;
    private String child;
    private String location;
    private int id;

    protected Hotel_data(Parcel in) {
        from = in.readString();
        to = in.readString();
        adult = in.readString();
        child = in.readString();
        location = in.readString();
        id = in.readInt();
    }

    public Hotel_data() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(from);
        dest.writeString(to);
        dest.writeString(adult);
        dest.writeString(child);
        dest.writeString(location);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Hotel_data> CREATOR = new Creator<Hotel_data>() {
        @Override
        public Hotel_data createFromParcel(Parcel in) {
            return new Hotel_data(in);
        }

        @Override
        public Hotel_data[] newArray(int size) {
            return new Hotel_data[size];
        }
    };

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
