package com.phptravelsnative.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phptravelsnative.Models.Amenities_Model;
import com.phptravelsnative.R;

import java.util.ArrayList;


public class VerticalList extends BaseAdapter {

    ArrayList<Amenities_Model> review_list = new ArrayList<>();
    LayoutInflater inflater;
    Context context;


    public VerticalList(Context context, ArrayList<Amenities_Model> myList) {
        this.review_list = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return review_list.size();
    }

    @Override
    public Amenities_Model getItem(int position) {
        return review_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.amenities, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        final Amenities_Model currentListData = getItem(position);
        mViewHolder.amenities_name.setText(currentListData.getName());

        mViewHolder.amenities_icon.setImageResource(currentListData.getEan_id_image());
        return convertView;
    }

    private class MyViewHolder {
        ImageView amenities_icon;
        TextView amenities_name;

        public MyViewHolder(View item) {
            amenities_icon = (ImageView) item.findViewById(R.id.ameinties_icon);
            amenities_name = (TextView) item.findViewById(R.id.ameinties_name);
        }
    }

}
