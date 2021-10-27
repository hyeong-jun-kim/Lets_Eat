package org.techtown.letseat.restaurant.list;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import org.techtown.letseat.MainActivity;
import org.techtown.letseat.Restaurant_Search;
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.R;
import org.techtown.letseat.restaurant.info.RestInfoMain;
import org.techtown.letseat.util.GpsTracker;
import org.techtown.letseat.util.PhotoSave;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RestListMain extends AppCompatActivity {

    ProgressBar progressBar;
    ArrayList list = new ArrayList<>();

    MainActivity mainActivity;
    ArrayList<Integer> resIdList = new ArrayList<>();
    private double latitude;
    private double longitude;

    private static final int PERMISSIONS_REQUEST_CODE = 100;

    EditText search_restaurant;
    private RestListAdapter adapter = new RestListAdapter();
    private RestListAdapter restaurant_info_adapter = new RestListAdapter();
    RecyclerView recyclerView;
    ImageButton koreanFood_button, chinaFood_button, japanFood_button, westernFood_Button, onemanFood_Button;
    Button search_btn;
    int resId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.res_list_activity);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.loading);
        progressBar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude",0);
        longitude = intent.getDoubleExtra("longitude",0);
        get_Restaurant();

        mainActivity = new MainActivity();


        /* search_restaurant = findViewById(R.id.search_restaurant);
        search_btn = findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text =  search_restaurant.getText().toString();
                Intent intent = new Intent(getApplicationContext(), Restaurant_Search.class);
                intent.putExtra("text",text);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                startActivity(intent);
            }
        }); */


        koreanFood_button = findViewById(R.id.koreanFood_button);
        koreanFood_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RestList_KoreanFood.class);
                intent.putExtra("text","koreanFood");
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                startActivity(intent);
            }
        });

        chinaFood_button = findViewById(R.id.chinaFood_button);
        chinaFood_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RestList_chinaFood.class);
                intent.putExtra("text","chineseFood");
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                startActivity(intent);
            }
        });

        japanFood_button = findViewById(R.id.japanFood_button);
        japanFood_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RestList_japanFood.class);
                intent.putExtra("text","japaneseFood");
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                startActivity(intent);
            }
        });

        westernFood_Button = findViewById(R.id.westernFood_Button);
        westernFood_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RestList_westernFood.class);
                intent.putExtra("text","westernFood");
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                startActivity(intent);
            }
        });

        onemanFood_Button = findViewById(R.id.onemanFood_Button);
        onemanFood_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RestList_onemanFood.class);
                intent.putExtra("text","westernFood");
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                startActivity(intent);
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void start(){
        //recycleView 초기화

        adapter.setItems(new RestListData().getItems(list));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.INVISIBLE);

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



    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {

                //위치 값을 가져올 수 있음
                ;
            }
        }
    }

}