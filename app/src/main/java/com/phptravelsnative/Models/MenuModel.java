package com.phptravelsnative.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hamza on 8/4/2016.
 */
public class MenuModel implements Parcelable {
    private int title;
    private boolean status;
    private String type;

    public MenuModel() {
    }

    protected MenuModel(Parcel in) {
        title = in.readInt();
        status = in.readByte() != 0;
        type = in.readString();
        imgID = in.readInt();
        Description = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(title);
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeString(type);
        dest.writeInt(imgID);
        dest.writeInt(Description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MenuModel> CREATOR = new Creator<MenuModel>() {
        @Override
        public MenuModel createFromParcel(Parcel in) {
            return new MenuModel(in);
        }

        @Override
        public MenuModel[] newArray(int size) {
            return new MenuModel[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    private int imgID;

    public int getDescription() {
        return Description;
    }

    public void setDescription(int description) {
        Description = description;
    }

    private int Description;

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
