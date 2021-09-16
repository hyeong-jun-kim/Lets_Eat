package org.techtown.letseat.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import org.techtown.letseat.R;

public class OrderActivity extends AppCompatActivity {

    public FragmentManager fm;
    FragmentTransaction tran;
    fragment_order fragment_order;
    fragment_order2 fragment_order2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_main);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        fragment_order = new fragment_order();
        fragment_order2 = new fragment_order2();

        getSupportFragmentManager().beginTransaction().replace(R.id.order_frame,fragment_order).commit();
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

        fm = getSupportFragmentManager();
        tran = fm.beginTransaction();

        switch (index) {
            case 0 :
                tran.replace(R.id.order_frame, fragment_order);
                tran.commit();
                break ;
            case 1 :
                tran.replace(R.id.order_frame, fragment_order2);
                tran.commit();
                break ;
        }
    }
}