package org.techtown.letseat.restaurant.info;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.R;
import org.techtown.letseat.util.ViewPagerAdapter;

public class RestInfoMain extends AppCompatActivity {
    res_info_fragment1 fragment1;
    Res_info_fragment2 fragment2;
    res_info_fragment3 fragment3;


    private TabLayout tabs;
    private ViewPager viewPager;
    String string, url;
    TextView res_title;
    int resIdget;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restab_info_activity);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);

        tabs = findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(viewPager);

        fragment1 = new res_info_fragment1();
        fragment2 = new Res_info_fragment2();
        fragment3 = new res_info_fragment3();

        Intent i = getIntent();
        if(i.getStringExtra("text").equals("All")){
            string = i.getStringExtra("text");      //string.value = "All"
        }
        else if(i.getStringExtra("text").equals("koreanFood")){
            string = i.getStringExtra("text");      ////string.value = "koreanFood"
        }
        else if(i.getStringExtra("text").equals("chineseFood")){
            string = i.getStringExtra("text");      ////string.value = "koreanFood"
        }
        else if(i.getStringExtra("text").equals("japaneseFood")){
            string = i.getStringExtra("text");      ////string.value = "koreanFood"
        }
        else if(i.getStringExtra("text").equals("westernFood")){
            string = i.getStringExtra("text");      ////string.value = "koreanFood"
        }
        else if(i.getStringExtra("text").equals("onemanFood")){
            string = i.getStringExtra("text");      ////string.value = "koreanFood"
        }
        else if(i.getStringExtra("text")!=null){
            string = i.getStringExtra("text");
        }

        resIdget = i.getIntExtra("send_resId",0);


        res_title = findViewById(R.id.waiting);

        Bundle extras = getIntent().getExtras();
        Log.d("ds","ds");


        Bundle bundle = new Bundle();
        bundle.putInt("send_resId",resIdget);
        fragment1.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().commit();

        if(string.equals("chineseFood")){
            Bundle bundle1 = new Bundle();
            bundle1.putString("text","chineseFood");
            bundle1.putInt("send_resId",resIdget);
            fragment2.setArguments(bundle1);
            fragment3.setArguments(bundle1);
        }
        else if(string.equals("koreanFood")){
            Bundle bundle1 = new Bundle();
            bundle1.putString("text","koreanFood");
            bundle1.putInt("send_resId",resIdget);
            fragment2.setArguments(bundle1);
            fragment3.setArguments(bundle1);
        }
        else if(string.equals("japaneseFood")){
            Bundle bundle1 = new Bundle();
            bundle1.putString("text","japaneseFood");
            bundle1.putInt("send_resId",resIdget);
            fragment2.setArguments(bundle1);
            fragment3.setArguments(bundle1);
        }
        else if(string.equals("westernFood")){
            Bundle bundle1 = new Bundle();
            bundle1.putString("text","westernFood");
            bundle1.putInt("send_resId",resIdget);
            fragment2.setArguments(bundle1);
            fragment3.setArguments(bundle1);
        }
        else if(string.equals("onemanFood")){
            Bundle bundle1 = new Bundle();
            bundle1.putString("text","onemanFood");
            bundle1.putInt("send_resId",resIdget);
            fragment2.setArguments(bundle1);
            fragment3.setArguments(bundle1);
        }
        else if(string.equals("All")){
            Bundle bundle1 = new Bundle();
            bundle1.putString("text","All");
            bundle1.putInt("send_resId",resIdget);
            fragment2.setArguments(bundle1);
            fragment3.setArguments(bundle1);
        }
        else if(string != null){
            Bundle bundle1 = new Bundle();
            bundle1.putString("text",string);
            bundle1.putInt("send_resId",resIdget);
            fragment2.setArguments(bundle1);
            fragment3.setArguments(bundle1);
        }   //검색기능


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(fragment1,"메뉴");
        viewPagerAdapter.addFragment(fragment2,"정보");
        viewPagerAdapter.addFragment(fragment3,"리뷰");
        viewPager.setAdapter(viewPagerAdapter);

        tabs.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });

        get_Restaurant();
    }

    void get_Restaurant() {

        if(string.equals("koreanFood")){
            url = "http://125.132.62.150:8000/letseat/store/findRestaurant?restype=koreanFood";
        }
        else if(string.equals("chineseFood")){
            url = "http://125.132.62.150:8000/letseat/store/findRestaurant?restype=chineseFood";
        }
        else if(string.equals("japaneseFood")){
            url = "http://125.132.62.150:8000/letseat/store/findRestaurant?restype=japaneseFood";
        }
        else if(string.equals("westernFood")){
            url = "http://125.132.62.150:8000/letseat/store/findRestaurant?restype=westernFood";
        }
        else if(string.equals("onemanFood")){
            url = "http://125.132.62.150:8000/letseat/store/find/aloneAble";
        }
        else if(string.equals("All")){
            url = "http://125.132.62.150:8000/letseat/store/findAll";
        }
        else if(string != null){
            url = "http://125.132.62.150:8000/letseat/store/searchRestaurant?name="+string;
        }


        Log.d("ds","ds");

        JSONArray getData = new JSONArray();


        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                getData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i<response.length(); i++){
                                JSONObject jsonObject = (JSONObject)response.get(i);

                                String title = jsonObject.getString("resName");
                                int resid = jsonObject.getInt("resId");

                                if(resid == resIdget){
                                    res_title.setText(title);
                                }


                                Log.d("응답", response.toString());
                            }



                        } catch (JSONException e) {
                            Log.d("씨발",e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("에러", error.toString());
                    }
                }
        );
        request.setShouldCache(false); // 이전 결과 있어도 새로 요청해 응답을 보내줌
        AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext()); // requsetQueue 초기화
        AppHelper.requestQueue.add(request);
    }
}