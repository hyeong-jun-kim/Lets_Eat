package org.techtown.letseat.restaurant.info;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import org.techtown.letseat.AppHelper;
import org.techtown.letseat.R;
import org.techtown.letseat.ViewPagerAdapter;

public class RestInfoMain extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private res_info_fragment1 fragment1;
    private Res_info_fragment2 fragment2;
    private res_info_fragment3 fragment3;

    TextView res_title;
    int data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restab_info_activity);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        fragment1 = new res_info_fragment1();
        fragment2 = new Res_info_fragment2();
        fragment3 = new res_info_fragment3();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(fragment1, "메뉴");
        viewPagerAdapter.addFragment(fragment2, "정보");
        viewPagerAdapter.addFragment(fragment3, "리뷰");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                super.onTabSelected(tab);
                int position = tab.getPosition();
                if(position == 1){

                    Res_info_fragment2 fragment = new Res_info_fragment2();
                    Bundle bundle = new Bundle();
                    bundle.putInt("ap",data);
                    fragment.setArguments(bundle);

                }
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

        res_title = findViewById(R.id.res_title);

        Bundle extras = getIntent().getExtras();
        data = extras.getInt("aP");
        Log.d("ds", "ds");

        get_Restaurant();
    }
    void get_Restaurant() {
        String url = "http://125.132.62.150:8000/letseat/store/findAll";



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