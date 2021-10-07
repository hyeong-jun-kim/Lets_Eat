package org.techtown.letseat.restaurant.info;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.techtown.letseat.R;
import org.techtown.letseat.RestItemReviewAdapter;
import org.techtown.letseat.RestItemReviewData;
import org.techtown.letseat.Review.Reviewdata;

import java.util.ArrayList;

public class res_info_fragment3 extends Fragment {

    View view;
    private RestItemReviewAdapter adapter = new RestItemReviewAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.res_info_fragment3, container, false);

        adapter.setItems(new RestItemReviewData().getItems());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        return view;

    }

}