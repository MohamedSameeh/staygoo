package com.phptravelsnative.Fragments;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phptravelsnative.Adapters.Amenites_adpater_expedia;
import com.phptravelsnative.Models.Amenities_Model;
import com.phptravelsnative.Models.OverView;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.lib.Parallex.NotifyingScrollView;
import com.phptravelsnative.utality.lib.Parallex.ScrollViewFragment;

import java.util.ArrayList;

public class overview_expedia extends ScrollViewFragment {

    private OverView overView;
    private TextView textDesc;
    private LinearLayout amenitiesLayout;
    private ArrayList<Amenities_Model> amenitiesList;

    public static Fragment newInstance(int position, OverView overView, ArrayList<Amenities_Model> amenitiesList) {
        overview_expedia fragment = new overview_expedia();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putParcelable("ov", overView);
        args.putParcelableArrayList("am", amenitiesList);
        fragment.setArguments(args);
        return fragment;
    }

    public overview_expedia() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPosition = requireArguments().getInt(ARG_POSITION);
        overView = requireArguments().getParcelable("ov");
        amenitiesList = requireArguments().getParcelableArrayList("am");

        View view = inflater.inflate(R.layout.expedia_scrollview, container, false);
        mScrollView = view.findViewById(R.id.scrollview);

        setupRecyclerView(view);
        setupViews(view);

        setScrollViewOnScrollListener();
        return view;
    }

    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.horizontal_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        Amenites_adpater_expedia adapter = new Amenites_adpater_expedia(amenitiesList, getContext());
        recyclerView.setAdapter(adapter);

        // Hide amenities layout if there are no items
        amenitiesLayout = view.findViewById(R.id.amenities_layout);
        if (adapter.getItemCount() <= 0) {
            amenitiesLayout.setVisibility(View.GONE);
        }
    }

    private void setupViews(View view) {
        TextView textPolicy = view.findViewById(R.id.policy);
        textPolicy.setText(fromHtml(overView.getPolicy()));

        textDesc = view.findViewById(R.id.desc);
        textDesc.setText(fromHtml(overView.getDesc()));
    }

    private Spanned fromHtml(String source) {
        if (source == null) return Html.fromHtml("");
        source = source.replaceFirst("&apos;", "'");
        return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
    }
}
