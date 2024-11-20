package com.phptravelsnative.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by apple on 21/10/2016.
 */

public class ExpediaExtra extends ArrayList<Parcelable> implements Parcelable {

    public String roomTypeCode;
    public String rateKey;
    public String rateCode;
    public String sessionId;
    public String first_name;
    public String last_name;
    public String input_email;
    public String input_address;
    public String input_phone;
    public String input_city;
    public String input_postal_code;
    public String input_state;
    public String input_country;
    public String input_card_number;
    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String total_price;
    public String expairy;
    public String input_card_type;
    public String input_card_cvw;

    protected ExpediaExtra(Parcel in) {
        roomTypeCode = in.readString();
        rateKey = in.readString();
        rateCode = in.readString();
        sessionId = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        input_email = in.readString();
        input_address = in.readString();
        input_phone = in.readString();
        input_city = in.readString();
        input_postal_code = in.readString();
        input_state = in.readString();
        input_country = in.readString();
        input_card_number = in.readString();
        expairy = in.readString();
        input_card_type = in.readString();
        input_card_cvw = in.readString();
        total_price = in.readString();
    }

    public ExpediaExtra()
    {

    }
    public static final Creator<ExpediaExtra> CREATOR = new Creator<ExpediaExtra>() {
        @Override
        public ExpediaExtra createFromParcel(Parcel in) {
            return new ExpediaExtra(in);
        }

        @Override
        public ExpediaExtra[] newArray(int size) {
            return new ExpediaExtra[size];
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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getInput_email() {
        return input_email;
    }

    public void setInput_email(String input_email) {
        this.input_email = input_email;
    }

    public String getInput_address() {
        return input_address;
    }

    public void setInput_address(String input_address) {
        this.input_address = input_address;
    }

    public String getInput_phone() {
        return input_phone;
    }

    public void setInput_phone(String input_phone) {
        this.input_phone = input_phone;
    }

    public String getInput_city() {
        return input_city;
    }

    public void setInput_city(String input_city) {
        this.input_city = input_city;
    }

    public String getInput_postal_code() {
        return input_postal_code;
    }

    public void setInput_postal_code(String input_postal_code) {
        this.input_postal_code = input_postal_code;
    }

    public String getInput_state() {
        return input_state;
    }

    public void setInput_state(String input_state) {
        this.input_state = input_state;
    }

    public String getInput_country() {
        return input_country;
    }

    public void setInput_country(String input_country) {
        this.input_country = input_country;
    }

    public String getInput_card_number() {
        return input_card_number;
    }

    public void setInput_card_number(String input_card_number) {
        this.input_card_number = input_card_number;
    }

    public String getExpairy() {
        return expairy;
    }

    public void setExpairy(String expairy) {
        this.expairy = expairy;
    }

    public String getInput_card_type() {
        return input_card_type;
    }

    public void setInput_card_type(String input_card_type) {
        this.input_card_type = input_card_type;
    }

    public String getInput_card_cvw() {
        return input_card_cvw;
    }

    public void setInput_card_cvw(String input_card_cvw) {
        this.input_card_cvw = input_card_cvw;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(roomTypeCode);
        dest.writeString(rateKey);
        dest.writeString(rateCode);
        dest.writeString(sessionId);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(input_email);
        dest.writeString(input_address);
        dest.writeString(input_phone);
        dest.writeString(input_city);
        dest.writeString(input_postal_code);
        dest.writeString(input_state);
        dest.writeString(input_country);
        dest.writeString(input_card_number);
        dest.writeString(expairy);
        dest.writeString(input_card_type);
        dest.writeString(input_card_cvw);
        dest.writeString(total_price);
    }
}
