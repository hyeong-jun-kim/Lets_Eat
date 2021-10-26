package org.techtown.letseat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;

import org.techtown.letseat.restaurant.review.RestItemReviewAdapter;
import org.techtown.letseat.restaurant.review.RestItemReviewData;

public class MytabreviewManage extends AppCompatActivity {
    private mytabitemreviewadapter adapter = new mytabitemreviewadapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytabreview_manage);

        MaterialToolbar toolbar = findViewById(R.id.topMain);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapter.setItems(new mytabreviewdata().getItems());

        RecyclerView recyclerView = findViewById(R.id.myTabReviewrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(MytabreviewManage.this));
        recyclerView.setAdapter(adapter);


    }
}