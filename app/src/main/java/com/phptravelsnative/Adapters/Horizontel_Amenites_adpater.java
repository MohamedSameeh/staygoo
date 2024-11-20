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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Horizontel_Amenites_adpater extends RecyclerView.Adapter<Horizontel_Amenites_adpater.MyViewHolder> {

    private ArrayList<Amenities_Model> horizontalList;
    Context c;
    boolean checkEan;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView amenities_name;
        public TextView amenities_name2;
        public ImageView amenities_icon;

        public MyViewHolder(View view) {
            super(view);
            amenities_icon = (ImageView) view.findViewById(R.id.ameinties_icon);
            amenities_name = (TextView) view.findViewById(R.id.ameinties_name);
            amenities_name2 = (TextView) view.findViewById(R.id.ameinties_name2);

        }
    }


    public Horizontel_Amenites_adpater(ArrayList<Amenities_Model> horizontalList, Context c, boolean type) {
        this.horizontalList = horizontalList;
        this.c=c;
        checkEan=type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_child_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        String s="";
        holder.amenities_name.setText(horizontalList.get(position).getName().split(" ")[0]);
        for(int i=1;i<horizontalList.get(position).getName().split(" ").length;i++)
                s+=horizontalList.get(position).getName().split(" ")[i]+" ";

        holder.amenities_name2.setText(s);
        if(checkEan) {
            holder.amenities_icon.setImageResource(R.drawable.ic_checked);
        }else {
            Picasso.get().load(horizontalList.get(position).getIcon()).into(holder.amenities_icon);
        }
    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }
}
