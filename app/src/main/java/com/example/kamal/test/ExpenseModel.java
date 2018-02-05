package com.example.kamal.test;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KAMAL on 2/5/2018.
 */

public class ExpenseModel implements Parcelable {

    private String ID;
    private String Title;
    private String Amount;
    private String date;
    private String Category;


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ID);
        dest.writeString(this.Title);
        dest.writeString(this.Amount);
        dest.writeString(this.date);
        dest.writeString(this.Category);
    }

    public ExpenseModel() {
    }

    protected ExpenseModel(Parcel in) {
        this.ID = in.readString();
        this.Title = in.readString();
        this.Amount = in.readString();
        this.date = in.readString();
        this.Category = in.readString();
    }

    public static final Parcelable.Creator<ExpenseModel> CREATOR = new Parcelable.Creator<ExpenseModel>() {
        @Override
        public ExpenseModel createFromParcel(Parcel source) {
            return new ExpenseModel(source);
        }

        @Override
        public ExpenseModel[] newArray(int size) {
            return new ExpenseModel[size];
        }
    };
}
