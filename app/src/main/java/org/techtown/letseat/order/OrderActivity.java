package org.techtown.letseat.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

import org.techtown.letseat.MainActivity;
import org.techtown.letseat.R;
import org.techtown.letseat.RestSearch2;
import org.techtown.letseat.restaurant.info.Res_info_fragment2;
import org.techtown.letseat.restaurant.info.res_info_fragment1;
import org.techtown.letseat.restaurant.info.res_info_fragment3;
import org.techtown.letseat.util.ViewPagerAdapter;

public class OrderActivity extends AppCompatActivity {

    Fragment_order fragment_order;
    Fragment_order2 fragment_order2;
    private ViewPager viewPager;
    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_main);

        viewPager = findViewById(R.id.orderView_pager);
        viewPager.setOffscreenPageLimit(2);

        tabs = findViewById(R.id.orderTab_layout);
        tabs.setupWithViewPager(viewPager);

        fragment_order = new Fragment_order();
        fragment_order2 = new Fragment_order2();

        MaterialToolbar toolbar = findViewById(R.id.topMain);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(homeIntent);
            }
        });

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(fragment_order,"주문 내역");
        viewPagerAdapter.addFragment(fragment_order2,"준비중");
        viewPager.setAdapter(viewPagerAdapter);

        tabs.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });
    }
}