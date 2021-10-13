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

    int resId;
    RecyclerView recyclerView;
    ArrayList list = new ArrayList<>();
    private Order_recycle_adapter adapter = new Order_recycle_adapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);


        Bundle extra = this.getArguments();
        if (extra != null) {
            resId = extra.getInt("resId");  //식당의 resId
        }
        get_Restaurant();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void start(){
        //recycleView 초기화

        adapter.setItems(new Orderdata().getItems(list));
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
        String url = "http://125.132.62.150:8000/letseat/store/menu/load?resId=1";


        JSONArray getData = new JSONArray();

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                getData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String menuName, image, price;
                            int get_resId;
                            Bitmap bitmap;

                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = (JSONObject) response.get(i);

                                menuName = jsonObject.getString("name");
                                image = jsonObject.getString("photo");
                                bitmap = PhotoSave.StringToBitmap(image);
                                price = jsonObject.getString("price");

                                    list.add(bitmap);
                                    list.add(menuName);
                                    list.add(price);
                                    list.add("resName");


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
        AppHelper.requestQueue = Volley.newRequestQueue(getActivity()); // requsetQueue 초기화
        AppHelper.requestQueue.add(request);
    }
}