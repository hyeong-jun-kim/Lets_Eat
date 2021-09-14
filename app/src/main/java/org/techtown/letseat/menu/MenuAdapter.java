package org.techtown.letseat.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.letseat.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{
    ArrayList<Menu> items = new ArrayList<Menu>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.menu_recycle, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Menu item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(Menu item){
        items.add(item);
    }
    public void setItems(ArrayList<Menu> items){
        this.items = items;
    }
    public Menu getItem(int position){
        return items.get(position);
    }
    public void setItem(int position, Menu item){
        items.set(position, item);
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView price;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.image);
        }
        public void setItem(Menu item){
            name.setText(item.getName());
            price.setText(item.getPrice());
            imageView.setImageResource(item.getResId());
        }
    }
}
