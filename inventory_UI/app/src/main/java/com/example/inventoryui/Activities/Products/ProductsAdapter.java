package com.example.inventoryui.Activities.Products;

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

import com.example.inventoryui.Models.Product.Product;
import com.example.inventoryui.Models.Product.ProductType;
import com.example.inventoryui.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.RecyclerViewHolder> {

    final String TAG = "Products_Adapter";

    Context context;
    ArrayList<Product> products;
    SimpleDateFormat ft;
    private final OnItemClickListener listener;
    private final OnLongClickListener onLongClickListener;
    public ActionMode.Callback actionModeCallbacks ;

    public interface OnItemClickListener {
        void onItemClick(Product item, int position);
    }

    public interface OnLongClickListener{
        void onLongItemClick(Product item, int position);
    }

    boolean multiSelect = false;
    public void setMultiSelect(boolean multiSelect){
        this.multiSelect = multiSelect;
    }
   // private ArrayList<Integer> selectedItems = new ArrayList<Integer>();

    public ProductsAdapter(Context context, ArrayList<Product> products,
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

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_view, parent,false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {

        holder.bind(products.get(position), listener);//,onLongClickListener);

        holder.productNameCardViewTextView.setText(products.get(position).getName());
        holder.numberCardViewTextView.setText(products.get(position).getInventoryNumber());

        Date dateCreated=products.get(position).getDateCreated();
        holder.dateCreatedTextView.setText(ft.format(dateCreated));

        ProductType productType=products.get(position).getProductType();
        holder.productTypeTextView.setText(productType.toString());

        String available="missing";
        if(products.get(position).isAvailable()){available="available";}
        holder.isAvailableTextView.setText(available);

        boolean discarded=products.get(position).isDiscarded();
        holder.isDiscardedOrYearsToDiscardTextView.setText(discarded ? "discarded" : "");
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
        TextView amortizationPercent;
        TextView yearsToMAconvertion;
        //View itemView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            this.numberCardViewTextView = itemView.findViewById(R.id.numberCardViewTextView);
            this.productTypeTextView=itemView.findViewById(R.id.productTypeTextView);
            this.productNameCardViewTextView=itemView.findViewById(R.id.productNameCardViewTextView);
            this.isAvailableTextView=itemView.findViewById(R.id.isAvailableTextView);
            this.dateCreatedTextView=itemView.findViewById(R.id.dateCreatedTextView);
            this.isDiscardedOrYearsToDiscardTextView=itemView.findViewById(R.id.isDiscardedOrYearsToDiscardTextView);

            this.DMAtextViewsContainer=itemView.findViewById(R.id.DMAtextViews);
            this.amortizationPercent=itemView.findViewById(R.id.DMAamortizationPercentTextView);
            this.yearsToMAconvertion=itemView.findViewById(R.id.DMAyearsToMavonvertionTextView);
        }

       public void bind(final Product item, final OnItemClickListener listener){//, final OnLongClickListener onLongClickListener) {

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
                      onLongClickListener.onLongItemClick(item,position);
                 }
                   return true;
              }
           });
        }
    }

}
