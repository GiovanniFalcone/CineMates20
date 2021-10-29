package com.cinemates20.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Review implements Parcelable {
    private String textReview, author, titleMovie, idReview;
    private Date dateAndTime;
    private boolean isInappropriate, hasCommentWithSpoiler;
    private int totalLike, totalDislike, totalLove, totalClap, totalGrrr, idMovie, totalValuation, counterForSpoiler, counterForLanguage;
    private float movieValuation, rating;

    public Review() {}

    public Review(String textReview, String author, String titleMovie, String idReview, Date dateAndTime, int totalLike, int totalDislike, int totalLove, int totalClap, int totalGrrr, int idMovie, float movieValuation, float rating, int totalValuation, int counterForSpoiler, int counterForLanguage, boolean isInappropriate, boolean hasCommentWithSpoiler) {
        this.textReview = textReview;
        this.author = author;
        this.titleMovie = titleMovie;
        this.idReview = idReview;
        this.dateAndTime = dateAndTime;
        this.totalLike = totalLike;
        this.totalDislike = totalDislike;
        this.totalLove = totalLove;
        this.totalClap = totalClap;
        this.totalGrrr = totalGrrr;
        this.idMovie = idMovie;
        this.movieValuation = movieValuation;
        this.rating = rating;
        this.totalValuation = totalValuation;
        this.counterForSpoiler = counterForSpoiler;
        this.counterForLanguage = counterForLanguage;
        this.isInappropriate = isInappropriate;
        this.hasCommentWithSpoiler = hasCommentWithSpoiler;
    }

    public Review(Parcel parcel) {
        textReview = parcel.readString();
        author = parcel.readString();
        titleMovie = parcel.readString();
        idReview = parcel.readString();
        long tmpDate = parcel.readLong();
        dateAndTime = tmpDate == -1 ? null : new Date(tmpDate);
        totalLike = parcel.readInt();
        totalDislike = parcel.readInt();
        totalLove = parcel.readInt();
        totalClap = parcel.readInt();
        totalGrrr = parcel.readInt();
        idMovie = parcel.readInt();
        movieValuation = parcel.readFloat();
        rating = parcel.readFloat();
        totalValuation = parcel.readInt();;
        counterForSpoiler = parcel.readInt();;
        counterForLanguage = parcel.readInt();;
        isInappropriate = parcel.readByte() != 0;
        hasCommentWithSpoiler = parcel.readByte() != 0;
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

    public int getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public int getTotalDislike() {
        return totalDislike;
    }

    public void setTotalDislike(int totalDislike) {
        this.totalDislike = totalDislike;
    }

    public int getTotalLove() {
        return totalLove;
    }

    public void setTotalLove(int totalLove) {
        this.totalLove = totalLove;
    }

    public int getTotalClap() {
        return totalClap;
    }

    public void setTotalClap(int totalClap) {
        this.totalClap = totalClap;
    }

    public int getTotalGrrr() {
        return totalGrrr;
    }

    public void setTotalGrrr(int totalGrrr) {
        this.totalGrrr = totalGrrr;
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

    public boolean isHasCommentWithSpoiler() {
        return hasCommentWithSpoiler;
    }

    public void setHasCommentWithSpoiler(boolean hasCommentWithSpoiler) {
        this.hasCommentWithSpoiler = hasCommentWithSpoiler;
    }

    @Override
    public String toString() {
        return "Review{" +
                "textReview='" + textReview + '\'' +
                ", author='" + author + '\'' +
                ", titleMovie='" + titleMovie + '\'' +
                ", idReview='" + idReview + '\'' +
                ", dateAndTime=" + dateAndTime + '\'' +
                ", totalLike=" + totalLike + '\'' +
                ", totalDislike=" + totalDislike + '\'' +
                ", totalLove=" + totalLove + '\'' +
                ", totalClap=" + totalClap + '\'' +
                ", totalGrrr=" + totalGrrr + '\'' +
                ", idMovie=" + idMovie + '\'' +
                ", movieValuation=" + movieValuation + '\'' +
                ", rating=" + rating + '\'' +
                ", totalValuation=" + totalValuation + '\'' +
                ", counterForSpoiler=" + counterForSpoiler + '\'' +
                ", counterForLanguage=" + counterForLanguage + '\'' +
                ", isInappropriate=" + isInappropriate + '\'' +
                ", hasCommentWithSpoiler=" + hasCommentWithSpoiler +
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
        parcel.writeInt(totalLike);
        parcel.writeInt(totalDislike);
        parcel.writeInt(totalLove);
        parcel.writeInt(totalClap);
        parcel.writeInt(totalGrrr);
        parcel.writeInt(idMovie);
        parcel.writeFloat(movieValuation);
        parcel.writeFloat(rating);
        parcel.writeInt(totalValuation);
        parcel.writeInt(counterForSpoiler);
        parcel.writeInt(counterForLanguage);
        parcel.writeByte((byte) (isInappropriate ? 1 : 0));
        parcel.writeByte((byte) (hasCommentWithSpoiler ? 1 : 0));
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
