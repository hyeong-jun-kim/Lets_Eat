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

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private MenuAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Menu> list = new ArrayList<>();

    private String mParam1;
    private String mParam2;

    public res_info_fragment1() {
        // Required empty public constructor
    }

    public static res_info_fragment1 newInstance(String param1, String param2) {
        res_info_fragment1 fragment = new res_info_fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prepareDate();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.res_info_fragment1, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        adapter = new MenuAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;

    }

    private void prepareDate(){
        list.clear();
        list.add(new Menu("마라탕","10000원", R.drawable.menuimg1));
        list.add(new Menu("해물탕", "5000원", R.drawable.menuimg2));

    }
}