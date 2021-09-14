package org.techtown.letseat;

import org.techtown.letseat.restaurant.rest_recycle_item;

import java.util.ArrayList;

public class data {

    ArrayList<rest_recycle_item> items = new ArrayList<>();

    public ArrayList<rest_recycle_item> getItems() {

        rest_recycle_item rest1 = new rest_recycle_item(R.drawable.image1,
                "중식", "하오탕", "경기도 용인시 수지구 147-2");

        rest_recycle_item rest2 = new rest_recycle_item(R.drawable.image2,
                "한식", "뼈대있는가문", "경기도 용인시 기흥구 282-2");

        rest_recycle_item rest3 = new rest_recycle_item(R.drawable.image3,
                "한식", "곱순네 순대국", "경기도 성남시 수정구 182-1");

        items.add(rest1);
        items.add(rest2);
        items.add(rest3);

        return items;
    }
}
