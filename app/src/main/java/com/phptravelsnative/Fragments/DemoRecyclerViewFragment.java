package com.phptravelsnative.Fragments;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phptravelsnative.Adapters.RecyclerAdapter;
import com.phptravelsnative.Models.review_model;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.lib.Parallex.RecyclerViewFragment;

import java.util.ArrayList;


public class DemoRecyclerViewFragment extends RecyclerViewFragment {

    public static final String TAG = DemoRecyclerViewFragment.class.getSimpleName();

    private LinearLayoutManager mLayoutMgr;

    public static Fragment newInstance(int position, ArrayList<review_model> rv) {
        DemoRecyclerViewFragment fragment = new DemoRecyclerViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putParcelableArrayList("model", rv);
        fragment.setArguments(args);
        return fragment;
    }

    public DemoRecyclerViewFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPosition = getArguments().getInt(ARG_POSITION);

        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        ArrayList<review_model> rv=getArguments().getParcelableArrayList("model");
        setupRecyclerView(rv);
        return view;
    }

    @Override
    protected void setScrollOnLayoutManager(int scrollY) {
        mLayoutMgr.scrollToPositionWithOffset(0, -scrollY);
    }

    private void setupRecyclerView(ArrayList<review_model> rv) {
        mLayoutMgr = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutMgr);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(rv);
        mRecyclerView.setAdapter(recyclerAdapter);

        setRecyclerViewOnScrollListener();
    }

}
