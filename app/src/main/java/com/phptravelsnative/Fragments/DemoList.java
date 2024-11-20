package com.phptravelsnative.Fragments;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.phptravelsnative.Adapters.ListingAdapters;
import com.phptravelsnative.Models.HotelModel;
import com.phptravelsnative.Models.Hotel_data;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.lib.Parallex.ListViewFragment;

import java.util.ArrayList;


public class DemoList extends ListViewFragment {

    public static final String TAG = DemoListViewFragment.class.getSimpleName();
    String checkType;


    public static Fragment newInstance(int position, Hotel_data hotel_data, ArrayList<HotelModel> am, String type) {
        DemoList fragment = new DemoList();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putParcelableArrayList("am", am);
        args.putParcelable("hotel", hotel_data);
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    public DemoList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mPosition = getArguments().getInt(ARG_POSITION);
        checkType=getArguments().getString("type");

        ArrayList<HotelModel> am=getArguments().getParcelableArrayList("am");

        Hotel_data hotel_data=getArguments().getParcelable("hotel");
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.setBackgroundResource(R.color.white);
        view.setBackgroundResource(R.color.white);
        View placeHolderView = inflater.inflate(R.layout.header_placeholder, mListView, false);
        mListView.addHeaderView(placeHolderView);
        ListingAdapters hotelsAdapters=new ListingAdapters(getContext(),am,hotel_data,checkType);
        mListView.setAdapter(hotelsAdapters);
        setListViewOnScrollListener();
        return view;
    }
}
