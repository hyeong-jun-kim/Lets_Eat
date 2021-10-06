package org.techtown.letseat.restaurant.info;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.techtown.letseat.R;
import org.techtown.letseat.menu.Menu;

import java.util.ArrayList;

public class res_info_fragment1 extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.res_info_fragment1, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        org.techtown.letseat.menu.MenuAdapter adapter = new org.techtown.letseat.menu.MenuAdapter();

        adapter.addItem(new Menu("마라탕", "3000원", R.drawable.menuimg1));
        adapter.addItem(new Menu("해물탕", "5000원", R.drawable.menuimg2));

        recyclerView.setAdapter(adapter);

        return view;

    }
}