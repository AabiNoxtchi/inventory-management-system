package com.example.inventoryui.Activities.Inventory;

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

import com.example.inventoryui.Models.Inventory.Inventory;
import com.example.inventoryui.Models.Inventory.ProductType;
import com.example.inventoryui.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class InventoriesAdapter extends RecyclerView.Adapter<InventoriesAdapter.RecyclerViewHolder> {

    Context context;
    ArrayList<Inventory> products;
    SimpleDateFormat ft;

    private final OnItemClickListener listener;
    private final OnLongClickListener onLongClickListener;
    public ActionMode.Callback actionModeCallbacks ;

    public interface OnItemClickListener {
        void onItemClick(Inventory item, int position);
    }

    public interface OnLongClickListener{
        void onLongItemClick(Inventory item, int position);
    }

    boolean multiSelect = false;
    public void setMultiSelect(boolean multiSelect){
        this.multiSelect = multiSelect;
    }

    public InventoriesAdapter(Context context, ArrayList<Inventory> products,
                              OnItemClickListener listener,
                              OnLongClickListener OnLongClickListener, ActionMode.Callback actionModeCallbacks) {
        this.context=context;
        this.products = products;
        this.listener = listener;
        this.onLongClickListener = OnLongClickListener;
        this.actionModeCallbacks = actionModeCallbacks;
        this.ft= new SimpleDateFormat ("E yyyy.MM.dd ");

    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_card_view, parent,false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {

        holder.bind(products.get(position), listener);//,onLongClickListener);

        holder.productNameCardViewTextView.setText(products.get(position).getProductName());
        holder.numberCardViewTextView.setText(products.get(position).getInventoryNumber());

        Date dateCreated=products.get(position).getDateCreated();
        holder.dateCreatedTextView.setText(ft.format(dateCreated));

        ProductType productType=products.get(position).getProductType();
        holder.productTypeTextView.setText(productType.toString());
        holder.isAvailableTextView.setText(products.get(position).getEcondition().toString());

        boolean discarded=products.get(position).isDiscarded();
        holder.isDiscardedOrYearsToDiscardTextView.setText(discarded ? "Discarded" : "Alive");

        holder.PriceTextView.setText(""+products.get(position).getPrice()+" BGN");
        holder.total.setText(""+products.get(position).getTotalAmortization()+" BGN");
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView numberCardViewTextView;
        TextView productTypeTextView;
        TextView productNameCardViewTextView;
        TextView isAvailableTextView;
        TextView dateCreatedTextView;
        TextView isDiscardedOrYearsToDiscardTextView;
        RelativeLayout DMAtextViewsContainer;

        TextView total;
        RelativeLayout PriceViews;
        TextView PriceTextView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            this.numberCardViewTextView = itemView.findViewById(R.id.numberCardViewTextView);
            this.productTypeTextView=itemView.findViewById(R.id.productTypeTextView);
            this.productNameCardViewTextView=itemView.findViewById(R.id.productNameCardViewTextView);
            this.isAvailableTextView=itemView.findViewById(R.id.isAvailableTextView);
            this.dateCreatedTextView=itemView.findViewById(R.id.dateCreatedTextView);
            this.isDiscardedOrYearsToDiscardTextView=itemView.findViewById(R.id.isDiscardedOrYearsToDiscardTextView);

            this.DMAtextViewsContainer=itemView.findViewById(R.id.DMAtextViews);
            this.total=itemView.findViewById(R.id.TotalAmortizationTextView);

            this.PriceViews=itemView.findViewById(R.id.PriceViews);
            PriceTextView=itemView.findViewById(R.id.PriceTextView);

        }

       public void bind(final Inventory item, final OnItemClickListener listener){

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
                   if (listener != null && position != RecyclerView.NO_POSITION ) {
                       listener.onItemClick(item,position);
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
