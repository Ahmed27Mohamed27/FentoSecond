package com.money.deliveryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.ViewHolder> {

    private List<ModelDataOrders> ModelDataOrders = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_data, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.name.setText(ModelDataOrders.get(position).getName());
        holder.phone.setText(ModelDataOrders.get(position).getPhone());
        holder.address.setText(ModelDataOrders.get(position).getAddress());
        holder.kind_product.setText(ModelDataOrders.get(position).getKind());
        if (ModelDataOrders.get(position).getNone().equals("ملغي")) {
            holder.state.setBackgroundResource(R.drawable.background_hagz_red);
        } else if (ModelDataOrders.get(position).getNone().equals("تم التنفيذ")) {
            holder.state.setBackgroundResource(R.drawable.background_hagz_green);
        }
        holder.state.setText(ModelDataOrders.get(position).getNone());
    }

    @Override
    public int getItemCount() {
        return ModelDataOrders.size();
    }

    public void setList(List<ModelDataOrders> ModelDataOrders) {
        this.ModelDataOrders = ModelDataOrders;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, kind_product, state, address;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            phone = itemView.findViewById(R.id.phone);
            kind_product = itemView.findViewById(R.id.kind_product);
            address = itemView.findViewById(R.id.address);
            name = itemView.findViewById(R.id.name);
            state = itemView.findViewById(R.id.state);
        }
    }
}