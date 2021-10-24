package org.techtown.letseat.order;

import android.graphics.Bitmap;

public class OrderItem {
    private int resId;
    private Bitmap bitmap;
    private String menuName;
    private String price;
    private String resName;
    private String orderTime;

    public OrderItem(int resId, Bitmap bitmap, String menuName, String price, String resName, String orderTime) {
        this.resId = resId;
        this.bitmap = bitmap;
        this.menuName = menuName;
        this.price = price;
        this.resName = resName;
        this.orderTime = orderTime;
    }
    public int getResId(){return resId;}
    public Bitmap getBitmap() {return  bitmap;}
    public String getMenuName() {
        return menuName;
    }
    public String getPrice() {
        return price;
    }
    public String getResName() {
        return resName;
    }
    public String getOrderTime(){return orderTime;}

}

