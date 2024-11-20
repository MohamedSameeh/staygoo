package com.phptravelsnative.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 09/11/2016.
 */

public class car_model implements Parcelable {

    private int pickupId;
    private int dropOfId;
    private String pickupTime;
    private String dropOfTime;
    private String pickupDate;
    private String dropOfDate;
    private String totalPrice;
    private String DepositePrice;
    private String taxPrice;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected car_model(Parcel in) {
        pickupId = in.readInt();
        dropOfId = in.readInt();
        id = in.readInt();
        pickupTime = in.readString();
        dropOfTime = in.readString();
        pickupDate = in.readString();
        dropOfDate = in.readString();
        taxPrice = in.readString();
        DepositePrice = in.readString();
        totalPrice = in.readString();
    }
    public car_model()
    {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pickupId);
        dest.writeInt(dropOfId);
        dest.writeInt(id);
        dest.writeString(pickupTime);
        dest.writeString(dropOfTime);
        dest.writeString(pickupDate);
        dest.writeString(dropOfDate);
        dest.writeString(taxPrice);
        dest.writeString(DepositePrice);
        dest.writeString(totalPrice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<car_model> CREATOR = new Creator<car_model>() {
        @Override
        public car_model createFromParcel(Parcel in) {
            return new car_model(in);
        }

        @Override
        public car_model[] newArray(int size) {
            return new car_model[size];
        }
    };

    public int getPickupId() {
        return pickupId;
    }
    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDepositePrice() {
        return DepositePrice;
    }

    public void setDepositePrice(String depositePrice) {
        DepositePrice = depositePrice;
    }

    public String getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(String taxPrice) {
        this.taxPrice = taxPrice;
    }

    public void setPickupId(int pickupId) {
        this.pickupId = pickupId;
    }

    public int getDropOfId() {
        return dropOfId;
    }

    public void setDropOfId(int dropOfId) {
        this.dropOfId = dropOfId;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getDropOfTime() {
        return dropOfTime;
    }

    public void setDropOfTime(String dropOfTime) {
        this.dropOfTime = dropOfTime;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getDropOfDate() {
        return dropOfDate;
    }

    public void setDropOfDate(String dropOfDate) {
        this.dropOfDate = dropOfDate;
    }
}
