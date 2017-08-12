package com.agungsantoso.udacity.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by agung.santoso on 10/08/2017.
 */

public class ReviewParcel
        implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ReviewParcel createFromParcel(Parcel in) {
            return new ReviewParcel(in);
        }

        public ReviewParcel[] newArray(int size) {
            return new ReviewParcel[size];
        }
    };

    String author;
    String review;

    public ReviewParcel(
            String author,
            String review) {
        this.author = author;
        this.review = review;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String name) {
        this.author = author;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String key) {
        this.review = review;
    }

    public ReviewParcel(Parcel in) {
        this.author = in.readString();
        this.review = in.readString();
    }

    @Override
    public void writeToParcel(
            Parcel dest,
            int flags
    ) {
        dest.writeString(this.author);
        dest.writeString(this.review);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "parcel";
    }
}
