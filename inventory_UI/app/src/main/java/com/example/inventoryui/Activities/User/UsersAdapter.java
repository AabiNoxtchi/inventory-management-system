package com.example.inventoryui.Activities.User;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Models.User.User;
import com.example.inventoryui.R;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.RecyclerViewHolder> {

    Context context;
    ArrayList<User> users;

    private final OnItemClickListener listener;
    private final OnLongClickListener onLongClickListener;

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

    public UsersAdapter(Context context,ArrayList<User> users,OnItemClickListener listener ,
                        OnLongClickListener onLongClickListener, ActionMode.Callback actionModeCallbacks) {
        this.context=context;
        this.users = users;
        this.listener=listener;
        this.onLongClickListener=onLongClickListener;
        this.actionModeCallbacks = actionModeCallbacks;
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
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView userNameCardViewTextView;
        TextView nameLabel;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userNameCardViewTextView = itemView.findViewById(R.id.userNameCardViewTextView);
            this.nameLabel=itemView.findViewById(R.id.userNameLabel);
        }

        public void bind(final User item, final OnItemClickListener listener, final OnLongClickListener onLongClickListener) {

            final int position = getAdapterPosition();

            if(item.isSelected())
            {
                ((CardView)itemView).setCardBackgroundColor(Color.rgb(232, 248, 245));//232, 248, 245);//context.getResources().getColor(R.color.selected, context.getTheme()));//.setBackgroundResource(R.color.selected);
            }else{
                ((CardView)itemView).setCardBackgroundColor(Color.WHITE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // int position = getAdapterPosition();
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
