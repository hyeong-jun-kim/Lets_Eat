package org.techtown.letseat.photo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.MainActivity;
import org.techtown.letseat.R;
import org.techtown.letseat.RestSearch2;
import org.techtown.letseat.ReviewSearch;
import org.techtown.letseat.Review_Search;
import org.techtown.letseat.restaurant.review.RestItemReviewData;
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.util.PhotoSave;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhotoList extends AppCompatActivity {
    ProgressBar progressBar;
    private PhotoRecyclerAdapter adapter;
    private Button menu_tag1,menu_tag2,menu_tag3,menu_tag4,menu_tag5;
    static public PhotoList photoList;
    public boolean check = false;
    PhotoFragment photoFragment;
    FragmentManager fm;
    FragmentTransaction ft;
    ArrayList reviewImageList = new ArrayList<>();
    ArrayList reviewNameList = new ArrayList<>();
    ArrayList contentList = new ArrayList<>();
    ArrayList rateList = new ArrayList<>();
    String res_name, content;
    Double get_rate,latitude,longitude;
    float rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_list_activity);
        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude",0);
        longitude = intent.getDoubleExtra("longitude",0);

        photoList = this;
        progressBar = findViewById(R.id.loading);
        progressBar.setVisibility(View.VISIBLE);
        menu_tag1 = findViewById(R.id.menu_tag1);
        menu_tag2 = findViewById(R.id.menu_tag2);
        menu_tag3 = findViewById(R.id.menu_tag3);
        menu_tag4 = findViewById(R.id.menu_tag4);
        menu_tag5 = findViewById(R.id.menu_tag5);

        menu_tag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Review_Search.class);
                intent.putExtra("text",menu_tag1.getText().toString());
                startActivity(intent);
            }
        });
        get_Review();
        get_ReviewTag();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        // 사진 클릭할 시 나오는 이벤트
        MaterialToolbar toolbar = findViewById(R.id.topMain);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(homeIntent);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.actionsearch:
                        Intent settingIntent = new Intent(getApplicationContext(), ReviewSearch.class);
                        startActivity(settingIntent);
                        break;
                }
                return true;
            }
        });

    }
    //메뉴 태그 가져오기
    void get_ReviewTag() {
        String url = "http://125.132.62.150:8000/letseat/review/load/randomMenu";


        JSONArray getData = new JSONArray();

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                getData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            menu_tag1.setText(response.get(0).toString());
                            menu_tag2.setText(response.get(1).toString());
                            menu_tag3.setText(response.get(2).toString());
                            menu_tag4.setText(response.get(3).toString());
                            menu_tag5.setText(response.get(4).toString());
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

    //전체 리뷰 가져오기
    void get_Review() {
        String url = "http://125.132.62.150:8000/letseat/review/load/all";


        JSONArray getData = new JSONArray();

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                getData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String image;
                            Bitmap bitmap;

                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                JSONObject res_jsonObject = jsonObject.getJSONObject("restaurant");

                                res_name = res_jsonObject.getString("resName");
                                image = jsonObject.getString("image");
                                bitmap = PhotoSave.StringToBitmap(image);
                                content = jsonObject.getString("content");
                                get_rate = jsonObject.getDouble("rate");
                                rate = get_rate.floatValue();
                                    reviewNameList.add(res_name);
                                    reviewImageList.add(bitmap);
                                    contentList.add(content);
                                    rateList.add(rate);
                                    Log.d("ds","ds");
                            }
                            init();
                            Log.d("응답", response.toString());
                        } catch (JSONException e) {
                            Log.d("예외", e.toString());
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

    // 처음 시작 시 리사이클러뷰 세팅하기
    private void init() {
        RecyclerView recyclerView = findViewById(R.id.photoRecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PhotoRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        getData();
    }

    // 처음 시작 시 리사이클러뷰 불러오기
    private void getData() {
        for (int i = 0; i < reviewImageList.size(); i++) {
            PhotoData data = new PhotoData();
            data.setResId((Bitmap) reviewImageList.get(i));
            adapter.addItem(data);
            progressBar.setVisibility(View.INVISIBLE);
        }
        adapter.setOnItemClicklistener(new OnPhotoItemClickListener() {
            @Override
            public void onItemClick(PhotoRecyclerAdapter.ItemViewHolder holder, View view,
                                    int position) {
                if (!check) {
                    check = true;
                    photoFragment = new PhotoFragment();
                    ft = fm.beginTransaction();
                    // 여기에 데이터베이스 정보 넣어야 함
                    photoFragment.setresId((Bitmap) reviewImageList.get(holder.getAdapterPosition()));
                    photoFragment.setTitle((String) reviewNameList.get(holder.getAdapterPosition()));
                    photoFragment.setReview((String) contentList.get(holder.getAdapterPosition()));
                    photoFragment.setRate((Float) rateList.get(holder.getAdapterPosition()));
                    ft.add(R.id.photoFragment, photoFragment);
                    ft.commit();
                }
            }
        });
    }
}