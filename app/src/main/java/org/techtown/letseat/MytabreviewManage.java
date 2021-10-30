package org.techtown.letseat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;

public class MytabreviewManage extends AppCompatActivity {
    private MytabItemRevieAdapter adapter = new MytabItemRevieAdapter();
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

        adapter.setItems(new MyTabreViewData().getItems());

        RecyclerView recyclerView = findViewById(R.id.myTabReviewrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(MytabreviewManage.this));
        recyclerView.setAdapter(adapter);


    }
}