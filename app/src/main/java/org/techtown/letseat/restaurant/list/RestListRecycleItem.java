package org.techtown.letseat.restaurant.list;

import android.graphics.Bitmap;

public class RestListRecycleItem{

    private Bitmap bitmap;
    private String genre;
    private String name;
    private float rate;

    public RestListRecycleItem(Bitmap bitmap, String genre, String name,float rate) {

        this.bitmap = bitmap;
        this.genre = genre;
        this.name = name;
        this.rate = rate;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getGenre() {
        return genre;
    }

    public String getName() {
        return name;
    }

    public float getRate() {
        return rate;
    }
}
