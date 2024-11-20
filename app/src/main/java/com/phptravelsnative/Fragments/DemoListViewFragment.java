package com.phptravelsnative.Fragments;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.phptravelsnative.Adapters.VerticalList;
import com.phptravelsnative.Models.Amenities_Model;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.lib.Parallex.ListViewFragment;

import java.util.ArrayList;


public class DemoListViewFragment extends ListViewFragment {

    public static final String TAG = DemoListViewFragment.class.getSimpleName();


    public static Fragment newInstance(int position, ArrayList<Amenities_Model> am) {
        DemoListViewFragment fragment = new DemoListViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putParcelableArrayList("am", am);
        fragment.setArguments(args);
        return fragment;
    }

    public DemoListViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mPosition = getArguments().getInt(ARG_POSITION);

        ArrayList<Amenities_Model> am=getArguments().getParcelableArrayList("am");

        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        mListView = (ListView) view.findViewById(R.id.listview);
        View placeHolderView = inflater.inflate(R.layout.header_placeholder, mListView, false);
        mListView.addHeaderView(placeHolderView);

        setAdapter(am);
        setListViewOnScrollListener();

        return view;
    }

    private void setAdapter(ArrayList<Amenities_Model>rv) {

        mListView.setAdapter(new VerticalList(getContext(),rv));
    }

}
