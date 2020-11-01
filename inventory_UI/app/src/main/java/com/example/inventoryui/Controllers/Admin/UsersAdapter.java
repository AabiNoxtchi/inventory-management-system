package com.example.inventoryui.Controllers.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Models.User;
import com.example.inventoryui.R;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.RecyclerViewHolder> {

    Context context;
    ArrayList<User> users;
    private final OnItemClickListener listener;
    private final OnLongClickListener onLongClickListener;
    boolean warningFlag;

    public interface OnItemClickListener {
        void onItemClick(User item);
    }

    public interface OnLongClickListener{
        void onLongItemClick(User item);
    }

    public UsersAdapter(Context context,ArrayList<User> users,OnItemClickListener listener ,OnLongClickListener onLongClickListener) {
        this.context=context;
        this.users = users;
        this.listener=listener;
        this.onLongClickListener=onLongClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.users_card_view,null);//RecyclerViewHolder
        return new RecyclerViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        holder.bind(users.get(position), listener,onLongClickListener);
        holder.userNameCardViewTextView.setText(users.get(position).getUserName());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView userNameCardViewTextView;
        TextView nameLabel;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            this.userNameCardViewTextView = itemView.findViewById(R.id.userNameCardViewTextView);
            this.nameLabel=itemView.findViewById(R.id.userNameLabel);
        }

        public void bind(final User item, final OnItemClickListener listener, final OnLongClickListener onLongClickListener) {

            final int position = getAdapterPosition();

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(item);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onLongClickListener != null && position != RecyclerView.NO_POSITION) {
                        onLongClickListener.onLongItemClick(item);
                    }
                    return true;
                }
            });


        }
    }


}
