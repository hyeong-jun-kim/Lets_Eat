package org.techtown.letseat.restaurant.info;

import androidx.appcompat.app.AppCompatActivity;

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

public class RestInfoMain extends AppCompatActivity {
    res_info_fragment1 fragment1;
    Res_info_fragment2 fragment2;
    res_info_fragment3 fragment3;

    String string, url;
    TextView res_title;
    int data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restab_info_activity);

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
        else if(i.getStringExtra("text")!=null){
            string = i.getStringExtra("text");
        }

        fragment1 = new res_info_fragment1();
        fragment2 = new Res_info_fragment2();
        fragment3 = new res_info_fragment3();

        res_title = findViewById(R.id.res_title);

        Bundle extras = getIntent().getExtras();
        data = extras.getInt("aP");
        Log.d("ds","ds");


        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment1).commit();

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("메뉴"));
        tabs.addTab(tabs.newTab().setText("정보"));
        tabs.addTab(tabs.newTab().setText("리뷰"));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                if(position == 0){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment1).commit();
                }
                else if(position == 1){


                    if(string.equals("chineseFood")){
                        Res_info_fragment2 fragment = new Res_info_fragment2();
                        Bundle bundle = new Bundle();
                        bundle.putInt("ap",data);
                        bundle.putString("text","chineseFood");
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();
                    }
                    else if(string.equals("koreanFood")){
                        Res_info_fragment2 fragment = new Res_info_fragment2();
                        Bundle bundle = new Bundle();
                        bundle.putInt("ap",data);
                        bundle.putString("text","koreanFood");
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();
                    }
                    else if(string.equals("japaneseFood")){
                        Res_info_fragment2 fragment = new Res_info_fragment2();
                        Bundle bundle = new Bundle();
                        bundle.putInt("ap",data);
                        bundle.putString("text","japaneseFood");
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();
                    }
                    else if(string.equals("westernFood")){
                        Res_info_fragment2 fragment = new Res_info_fragment2();
                        Bundle bundle = new Bundle();
                        bundle.putInt("ap",data);
                        bundle.putString("text","westernFood");
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();
                    }
                    else if(string.equals("All")){
                        Res_info_fragment2 fragment = new Res_info_fragment2();
                        Bundle bundle = new Bundle();
                        bundle.putInt("ap",data);
                        bundle.putString("text","All");
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();
                    }
                    else if(string != null){
                        Res_info_fragment2 fragment = new Res_info_fragment2();
                        Bundle bundle = new Bundle();
                        bundle.putInt("ap",data);
                        bundle.putString("text",string);
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();
                    }   //검색기능


                }
                else if(position ==2){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment3).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        get_Restaurant();
    }
    void get_Restaurant() {

        if(string.equals("koreanFood")){
            url = "http://125.132.62.150:8000/letseat/store/findRestaurant?restype=koreanFood";
        }
        else if(string.equals("All")){
            url = "http://125.132.62.150:8000/letseat/store/findAll";
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
        else if(string != null){
            url = "http://125.132.62.150:8000/letseat/store/searchRestaurant?name="+string;
        }



        JSONArray getData = new JSONArray();

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                getData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            JSONObject jsonObject = (JSONObject) response.get(data);
                            String title = jsonObject.getString("resName");

                            res_title.setText(title);

                            Log.d("응답", response.toString());

                        } catch (JSONException e) {
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