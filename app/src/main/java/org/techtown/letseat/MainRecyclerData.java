package org.techtown.letseat;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class MainRecyclerData extends ArrayList<MainRecyclerData> {
    private String restNameTv;
    private Bitmap bitmap;

    public MainRecyclerData(String restNameTv, Bitmap bitmap) {
        this.restNameTv = restNameTv;
        this.bitmap = bitmap;
    }

    public String getRestNameTv() {
        return restNameTv;
    }

    public void setRestNameTv(String restNameTv) {
        this.restNameTv = restNameTv;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

}
