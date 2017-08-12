package com.agungsantoso.udacity.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by agung.santoso on 10/08/2017.
 */

public class VideoParcel
        implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public VideoParcel createFromParcel(Parcel in) {
            return new VideoParcel(in);
        }

        public VideoParcel[] newArray(int size) {
            return new VideoParcel[size];
        }
    };

    String name;
    String key;
    String site;

    public VideoParcel(
            String key,
            String name,
            String site) {
        this.name = name;
        this.key = key;
        this.site = site;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public VideoParcel(Parcel in) {
        this.name = in.readString();
        this.key = in.readString();
        this.site = in.readString();
    }

    @Override
    public void writeToParcel(
            Parcel dest,
            int flags
    ) {
        dest.writeString(this.name);
        dest.writeString(this.key);
        dest.writeString(this.site);
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
