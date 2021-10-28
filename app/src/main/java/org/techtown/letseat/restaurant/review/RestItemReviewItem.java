package org.techtown.letseat.restaurant.review;

import android.graphics.Bitmap;

public class RestItemReviewItem {

    private String email;
    private String content;
    private float rate;
    private Bitmap bitmap;
    private String date;

    public RestItemReviewItem(String email, float rate, String content, Bitmap bitmap,String date){

        this.email = email;
        this.rate = rate;
        this.content = content;
        this.bitmap = bitmap;
        this.date = date;

    }

    public String getEmail() {
        return email;
    }

    public float getRate() {
        return rate;
    }

    public String getContent() {
        return content;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getDate() {return date;}
}

