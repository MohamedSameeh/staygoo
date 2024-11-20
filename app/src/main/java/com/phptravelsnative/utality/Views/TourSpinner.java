package com.phptravelsnative.utality.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.phptravelsnative.R;


public class TourSpinner extends TextView implements TextView.OnClickListener{

        PopupWindow popupWindow;
        String[] abc;
        String[] id;
        Context context;
        String check;
        int i=0;
        LayoutInflater inflater;
        public TourSpinner(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.context=context;
            inflater = LayoutInflater.from(this.context);

            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.app, 0, 0);
            try {
                check = ta.getString(R.styleable.app_type);
                if (check.equals("card")) {
                    i=1;
                } else {
                    i=0;
                }
            } finally {
                ta.recycle();
            }

            setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            View convertView = inflater.inflate(R.layout.list_view, null);

            ListView listView= (ListView) convertView.findViewById(R.id.listview);

            listView.setOnItemClickListener(new DogsDropdownOnItemClickListener());



            if(i==0) {
                ListAdapter listAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_layout, abc);
                listView.setAdapter(listAdapter);
                listView.setBackgroundColor(getResources().getColor(R.color.white));

                popupWindow = new PopupWindow(
                        convertView,
                        measureContentWidth(listAdapter),
                        (getHeight() * 5) + 10
                );
            }
            else {
                ListAdapter listAdapter= new ArrayAdapter<String>(getContext(), R.layout.card_spinner_layout, abc);
                listView.setBackgroundColor(getResources().getColor(R.color.jet));
                listView.setAdapter(listAdapter);
                popupWindow = new PopupWindow(
                        convertView,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
            }

            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable());
            popupWindow.setOutsideTouchable(true);
            popupWindow.showAsDropDown(v, 0,-getHeight(), Gravity.TOP);



        }
        public class DogsDropdownOnItemClickListener implements AdapterView.OnItemClickListener {
            String TAG = "DogsDropdownOnItemClickListener.java";

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {

                Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
                fadeInAnimation.setDuration(10);
                v.startAnimation(fadeInAnimation);
                popupWindow.dismiss();
                String selectedItemText = ((TextView) v).getText().toString();
                setText(selectedItemText);
                setTag(id[arg2]);

            }
        }
         public void  listAdapter(String name[],String type[])
         {
             abc=name;
             id=type;
         }
    private int measureContentWidth(ListAdapter listAdapter) {
        ViewGroup mMeasureParent = null;
        int maxWidth = 0;
        View itemView = null;
        int itemType = 0;

        final ListAdapter adapter = listAdapter;
        final int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            final int positionType = adapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }

            if (mMeasureParent == null) {
                mMeasureParent = new FrameLayout(getContext());
            }

            itemView = adapter.getView(i, itemView, mMeasureParent);
            itemView.measure(widthMeasureSpec, heightMeasureSpec);



            final int itemWidth = itemView.getMeasuredWidth();


            if (itemWidth > maxWidth) {
                maxWidth = itemWidth;
            }
        }
            return maxWidth;

    }
}
