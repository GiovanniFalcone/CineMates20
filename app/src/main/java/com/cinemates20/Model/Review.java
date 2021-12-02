package com.cinemates20.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Review implements Parcelable {

    private String textReview, author, titleMovie, idReview;
    private Date dateAndTime;
    private boolean isInappropriate;
    private int idMovie, totalValuation, counterForSpoiler, counterForLanguage;
    private float movieValuation, rating;

    public Review() {}

    public Review(String textReview, String author, String titleMovie, String idReview,
                  Date dateAndTime, int idMovie, float movieValuation, float rating,
                  int totalValuation, int counterForSpoiler, int counterForLanguage,
                  boolean isInappropriate) {
        this.textReview = textReview;
        this.author = author;
        this.titleMovie = titleMovie;
        this.idReview = idReview;
        this.dateAndTime = dateAndTime;
        this.idMovie = idMovie;
        this.movieValuation = movieValuation;
        this.rating = rating;
        this.totalValuation = totalValuation;
        this.counterForSpoiler = counterForSpoiler;
        this.counterForLanguage = counterForLanguage;
        this.isInappropriate = isInappropriate;
    }

    public Review(Parcel parcel) {
        textReview = parcel.readString();
        author = parcel.readString();
        titleMovie = parcel.readString();
        idReview = parcel.readString();
        long tmpDate = parcel.readLong();
        dateAndTime = tmpDate == -1 ? null : new Date(tmpDate);
        idMovie = parcel.readInt();
        movieValuation = parcel.readFloat();
        rating = parcel.readFloat();
        totalValuation = parcel.readInt();;
        counterForSpoiler = parcel.readInt();;
        counterForLanguage = parcel.readInt();;
        isInappropriate = parcel.readByte() != 0;
    }

    public String getTextReview() {
        return textReview;
    }

    public void setTextReview(String textReview) {
        this.textReview = textReview;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitleMovie() {
        return titleMovie;
    }

    public void setTitleMovie(String titleMovie) {
        this.titleMovie = titleMovie;
    }

    public String getIdReview() {
        return idReview;
    }

    public void setIdReview(String idReview) {
        this.idReview = idReview;
    }

    public Date getDateAndTime() {
        return (Date)dateAndTime.clone();
    }

    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }

    public int getTotalValuation() {
        return totalValuation;
    }

    public void setTotalValuation(int totalValuation) {
        this.totalValuation = totalValuation;
    }

    public float getMovieValuation() {
        return movieValuation;
    }

    public void setMovieValuation(float movieValuation) {
        this.movieValuation = movieValuation;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getCounterForSpoiler() {
        return counterForSpoiler;
    }

    public void setCounterForSpoiler(int counterForSpoiler) {
        this.counterForSpoiler = counterForSpoiler;
    }

    public int getCounterForLanguage() {
        return counterForLanguage;
    }

    public void setCounterForLanguage(int counterForLanguage) {
        this.counterForLanguage = counterForLanguage;
    }

    public boolean isIsInappropriate() {
        return isInappropriate;
    }

    public void setIsInappropriate(boolean isInappropriate) {
        this.isInappropriate = isInappropriate;
    }

    @Override
    public String toString() {
        return "Review{" +
                "textReview='" + textReview + '\'' +
                ", author='" + author + '\'' +
                ", titleMovie='" + titleMovie + '\'' +
                ", idReview='" + idReview + '\'' +
                ", dateAndTime=" + dateAndTime + '\'' +
                ", idMovie=" + idMovie + '\'' +
                ", movieValuation=" + movieValuation + '\'' +
                ", rating=" + rating + '\'' +
                ", totalValuation=" + totalValuation + '\'' +
                ", counterForSpoiler=" + counterForSpoiler + '\'' +
                ", counterForLanguage=" + counterForLanguage + '\'' +
                ", isInappropriate=" + isInappropriate +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(textReview);
        parcel.writeString(author);
        parcel.writeString(titleMovie);
        parcel.writeString(idReview);
        parcel.writeLong(dateAndTime != null ? dateAndTime.getTime() : -1);
        parcel.writeInt(idMovie);
        parcel.writeFloat(movieValuation);
        parcel.writeFloat(rating);
        parcel.writeInt(totalValuation);
        parcel.writeInt(counterForSpoiler);
        parcel.writeInt(counterForLanguage);
        parcel.writeByte((byte) (isInappropriate ? 1 : 0));
    }

    public static final Parcelable.Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[0];
        }
    };
    
}
