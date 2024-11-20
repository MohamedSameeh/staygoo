package com.phptravelsnative.Fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.phptravelsnative.R;
import com.squareup.picasso.Picasso;

public class Blog_detail extends Fragment {


    private String desc,image_url;


    public static Blog_detail newInstance(String param1, String param2) {
        Blog_detail fragment = new Blog_detail();
        Bundle args = new Bundle();
        args.putString("desc", param1);
        args.putString("image_url", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            desc = getArguments().getString("desc");
            image_url = getArguments().getString("image_url");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.detail_blog, container, false);

        ImageView imageView= (ImageView) v.findViewById(R.id.blog_detail_ivIcon);
        TextView textView= (TextView) v.findViewById(R.id.textview);

        textView.setText(desc);

        Picasso.get().load(image_url).into(imageView);

        return  v;
    }
}
