package com.phptravelsnative.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 02/09/2016.
 */

public class DetailModel implements Parcelable {

    private String sliderImages;

    private  String rating;
    private  String review_by;
    private  String review_comment;
    private  String review_date;


    protected DetailModel(Parcel in) {
        sliderImages = in.readString();
        rating = in.readString();
        review_by = in.readString();
        review_comment = in.readString();
        review_date = in.readString();
    }
    public DetailModel() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sliderImages);
        dest.writeString(rating);
        dest.writeString(review_by);
        dest.writeString(review_comment);
        dest.writeString(review_date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DetailModel> CREATOR = new Creator<DetailModel>() {
        @Override
        public DetailModel createFromParcel(Parcel in) {
            return new DetailModel(in);
        }

        @Override
        public DetailModel[] newArray(int size) {
            return new DetailModel[size];
        }
    };
    public String getSliderImages() {
        return sliderImages;
    }

    public void setSliderImages(String sliderImages) {
        this.sliderImages = sliderImages;
    }
}
