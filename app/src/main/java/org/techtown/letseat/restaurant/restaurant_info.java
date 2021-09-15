package org.techtown.letseat.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.techtown.letseat.R;
import org.techtown.letseat.menu.MenuActivity;
import org.techtown.letseat.mytab.MyTab;

public class restaurant_info extends AppCompatActivity {

    private restaurant_info_adapter adapter = new restaurant_info_adapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.res_info_activity);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setItems(new restaurant_info_data().getItems());

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