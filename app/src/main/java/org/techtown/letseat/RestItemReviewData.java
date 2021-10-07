package org.techtown.letseat;

import java.util.ArrayList;

public class RestItemReviewData {

    ArrayList<RestItemReviewItem> items = new ArrayList<>();

    public ArrayList<RestItemReviewItem> getItems() {

        RestItemReviewItem order1 = new RestItemReviewItem(R.drawable.image1,
                "tjaudwlscjswo", "2021/10/28");

        RestItemReviewItem order2 = new RestItemReviewItem(R.drawable.image2,
                "dksxogus", "2021/10/28");

        RestItemReviewItem order3 = new RestItemReviewItem(R.drawable.image3,
                "rlagudwns", "2021/10/28");

        items.add(order1);
        items.add(order2);
        items.add(order3);

        return items;
    }
}


