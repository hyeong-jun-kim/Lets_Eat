package org.techtown.letseat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

public class OrderActivity extends AppCompatActivity {

    private Order_recycle_adapter adapter = new Order_recycle_adapter();
    private Order_recycle_adapter2 adapter2 = new Order_recycle_adapter2();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);


        adapter.setItems(new Orderdata().getItems());
        adapter2.setItems(new Orderdata2().getItems());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                changeView(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        }) ;


    }
    private void changeView(int index) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView recyclerView2 = findViewById(R.id.recycler_view2);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView2.setAdapter(adapter2);

        switch (index) {
            case 0 :
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView2.setVisibility(View.INVISIBLE);
                break ;
            case 1 :
                recyclerView.setVisibility(View.INVISIBLE);
                recyclerView2.setVisibility(View.VISIBLE);
                break ;
        }
    }
}