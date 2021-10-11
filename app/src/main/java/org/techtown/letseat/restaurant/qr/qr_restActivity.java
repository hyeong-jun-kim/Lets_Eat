package org.techtown.letseat.restaurant.qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.menu.MenuAdapter;
import org.techtown.letseat.menu.MenuData;
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.R;
import org.techtown.letseat.ViewPagerAdapter;
import org.techtown.letseat.restaurant.info.Res_info_fragment2;
import org.techtown.letseat.restaurant.info.res_info_fragment1;
import org.techtown.letseat.restaurant.info.res_info_fragment3;
import org.techtown.letseat.util.PhotoSave;

import java.util.ArrayList;

public class qr_restActivity extends AppCompatActivity {
    private ArrayList<QR_Menu> list = new ArrayList<>();
    private ImageView resImage;
    private int resId, tableNumber;
    private QR_MenuAdapter adapter = new QR_MenuAdapter();
    private RecyclerView recyclerView;
    private View view;
    TextView res_title, res_table;
    int data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_rest);
        res_title = findViewById(R.id.res_title);
        res_table = findViewById(R.id.res_tableNumber);
        resImage = findViewById(R.id.qr_res_image);
        recyclerView = findViewById(R.id.qr_recyclerView);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        resId = bundle.getInt("resId");
        tableNumber = bundle.getInt("tableNumber");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        get_Restaurant();
        get_MenuData();
    }
    void get_Restaurant() {
        String url = "http://125.132.62.150:8000/letseat/store/findOne?resId="+resId;
        JsonObject jsonObject = new JsonObject();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Bitmap bitmap = null;
                        String title = null;
                        String image = null;
                        try {
                            title = response.getString("resName");
                            image = response.getString("image");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        bitmap = PhotoSave.StringToBitmap(image);
                        resImage.setImageBitmap(bitmap);
                        res_title.setText(title);
                        res_table.setText(tableNumber+"번 테이블");
                        Log.d("응답", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        request.setShouldCache(false); // 이전 결과 있어도 새로 요청해 응답을 보내줌
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requsetQueue 초기화
        AppHelper.requestQueue.add(request);
    }
    // 메뉴 리스트 가져오기
    void get_MenuData() {
        String url = "http://125.132.62.150:8000/letseat/store/menu/load?resId="+resId;
        JSONArray getData = new JSONArray();
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                getData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String menuPrice,menuName,image;
                            Bitmap bitmap;

                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                menuPrice = jsonObject.getString("price");
                                menuName = jsonObject.getString("name");
                                image = jsonObject.getString("photo");
                                bitmap = PhotoSave.StringToBitmap(image);
                                QR_Menu menu = new QR_Menu(bitmap, menuName, menuPrice);
                                list.add(menu);
                            }
                            adapter.setItems(list);
                            adapter.notifyDataSetChanged();
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