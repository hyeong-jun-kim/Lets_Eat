package org.techtown.letseat.order;

import android.graphics.Bitmap;

import org.techtown.letseat.R;
import org.techtown.letseat.restaurant.list.RestListRecycleItem;

import java.util.ArrayList;

public class Orderdata {

    ArrayList<Order_recycle_item> items = new ArrayList<>();
    Order_recycle_item rest[] = new Order_recycle_item[100];

    int p = 0;

    public ArrayList<Order_recycle_item> getItems(ArrayList list) {

        for(int i = 0; i < list.size()/2; i++){
            rest[i+1] = new Order_recycle_item((Bitmap) list.get(p),
                    list.get(p+1).toString());
            items.add(rest[i+1]);
            p += 2;
        }



        return items;
    }
}

