package org.techtown.letseat.order;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.letseat.R;
import org.techtown.letseat.Review.ReviewActivity;

import java.util.ArrayList;

public class Order_recycle_adapter extends RecyclerView.Adapter<Order_recycle_adapter.ViewHolder>
implements OnReviewItemClickListner{

    private ArrayList<OrderItem> items = new ArrayList<>();
    private Context context;
    OnReviewItemClickListner listner;

    @NonNull
    @Override
    public Order_recycle_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_recycle, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        context = parent.getContext();

        return viewHolder;
    }

    public void OnItemClick(ViewHolder holder, View view, int position){
        if(listner != null){
            listner.OnItemClick(holder,view,position);
        }
    }
    public void setItemClickListner(OnReviewItemClickListner listner){
        this.listner = listner;

    }
    @Override
    public void onBindViewHolder(@NonNull Order_recycle_adapter.ViewHolder viewHolder, int position) {

        OrderItem item = items.get(position);

        viewHolder.ivRest.setImageBitmap(item.getBitmap());
        viewHolder.tvName.setText(item.getResName());
        viewHolder.tvmenuName.setText(item.getMenuName());
        viewHolder.tvPrice.setText(item.getPrice());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<OrderItem> items) {
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivRest;
        TextView tvName, tvmenuName, tvPrice, tvOrder;
        ImageButton btnReview;

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
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listner != null){
                        listner.OnItemClick(ViewHolder.this,v,position);
                    }
                    Intent intent = new Intent(context, ReviewActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}

