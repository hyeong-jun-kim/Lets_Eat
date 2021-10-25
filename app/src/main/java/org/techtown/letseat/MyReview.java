package org.techtown.letseat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.R;
import org.techtown.letseat.menu.MenuData;
import org.techtown.letseat.restaurant.list.OnRestaurantItemClickListner;
import org.techtown.letseat.restaurant.list.RestListAdapter;
import org.techtown.letseat.restaurant.list.RestListData;
import org.techtown.letseat.restaurant.list.RestListRecycleItem;
import org.techtown.letseat.restaurant.review.RestItemReviewAdapter;
import org.techtown.letseat.restaurant.review.RestItemReviewData;
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.util.PhotoSave;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MyReview extends AppCompatActivity {

    ArrayList list = new ArrayList<>();
    ArrayList<Integer> resIdList = new ArrayList<>();
    RecyclerView recyclerView;
    int userId;
    String text;
    View view;
    private RestItemReviewAdapter adapter = new RestItemReviewAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review);

        recyclerView = findViewById(R.id.recyclerView);


        Intent intent = getIntent();
        userId = intent.getIntExtra("userId",0);

        get_Restaurant();

    }


    void get_Restaurant() {
        String url = "http://125.132.62.150:8000/letseat/review/load/user?userId="+userId;


        JSONArray getData = new JSONArray();

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                getData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String email,image,content;
                            Double getrate;
                            float rate;
                            Bitmap bitmap;
                            ArrayList arrayList = new ArrayList();

                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                JSONObject userobject = jsonObject.getJSONObject("user");
                                email = userobject.getString("email");//user 이메일
                                getrate = jsonObject.getDouble("rate");   //별점
                                rate = getrate.floatValue();   //별점 float형으로 변환
                                content = jsonObject.getString("content");
                                image = jsonObject.getString("image");
                                bitmap = PhotoSave.StringToBitmap(image);


                                list.add(email);
                                list.add(rate);
                                list.add(content);
                                list.add(bitmap);

                            }

                            adapter.setItems(new RestItemReviewData().getItems(list));
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(adapter);

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
}