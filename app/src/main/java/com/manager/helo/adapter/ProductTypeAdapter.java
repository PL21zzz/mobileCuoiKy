package com.manager.helo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.manager.helo.R;
import com.manager.helo.model.ProductType;

import java.util.List;

public class ProductTypeAdapter extends BaseAdapter {
    List<ProductType> array;
    Context context;

    public ProductTypeAdapter(Context context, List<ProductType> array) {
        this.array = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder {
        TextView textProductName;
        ImageView imageProduct;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_product, null);
            viewHolder.textProductName = view.findViewById(R.id.item_productName);
            viewHolder.imageProduct = view.findViewById(R.id.item_image);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textProductName.setText(array.get(i).getProduct_name());
        Glide.with(context).load(array.get(i).getProduct_image()).into(viewHolder.imageProduct);

        return view;
    }
}
