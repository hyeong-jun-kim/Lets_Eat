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

public class RestItemReviewAdapter extends RecyclerView.Adapter<RestItemReviewAdapter.ViewHolder> {

    private ArrayList<RestItemReviewItem> items = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_recycle, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        context = parent.getContext();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        RestItemReviewItem item = items.get(position);

        viewHolder.userImageIv.setImageResource(item.getSrc());
        viewHolder.dateTv.setText(item.getDate());
        viewHolder.idTv.setText(item.getId());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<RestItemReviewItem> items) {
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImageIv, reviewImageIv;
        TextView dateTv,idTv, ratingBarTv,reviewTv;
        RatingBar ratingBar;


        ViewHolder(View itemView) {
            super(itemView);

            userImageIv = itemView.findViewById(R.id.userImageIv);
            reviewImageIv = itemView.findViewById(R.id.reviewImageIv);
            dateTv = itemView.findViewById(R.id.dateTv);
            idTv = itemView.findViewById(R.id.idTv);
            reviewTv = itemView.findViewById(R.id.reviewTv);
            ratingBarTv = itemView.findViewById(R.id.ratingBarTv);
            ratingBar = itemView.findViewById(R.id.ratingBar);

        }
    }
}

