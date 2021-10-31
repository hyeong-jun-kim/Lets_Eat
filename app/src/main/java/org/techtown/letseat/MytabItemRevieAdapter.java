package org.techtown.letseat;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MytabItemRevieAdapter extends RecyclerView.Adapter<MytabItemRevieAdapter.ViewHolder> {

    private ArrayList<MyTabreViewItem> items = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public MytabItemRevieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mytabreview_recycler, parent, false);
        MytabItemRevieAdapter.ViewHolder viewHolder = new MytabItemRevieAdapter.ViewHolder(itemView);
        context = parent.getContext();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MytabItemRevieAdapter.ViewHolder viewHolder, int position) {

        MyTabreViewItem item = items.get(position);

        viewHolder.dateTv.setText(item.getDate());
        viewHolder.idTv.setText(item.getId());
        viewHolder.MenuTv.setText(item.getMenuTv());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<MyTabreViewItem> items) {
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView reviewImageIv;
        TextView dateTv,idTv, ratingBarTv,reviewTv,MenuTv;
        RatingBar ratingBar;


        ViewHolder(View itemView) {
            super(itemView);

            reviewImageIv = itemView.findViewById(R.id.reviewImageIv);
            dateTv = itemView.findViewById(R.id.dateTv);
            idTv = itemView.findViewById(R.id.idTv);
            reviewTv = itemView.findViewById(R.id.reviewTv);
            ratingBarTv = itemView.findViewById(R.id.ratingBarTv);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            MenuTv = itemView.findViewById(R.id.MenuTv);

        }
    }
}


