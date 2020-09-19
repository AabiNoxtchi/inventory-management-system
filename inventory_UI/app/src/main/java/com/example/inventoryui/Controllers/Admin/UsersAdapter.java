package com.example.inventoryui.Controllers.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Models.User;
import com.example.inventoryui.R;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    Context context;
    ArrayList<User> users;

    public UsersAdapter(Context context,ArrayList<User> users) {
        this.context=context;
        this.users = users;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.users_card_view,null);//RecyclerViewHolder
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        holder.userNameCardViewTextView.setText(users.get(position).getUserName());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
