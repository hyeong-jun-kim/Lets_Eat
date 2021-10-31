package org.techtown.letseat.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.techtown.letseat.R;
import org.techtown.letseat.Review.ReviewActivity;
import org.techtown.letseat.util.AppHelper;

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
        if(!item.getReviewYN().equals("N")){
            viewHolder.btnReview.setVisibility(View.INVISIBLE);
            viewHolder.btnReview.setEnabled(false);
        }else{
            viewHolder.btnReview.setVisibility(View.VISIBLE);
            viewHolder.btnReview.setEnabled(true);
        }
        viewHolder.ivRest.setImageBitmap(item.getBitmap());
        viewHolder.tvName.setText(item.getResName());
        viewHolder.tvmenuName.setText(item.getMenuName());
        viewHolder.tvPrice.setText(item.getPrice());
        viewHolder.tvOrder.setText(item.getOrderTime());
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
                    ViewHolder viewHolder = ViewHolder.this;
                    checkReviewYN(items.get(position).getOrderId(),position);
                    }
            });
        }
        public void checkReviewYN(int orderId, int position){
            String url = "http://125.132.62.150:8000/letseat/order/list/get/reviewYN?orderId="+orderId;
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("Y")){
                                Toast.makeText(context, "현재 주문은 리뷰가 등록된 상태입니다.", Toast.LENGTH_SHORT).show();
                            }else{
                                Bundle bundle = new Bundle();
                                bundle.putInt("resId",items.get(position).getResId());
                                bundle.putInt("orderId",items.get(position).getOrderId());
                                Intent intent = new Intent(context, ReviewActivity.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("error", error.toString());
                        }
                    }
            );
            request.setShouldCache(false); // 이전 결과 있어도 새로 요청해 응답을 보내줌
            AppHelper.requestQueue.add(request);
        }
    }
}

