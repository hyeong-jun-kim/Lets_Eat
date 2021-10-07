package org.techtown.letseat.restaurant.list;

import android.graphics.Bitmap;

public class RestListRecycleItem{

    private Bitmap bitmap;
    private String genre;
    private String name;
    private String adress;

    public RestListRecycleItem(Bitmap bitmap, String genre, String name, String adress) {

        this.bitmap = bitmap;
        this.genre = genre;
        this.name = name;
        this.adress = adress;

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

    public String getAdress() {
        return adress;
    }
}
