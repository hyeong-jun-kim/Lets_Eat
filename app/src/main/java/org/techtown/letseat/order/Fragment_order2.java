package org.techtown.letseat.order;

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
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.util.PhotoSave;

import java.util.ArrayList;

public class Fragment_order2 extends Fragment {
    View view;
    private Order_recycle_adapter2 adapter2 = new Order_recycle_adapter2();
    ArrayList<Integer> orderLists = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order2, container, false);

        adapter2.setItems(new Orderdata2().getItems());

        RecyclerView recyclerView2 = view.findViewById(R.id.recycler_view2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView2.setAdapter(adapter2);

        return view;
    }
    // 현재 대기중인 주문리스트 받기
    void getWatingOrderList() {
        int userId = MainActivity.userId;
        String url = "http://125.132.62.150:8000/letseat/order/list/user?userId="+userId;
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String menuName, image, price;
                            int get_resId;
                            Bitmap bitmap;
                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                int orderId  = jsonObject.getInt("orderId");
                                orderLists.add(orderId);
                            }
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
        AppHelper.requestQueue = Volley.newRequestQueue(getActivity()); // requsetQueue 초기화
        AppHelper.requestQueue.add(request);
    }
    // 현재 대기중인 메뉴리스트 받기
    void getMenuList() {
        for (int i = 0; i < orderLists.size(); i++) {
            String url = "http://125.132.62.150:8000/letseat//order/menu/load=";
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                String menuName, image, price;
                                int get_resId;
                                Bitmap bitmap;
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) response.get(i);
                                    int orderId = jsonObject.getInt("orderId");
                                    orderLists.add(orderId);
                                }
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
            AppHelper.requestQueue = Volley.newRequestQueue(getActivity()); // requsetQueue 초기화
            AppHelper.requestQueue.add(request);
        }
    }
}