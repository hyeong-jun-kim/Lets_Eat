package org.techtown.letseat.order;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Orderdata {

    ArrayList<OrderItem> items = new ArrayList<>();
    OrderItem rest[] = new OrderItem[100];

    int p = 0;

    public ArrayList<OrderItem> getItems(ArrayList list) {

        for(int i = 0; i < list.size()/4; i++){
            rest[i+1] = new OrderItem((Bitmap) list.get(p),
                    list.get(p+1).toString(),list.get(p+2).toString(),list.get(p+3).toString());
            items.add(rest[i+1]);
            p += 4;
        }



        return items;
    }
}

