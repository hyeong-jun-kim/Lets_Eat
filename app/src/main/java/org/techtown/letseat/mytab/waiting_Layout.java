package org.techtown.letseat.mytab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.techtown.letseat.R;
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.RestSearch2;
import org.techtown.letseat.restaurant.list.RestListAdapter;

public class waiting_Layout extends AppCompatActivity {
    TextView peopleNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytab_waiting);
        peopleNum = findViewById(R.id.peopleNum);
    }
    void getWatingOrderList() {
        String url = "http://125.132.62.150:8000/letseat/waiting/user/load?userId=1";
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                                int waiting_number = response.getInt(0);
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
        request.setShouldCache(false); // 이전 결과 있어도 새로 요청해 응답을 보냄
        AppHelper.requestQueue.add(request);

        MaterialToolbar toolbar = findViewById(R.id.topMain);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}