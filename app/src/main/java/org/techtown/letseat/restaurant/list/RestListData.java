package org.techtown.letseat.restaurant.list;

import org.techtown.letseat.R;

import java.util.ArrayList;

public class RestListData {

    ArrayList<RestListRecycleItem> items = new ArrayList<>();

    public ArrayList<RestListRecycleItem> getItems() {

        RestListRecycleItem rest1 = new RestListRecycleItem(R.drawable.image1,
                "중식", "하오탕", "경기도 용인시 수지구 147-2");

        RestListRecycleItem rest2 = new RestListRecycleItem(R.drawable.image2,
                "한식", "뼈대있는가문", "경기도 용인시 기흥구 282-2");

        RestListRecycleItem rest3 = new RestListRecycleItem(R.drawable.image3,
                "한식", "곱순네 순대국", "경기도 성남시 수정구 182-1");

        items.add(rest1);
        items.add(rest2);
        items.add(rest3);



        return items;
    }

}
