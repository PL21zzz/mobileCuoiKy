package com.manager.helo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.manager.helo.R;
import com.manager.helo.model.Item;

import java.util.List;

public class DetailOrderAdapter extends RecyclerView.Adapter<DetailOrderAdapter.MyViewHolder> {
    Context context;
    List<Item> itemList;

    public DetailOrderAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_order, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.prdNameItemOrder.setText(item.getProduct_name() + " ");
        holder.prdQttItemOrder.setText("Số lượng: " + item.getQuantity());
        Glide.with(context).load(item.getProduct_image()).into(holder.imgItemDetail);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgItemDetail;
        TextView prdNameItemOrder, prdQttItemOrder;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgItemDetail = itemView.findViewById(R.id.imgItemDetail);
            prdNameItemOrder = itemView.findViewById(R.id.prdNameItemOrder);
            prdQttItemOrder = itemView.findViewById(R.id.prdQttItemOrder);
        }
    }
}
