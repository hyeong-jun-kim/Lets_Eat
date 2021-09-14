package org.techtown.letseat;

import java.util.ArrayList;

public class Orderdata {

    ArrayList<Order_recycle_item> items = new ArrayList<>();

    public ArrayList<Order_recycle_item> getItems() {

        Order_recycle_item order1 = new Order_recycle_item(R.drawable.image1,
                "9000원", "하오탕", "마라탕","배달완료");

        Order_recycle_item order2 = new Order_recycle_item(R.drawable.image2,
                "8000원", "뼈대있는가문", "뼈해장국","배달완료");

        Order_recycle_item order3 = new Order_recycle_item(R.drawable.image3,
                "6000원", "곱순네 순대국", "순대국","배달완료");

        items.add(order1);
        items.add(order2);
        items.add(order3);

        return items;
    }
}

