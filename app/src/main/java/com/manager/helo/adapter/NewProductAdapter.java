package com.manager.helo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.manager.helo.Interface.ItemClickListener;
import com.manager.helo.Utils.Utils;
import com.manager.helo.activity.DetailActivity;
import com.manager.helo.model.NewProduct;
import com.manager.helo.R;
import com.manager.helo.model.eventBus.FixDelEvent;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.MyViewHolder> {
    Context context;
    List<NewProduct> array;

    public NewProductAdapter(Context context, List<NewProduct> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_product, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NewProduct newProduct = array.get(position);
        holder.txtPrdName.setText(newProduct.getProduct_name());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        String getPrdPrice = "Giá: " + decimalFormat.format(Double.parseDouble(newProduct.getProduct_price())) + "Đ";
        holder.txtPrdPrice.setText(getPrdPrice);
        if (newProduct.getProduct_image().contains("http")) {
            Glide.with(context).load(newProduct.getProduct_image()).into(holder.imgPrdImage);
        }else {
            String img = Utils.baseURL + "images/" + newProduct.getProduct_image();
            Glide.with(context).load(img).into(holder.imgPrdImage);
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isOnclick) {
                if (!isOnclick) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("detail", newProduct);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else {
                    EventBus.getDefault().postSticky(new FixDelEvent(newProduct));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {
        TextView txtPrdName, txtPrdPrice;
        ImageView imgPrdImage;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPrdName = itemView.findViewById(R.id.itemNew_prdName);
            txtPrdPrice = itemView.findViewById(R.id.itemNew_prdPrice);
            imgPrdImage = itemView.findViewById(R.id.itemNew_prdImage);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0,0,getAdapterPosition(), "Sửa");
            menu.add(0,1,getAdapterPosition(), "Xóa");
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return false;
        }
    }
}
