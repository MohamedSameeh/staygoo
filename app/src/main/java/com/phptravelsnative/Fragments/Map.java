package com.phptravelsnative.Fragments;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.phptravelsnative.Models.OverView;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.lib.Parallex.NotifyingScrollView;
import com.phptravelsnative.utality.lib.Parallex.ScrollViewFragment;


public class Map extends ScrollViewFragment implements OnMapReadyCallback {

    OverView overView;
    MapView mMapView;

    public static Fragment newInstance(int position, OverView ov) {
        Map fragment = new Map();


        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putParcelable("ov", ov);
        fragment.setArguments(args);
        return fragment;
    }

    public Map() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPosition = getArguments().getInt(ARG_POSITION);
        overView = getArguments().getParcelable("ov");


        View view = inflater.inflate(R.layout.map, container, false);
        mScrollView = (NotifyingScrollView) view.findViewById(R.id.scrollview);

        mMapView = (MapView) view.findViewById(R.id.mapView1);
        if(overView.getLatitude()!=null || overView.getLongitude()!=null)
            mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);



        setScrollViewOnScrollListener();


        return view;
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

}
