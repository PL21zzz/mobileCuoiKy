package com.manager.helo.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.manager.helo.Interface.ImageClickListener;
import com.manager.helo.R;
import com.manager.helo.Utils.Utils;
import com.manager.helo.model.Cart;
import com.manager.helo.model.eventBus.TotalEvent;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    List<Cart> arrCart;

    public CartAdapter(Context context, List<Cart> arrCart) {
        this.context = context;
        this.arrCart = arrCart;
    }

    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyViewHolder holder, int position) {
        Cart cart = arrCart.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.prdNameCart.setText(cart.getPrdName());
        holder.prdQttCart.setText(cart.getPrdQtt() + " ");
        Glide.with(context).load(cart.getPrdImage()).into(holder.prdImageCart);
        holder.prdPriceCart.setText(decimalFormat.format(cart.getPrdPrice()));
        long totalPrice = cart.getPrdPrice() * cart.getPrdQtt();
        holder.prdTotalPrice.setText(decimalFormat.format(totalPrice));
        holder.itemCartCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Utils.arrBuyCard.add(cart);
                    EventBus.getDefault().postSticky(new TotalEvent());
                }else {
                    for (int i = 0; i < Utils.arrBuyCard.size(); i++) {
                        if (Utils.arrBuyCard.get(i).getPrdId() == cart.getPrdId()){
                            Utils.arrBuyCard.remove(i);
                            EventBus.getDefault().postSticky(new TotalEvent());
                        }
                    }
                }
            }
        });

        holder.setImageClickListener(new ImageClickListener() {
            @Override
            public void onImageClick(View v, int pos, int value) {
                if (value == 1) {
                    if (arrCart.get(pos).getPrdQtt() > 1) {
                        int newQtt = arrCart.get(pos).getPrdQtt() - 1;
                        arrCart.get(pos).setPrdQtt(newQtt);

                        holder.prdQttCart.setText(arrCart.get(pos).getPrdQtt() + " ");
                        long totalPrice = arrCart.get(pos).getPrdPrice() * arrCart.get(pos).getPrdQtt();
                        holder.prdTotalPrice.setText(decimalFormat.format(totalPrice));
                        EventBus.getDefault().postSticky(new TotalEvent());
                    } else if (arrCart.get(pos).getPrdQtt() == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm này không?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.arrCard.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TotalEvent());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                } else if (value == 2) {
                    if (arrCart.get(pos).getPrdQtt() < 11) {
                        int newQtt = arrCart.get(pos).getPrdQtt() + 1;
                        arrCart.get(pos).setPrdQtt(newQtt);
                    }
                    holder.prdQttCart.setText(arrCart.get(pos).getPrdQtt() + " ");
                    long totalPrice = arrCart.get(pos).getPrdPrice() * arrCart.get(pos).getPrdQtt();
                    holder.prdTotalPrice.setText(decimalFormat.format(totalPrice));
                    EventBus.getDefault().postSticky(new TotalEvent());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrCart.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView prdImageCart, prdImageMN, prdImagePL;
        TextView prdNameCart, prdPriceCart, prdQttCart, prdTotalPrice;
        ImageClickListener imageClickListener;
        CheckBox itemCartCheck;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            prdImageCart = itemView.findViewById(R.id.prdImageCart);
            prdNameCart = itemView.findViewById(R.id.prdNameCart);
            prdPriceCart = itemView.findViewById(R.id.prdPriceCart);
            prdQttCart = itemView.findViewById(R.id.prdQttCart);
            prdTotalPrice = itemView.findViewById(R.id.prdTotalPrice);
            prdImageMN = itemView.findViewById(R.id.prdImageMN);
            prdImagePL = itemView.findViewById(R.id.prdImagePL);
            itemCartCheck = itemView.findViewById(R.id.itemCartCheck);

            //event click
            prdImageMN.setOnClickListener(this);
            prdImagePL.setOnClickListener(this);
        }

        public void setImageClickListener(ImageClickListener imageClickListener) {
            this.imageClickListener = imageClickListener;
        }

        @Override
        public void onClick(View v) {
            if (v == prdImageMN) {
                imageClickListener.onImageClick(v, getAdapterPosition(), 1);
            } else if (v == prdImagePL) {
                imageClickListener.onImageClick(v, getAdapterPosition(), 2);
            }
        }
    }
}
