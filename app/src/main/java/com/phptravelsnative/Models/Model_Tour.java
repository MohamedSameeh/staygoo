package com.phptravelsnative.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 14/11/2016.
 */

public class Model_Tour implements Parcelable {


    private int id;
    private String type;
    private String date;
    private String gusest;
    private int maxAdult;
    private int maxInflants;
    private String Currency;
    private String symbolCurrency;
    private String title;
    private int maxChild;
    private int perAdultPrice;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private int perChildPrice;
    private int perInfantPrice;


    public Model_Tour() {
    }

    protected Model_Tour(Parcel in) {
        id = in.readInt();
        type = in.readString();
        date = in.readString();
        gusest = in.readString();
        maxAdult = in.readInt();
        maxInflants = in.readInt();
        Currency = in.readString();
        symbolCurrency = in.readString();
        title = in.readString();
        maxChild = in.readInt();
        perAdultPrice = in.readInt();
        perChildPrice = in.readInt();
        perInfantPrice = in.readInt();
    }

    public static final Creator<Model_Tour> CREATOR = new Creator<Model_Tour>() {
        @Override
        public Model_Tour createFromParcel(Parcel in) {
            return new Model_Tour(in);
        }

        @Override
        public Model_Tour[] newArray(int size) {
            return new Model_Tour[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGusest() {
        return gusest;
    }

    public void setGusest(String gusest) {
        this.gusest = gusest;
    }

    public int getMaxAdult() {
        return maxAdult;
    }

    public void setMaxAdult(int maxAdult) {
        this.maxAdult = maxAdult;
    }

    public int getMaxInflants() {
        return maxInflants;
    }

    public void setMaxInflants(int maxInflants) {
        this.maxInflants = maxInflants;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getSymbolCurrency() {
        return symbolCurrency;
    }

    public void setSymbolCurrency(String symbolCurrency) {
        this.symbolCurrency = symbolCurrency;
    }

    public int getMaxChild() {
        return maxChild;
    }

    public void setMaxChild(int maxChild) {
        this.maxChild = maxChild;
    }

    public int getPerAdultPrice() {
        return perAdultPrice;
    }

    public void setPerAdultPrice(int perAdultPrice) {
        this.perAdultPrice = perAdultPrice;
    }

    public int getPerInfantPrice() {
        return perInfantPrice;
    }

    public void setPerInfantPrice(int perInfantPrice) {
        this.perInfantPrice = perInfantPrice;
    }

    public int getPerChildPrice() {
        return perChildPrice;
    }

    public void setPerChildPrice(int perChildPrice) {
        this.perChildPrice = perChildPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeString(date);
        dest.writeString(gusest);
        dest.writeInt(maxAdult);
        dest.writeInt(maxInflants);
        dest.writeString(Currency);
        dest.writeString(symbolCurrency);
        dest.writeString(title);
        dest.writeInt(maxChild);
        dest.writeInt(perAdultPrice);
        dest.writeInt(perChildPrice);
        dest.writeInt(perInfantPrice);
    }
}

