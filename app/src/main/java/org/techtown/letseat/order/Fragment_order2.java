package org.techtown.letseat.order;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.techtown.letseat.R;

public class Fragment_order2 extends Fragment {
    View view;
    private Order_recycle_adapter2 adapter2 = new Order_recycle_adapter2();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order2, container, false);

        adapter2.setItems(new Orderdata2().getItems());

        RecyclerView recyclerView2 = view.findViewById(R.id.recycler_view2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView2.setAdapter(adapter2);

        return view;
    }
}