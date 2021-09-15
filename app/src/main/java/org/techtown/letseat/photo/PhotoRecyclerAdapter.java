package org.techtown.letseat.photo;

import android.provider.ContactsContract;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.letseat.R;

import java.util.ArrayList;

public class PhotoRecyclerAdapter extends RecyclerView.Adapter<PhotoRecyclerAdapter.ItemViewHolder> implements OnPhotoItemClickListener{
    private ArrayList<PhotoData> photoData = new ArrayList<>();
    private OnPhotoItemClickListener listener;
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_recycle, parent, false);
        return new ItemViewHolder(view, this);
    }
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(photoData.get(position));
    }
    @Override
    public int getItemCount() {
        return photoData.size();
    }
    void addItem(PhotoData data){
        photoData.add(data);
    }
    public void setOnItemClicklistener(OnPhotoItemClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onItemClick(ItemViewHolder holder, View view, int position) {
        if(listener != null)
            listener.onItemClick(holder, view, position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
    private ImageView photo;
        public ItemViewHolder(View itemView, final OnPhotoItemClickListener listener) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ItemViewHolder.this, v, position);
                    }
                }
            });
        }
        void onBind(PhotoData data){
            photo.setImageResource(data.getResId());
        }
    }
}
