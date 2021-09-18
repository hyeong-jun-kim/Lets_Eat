package org.techtown.letseat.restaurant.info;

import org.techtown.letseat.R;
import org.techtown.letseat.restaurant.list.RestListMain;

import java.util.ArrayList;

public class RestInfoDataMain extends RestListMain {

    ArrayList<RestInfoRecycleItem> RestInfoRecycleItems = new ArrayList<>();

    public ArrayList<RestInfoRecycleItem> getItems() {


        RestInfoRecycleItem item1 = new RestInfoRecycleItem(R.drawable.image1,
                "하오탕", "안녕하세요 찾아주셔서 감사합니다.", "위치   경기도 용인시 수지구 291-2", "전화번호   031-234-3392",
                "영업시간   AM 10:00 ~ PM 22:00", "주차가능");

        RestInfoRecycleItem item2 = new RestInfoRecycleItem(R.drawable.image2,
                "하오탕", "안녕하세요 찾아주셔서 감사합니다.", "위치   경기도 용인시 수지구 291-2", "전화번호   031-234-3392",
                "영업시간   AM 10:00 ~ PM 22:00", "주차가능");


        RestInfoRecycleItems.add(item1);
        RestInfoRecycleItems.add(item2);



        return RestInfoRecycleItems;


    }


}
