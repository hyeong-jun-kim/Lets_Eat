package org.techtown.letseat.order;

import android.graphics.Bitmap;

public class Order_recycle_item {

    private Bitmap bitmap;
    private String name;


    public Order_recycle_item(Bitmap bitmap, String name) {

        this.bitmap = bitmap;
        this.name = name;

    }

    public Bitmap getBitmap() {return  bitmap;}
    public String getName() {
        return name;
    }


}

