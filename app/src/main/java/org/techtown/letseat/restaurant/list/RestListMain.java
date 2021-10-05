package org.techtown.letseat.restaurant.list;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.letseat.AppHelper;
import org.techtown.letseat.R;
import org.techtown.letseat.restaurant.info.RestInfoMain;


import java.util.ArrayList;

public class RestListMain extends AppCompatActivity {

    ArrayList list = new ArrayList<>();

    private RestListAdapter adapter = new RestListAdapter();
    private RestListAdapter restaurant_info_adapter = new RestListAdapter();
    RecyclerView recyclerView;
    Button koreanFood_button, chinaFood_button, japanFood_button, westernFood_Button, onemanFood_Button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.res_list_activity);
        recyclerView = findViewById(R.id.recycler_view);

        koreanFood_button = findViewById(R.id.koreanFood_button);
        koreanFood_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RestList_KoreanFood.class);
                startActivity(intent);
            }
        });

        chinaFood_button = findViewById(R.id.chinaFood_button);
        chinaFood_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RestList_chinaFood.class);
                startActivity(intent);
            }
        });

        japanFood_button = findViewById(R.id.japanFood_button);
        japanFood_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RestList_japanFood.class);
                startActivity(intent);
            }
        });

        westernFood_Button = findViewById(R.id.westernFood_Button);
        westernFood_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RestList_westernFood.class);
                startActivity(intent);
            }
        });

        onemanFood_Button = findViewById(R.id.onemanFood_Button);
        onemanFood_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RestList_onemanFood.class);
                startActivity(intent);
            }
        });

        get_Restaurant();
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



                Intent intent = new Intent(getApplicationContext(), RestInfoMain.class);
                intent.putExtra("aP",adapterPosition);
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
                            String restype,resName,location;
                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                restype = jsonObject.getString("restype");
                                resName = jsonObject.getString("resName");
                                location = jsonObject.getString("location");


                                list.add(restype);
                                list.add(resName);
                                list.add(location);
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