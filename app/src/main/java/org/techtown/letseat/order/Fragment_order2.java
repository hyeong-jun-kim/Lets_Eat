package org.techtown.letseat.order;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.MainActivity;
import org.techtown.letseat.R;
import org.techtown.letseat.photo.Photo;
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.util.PhotoSave;

import java.util.ArrayList;

public class Fragment_order2 extends Fragment {
    View view;
    private int userId = MainActivity.userId;
    private Order_recycle_adapter2 adapter2 = new Order_recycle_adapter2();
    private RecyclerView recyclerView2;
    ArrayList<Integer> orderLists = new ArrayList<>();
    ArrayList<Integer> resLists = new ArrayList<>();
    ArrayList<String> resNameLists = new ArrayList<>();
    ArrayList<OrderItem2> waiting_orders = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AppHelper.requestQueue = Volley.newRequestQueue(getActivity()); // requsetQueue 초기화
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order2, container, false);
        adapter2.setItems(new Orderdata2().getItems());
        recyclerView2 = view.findViewById(R.id.recycler_view2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getWatingOrderList();
        //getMenuList();
        adapter2.setItems(waiting_orders);
        recyclerView2.setAdapter(adapter2);

        return view;
    }
    // 현재 대기중인 주문리스트 받기
    void getWatingOrderList() {
        String url = "http://125.132.62.150:8000/letseat/order/list/user?userId="+userId;
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++){
                                String menus = "";
                                int sum = 0;
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                ArrayList<String > menuList = new ArrayList<String>();
                                String image = jsonObject.getString("image");
                                String orderTime = jsonObject.getString("orderTime") +" 서빙 대기중";
                                String resName = jsonObject.getString("resName");
                                Bitmap bitmap = PhotoSave.StringToBitmap(image);
                                JSONArray resMenus = jsonObject.getJSONArray("resMenus");
                                for(int j = 0; j < resMenus.length(); j++){
                                    JSONObject menu = resMenus.getJSONObject(j);
                                    String menu_name = menu.getString("name");
                                    int price = menu.getInt("price");
                                    menuList.add(menu_name);
                                    sum += price;
                                }
                                JSONArray orderMenus = jsonObject.getJSONArray("orderMenus");
                                for(int j = 0; j < orderMenus.length(); j++){
                                    JSONObject orderMenu = orderMenus.getJSONObject(j);
                                    int amount = orderMenu.getInt("amount");
                                    if(j == 0){
                                        menus = menuList.get(j) + " " + amount +"개 ";
                                    }else{
                                        menus += ","+menuList.get(j) + " " + amount +"개 ";
                                    }
                                }
                                OrderItem2 orderItem = new OrderItem2(bitmap, menus, "총액:"+sum+"원", resName, orderTime);
                                waiting_orders.add(orderItem);
                            }
                            Log.d("응답", response.toString());
                        } catch (JSONException e) {
                            Log.d("예외", e.toString());
                            e.printStackTrace();
                        }
                        start();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("에러", error.toString());
                    }
                }
        );
        request.setShouldCache(false); // 이전 결과 있어도 새로 요청해 응답을 보냄
        AppHelper.requestQueue.add(request);
    }
    public void start(){
        adapter2.setItems(waiting_orders);
        adapter2.notifyDataSetChanged();;
    }
}