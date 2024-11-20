package com.phptravelsnative.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phptravelsnative.Models.Auto_Model;
import com.phptravelsnative.R;

import java.util.ArrayList;


public class SpinnnerImageAdapter extends BaseAdapter {

    ArrayList<Auto_Model> myList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public SpinnnerImageAdapter(Context context, ArrayList<Auto_Model> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {

        return myList.size()>0?myList.size()-1:myList.size();
    }

    @Override
    public Auto_Model getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
                convertView = inflater.inflate(R.layout.spinner_layout_lang, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        final Auto_Model currentListData = getItem(position);

        mViewHolder.name.setText(currentListData.getName());
        mViewHolder.iv_icon.setImageResource(currentListData.getImage_id());
        mViewHolder.name.setBackgroundColor(context.getResources().getColor(R.color.white));

        return convertView;
    }

    private class MyViewHolder {
        TextView name;
        ImageView iv_icon;

        public MyViewHolder(View item) {

            name = (TextView) item.findViewById(R.id.text);
            iv_icon=(ImageView)item.findViewById(R.id.iv_icon);
        }
    }
}
