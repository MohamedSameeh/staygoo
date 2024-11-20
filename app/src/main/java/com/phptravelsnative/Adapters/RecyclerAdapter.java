package com.phptravelsnative.Adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.phptravelsnative.Models.review_model;
import com.phptravelsnative.R;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_ITEM = 0;

    private ArrayList<review_model> mItemList;

    public RecyclerAdapter(ArrayList<review_model> rv) {
        super();
        mItemList = rv;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View view;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.recycler_header, viewGroup, false);
            return new RecyclerHeaderViewHolder(view);
        } else if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.recyclerview_item, viewGroup, false);
            return new RecyclerItemViewHolder(view);
        }

        throw new RuntimeException("Invalid view type " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {


        if(position>0 ) {
            RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;

            final review_model currentListData = mItemList.get(position-1);

            holder.review_name.setText(currentListData.getReview_by());
            holder.review_date.setText(currentListData.getReview_date());
            holder.review_rating.setText(currentListData.getRating());
            holder.review_comment.setText(currentListData.getReview_comment());
        }
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 1 : mItemList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    private static class RecyclerItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView review_name;
        private final TextView review_date;
        private final TextView review_rating;
        private final TextView review_comment;

        public RecyclerItemViewHolder(View itemView) {
            super(itemView);
            review_name = (TextView) itemView.findViewById(R.id.review_name);
            review_date = (TextView) itemView.findViewById(R.id.review_date);
            review_rating = (TextView) itemView.findViewById(R.id.current_reviw);
            review_comment = (TextView) itemView.findViewById(R.id.rivew_text);        }
    }

    private static class RecyclerHeaderViewHolder extends RecyclerView.ViewHolder {

        public RecyclerHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}

