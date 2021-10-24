package org.techtown.letseat.photo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import org.techtown.letseat.restaurant.review.RestItemReviewData;
import org.techtown.letseat.util.AppHelper;
import org.techtown.letseat.util.PhotoSave;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhotoList extends AppCompatActivity {
    private PhotoRecyclerAdapter adapter;
    private ImageView photo;
    private TextView title;
    private TextView review;
    static public PhotoList photoList;
    public boolean check = false;
    PhotoFragment photoFragment;
    FragmentManager fm;
    FragmentTransaction ft;
    ArrayList list = new ArrayList();


    //리스트에있는 이미지들을 불러온이미지들로 바꿔주면되네
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_list_activity);
        photoList = this;
        init();
        getData();
        get_Review();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        // 사진 클릭할 시 나오는 이벤트
        adapter.setOnItemClicklistener(new OnPhotoItemClickListener() {
            @Override
            public void onItemClick(PhotoRecyclerAdapter.ItemViewHolder holder, View view,
                                    int position) {
                if (!check) {
                    check = true;
                    photoFragment = new PhotoFragment();
                    ft = fm.beginTransaction();
                    // 여기에 데이터베이스 정보 넣어야 함
                    photoFragment.setresId((Integer) list.get(holder.getAdapterPosition()));
                    photoFragment.setTitle("맛집 제목");
                    photoFragment.setReview("아주 맛있어요~~");
                    ft.add(R.id.photoFragment, photoFragment);
                    ft.commit();
                }
            }
        });
    }
    void get_Review() {
        String url = "http://125.132.62.150:8000/letseat/review/load/res?resId=1";


        JSONArray getData = new JSONArray();

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                getData,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String image;
                            Bitmap bitmap;
                            ArrayList arrayList = new ArrayList();

                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                image = jsonObject.getString("image");
                                bitmap = PhotoSave.StringToBitmap(image);

                                list.add(bitmap);

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
        AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext()); // requsetQueue 초기화
        AppHelper.requestQueue.add(request);
    }

    // 처음 시작 시 리사이클러뷰 세팅하기
    private void init() {
        RecyclerView recyclerView = findViewById(R.id.photoRecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PhotoRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    // 처음 시작 시 리사이클러뷰 불러오기
    private void getData() {
        for (int i = 0; i < list.size(); i++) {
            PhotoData data = new PhotoData();
            data.setResId((Integer) list.get(i));
            adapter.addItem(data);
        }
    }
}