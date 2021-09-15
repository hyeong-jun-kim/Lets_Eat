package org.techtown.letseat.restaurant;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.letseat.R;

import java.util.ArrayList;

public class restaurant_info_data extends Rest_List{

    ArrayList<restaurant_info_item> restaurant_info_items = new ArrayList<>();

    public ArrayList<restaurant_info_item> getItems() {


        restaurant_info_item item1 = new restaurant_info_item(R.drawable.image1,
                "하오탕", "안녕하세요 찾아주셔서 감사합니다.", "위치   경기도 용인시 수지구 291-2", "전화번호   031-234-3392",
                "영업시간   AM 10:00 ~ PM 22:00", "주차가능");

        restaurant_info_item item2 = new restaurant_info_item(R.drawable.image2,
                "하오탕", "안녕하세요 찾아주셔서 감사합니다.", "위치   경기도 용인시 수지구 291-2", "전화번호   031-234-3392",
                "영업시간   AM 10:00 ~ PM 22:00", "주차가능");


        restaurant_info_items.add(item1);
        restaurant_info_items.add(item2);



        return restaurant_info_items;


    }


}
