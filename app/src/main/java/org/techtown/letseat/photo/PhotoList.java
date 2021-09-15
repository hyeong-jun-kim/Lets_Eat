package org.techtown.letseat.photo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.letseat.R;

import java.util.Arrays;
import java.util.List;

public class PhotoList extends AppCompatActivity {
    private PhotoRecyclerAdapter adapter;
    private ImageView photo;
    private TextView title;
    private TextView review;
    private boolean check = false;
    PhotoFragment photoFragment;
    FragmentManager fm;
    FragmentTransaction ft;
    List<Integer> listResId = Arrays.asList(R.drawable.image1, R.drawable.image2, R.drawable.image3,
            R.drawable.menuimg1, R.drawable.menuimg2, R.drawable.menuimg3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_list_activity);
        init();
        getData();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        // 사진 클릭할 시 나오는 이벤트
        adapter.setOnItemClicklistener(new OnPhotoItemClickListener() {
            @Override
            public void onItemClick(PhotoRecyclerAdapter.ItemViewHolder holder, View view,
                                    int position) {
                Log.d("ActivityEmpty", ""+getSupportFragmentManager().beginTransaction().isEmpty());
                if (getSupportFragmentManager().beginTransaction().isEmpty()) {
                    photoFragment = new PhotoFragment();
                    ft = fm.beginTransaction();
                    photoFragment.setresId(listResId.get(holder.getAdapterPosition()));
                    photoFragment.setTitle("맛집 제목");
                    photoFragment.setReview("아주 맛있어요~~");
                    ft.add(R.id.photoFragment, photoFragment);
                    ft.commit();
                }
            }
        });
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
        for (int i = 0; i < listResId.size(); i++) {
            PhotoData data = new PhotoData();
            data.setResId(listResId.get(i));
            adapter.addItem(data);
        }
    }
}