package org.techtown.letseat.photo;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {

    public int resId;
    public String title;
    public String review;
    public Photo(){}
    protected Photo(Parcel in){
        resId = in.readInt();
        title = in.readString();
        review = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(resId);
        dest.writeString(title);
        dest.writeString(review);
    }
    @Override
    public int describeContents(){
        return 0;
    }
    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }
        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
    public void setresId(int id){
        resId = id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setReview(String review){
        this.review = review;
    }
    public int getResId(){
        return resId;
    }
    public String getTitle(){
        return title;
    }
    public String getReview(){
        return review;
    }
}
