
package com.toby.eziketobenna.popularmovies.model.Movie;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Movie class to create movie objects
 */
@Entity(tableName = "movie")
public class Movie implements Parcelable {

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    private int roomId;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("poster_path")
    private String movieImagePath;
    @Expose
    private String overview;
    @SerializedName("vote_average")
    private Double voteAverage;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("id")
    private Integer id;

    @Ignore
    public Movie(String originalTitle, String movieImagePath, String overview, Double voteAverage, String releaseDate, String backdropPath, Integer id) {
        this.originalTitle = originalTitle;
        this.movieImagePath = movieImagePath;
        this.overview = overview;

        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.backdropPath = backdropPath;
        this.id = id;
    }

    @Ignore
    public Movie() {

    }

    public Movie(int roomId, String originalTitle, String movieImagePath, String overview, Double voteAverage, String releaseDate, String backdropPath, Integer id) {
        this.roomId = roomId;
        this.originalTitle = originalTitle;
        this.movieImagePath = movieImagePath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.backdropPath = backdropPath;
        this.id = id;
    }

    private Movie(Parcel in) {
        roomId = in.readInt();
        originalTitle = in.readString();
        movieImagePath = in.readString();
        overview = in.readString();
        if (in.readByte() == 0) {
            voteAverage = null;
        } else {
            voteAverage = in.readDouble();
        }
        releaseDate = in.readString();
        backdropPath = in.readString();
        id = in.readInt();
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getMovieImagePath() {
        return movieImagePath;
    }

    public void setMovieImagePath(String movieImagePath) {
        this.movieImagePath = movieImagePath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(roomId);
        dest.writeString(originalTitle);
        dest.writeString(movieImagePath);
        dest.writeString(overview);
        if (voteAverage == null) {
            dest.writeByte((byte) 0);
        } else {

            dest.writeByte((byte) 1);
            dest.writeDouble(voteAverage);
        }
        dest.writeString(releaseDate);
        dest.writeString(backdropPath);
        dest.writeInt(id);
    }
}
