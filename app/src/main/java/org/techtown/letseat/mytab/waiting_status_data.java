package org.techtown.letseat.mytab;

import org.techtown.letseat.R;
import org.techtown.letseat.restaurant.list.RestListRecycleItem;

import java.util.ArrayList;

public class waiting_status_data {

    ArrayList<RestListRecycleItem> items = new ArrayList<>();

    public ArrayList<RestListRecycleItem> getItems() {


        RestListRecycleItem rest2 = new RestListRecycleItem(R.drawable.image2,
                "한식", "뼈대있는가문", "경기도 용인시 기흥구 282-2");


        items.add(rest2);

        return items;
    }
}
