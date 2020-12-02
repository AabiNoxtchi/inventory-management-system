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
   // SimpleDateFormat forSubstractionDateFormat;
    private final OnItemClickListener listener;
    private final OnLongClickListener onLongClickListener;
    public ActionMode.Callback actionModeCallbacks ;

   // boolean warningFlag;

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





    //= new ActionMode.Callback() {
       /* @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
            menu.add("Delete");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            for (Integer intItem : selectedItems) {
               // items.remove(intItem);
            }
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedItems.clear();
            notifyDataSetChanged();
        }
    };
*/
    public ProductsAdapter(Context context, ArrayList<Product> products,
                           OnItemClickListener listener,
                           OnLongClickListener OnLongClickListener, ActionMode.Callback actionModeCallbacks) {
        this.context=context;
        this.products = products;
        this.listener = listener;
        this.onLongClickListener = OnLongClickListener;
        this.actionModeCallbacks = actionModeCallbacks;
        this.ft= new SimpleDateFormat ("E yyyy.MM.dd ");
        //this.forSubstractionDateFormat= new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
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

       /* if(productType==ProductType.DMA){
           holder.DMAtextViewsContainer.setVisibility(View.VISIBLE);
           holder.amortizationPercent.setText(products.get(position).getAmortizationPercent().toString()+"%");
           //Long yearsLeft=getYearsLeft(Long days, int yearsToSubstract);
           holder.yearsToMAconvertion.setText(getTimeLeftString(dateCreated, products.get(position).getYearsToMAConvertion())+" to MA");
           if(warningFlag)holder.yearsToMAconvertion.setBackgroundColor(Color.RED);
           warningFlag=false;
        }else{
            holder.DMAtextViewsContainer.setVisibility(View.GONE);
        }*/

        String available="missing";
        if(products.get(position).isAvailable()){available="available";}
        holder.isAvailableTextView.setText(available);

        //String isDiscardedOrYearsToDiscard="discarded";
        boolean discarded=products.get(position).isDiscarded();
        holder.isDiscardedOrYearsToDiscardTextView.setText(discarded ? "discarded" : "");
        /*if(!discarded)
        {
                isDiscardedOrYearsToDiscard = getTimeLeftString(dateCreated,
                         products.get(position).getYearsToDiscard())+" to discard" ;

            if(warningFlag)holder.isDiscardedOrYearsToDiscardTextView.setBackgroundColor(Color.RED);
            warningFlag=false;
        }else{
            holder.isDiscardedOrYearsToDiscardTextView.setBackgroundColor(Color.rgb(220,220,220));
        }
        holder.isDiscardedOrYearsToDiscardTextView.setText(isDiscardedOrYearsToDiscard);*/


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

           // this.itemView = itemView;
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

   /* private String getTimeLeftString(Date dateCreated,int lifeYears){
        long diffInMillies=new Date().getTime()-dateCreated.getTime();
        long livedDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        long lifeDays=lifeYears*365;
        long daysLeft=lifeDays-livedDays;
        long yearsLeft=daysLeft/365;

        long weeksLeft=(daysLeft%365)/7;
        if(yearsLeft<1&&weeksLeft<4)warningFlag=true;
        String left="";
        left+=yearsLeft+"y";
        if(weeksLeft>0) {
            if(yearsLeft!=0)
                left+="+";
            else
                left="+"+weeksLeft+"w";
        }
        if (yearsLeft<1&&weeksLeft<1){
            left=(daysLeft%365)+"days";
        }
        return left+"";
    }*/
}
