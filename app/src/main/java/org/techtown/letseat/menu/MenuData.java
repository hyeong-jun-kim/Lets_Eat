package org.techtown.letseat.menu;

import android.os.Parcel;
import android.os.Parcelable;

public class MenuData implements Parcelable {

    public int resId;
    public String title;
    public String review;

    protected MenuData(Parcel in){
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
    public static final Creator<MenuData> CREATOR = new Creator<MenuData>() {
        @Override
        public MenuData createFromParcel(Parcel in) {
            return new MenuData(in);
        }
        @Override
        public MenuData[] newArray(int size) {
            return new MenuData[size];
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
