package org.techtown.letseat;

import java.util.ArrayList;

public class MyTabreViewData {

    ArrayList<MyTabreViewItem> items = new ArrayList<>();

    public ArrayList<MyTabreViewItem> getItems() {

        MyTabreViewItem order1 = new MyTabreViewItem(
                "뼈대있는가문", "2021/10/28","감자탕 大");

        MyTabreViewItem order2 = new MyTabreViewItem(
                "뼈대있는가문", "2021/10/30","감자탕 小");

        MyTabreViewItem order3 = new MyTabreViewItem(
                "뼈대있는가문", "2021/12/1", "감자탕 中");

        items.add(order1);
        items.add(order2);
        items.add(order3);

        return items;
    }
}



