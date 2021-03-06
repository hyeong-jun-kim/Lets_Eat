package org.techtown.letseat.restaurant.qr;

import android.annotation.SuppressLint;
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
    HashMap<Integer, Integer> amount_map = new HashMap<>();
    OnMenuItemClickListner listner;
    static public int sum = 0;
    static public HashMap<Integer, String> menuNames = new HashMap<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.menu_recycler2, viewGroup, false);
        qr_restActivity.sumTextView.setText("0");
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        // BindView 초기화
        QR_Menu item = items.get(position);
        viewHolder.imageView.setImageBitmap(item.getBitmap());
        viewHolder.name.setText(item.getName());
        viewHolder.price.setText(item.getPrice() + "원");
        viewHolder.excription.setText(item.getExcription());
        viewHolder.amount.setText("0");
        viewHolder.upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = viewHolder.name.getText().toString();
                int num = Integer.parseInt(viewHolder.amount.getText().toString());
                // 장바구니에 메뉴 넣기
                if (num >= 0) {
                    if (!selectMenu.contains(""+position)) {
                        selectMenu.add(""+position);
                    }
                }
                int price = Integer.parseInt(item.getPrice());
                menuNames.put(position, name+(num+1)+"개 ");
                sum += price;
                qr_restActivity.sumTextView.setText(sum + "원");
                viewHolder.amount.setText("" + (num + 1));
                amount_map.put(position, num+1);
            }
        });
        viewHolder.downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = viewHolder.name.getText().toString();
                int num = Integer.parseInt(viewHolder.amount.getText().toString());
                if (num > 0) {
                    if (num - 1 == 0) {
                        if (selectMenu.contains(""+position))
                            selectMenu.remove(position+"");
                        if(amount_map.containsKey(position)){
                            amount_map.remove(position);
                        }
                        if(menuNames.containsKey(position)){
                            menuNames.remove(position);
                        }
                        int price = Integer.parseInt(item.getPrice());
                        sum -= price;
                        qr_restActivity.sumTextView.setText(sum + "원");
                        viewHolder.amount.setText("" + (num - 1));
                        return;
                    }
                    int price = Integer.parseInt(item.getPrice());
                    sum -= price;
                    qr_restActivity.sumTextView.setText(sum + "원");
                    viewHolder.amount.setText("" + (num - 1));
                    amount_map.put(position, num-1);
                    menuNames.put(position, name+(num-1)+"개 ");
                }else{

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

    // amount hashMap 반환
    public HashMap<Integer, Integer> getAmount_map(){return amount_map;}

    @Override
    public void OnItemClick(MenuAdapter.ViewHolder holder, View view, int position) {
        if (listner != null) {
            listner.OnItemClick(holder, view, position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;
        TextView excription;
        TextView amount;
        ImageView imageView;
        ImageButton upButton, downButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            name = itemView.findViewById(R.id.name);
            excription = itemView.findViewById(R.id.excription);
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