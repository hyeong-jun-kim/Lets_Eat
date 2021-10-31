package org.techtown.letseat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class RestSearch2 extends AppCompatActivity {

    ArrayList list = new ArrayList<>();
    private RestListAdapter adapter = new RestListAdapter();
    int resId;
    ArrayList<Integer> resIdList = new ArrayList<>();
    RecyclerView recyclerView;
    EditText editText;
    String text;
    Double latitude, longitude;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_search);

        recyclerView = findViewById(R.id.review_search_recyclerview);
        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude",0);
        longitude = intent.getDoubleExtra("longitude",0);
        editText = findViewById(R.id.search_text);

        Button searchbtn = findViewById(R.id.search);
        MaterialToolbar toolbar = findViewById(R.id.topMain);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = editText.getText().toString();
                get_Restaurant();
            }
        });
    }

    public void start(){
        //recycleView 초기화

        adapter.setItems(new RestListData().getItems(list));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //클릭 이벤트
        adapter.setItemClickListner(new OnRestaurantItemClickListner() {
            @Override
            public void OnItemClick(RestListAdapter.ViewHolder holder, View view, int position) {

                int adapterPosition = holder.getAdapterPosition();

                RestListRecycleItem item = adapter.getItem(position);

                int send_resId = resIdList.get(adapterPosition);
                Log.d("ds","ds");

                Intent intent = new Intent(getApplicationContext(), RestInfoMain.class);
                intent.putExtra("aP",adapterPosition);
                intent.putExtra("text","All");
                intent.putExtra("send_resId",send_resId);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);

                startActivity(intent);

            }
        });

    }

    void get_Restaurant() {
        String url = "http://125.132.62.150:8000/letseat/store/searchRestaurant?name="+text;


        JSONArray getData = new JSONArray();

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                getData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String restype,resName,location,image;
                            Bitmap bitmap;

                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = (JSONObject) response.get(i);

                                location = jsonObject.getString("location");



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
                                    restype = jsonObject.getString("restype");
                                    resName = jsonObject.getString("resName");
                                    image = jsonObject.getString("image");
                                    bitmap = PhotoSave.StringToBitmap(image);
                                    resId = jsonObject.getInt("resId");

                                    list.add(bitmap);
                                    list.add(restype);
                                    list.add(resName);
                                    resIdList.add(resId);
                                }
                            }

                            start();

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
}
