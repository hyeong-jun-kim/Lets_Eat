package org.techtown.letseat.order;

import android.graphics.Bitmap;

public class OrderItem2 {

    private Bitmap bitmap;
    private String menuName;
    private String price;
    private String resName;
    private String orderTime;

    public OrderItem2(Bitmap bitmap, String menuName, String price, String resName, String orderTime) {
        this.bitmap = bitmap;
        this.menuName = menuName;
        this.price = price;
        this.resName = resName;
        this.orderTime = orderTime;
    }

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

