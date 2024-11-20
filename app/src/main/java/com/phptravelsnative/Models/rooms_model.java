package com.phptravelsnative.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 27/09/2016.
 */

public class rooms_model  implements Parcelable {

    private String title;
    private String id;
    private String stay;
    private String image;
    private String CurrencyCode;
    private String price;
    private int quantity;


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public rooms_model()
    {

    }

    protected rooms_model(Parcel in) {
        title = in.readString();
        id = in.readString();
        stay = in.readString();
        image = in.readString();
        CurrencyCode = in.readString();
        price = in.readString();
        quantity = in.readInt();
    }

    public static final Creator<rooms_model> CREATOR = new Creator<rooms_model>() {
        @Override
        public rooms_model createFromParcel(Parcel in) {
            return new rooms_model(in);
        }

        @Override
        public rooms_model[] newArray(int size) {
            return new rooms_model[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }




    public String getStay() {
        return stay;
    }

    public void setStay(String stay) {
        this.stay = stay;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(id);
        dest.writeString(stay);
        dest.writeString(image);
        dest.writeString(CurrencyCode);
        dest.writeString(price);
        dest.writeInt(quantity);
    }
}
