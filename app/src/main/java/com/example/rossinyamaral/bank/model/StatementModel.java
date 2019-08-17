package com.example.rossinyamaral.bank.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rossinyamaral on 08/12/18.
 */

public class StatementModel implements Parcelable {

    private String title;
    private String desc;
    private String date;
    private double value;

    public StatementModel() {}

    private StatementModel(Parcel in) {
        title = in.readString();
        desc = in.readString();
        date = in.readString();
        value = in.readDouble();
    }

    public static final Creator<StatementModel> CREATOR = new Creator<StatementModel>() {
        @Override
        public StatementModel createFromParcel(Parcel in) {
            return new StatementModel(in);
        }

        @Override
        public StatementModel[] newArray(int size) {
            return new StatementModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeString(date);
        dest.writeDouble(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
