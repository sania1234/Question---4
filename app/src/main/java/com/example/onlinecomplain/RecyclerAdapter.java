package com.example.onlinecomplain;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    static   List<user> dbList;
    static  Context context;
    RecyclerAdapter(Context context, List<user> dbList ){
        this.dbList = new ArrayList<user>();
        this.context = context;
        this.dbList = dbList;

    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_row, null);


        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {

        holder.category.setText("Complain : " + dbList.get(position).getCat());
        holder.useremail.setText("Complain By: " + dbList.get(position).getUserEmail());

    }

    @Override
    public int getItemCount() {
        return dbList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView category,useremail;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            category = (TextView) itemLayoutView
                    .findViewById(R.id.cat);
            useremail= (TextView)itemLayoutView.findViewById(R.id.uemail);
            itemLayoutView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, activityDetails.class);

            Bundle extras = new Bundle();
            extras.putInt("position",getAdapterPosition());
            intent.putExtras(extras);
            context.startActivity(intent);

        }
    }
}

