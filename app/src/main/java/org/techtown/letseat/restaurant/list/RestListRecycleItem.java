package org.techtown.letseat.restaurant.list;

import android.graphics.Bitmap;

public class RestListRecycleItem{

    private Bitmap bitmap;
    private String genre;
    private String name;

    public RestListRecycleItem(Bitmap bitmap, String genre, String name) {

        this.bitmap = bitmap;
        this.genre = genre;
        this.name = name;

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

}
