package org.techtown.letseat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.letseat.R;
import org.techtown.letseat.order.Orderdata;

import java.util.ArrayList;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>{
    private ArrayList<MainRecyclerData> Dataset = new ArrayList<>();
    private Context context;
    private Intent intent;





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_rest_recycler, parent, false);
        MainRecyclerAdapter.ViewHolder viewHolder = new MainRecyclerAdapter.ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerAdapter.ViewHolder holder, int position){

        MainRecyclerData item = Dataset.get(position);

        holder.restNameTv.setText(item.getRestNameTv());
        holder.restIv.setImageBitmap(item.getBitmap());

    }
    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos);
    }

    private MainRecyclerAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(MainRecyclerAdapter.OnItemClickListener listener)
    {
        this.mListener = listener;
    }
    @Override
    public int getItemCount(){
        return Dataset.size();
    }

    public void setItems(ArrayList<MainRecyclerData> items) {
        this.Dataset = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView restNameTv;
        public View view1;
        public ImageView restIv;

        public ViewHolder(View view){
            super(view);
            restNameTv = (TextView) view.findViewById(R.id.restNameTv);
            restIv = (ImageView) view.findViewById(R.id.restIv);


            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                    {
                        mListener.onItemClick(v, pos);
                    }
                }
            });
        }
    }
}
