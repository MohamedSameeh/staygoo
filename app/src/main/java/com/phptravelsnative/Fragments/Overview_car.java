package com.phptravelsnative.Fragments;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.phptravelsnative.Adapters.VerticalList;
import com.phptravelsnative.Models.Amenities_Model;
import com.phptravelsnative.Models.OverView;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.lib.Parallex.NotifyingScrollView;
import com.phptravelsnative.utality.lib.Parallex.ScrollViewFragment;

import java.util.ArrayList;


public class Overview_car extends ScrollViewFragment {

    OverView overView;
    TextView text_desc;


    public static Fragment newInstance(int position, OverView ov) {
        Overview_car fragment = new Overview_car();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putParcelable("ov", ov);
        fragment.setArguments(args);
        return fragment;
    }

    public Overview_car() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPosition = getArguments().getInt(ARG_POSITION);
        overView = getArguments().getParcelable("ov");


        View view = inflater.inflate(R.layout.car_fragment, container, false);
        mScrollView = (NotifyingScrollView) view.findViewById(R.id.scrollview);


        ListView text_credit_left = (ListView) view.findViewById(R.id.creditLeft);
        ListView text_credit_Right = (ListView) view.findViewById(R.id.creditRight);

        ArrayList<Amenities_Model> arrayListR= new ArrayList<>();
        ArrayList<Amenities_Model> arrayListL= new ArrayList<>();

        Amenities_Model amenities_model;
        String right[]= overView.getParmentsRights().split("90");
        String left[]= overView.getPaymentsLefts().split("90");
        for(int i=0;i<right.length;i++)
        {
            amenities_model=new Amenities_Model();
            amenities_model.setName(right[i]);
            amenities_model.setEan_id_image(R.drawable.ic_checked);
            arrayListR.add(amenities_model);
        }
        for(int i=0;i<left.length;i++)
        {
            amenities_model=new Amenities_Model();
            amenities_model.setName(left[i]);
            arrayListL.add(amenities_model);
            amenities_model.setEan_id_image(R.drawable.ic_checked);
        }
        text_credit_left.setAdapter(new VerticalList(getContext(),arrayListL));
        text_credit_Right.setAdapter(new VerticalList(getContext(),arrayListR));

        setListViewHeightBasedOnChildren(text_credit_left);
        setListViewHeightBasedOnChildren(text_credit_Right);



        TextView text_policy = (TextView) view.findViewById(R.id.policy);
        text_policy.setText(overView.getPolicy());


        setScrollViewOnScrollListener();

        text_desc = (TextView) view.findViewById(R.id.desc);

        TextView text_desc = (TextView) view.findViewById(R.id.desc);
        text_desc.setText(overView.getDesc());
        return view;
    }
    public  void setListViewHeightBasedOnChildren(ListView listView) {


        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight=0;
        View view = null;

        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            view = listAdapter.getView(i, view, listView);

            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
                        ViewGroup.LayoutParams.MATCH_PARENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + ((listView.getDividerHeight()) * (listAdapter.getCount()));

        listView.setLayoutParams(params);
        listView.requestLayout();

    }


}
