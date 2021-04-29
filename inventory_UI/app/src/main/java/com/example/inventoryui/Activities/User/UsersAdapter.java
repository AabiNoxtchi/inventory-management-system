package com.example.inventoryui.Activities.User;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Models.User.Role;
import com.example.inventoryui.Models.User.User;
import com.example.inventoryui.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.RecyclerViewHolder> {

    Context context;
    ArrayList<User> users;

    private final OnItemClickListener listener;
    private final OnLongClickListener onLongClickListener;

    private Role role;
    SimpleDateFormat ft;

    public ActionMode.Callback actionModeCallbacks ;

    public interface OnItemClickListener {
        void onItemClick(User item, int position);
    }

    public interface OnLongClickListener{
        void onLongItemClick(User item, int position);
    }

    boolean multiSelect = false;
    public void setMultiSelect(boolean multiSelect){
        this.multiSelect = multiSelect;
    }

    public UsersAdapter(Context context, ArrayList<User> users, Role role, OnItemClickListener listener ,
                        OnLongClickListener onLongClickListener, ActionMode.Callback actionModeCallbacks) {
        this.context=context;
        this.users = users;
        this.listener=listener;
        this.onLongClickListener=onLongClickListener;
        this.actionModeCallbacks = actionModeCallbacks;
        this.role = role;
        this.ft= new SimpleDateFormat ("E yyyy.MM.dd ");
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.users_card_view,null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.bind(users.get(position), listener,onLongClickListener);
        holder.userNameCardViewTextView.setText(users.get(position).getUserName());
        holder.emailCardViewTextView.setText(users.get(position).getEmail());
        if(role.equals(Role.ROLE_Mol)){
            holder.lastActiveRelativeLayout.setVisibility(View.GONE);
        }
        else if(role.equals(Role.ROLE_Admin)){
            Date lastActive = users.get(position).getLastActive();
            String date = lastActive != null ? ft.format(lastActive) : "-";
            holder.lastActiveCardViewTextView.setText(date);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView userNameCardViewTextView;
        TextView emailCardViewTextView;
        RelativeLayout lastActiveRelativeLayout;
        TextView lastActiveCardViewTextView;
        TextView nameLabel;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userNameCardViewTextView = itemView.findViewById(R.id.userNameCardViewTextView);
            this.nameLabel=itemView.findViewById(R.id.userNameLabel);
            this.emailCardViewTextView=itemView.findViewById(R.id.emailCardViewTextView);
            this.lastActiveRelativeLayout=itemView.findViewById(R.id.lastActiveRelativeLayout);
            this.lastActiveCardViewTextView=itemView.findViewById(R.id.lastActiveCardViewTextView);
        }

        public void bind(final User item, final OnItemClickListener listener, final OnLongClickListener onLongClickListener) {

            final int position = getAdapterPosition();

            if(item.isSelected())
            {
                ((CardView)itemView).setCardBackgroundColor(Color.rgb(232, 248, 245));
               }else{
                ((CardView)itemView).setCardBackgroundColor(Color.WHITE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(item, position);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    if (onLongClickListener != null && position != RecyclerView.NO_POSITION && !multiSelect ) {
                        multiSelect = true;
                        ((AppCompatActivity)v.getContext()).startSupportActionMode(actionModeCallbacks);
                        onLongClickListener.onLongItemClick(item, position);
                    }
                    return true;
                }
            });
        }
    }
}
