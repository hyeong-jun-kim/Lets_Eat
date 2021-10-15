package org.techtown.letseat.restaurant.list;

import android.graphics.Bitmap;

import org.techtown.letseat.R;

import java.util.ArrayList;

public class RestListData {


    ArrayList<RestListRecycleItem> items = new ArrayList<>();

    RestListRecycleItem rest[] = new RestListRecycleItem[100];


    int p = 0;

    public ArrayList<RestListRecycleItem> getItems(ArrayList list) {



        for(int i = 0; i < list.size()/3; i++){
            rest[i+1] = new RestListRecycleItem((Bitmap) list.get(p),
                    list.get(p+1).toString(), (String) list.get(p+2) );
            items.add(rest[i+1]);
            p += 3;
        }




        return items;
    }


}

