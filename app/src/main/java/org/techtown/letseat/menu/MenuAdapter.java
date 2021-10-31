package org.techtown.letseat.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.letseat.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>implements OnMenuItemClickListner{
    ArrayList<Menu> items = new ArrayList<Menu>();

    OnMenuItemClickListner listner;

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
        viewHolder.name.setText(item.getName());
        viewHolder.price.setText(item.getPrice()+"원");
        viewHolder.imageView.setImageBitmap(item.getBitmap());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Menu> items){
        this.items = items;
    }


    public void OnItemClick(ViewHolder holder, View view, int position){
        if(listner != null){
            listner.OnItemClick(holder,view,position);
        }
    }


    public void setItemClickListner(OnMenuItemClickListner listner){
        this.listner = listner;
    }

    public Menu getItem(int position){
        return items.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView price;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position =getAdapterPosition();
                    if(listner !=null){
                        listner.OnItemClick(ViewHolder.this,v,position);
                    }

                }
            });
        }

    }
}
