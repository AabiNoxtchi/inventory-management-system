package com.example.inventoryui.Activities.Products;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.HelperFilters.MinMaxValueFilter;
import com.example.inventoryui.Models.Product.SelectProduct;
import com.example.inventoryui.R;

import java.util.ArrayList;
import java.util.List;

public class SelectProductAdapter extends RecyclerView.Adapter<SelectProductAdapter.RecyclerViewHolder> {

    final String TAG = "ProductsSelect_Adapter";

    Context context;
    ArrayList<SelectProduct> products;
    ArrayList<SelectProduct> filteredProducts;
    ArrayList<SelectProduct> selected;

    TextView countTV;


    public SelectProductAdapter(Context context, ArrayList<SelectProduct> products , TextView countTV) {
        this.context=context;
        this.products = products;

        this.filteredProducts = new ArrayList<>();
        this.filteredProducts.addAll(products);
        this.countTV = countTV;

    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.select_product_item, parent,false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {

        holder.bind(products.get(position));

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public List<SelectProduct> getSelected(){
        return selected;
    }

    public void filter(String Text) {

        Text = Text.toLowerCase();
        products.clear();
        if (Text.length() == 0) {
            products.addAll(filteredProducts);
        }
        else
        {
            for (SelectProduct product : filteredProducts)
            {
                if (product.getName().toLowerCase().contains(Text))
                {
                    products.add(product);
                }
            }
        }
        notifyDataSetChanged();
        countTV.setText(products.size()+"");
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView productNameTextView;
        TextView totalTextView;
        //NumberPicker picker;
        EditText picker;

        ImageView up ;
        ImageView down;

        //View itemView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            this.productNameTextView = itemView.findViewById(R.id.productNameTextView);
            this.totalTextView=itemView.findViewById(R.id.totalTextView);
            this.picker=itemView.findViewById(R.id.count_picker);
            this.up=itemView.findViewById(R.id.btn_up);
            this.down=itemView.findViewById(R.id.btn_down);

        }

        public void bind(final SelectProduct item){

            final int position = getAdapterPosition();
            productNameTextView.setText(item.getName());
            Long l = item.getTotalCount();
            Integer max = l != null ? l.intValue() : null;
            totalTextView.setText(max.toString());
            picker.setText("0");
            picker.setFilters( new InputFilter[]{ new MinMaxValueFilter( 0 , max )});

            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int count = Integer.valueOf(picker.getText().toString());
                    if(count == max)return;
                    picker.setText(count+1+"");
                }
            });
            down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int count = Integer.valueOf(picker.getText().toString());
                    if(count == 0)return;
                    picker.setText(count-1+"");
                }
            });

            picker.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().isEmpty())return;
                    if(selected == null) selected = new ArrayList<>();

                    int count = Integer.valueOf(s.toString());
                    item.setCount(Integer.valueOf(s.toString()));

                    if(count>0 && !selected.contains(item)) {
                            selected.add(item);
                    }
                    else{
                        if(selected.contains(item))selected.remove(item);
                    }
                }
            });
        }
    }

}