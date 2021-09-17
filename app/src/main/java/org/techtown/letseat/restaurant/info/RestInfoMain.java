package org.techtown.letseat.restaurant.info;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.techtown.letseat.R;
import org.techtown.letseat.menu.MenuActivity;
import org.techtown.letseat.restaurant.list.RestListRecycleItem;

import java.util.ArrayList;

public class RestInfoMain extends AppCompatActivity {

    private RestInfoAdapter adapter = new RestInfoAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.res_info_activity);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        int adapterPosition = savedInstanceState.getInt("num");
        adapter.setItems(new RestInfoDataMain().getItems());

        Button menu_button = findViewById(R.id.menu_button);
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(intent);
            }
        });
    }
}