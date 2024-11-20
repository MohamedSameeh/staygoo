package com.phptravelsnative.utality.Views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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


public class LightSpinner extends TextView implements TextView.OnClickListener {

    PopupWindow popupWindow;
    String[] abc;
    Context context;
    LayoutInflater inflater;
    String check;
    int width = 0;

    public LightSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.app, 0, 0);
        try {
            check = ta.getString(R.styleable.app_type);
            String s;
            if (check.equals("child")) {
                s = getContext().getString(R.string.child);
                abc = new String[]{s.concat(" 0"), s.concat(" 1"), s.concat(" 2"), s.concat(" 3"), s.concat(" 4")};
                width = 160;
            } else {
                s = getContext().getString(R.string.adult);
                abc = new String[]{s.concat(" 1"), s.concat(" 2"), s.concat(" 3"), s.concat(" 4"), s.concat(" 5")};
                width = 175;

            }
        } finally {
            ta.recycle();
        }

        setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        // the drop down list is a list view

        View convertView = inflater.inflate(R.layout.list_view, null);

        ListView listView = (ListView) convertView.findViewById(R.id.listview);

        listView.setOnItemClickListener(new DogsDropdownOnItemClickListener());
        // some other visual settings
       ListAdapter listAdapter= new ArrayAdapter<String>(getContext(), R.layout.spinner_layout, abc);
        listView.setAdapter(listAdapter);

        popupWindow = new PopupWindow(
                convertView,
                measureContentWidth(listAdapter),
                (getHeight() * 5) + 10
        );

        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOutsideTouchable(true);


        popupWindow.showAsDropDown(v, 0, -getHeight(), Gravity.TOP);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // Override back button
             Log.d("Abced","Okeycall");
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
                return false;
            }
        return super.onKeyDown(keyCode, event);
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
            setTag(arg2);
        }
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

    public  int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

}
