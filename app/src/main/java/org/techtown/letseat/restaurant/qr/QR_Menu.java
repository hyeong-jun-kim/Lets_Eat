package org.techtown.letseat.restaurant.qr;

import android.graphics.Bitmap;

public class QR_Menu {
    private Bitmap bitmap;
    private String name;
    private String price;
    private String excription;
    public QR_Menu(Bitmap bitmap, String name, String price, String excription){
        this.bitmap = bitmap;
        this.name = name;
        this.price = price;
        this.excription = excription;
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
    public String getExcription(){return excription;}
    public void setExcription(String excription){this.excription = excription;}
}
