package com.phptravelsnative.Models;

import android.os.Parcel;
import android.os.Parcelable;


public class OverView implements Parcelable {

    private int id;

    private String parmentsRights;
    private Double latitude;
    private String chlid;
    private String adult;
    private String title;
    private String paymentsLefts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getChlid() {
        return chlid;
    }

    public void setChlid(String chlid) {
        this.chlid = chlid;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getSessionId() {

        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    private Double longitude;
    private String sessionId;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String desc;
    public OverView()
    {

    }
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    private  String policy;



    protected OverView(Parcel in) {
        paymentsLefts = in.readString();
        parmentsRights = in.readString();
        policy = in.readString();
        desc = in.readString();
        latitude=in.readDouble();
        longitude=in.readDouble();
        id=in.readInt();
        chlid=in.readString();
        adult=in.readString();
        sessionId=in.readString();
        title=in.readString();
    }

    public static final Creator<OverView> CREATOR = new Creator<OverView>() {
        @Override
        public OverView createFromParcel(Parcel in) {
            return new OverView(in);
        }

        @Override
        public OverView[] newArray(int size) {
            return new OverView[size];
        }
    };

    public String getPaymentsLefts() {
        return paymentsLefts;
    }

    public void setPaymentsLefts(String paymentsLefts) {
        this.paymentsLefts = paymentsLefts;
    }

    public String getParmentsRights() {
        return parmentsRights;
    }

    public void setParmentsRights(String parmentsRights) {
        this.parmentsRights = parmentsRights;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(paymentsLefts);
        dest.writeString(parmentsRights);
        dest.writeString(policy);
        dest.writeString(desc);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(id);
        dest.writeString(chlid);
        dest.writeString(adult);
        dest.writeString(sessionId);
        dest.writeString(title);
    }
}
