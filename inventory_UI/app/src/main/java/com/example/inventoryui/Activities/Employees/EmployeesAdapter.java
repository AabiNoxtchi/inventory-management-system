package com.example.inventoryui.Activities.Employees;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Models.User.Employee;
import com.example.inventoryui.R;

import java.util.ArrayList;

class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.RecyclerViewHolder> {

    Context context;
    ArrayList<Employee> employees;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Employee item);
    }

    public EmployeesAdapter(Context context, ArrayList<Employee> employees, OnItemClickListener listener) {
        this.context=context;
        this.employees = employees;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_card_view,null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.bind(employees.get(position), listener);
        holder.employeeNameCardViewTextView.setText(employees.get(position).getUserName());
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView employeeNameCardViewTextView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            this.employeeNameCardViewTextView=itemView.findViewById(R.id.employeeNameCardViewTextView);
        }

        public void bind(final Employee item, final OnItemClickListener listener) {
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
        }
    }

}
