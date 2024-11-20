package com.phptravelsnative.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.phptravelsnative.Models.Amenities_Model;
import com.phptravelsnative.R;

import java.util.ArrayList;


public class Amenites_adpater_expedia extends RecyclerView.Adapter<Amenites_adpater_expedia.MyViewHolder> {

    private ArrayList<Amenities_Model> horizontalList;
    Context c;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView amenities_name;
        public ImageView amenities_icon;

        public MyViewHolder(View view) {
            super(view);
            amenities_icon = (ImageView) view.findViewById(R.id.ameinties_icon);
            amenities_name = (TextView) view.findViewById(R.id.ameinties_name);

        }
    }


    public Amenites_adpater_expedia(ArrayList<Amenities_Model> horizontalList, Context c) {
        this.horizontalList = horizontalList;
        this.c=c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.amenities, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        String s="";
        holder.amenities_name.setText(horizontalList.get(position).getName());
        holder.amenities_icon.setImageResource(R.drawable.ic_checked);

    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }
}
