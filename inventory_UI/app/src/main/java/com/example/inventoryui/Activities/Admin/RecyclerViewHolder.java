package com.example.inventoryui.Activities.Admin;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView userNameCardViewTextView;
    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        this.userNameCardViewTextView=itemView.findViewById(R.id.userNameCardViewTextView);
    }


}
