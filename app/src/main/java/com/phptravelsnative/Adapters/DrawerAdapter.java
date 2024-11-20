package com.phptravelsnative.Adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.phptravelsnative.Models.DrawerItem;
import com.phptravelsnative.R;

import java.util.ArrayList;


public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {

    public final  int TYPE_HEADER = 0;
    public final  int TYPE_MENU = 1;

    private boolean b=false;


    private ArrayList<DrawerItem> drawerMenuList;

    private OnItemSelecteListener mListener;
    Context c;

    public DrawerAdapter(ArrayList<DrawerItem> drawerMenuList, Context context) {
        this.drawerMenuList = drawerMenuList;
        c=context;
    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == TYPE_HEADER){

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_header_navigation, parent, false);
            enableDisableView(view,false);

        }else{

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_item, parent, false);
        }


        return new DrawerViewHolder(view, viewType);
    }
    private   void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if ( view instanceof ViewGroup ) {
            ViewGroup group = (ViewGroup)view;

            for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }

    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {
        if(position == 0) {
            holder.headerText.setText(drawerMenuList.get(0).getTitle());
        }else{
            if(position!=drawerMenuList.size()) {

                holder.title.setText(drawerMenuList.get(position).getTitle());
                holder.icon.setImageResource(drawerMenuList.get(position).getIcon());
                if(drawerMenuList.get(position).getId().equals("profile") ||
                        drawerMenuList.get(position).getId().equals("my_booking") ||
                        drawerMenuList.get(position).getId().equals("logout") )
                {
                    holder.itemView.setBackgroundResource(R.color.iron);
                    expand(holder.itemView);

                }else
                {
                    holder.itemView.setBackgroundResource(R.color.base);

                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return drawerMenuList.size();
    }



    @Override
    public int getItemViewType(int position) {

        if(position == 0){
            return  TYPE_HEADER;
        }
        return TYPE_MENU;

    }

    class DrawerViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView headerText;
        ImageView icon;

        public DrawerViewHolder(View itemView, int viewType) {
            super(itemView);


            if(viewType == 0){
                headerText = (TextView)itemView.findViewById(R.id.headerText);
            }else {
                title = (TextView) itemView.findViewById(R.id.title);
                icon = (ImageView) itemView.findViewById(R.id.icon);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(view, getAdapterPosition());

                }
            });
        }

    }



    private   void expand(final View v) {
        b=true;
        v.measure(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RecyclerView.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(500);
        v.startAnimation(a);
    }

    public void setOnItemClickLister(OnItemSelecteListener mListener) {
        this.mListener = mListener;
    }

   public interface OnItemSelecteListener{
        public void onItemSelected(View v, int position);
    }

}