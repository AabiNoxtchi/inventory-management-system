package com.example.inventoryui.Activities.UserProfile;

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

import com.example.inventoryui.Models.UserProfile.UserProfile;
import com.example.inventoryui.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserProfilesAdapter extends RecyclerView.Adapter<UserProfilesAdapter.RecyclerViewHolder> {

    Context context;
    ArrayList<UserProfile> userProfiles;
    SimpleDateFormat ft;

    private final OnItemClickListener listener;
    private final OnLongClickListener onLongClickListener;

    public ActionMode.Callback actionModeCallbacks ;

    public interface OnItemClickListener {
        void onItemClick(UserProfile item, int position);
    }

    public interface OnLongClickListener{
        void onLongItemClick(UserProfile item, int position);
    }

    boolean multiSelect = false;
    public void setMultiSelect(boolean multiSelect){
        this.multiSelect = multiSelect;
    }

    public UserProfilesAdapter(Context context, ArrayList<UserProfile> userProfiles, OnItemClickListener listener ,
                               OnLongClickListener onLongClickListener, ActionMode.Callback actionModeCallbacks) {
        this.context=context;
        this.userProfiles = userProfiles;
        this.listener=listener;
        this.onLongClickListener=onLongClickListener;
        this.actionModeCallbacks = actionModeCallbacks;
        this.ft= new SimpleDateFormat ("E yyyy.MM.dd ");
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profiles_card_view,null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.bind(userProfiles.get(position), listener,onLongClickListener);
        holder.userNameCardViewTextView.setText(userProfiles.get(position).getUserName());
        holder.productNameCardViewTextView.setText(userProfiles.get(position).getProductName());
        holder.givenNameCardViewTextView.setText(ft.format(userProfiles.get(position).getGivenAt()));
        Date returnedAt = userProfiles.get(position).getReturnedAt();
        String returnedAtStr = (returnedAt != null) ? ft.format(returnedAt) : "-";
        holder.returnedNameCardViewTextView.setText(returnedAtStr);
    }

    @Override
    public int getItemCount() {
        return userProfiles.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView userNameCardViewTextView;
        TextView productNameCardViewTextView;
        TextView givenNameCardViewTextView;
        TextView returnedNameCardViewTextView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userNameCardViewTextView = itemView.findViewById(R.id.userNameCardViewTextView);
            this.productNameCardViewTextView = itemView.findViewById(R.id.productNameCardViewTextView);
            this.givenNameCardViewTextView = itemView.findViewById(R.id.givenNameCardViewTextView);
            this.returnedNameCardViewTextView = itemView.findViewById(R.id.returnedNameCardViewTextView);

        }

        public void bind(final UserProfile item, final OnItemClickListener listener, final OnLongClickListener onLongClickListener) {

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
