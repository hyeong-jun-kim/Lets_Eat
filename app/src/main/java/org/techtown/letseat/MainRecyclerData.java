package org.techtown.letseat;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class MainRecyclerData extends ArrayList<MainRecyclerData> {
    private String restNameTv;
    private Bitmap bitmap;
    private Double differ;

    public MainRecyclerData(Double differ, String restNameTv, Bitmap bitmap) {
        this.restNameTv = restNameTv;
        this.bitmap = bitmap;
        this.differ = differ;
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

    public Double getDiffer() {
        return differ;
    }

}
