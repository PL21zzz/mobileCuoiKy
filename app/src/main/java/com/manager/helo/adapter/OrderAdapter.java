package com.manager.helo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.helo.R;
import com.manager.helo.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private final RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.idOrder.setText("Đơn hàng:" + order.getId());
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.recycleViewItemOrder.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(order.getItem().size());
        //adapter detail order
        DetailOrderAdapter detailOrderAdapter = new DetailOrderAdapter(context, order.getItem());
        holder.recycleViewItemOrder.setLayoutManager(layoutManager);
        holder.recycleViewItemOrder.setAdapter(detailOrderAdapter);
        holder.recycleViewItemOrder.setRecycledViewPool(recycledViewPool);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView idOrder;
        RecyclerView recycleViewItemOrder;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            idOrder = itemView.findViewById(R.id.idOrder);
            recycleViewItemOrder = itemView.findViewById(R.id.recycleViewItemOrder);
        }
    }
}
