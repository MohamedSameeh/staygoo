package com.phptravelsnative.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.phptravelsnative.Adapters.Horizontel_Amenites_adpater;
import com.phptravelsnative.Adapters.VerticalList;
import com.phptravelsnative.Models.Amenities_Model;
import com.phptravelsnative.Models.OverView;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.lib.Parallex.NotifyingScrollView;
import com.phptravelsnative.utality.lib.Parallex.ScrollViewFragment;

import java.util.ArrayList;


public class SecondScrollViewFragment extends ScrollViewFragment implements OnMapReadyCallback {

    OverView overView;
    MapView mMapView;
    private ProgressDialog dialog;
    TextView text_desc;
    ArrayList<Amenities_Model> amenities_list;


    public static Fragment newInstance(int position, OverView ov, ArrayList<Amenities_Model> amenities_list) {
        SecondScrollViewFragment fragment = new SecondScrollViewFragment();


        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putParcelable("ov", ov);
        args.putParcelableArrayList("am", amenities_list);
        fragment.setArguments(args);
        return fragment;
    }

    public SecondScrollViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPosition = getArguments().getInt(ARG_POSITION);
        overView = getArguments().getParcelable("ov");
        dialog = new ProgressDialog(getContext());
        dialog = new ProgressDialog(getContext());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");
        amenities_list = getArguments().getParcelableArrayList("am");


        View view = inflater.inflate(R.layout.fragment_second_scroll_view, container, false);
        mScrollView = (NotifyingScrollView) view.findViewById(R.id.scrollview);

        mMapView = (MapView) view.findViewById(R.id.mapView1);
        if(overView.getLatitude()!=null || overView.getLongitude()!=null)
            mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        RecyclerView horizontal_recycler_view = (RecyclerView) view.findViewById(R.id.horizontal_recycler_view);

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);
        Horizontel_Amenites_adpater amenites_adpater=new Horizontel_Amenites_adpater(amenities_list,getContext(),false);

        horizontal_recycler_view.setAdapter(amenites_adpater);

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

        setScrollViewOnScrollListener();

        TextView text_policy = (TextView) view.findViewById(R.id.policy);
        text_policy.setText(fromHtml(overView.getPolicy()));

         text_desc = (TextView) view.findViewById(R.id.desc);

        text_desc.setText(fromHtml( fromHtml(overView.getDesc()) ));




        return view;
    }



    public  String fromHtml(String source) {
        source = source.replaceFirst("&apos;", "'");
        //Removes any connected item to the last bracket
        return source;

    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(overView.getLatitude(),overView.getLongitude())).title("Marker"));


        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(overView.getLatitude(),overView.getLongitude()));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(8);
        map.moveCamera(center);
        map.animateCamera(zoom);

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }
    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
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
