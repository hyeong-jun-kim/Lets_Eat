package org.techtown.letseat;

import java.util.ArrayList;

public class Orderdata2 {

    ArrayList<Order_recycle_item> items = new ArrayList<>();

    public ArrayList<Order_recycle_item> getItems() {

        Order_recycle_item order4 = new Order_recycle_item(R.drawable.image1,
                "9000원", "하오탕", "마라탕","배달중");

        Order_recycle_item order5 = new Order_recycle_item(R.drawable.image2,
                "8000원", "뼈대있는가문", "뼈해장국","배달중");

        Order_recycle_item order6 = new Order_recycle_item(R.drawable.image3,
                "6000원", "곱순네 순대국", "순대국","배달중");

        items.add(order4);
        items.add(order5);
        items.add(order6);
        return items;
    }
}

