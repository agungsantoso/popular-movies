package com.agungsantoso.udacity.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by agung.santoso on 10/08/2017.
 */

public class MovieParcel
    implements Parcelable {

    //http://www.vogella.com/tutorials/AndroidParcelable/article.html
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieParcel createFromParcel(Parcel in) {
            return new MovieParcel(in);
        }

        public MovieParcel[] newArray(int size) {
            return new MovieParcel[size];
        }
    };

    String id;
    String title;
    String poster;
    String release_date;
    Integer vote_count;
    Double vote_average;
    String plot;

    public MovieParcel(
            String id,
            String title,
            String poster,
            String release_date,
            Integer vote_count,
            Double vote_average,
            String plot) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.release_date = release_date;
        this.vote_count = vote_count;
        this.vote_average = vote_average;
        this.plot = plot;
    }

    public String getId() { return id; }

    public void setId() { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public void setReleaseDate(String release_date) {
        this.release_date = release_date;
    }

    public Integer getVoteCount() {
        return vote_count;
    }

    public void setVoteCount(Integer vote_count) {
        this.vote_count = vote_count;
    }

    public Double getVoteAverage() {
        return vote_average;
    }

    public void setVoteAverage(Double vote_average) {
        this.vote_average = vote_average;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public MovieParcel(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.poster = in.readString();
        this.release_date = in.readString();
        this.vote_count = in.readInt();
        this.vote_average = in.readDouble();
        this.plot = in.readString();
    }

    @Override
    public void writeToParcel(
            Parcel dest,
            int flags
    ) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.poster);
        dest.writeString(this.release_date);
        dest.writeInt(this.vote_count);
        dest.writeDouble(this.vote_average);
        dest.writeString(this.plot);
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
