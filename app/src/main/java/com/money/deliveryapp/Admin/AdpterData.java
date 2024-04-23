package com.money.deliveryapp.Admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.money.deliveryapp.R;

import java.util.ArrayList;
import java.util.List;

public class AdpterData extends RecyclerView.Adapter<AdpterData.MyviewHolder> {
    ArrayList<Fetch> fetches;
    Context context;

    public AdpterData(ArrayList<Fetch> fetches, Context context) {
        this.fetches = fetches;
        this.context = context;
    }
    public void setFilteredList(List<Fetch> filteredList){
        this.fetches = (ArrayList<Fetch>) filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_data_admin,parent,false);
        return new MyviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        Fetch f = fetches.get(position);
        holder.name.setText(f.getName());
        holder.email.setText(f.getEmail());
        holder.password.setText(f.getPassword());
        Glide.with(context).load(f.getImage()).placeholder(R.drawable.profile).into(holder.img1);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(),Info.class);
                i.putExtra("email",f.getEmail());
                i.putExtra("name",f.getName());
                i.putExtra("password",f.getPassword());
                i.putExtra("phone",f.getMobile());
                i.putExtra("uid",f.getUid());
                i.putExtra("image",f.getImage());
                i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
                holder.itemView.getContext().startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return fetches.size();
    }

    class MyviewHolder extends RecyclerView.ViewHolder{
        TextView name,email,password;
        ImageView img1;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtName);
            email = itemView.findViewById(R.id.txtCountry);
            password = itemView.findViewById(R.id.txtId);
            img1 = itemView.findViewById(R.id.img1);
        }
    }
}
