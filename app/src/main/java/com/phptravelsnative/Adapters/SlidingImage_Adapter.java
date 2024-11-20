package com.phptravelsnative.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.phptravelsnative.Activities.FullscreenImages;
import com.phptravelsnative.Models.DetailModel;
import com.phptravelsnative.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SlidingImage_Adapter extends PagerAdapter {


    private ArrayList<DetailModel> IMAGES;
    private LayoutInflater inflater;
    private Context context;

    Boolean check_fullscreen;

    public SlidingImage_Adapter(Context context, ArrayList<DetailModel> IMAGES, boolean b) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
        check_fullscreen=b;

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        final Intent intent=new Intent(context,FullscreenImages.class);

        DetailModel dm=IMAGES.get(position);
        Picasso.get().load(dm.getSliderImages()).into(imageView);

        view.addView(imageLayout, 0);

        if(check_fullscreen) {
            imageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    intent.putParcelableArrayListExtra("list_image", IMAGES);
                    context.startActivity(intent);

                }
            });
        }
        else
        {
            imageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((FullscreenImages)context).finish();

                }
            });

        }

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
