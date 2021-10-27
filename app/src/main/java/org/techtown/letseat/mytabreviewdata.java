package org.techtown.letseat;

import java.util.ArrayList;

public class mytabreviewdata {

    ArrayList<mytabreviewitem> items = new ArrayList<>();

    public ArrayList<mytabreviewitem> getItems() {

        mytabreviewitem order1 = new mytabreviewitem(
                "뼈대있는가문", "2021/10/28","감자탕 大");

        mytabreviewitem order2 = new mytabreviewitem(
                "뼈대있는가문", "2021/10/30","감자탕 小");

        mytabreviewitem order3 = new mytabreviewitem(
                "뼈대있는가문", "2021/12/1", "감자탕 中");

        items.add(order1);
        items.add(order2);
        items.add(order3);

        return items;
    }
}



