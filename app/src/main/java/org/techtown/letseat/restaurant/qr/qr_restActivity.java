package org.techtown.letseat.restaurant.qr;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import org.techtown.letseat.MainActivity;
import org.techtown.letseat.menu.MenuAdapter;
import org.techtown.letseat.menu.MenuData;
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.R;
import org.techtown.letseat.ViewPagerAdapter;
import org.techtown.letseat.restaurant.info.Res_info_fragment2;
import org.techtown.letseat.restaurant.info.res_info_fragment1;
import org.techtown.letseat.restaurant.info.res_info_fragment3;
import org.techtown.letseat.util.PhotoSave;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class qr_restActivity extends AppCompatActivity {
    private ArrayList<QR_Menu> list = new ArrayList<>();
    private ArrayList<Integer> menus_id = new ArrayList<>();
    private ImageView resImage;
    private int resId, tableNumber, orderId;
    private QR_MenuAdapter adapter = new QR_MenuAdapter();
    private RecyclerView recyclerView;
    private View view;
    private Button orderButton;
    TextView res_title, res_table;
    int data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_rest);
        res_title = findViewById(R.id.res_title);
        res_table = findViewById(R.id.res_tableNumber);
        resImage = findViewById(R.id.qr_res_image);
        orderButton = findViewById(R.id.qr_order_button);
        recyclerView = findViewById(R.id.qr_recyclerView);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        resId = bundle.getInt("resId");
        tableNumber = bundle.getInt("tableNumber");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                requestOrderList();

                for(int i = 0; i < menus_id.size(); i++){
                    requestMenuList(1, menus_id.get(i));
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "성공적으로 주문이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        get_Restaurant();
        get_MenuData();
    }
    // 주문 리스트 보내기
    @RequiresApi(api = Build.VERSION_CODES.O)
    void requestOrderList(){
        LocalDateTime now = LocalDateTime.now();
        String orderTime = now.format(DateTimeFormatter.ofPattern("HH시 mm분 ss초"));
        String url = "http://125.132.62.150:8000/letseat/order/list/register";
        JSONObject postData = new JSONObject();
        JSONObject restData = new JSONObject();
        JSONObject userData = new JSONObject();
        int userId = MainActivity.userId;
        try {
            restData.put("resId", resId);
            userData.put("userId",userId);
            postData.put("orderTime",orderTime);
            postData.put("tableNumber",tableNumber);
            postData.put("user",userData);
            postData.put("restaurant",restData);
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            orderId = response.getInt("orderId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "연결 불량으로 인해 주문 실패.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        );
        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requsetQueue 초기화
        AppHelper.requestQueue.add(request);
    }
    // 메뉴 리스트 보내기
    void requestMenuList(int amount, int resMenuId){
        String url = "http://125.132.62.150:8000/letseat/order/menu/register";
        JSONObject postData = new JSONObject();
        JSONObject orderData = new JSONObject();
        JSONObject menuData = new JSONObject();
        try {
            menuData.put("resMenuId",resMenuId);
            orderData.put("orderId", orderId);
            postData.put("orderList",orderData);
            postData.put("amount",amount);
            postData.put("resMenu",menuData);
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("OrderMenu","성공");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("OrderMenu","Error: " + error);
                    }
                }
        );
        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requsetQueue 초기화
        AppHelper.requestQueue.add(request);
    }
    // 레스토랑 정보 가져오기
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
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "연결 불량으로 인해 주문 실패.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        );
        request.setShouldCache(false);
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
                            int menuId;
                            String menuPrice,menuName,image;
                            Bitmap bitmap;

                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                menuId = jsonObject.getInt("resMenuId");
                                menuPrice = jsonObject.getString("price");
                                menuName = jsonObject.getString("name");
                                image = jsonObject.getString("photo");
                                bitmap = PhotoSave.StringToBitmap(image);
                                QR_Menu menu = new QR_Menu(bitmap, menuName, menuPrice);
                                list.add(menu);
                                menus_id.add(menuId);
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