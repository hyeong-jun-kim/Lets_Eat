package org.techtown.letseat.mytab;

import org.techtown.letseat.R;
import org.techtown.letseat.restaurant.rest_recycle_item;

import java.util.ArrayList;

public class waiting_status_data {

    ArrayList<rest_recycle_item> items = new ArrayList<>();

    public ArrayList<rest_recycle_item> getItems() {


        rest_recycle_item rest2 = new rest_recycle_item(R.drawable.image2,
                "한식", "뼈대있는가문", "경기도 용인시 기흥구 282-2");


        items.add(rest2);

        return items;
    }
}
