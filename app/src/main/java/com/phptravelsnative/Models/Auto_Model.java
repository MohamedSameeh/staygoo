package com.phptravelsnative.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 03/11/2016.
 */

public class Auto_Model implements Parcelable {
    private String name;

    private int image_id;
    private String type;
    private int id;

    protected Auto_Model(Parcel in) {
        name = in.readString();
        image_id = in.readInt();
        type = in.readString();
        id = in.readInt();
    }
    public Auto_Model()
    {

    }

    public static final Creator<Auto_Model> CREATOR = new Creator<Auto_Model>() {
        @Override
        public Auto_Model createFromParcel(Parcel in) {
            return new Auto_Model(in);
        }

        @Override
        public Auto_Model[] newArray(int size) {
            return new Auto_Model[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(image_id);
        dest.writeString(type);
        dest.writeInt(id);
    }
}
