package org.techtown.letseat.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import org.techtown.letseat.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        MenuAdapter adapter = new MenuAdapter();

        // 여기는 리사이클러 뷰 테스트용임
        adapter.addItem(new Menu("마라탕", "3000원", R.drawable.menuimg1));
        adapter.addItem(new Menu("해물탕", "5000원", R.drawable.menuimg2));

        recyclerView.setAdapter(adapter);
    }
}