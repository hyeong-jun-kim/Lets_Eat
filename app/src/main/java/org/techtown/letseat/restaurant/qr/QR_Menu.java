package org.techtown.letseat.restaurant.qr;

import android.graphics.Bitmap;

public class QR_Menu {
    private Bitmap bitmap;
    private String name;
    private String price;

    public QR_Menu(Bitmap bitmap, String name, String price){
        this.bitmap = bitmap;
        this.name = name;
        this.price = price;

    }
    public Bitmap getBitmap(){
        return bitmap;
    }
    public void setBitmap(){
        this.bitmap = bitmap;
    }
    public String getPrice(){
        return price;
    }
    public void setPrice(String price){
        this.price = price;
    }
    public String getName(){
        return name;
    }
    public void setResId(int resId){
        this.name = name;
    }
}
