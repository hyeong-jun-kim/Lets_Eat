package org.techtown.letseat.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.letseat.R;

import java.util.ArrayList;

public class Rest_List extends AppCompatActivity {

    private rest_recycle_adapter adapter = new rest_recycle_adapter();
    private restaurant_info_adapter restaurant_info_adapter = new restaurant_info_adapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.res_list_activity);

        //recycleView 초기화
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setItems(new data().getItems());

        adapter.setItemClickListner(new OnRestaurantItemClickListner() {
            @Override
            public void OnItemClick(rest_recycle_adapter.ViewHolder holder, View view, int position) {

                int adapterPosition = holder.getAdapterPosition();

                rest_recycle_item item = adapter.getItem(position);

                if(adapterPosition == 1){
                    Intent intent = new Intent(getApplicationContext(),restaurant_info.class);
                    startActivity(intent);
                }
            }
        });
    }
}