package org.techtown.letseat.restaurant.qr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.letseat.R;
import org.techtown.letseat.menu.Menu;
import org.techtown.letseat.menu.MenuAdapter;
import org.techtown.letseat.menu.OnMenuItemClickListner;

import java.util.ArrayList;
import java.util.HashMap;

public class QR_MenuAdapter extends RecyclerView.Adapter<QR_MenuAdapter.ViewHolder> implements OnMenuItemClickListner {
    ArrayList<QR_Menu> items = new ArrayList<QR_Menu>();
    ArrayList<String> selectMenu = new ArrayList<>();
    OnMenuItemClickListner listner;
    static public int sum = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.menu_recycler2, viewGroup, false);
        qr_restActivity.sumTextView.setText("0");
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // BindView 초기화
        QR_Menu item = items.get(position);
        viewHolder.name.setText(item.getName());
        viewHolder.price.setText(item.getPrice() + "원");
        viewHolder.amount.setText("0");
        viewHolder.upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(viewHolder.amount.getText().toString());
                // 장바구니에 메뉴 넣기
                if (num >= 0) {
                    if (!selectMenu.contains(""+position)) {
                        selectMenu.add(""+position);
                    }
                }
                int price = Integer.parseInt(item.getPrice());
                sum += price;
                qr_restActivity.sumTextView.setText(sum + "원");
                viewHolder.amount.setText("" + (num + 1));
            }
        });
        viewHolder.downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(viewHolder.amount.getText().toString());
                if (num > 0) {
                    if (num - 1 == 0) {
                        if (selectMenu.contains(""+position))
                            selectMenu.remove(position+"");
                    }
                    int price = Integer.parseInt(item.getPrice());
                    sum -= price;
                    qr_restActivity.sumTextView.setText(sum + "원");
                    viewHolder.amount.setText("" + (num - 1));
                }
            }
        });

    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<QR_Menu> items) {
        this.items = items;
    }

    public void setItemClickListner(OnMenuItemClickListner listner) {
        this.listner = listner;
    }

    public QR_Menu getItem(int position) {
        return items.get(position);
    }

    // 장바구니 반환
    public ArrayList<String> getSelectMenu() {
        return selectMenu;
    }

    @Override
    public void OnItemClick(MenuAdapter.ViewHolder holder, View view, int position) {
        if (listner != null) {
            listner.OnItemClick(holder, view, position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;
        TextView amount;
        ImageView imageView;
        ImageButton upButton, downButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.image);
            upButton = itemView.findViewById(R.id.up_button);
            downButton = itemView.findViewById(R.id.down_button);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    /*if(listner !=null){
                        listner.OnItemClick(ViewHolder.this,v,position);
                    }*/
                }
            });
        }


    }
}
