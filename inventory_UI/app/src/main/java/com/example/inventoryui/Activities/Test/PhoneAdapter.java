package com.example.inventoryui.Activities.Test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.R;

import java.util.List;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneViewHolder>{
    private Context context;
    private List<ProductObject> productList;
    PhoneAdapter(Context context, List<ProductObject> productList) {
        this.context = context;
        this.productList = productList;
    }
    @NonNull
    @Override
    public PhoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item_card_view, parent, false);
        return new PhoneViewHolder(view);
    }
    @Override
    public void onBindViewHolder(PhoneViewHolder holder, int position){
        ProductObject productObject = productList.get(position);
        int imageRes = getResourceId(context, productObject.getImagePath(), context.getPackageName());
        holder.phoneImage.setImageResource(imageRes);
        holder.phoneName.setText(productObject.getName());
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }
    private static int getResourceId(Context context, String pVariableName, String pPackageName) throws RuntimeException {
        try {
            return context.getResources().getIdentifier("phone_foreground", "mipmap", pPackageName);
        } catch (Exception e) {
            throw new RuntimeException("Error getting Resource ID.", e);
        }
    }
}
