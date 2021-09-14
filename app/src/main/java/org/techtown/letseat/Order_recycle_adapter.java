package org.techtown.letseat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Order_recycle_adapter extends RecyclerView.Adapter<Order_recycle_adapter.ViewHolder> {

    private ArrayList<Order_recycle_item> items = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public Order_recycle_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_recycle, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        context = parent.getContext();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Order_recycle_adapter.ViewHolder viewHolder, int position) {

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
        Button btnReview;

        ViewHolder(View itemView) {
            super(itemView);

            ivRest = itemView.findViewById(R.id.iv_item_order_res);
            tvName = itemView.findViewById(R.id.tv_item_order_name);
            tvmenuName = itemView.findViewById(R.id.tv_item_order_menuName);
            tvPrice = itemView.findViewById(R.id.tv_item_order_price);
            tvOrder = itemView.findViewById(R.id.tv_item_order_complete);
            btnReview = itemView.findViewById(R.id.btnReview);

            btnReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,ReviewActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}

