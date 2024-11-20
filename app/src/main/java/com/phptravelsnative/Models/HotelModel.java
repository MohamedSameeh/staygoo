package com.phptravelsnative.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hamza on 8/4/2016.
 */
public class HotelModel implements Parcelable {


    private String price;
    private String title;
    private String thumbnail;
    private int starsCount;
    private String currCode;
    private String currSymbol;
    private String location;
    private String rating;
    private String type;
    private int id;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String roomTypeCode;
    public String rateKey;
    public String rateCode;


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    protected HotelModel(Parcel in) {
        price = in.readString();
        title = in.readString();
        thumbnail = in.readString();
        starsCount = in.readInt();
        currCode = in.readString();
        currSymbol = in.readString();
        id = in.readInt();
        roomTypeCode = in.readString();
        rateKey = in.readString();
        rateCode = in.readString();
        rating = in.readString();
        location = in.readString();
        type = in.readString();
    }
    public HotelModel()
    {

    }

    public static final Creator<HotelModel> CREATOR = new Creator<HotelModel>() {
        @Override
        public HotelModel createFromParcel(Parcel in) {
            return new HotelModel(in);
        }

        @Override
        public HotelModel[] newArray(int size) {
            return new HotelModel[size];
        }
    };

    public String getRoomTypeCode() {
        return roomTypeCode;
    }

    public void setRoomTypeCode(String roomTypeCode) {
        this.roomTypeCode = roomTypeCode;
    }

    public String getRateKey() {
        return rateKey;
    }

    public void setRateKey(String rateKey) {
        this.rateKey = rateKey;
    }

    public String getRateCode() {
        return rateCode;
    }

    public void setRateCode(String rateCode) {
        this.rateCode = rateCode;
    }

    public String getcurrSymbol() {
        return currSymbol;
    }

    public void setid(int id) {

        this.id = id;
    }
    public int getid() {
        return id;
    }


    public void setcurrSymbol(String currSymbol) {

        this.currSymbol = currSymbol;
    }
    public String getName() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getStarsCount() {
        return starsCount;
    }


    public void setStarsCount(int starsCount) {
        this.starsCount = starsCount;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setcurrCode(String currCode) {
        this.currCode = currCode;
    } public String getcurrCode() {
        return currCode;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(price);
        dest.writeString(title);
        dest.writeString(thumbnail);

        dest.writeInt(starsCount);
        dest.writeString(currCode);
        dest.writeString(currSymbol);
        dest.writeInt(id);
        dest.writeString(roomTypeCode);
        dest.writeString(rateKey);
        dest.writeString(rateCode);
        dest.writeString(location);
        dest.writeString(rating);
        dest.writeString(type);
    }
}
