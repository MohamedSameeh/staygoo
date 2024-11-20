package com.phptravelsnative.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phptravelsnative.Activities.MainLayout;
import com.phptravelsnative.Models.review_model;
import com.phptravelsnative.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BlogAdapter extends BaseAdapter {

    ArrayList<review_model> myList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;


    public BlogAdapter(Context context, ArrayList<review_model> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public review_model getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.blog_child, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

            final review_model currentListData = getItem(position);
            mViewHolder.tvName.setText(currentListData.getReview_by());
            mViewHolder.tvDescription.setText(currentListData.getRating());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MainLayout.class);
                intent.putExtra("CheckLayout","blog_detail");
                intent.putExtra("desc",currentListData.getRating());
                intent.putExtra("image_url",currentListData.getReview_comment());
                context.startActivity(intent);

            }
        });

        Picasso.get().load(currentListData.getReview_comment()).resize(270,240).into(mViewHolder.ivIcon);

        return convertView;
    }
    private class MyViewHolder {
        TextView tvName;
        ImageView ivIcon;
        TextView tvDescription;

        public MyViewHolder(View item) {
            tvName = (TextView) item.findViewById(R.id.title);
            tvDescription = (TextView) item.findViewById(R.id.desc);
            ivIcon = (ImageView) item.findViewById(R.id.imageView);
        }
    }
}