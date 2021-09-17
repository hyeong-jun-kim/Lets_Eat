package org.techtown.letseat.restaurant.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.letseat.R;
import org.techtown.letseat.restaurant.info.RestInfoAdapter;

import java.util.ArrayList;

public class RestListMain extends AppCompatActivity {

    private RestListAdapter adapter = new RestListAdapter ();
    private RestInfoAdapter restaurant_info_adapter = new RestInfoAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.res_list_activity);

        //recycleView 초기화
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setItems(new RestListData().getItems());

        adapter.setItemClickListner(new OnRestaurantItemClickListner() {
            @Override
            public void OnItemClick(RestListAdapter.ViewHolder holder, View view, int position) {

                int adapterPosition = holder.getAdapterPosition();

                RestListRecycleItem item = adapter.getItem(position);


                if(adapterPosition == 1){
                    Intent intent = new Intent(getApplicationContext(),RestIn.class);
                    startActivity(intent);
                }
            }
        });
    }
}