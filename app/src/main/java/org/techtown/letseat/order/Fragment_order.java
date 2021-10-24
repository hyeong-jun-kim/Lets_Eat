package org.techtown.letseat.order;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.MainActivity;
import org.techtown.letseat.R;
import org.techtown.letseat.restaurant.info.RestInfoMain;
import org.techtown.letseat.restaurant.list.OnRestaurantItemClickListner;
import org.techtown.letseat.restaurant.list.RestListAdapter;
import org.techtown.letseat.restaurant.list.RestListData;
import org.techtown.letseat.restaurant.list.RestListRecycleItem;
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.util.PhotoSave;

import java.util.ArrayList;

public class Fragment_order extends Fragment {
    private int userId = MainActivity.userId;
    RecyclerView recyclerView;
    ArrayList<OrderItem> items = new ArrayList<OrderItem>();
    private Order_recycle_adapter adapter = new Order_recycle_adapter();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        AppHelper.requestQueue = Volley.newRequestQueue(getActivity()); // requsetQueue 초기화
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
        get_Restaurant();
        return view;
    }
    public void start(){
        //recycleView 초기화
        adapter.setItems(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        //클릭 이벤트
        adapter.setItemClickListner(new OnReviewItemClickListner() {
            @Override
            public void OnItemClick(Order_recycle_adapter.ViewHolder holder, View view, int position) {

            }
        });
    }
    void get_Restaurant() {
        String url = "http://125.132.62.150:8000/letseat/order/list/complete/load?userId="+userId;
        JSONArray getData = new JSONArray();
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                getData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                String menus = "";
                                int sum = 0;
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                ArrayList<String> menuList = new ArrayList<String>();
                                int resId = jsonObject.getInt("resId");
                                String image = jsonObject.getString("image");
                                String orderTime = jsonObject.getString("orderTime") + " 서빙 대기중";
                                String resName = jsonObject.getString("resName");
                                Bitmap bitmap = PhotoSave.StringToBitmap(image);
                                JSONArray resMenus = jsonObject.getJSONArray("resMenus");
                                for (int j = 0; j < resMenus.length(); j++) {
                                    JSONObject menu = resMenus.getJSONObject(j);
                                    String menu_name = menu.getString("name");
                                    int price = menu.getInt("price");
                                    menuList.add(menu_name);
                                    sum += price;
                                }
                                JSONArray orderMenus = jsonObject.getJSONArray("orderMenus");
                                for (int j = 0; j < orderMenus.length(); j++) {
                                    JSONObject orderMenu = orderMenus.getJSONObject(j);
                                    int amount = orderMenu.getInt("amount");
                                    if (j == 0) {
                                        menus = menuList.get(j) + " " + amount + "개 ";
                                    } else {
                                        menus += "," + menuList.get(j) + " " + amount + "개 ";
                                    }
                                }
                                OrderItem orderItem = new OrderItem(resId, bitmap, menus, "총액:" + sum + "원", resName, orderTime);
                                items.add(orderItem);
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
        request.setShouldCache(false); // 이전 결과 있어도 새로 요청해 응답을 보내줌
        AppHelper.requestQueue.add(request);
    }
}