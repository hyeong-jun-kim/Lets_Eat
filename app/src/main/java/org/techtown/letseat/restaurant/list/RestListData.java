package org.techtown.letseat.restaurant.list;

import org.techtown.letseat.R;

import java.util.ArrayList;

public class RestListData {


    ArrayList<RestListRecycleItem> items = new ArrayList<>();

    RestListRecycleItem rest[] = new RestListRecycleItem[100];

    ArrayList<Integer> imageViews = new ArrayList<Integer>();

    int p = 0;

    public ArrayList<RestListRecycleItem> getItems(ArrayList list) {



        for(int i = 0; i < list.size()/3; i++){
            imageViews.add(R.drawable.store_sample);
            rest[i+1] = new RestListRecycleItem(imageViews.get(i),
                    list.get(p).toString(), (String) list.get(p+1), (String) list.get(p+2));
            items.add(rest[i+1]);
            p += 3;
        }




        return items;
    }


}

