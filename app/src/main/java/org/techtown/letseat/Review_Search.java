package org.techtown.letseat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.photo.OnPhotoItemClickListener;
import org.techtown.letseat.photo.PhotoData;
import org.techtown.letseat.photo.PhotoFragment;
import org.techtown.letseat.photo.PhotoList;
import org.techtown.letseat.photo.PhotoRecyclerAdapter;
import org.techtown.letseat.restaurant.info.RestInfoMain;
import org.techtown.letseat.restaurant.list.OnRestaurantItemClickListner;
import org.techtown.letseat.restaurant.list.RestListAdapter;
import org.techtown.letseat.restaurant.list.RestListData;
import org.techtown.letseat.restaurant.list.RestListRecycleItem;
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.util.PhotoSave;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Review_Search extends AppCompatActivity {

    private final Double latitude = MainActivity.mainActivity.latitude;
    private final Double longitude = MainActivity.mainActivity.longitude;

    static public Review_Search review_Search;
    private PhotoRecyclerAdapter adapter;
    public boolean check = false;
    PhotoFragment photoFragment;
    FragmentManager fm;
    FragmentTransaction ft;
    ArrayList reviewImageList = new ArrayList<>();
    ArrayList reviewNameList = new ArrayList<>();
    ArrayList contentList = new ArrayList<>();
    ArrayList rateList = new ArrayList<>();
    String res_name, content;
    Double get_rate;
    float rate;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_search_recycler);

        review_Search = this;
        Intent intent = getIntent();
        text = intent.getStringExtra("text");
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        get_Restaurant();
    }

    void get_Restaurant() {
        String url = "http://125.132.62.150:8000/letseat/review/load/menuName?menuName="+text;


        JSONArray getData = new JSONArray();

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                getData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String image,location;
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
                                location = res_jsonObject.getString("location");

                                Geocoder geocoder = new Geocoder(getBaseContext());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocationName(location, 3);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Address address = addresses.get(0);
                                LatLng place = new LatLng(address.getLatitude(), address.getLongitude());
                                double lat = place.latitude;
                                double lon = place.longitude;
                                if((latitude < lat+0.05 && lat-0.05 < latitude) || (longitude < lon+0.07 && lon-0.07 < longitude)){
                                    reviewNameList.add(res_name);
                                    reviewImageList.add(bitmap);
                                    contentList.add(content);
                                    rateList.add(rate);
                                    Log.d("ds","ds");
                                }
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
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requsetQueue 초기화
        AppHelper.requestQueue.add(request);
    }
    private void init() {
        RecyclerView recyclerView = findViewById(R.id.review_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PhotoRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        getData();
    }
    private void getData() {
        for (int i = 0; i < reviewImageList.size(); i++) {
            PhotoData data = new PhotoData();
            data.setResId((Bitmap) reviewImageList.get(i));
            adapter.addItem(data);
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