package org.techtown.letseat.restaurant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.letseat.R;

import java.util.ArrayList;

public class restaurant_info_adapter extends RecyclerView.Adapter<restaurant_info_adapter.ViewHolder> {

    private ArrayList<restaurant_info_item> items = new ArrayList<>();

    @NonNull
    @Override
    public restaurant_info_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_info_recycle, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull restaurant_info_adapter.ViewHolder holder, int position) {

        restaurant_info_item item = items.get(position);

        holder.restaurant_image.setImageResource(item.getSrc());
        holder.res_title.setText(item.getTitle());
        holder.res_subtitle.setText(item.getSubtitle());
        holder.res_place.setText(item.getPlace());
        holder.res_phonenum.setText(item.getPhonenum());
        holder.res_time.setText(item.getTime());
        holder.res_parking.setText(item.getParking());



    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void setItems(ArrayList<restaurant_info_item> items) {
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView restaurant_image;
        TextView res_title, res_subtitle, res_place, res_phonenum, res_time, res_parking;

        ViewHolder(View itemView){
            super(itemView);

            restaurant_image = itemView.findViewById(R.id.restaurant_image);
            res_title = itemView.findViewById(R.id.res_title);
            res_subtitle = itemView.findViewById(R.id.res_subtitle);
            res_place = itemView.findViewById(R.id.res_place);
            res_phonenum = itemView.findViewById(R.id.res_phonenum);
            res_time = itemView.findViewById(R.id.res_time);
            res_parking = itemView.findViewById(R.id.res_parking);
        }
    }
}
