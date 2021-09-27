package org.techtown.letseat.restaurant.info;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import org.techtown.letseat.R;

public class RestInfoMain extends AppCompatActivity {
    res_info_fragment1 fragment1;
    res_info_fragment2 fragment2;
    res_info_fragment3 fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restab_info_activity);

        fragment1 = new res_info_fragment1();
        fragment2 = new res_info_fragment2();
        fragment3 = new res_info_fragment3();

        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment1).commit();

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("메뉴"));
        tabs.addTab(tabs.newTab().setText("정보"));
        tabs.addTab(tabs.newTab().setText("리뷰"));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                if(position == 0){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment1).commit();
                }
                else if(position == 1){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment2).commit();
                }
                else if(position ==2){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment3).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}