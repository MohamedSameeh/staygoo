package com.phptravelsnative.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phptravelsnative.Models.Auto_Model;
import com.phptravelsnative.R;

import java.util.ArrayList;
import java.util.List;


public class AutoSuggestedAdapter extends ArrayAdapter<Auto_Model> {
    private LayoutInflater layoutInflater;
    List<Auto_Model> mCustomers;

    private Filter mFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((Auto_Model)resultValue).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null) {
                ArrayList<Auto_Model> suggestions = new ArrayList<Auto_Model>();
                for (Auto_Model customer : mCustomers) {
                    if (customer.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(customer);
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results != null && results.count > 0) {
                // we have filtered results
                addAll((ArrayList<Auto_Model>) results.values);
            } else {
                // no filter, add entire original list back in
                addAll(mCustomers);
            }
            notifyDataSetChanged();
        }
    };

    public AutoSuggestedAdapter(Context context, int textViewResourceId, List<Auto_Model> customers) {
        super(context, textViewResourceId, customers);
        // copy all the customers into a master list
        mCustomers = new ArrayList<Auto_Model>(customers.size());
        mCustomers.addAll(customers);
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.auto_complete, null);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        Auto_Model customer = getItem(position);

        mViewHolder.name.setText(customer.getName());
        mViewHolder.ivIcon.setImageResource(customer.getImage_id());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class MyViewHolder {
        TextView name;
        ImageView ivIcon;

        public MyViewHolder(View item) {
            name = (TextView) item.findViewById(R.id.countryName);
            ivIcon=(ImageView)item.findViewById(R.id.imageView);

        }
    }
}
