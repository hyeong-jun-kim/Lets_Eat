package org.techtown.letseat.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.letseat.R;

import java.util.ArrayList;

public class Order_recycle_adapter2 extends RecyclerView.Adapter<Order_recycle_adapter2.ViewHolder> {

    private ArrayList<Order_recycle_item> items = new ArrayList<>();

    @NonNull
    @Override
    public Order_recycle_adapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_recycle2, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Order_recycle_adapter2.ViewHolder viewHolder, int position) {

        Order_recycle_item item = items.get(position);

        viewHolder.ivRest.setImageResource(item.getSrc());
        viewHolder.tvName.setText(item.getName());
        viewHolder.tvmenuName.setText(item.getMenuName());
        viewHolder.tvPrice.setText(item.getPrice());
        viewHolder.tvOrder.setText(item.getOrder());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Order_recycle_item> items) {
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivRest;
        TextView tvName, tvmenuName, tvPrice, tvOrder;


        ViewHolder(View itemView) {
            super(itemView);

            ivRest = itemView.findViewById(R.id.iv_item_order_res);
            tvName = itemView.findViewById(R.id.tv_item_order_name);
            tvmenuName = itemView.findViewById(R.id.tv_item_order_menuName);
            tvPrice = itemView.findViewById(R.id.tv_item_order_price);
            tvOrder = itemView.findViewById(R.id.tv_item_order_complete);
        }
    }
}

