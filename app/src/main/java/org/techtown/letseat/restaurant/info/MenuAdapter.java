package org.techtown.letseat.restaurant.info;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.letseat.R;
import org.techtown.letseat.menu.Menu;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{
    private ArrayList<Menu> mDataset;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price;
        public ImageView image;

        //ViewHolder
        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view. findViewById(R.id.text);
            image = (ImageView) view. findViewById(R.id.image);
        }
    }

    public MenuAdapter(ArrayList<Menu> myData){
        this.mDataset = myData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_recycle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.ViewHolder holder, int position) {


        holder.name.setText(mDataset.get(position).getName());

        //클릭이벤트
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //클릭시 name과 좌표정보를 지도 프래그먼트로 보내자.
                AppCompatActivity activity = (AppCompatActivity) view.getContext();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}