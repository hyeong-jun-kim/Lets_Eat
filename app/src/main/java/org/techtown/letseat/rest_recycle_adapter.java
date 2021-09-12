package org.techtown.letseat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class rest_recycle_adapter extends RecyclerView.Adapter<rest_recycle_adapter.ViewHolder> {

    private ArrayList<rest_recycle_item> items = new ArrayList<>();

    @NonNull
    @Override
    public rest_recycle_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_recycle, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull rest_recycle_adapter.ViewHolder viewHolder, int position) {

        rest_recycle_item item = items.get(position);

        viewHolder.ivRest.setImageResource(item.getSrc());
        viewHolder.tvName.setText(item.getName());
        viewHolder.tvAdress.setText(item.getAdress());
        viewHolder.tvGenre.setText(item.getGenre());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<rest_recycle_item> items) {
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivRest;
        TextView tvName, tvAdress, tvGenre;

        ViewHolder(View itemView) {
            super(itemView);

            ivRest = itemView.findViewById(R.id.iv_item_res);

            tvName = itemView.findViewById(R.id.tv_item_res_name);
            tvAdress = itemView.findViewById(R.id.tv_item_res_adress);
            tvGenre = itemView.findViewById(R.id.tv_item_food_genre);
        }
    }
}
