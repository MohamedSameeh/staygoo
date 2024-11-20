package com.phptravelsnative.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 19/09/2016.
 */

public class review_model implements Parcelable{


    private  String rating;
    private  String review_by;
    private  String review_comment;
    private  String review_date;

    public review_model() {
    }

    protected review_model(Parcel in) {
        rating = in.readString();
        review_by = in.readString();
        review_comment = in.readString();
        review_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rating);
        dest.writeString(review_by);
        dest.writeString(review_comment);
        dest.writeString(review_date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<review_model> CREATOR = new Creator<review_model>() {
        @Override
        public review_model createFromParcel(Parcel in) {
            return new review_model(in);
        }

        @Override
        public review_model[] newArray(int size) {
            return new review_model[size];
        }
    };

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview_by() {
        return review_by;
    }

    public void setReview_by(String review_by) {
        this.review_by = review_by;
    }

    public String getReview_comment() {
        return review_comment;
    }

    public void setReview_comment(String review_comment) {
        this.review_comment = review_comment;
    }

    public String getReview_date() {
        return review_date;
    }

    public void setReview_date(String review_date) {
        this.review_date = review_date;
    }
}
