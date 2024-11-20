package com.phptravelsnative.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 21/09/2016.
 */

public class Amenities_Model implements Parcelable {

    private String icon;
    private String name;

    public Amenities_Model(Parcel in) {
        icon = in.readString();
        name = in.readString();
        ean_id_image = in.readInt();
    }
    public Amenities_Model() {
    }

    public static final Creator<Amenities_Model> CREATOR = new Creator<Amenities_Model>() {
        @Override
        public Amenities_Model createFromParcel(Parcel in) {
            return new Amenities_Model(in);
        }

        @Override
        public Amenities_Model[] newArray(int size) {
            return new Amenities_Model[size];
        }
    };

    public int getEan_id_image() {
        return ean_id_image;
    }

    public void setEan_id_image(int ean_id_image) {
        this.ean_id_image = ean_id_image;
    }

    private int ean_id_image;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
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
        dest.writeString(icon);
        dest.writeString(name);
        dest.writeInt(ean_id_image);
    }
}
