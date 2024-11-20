package com.phptravelsnative.Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.phptravelsnative.Activities.Invoice;
import com.phptravelsnative.Activities.WebViewInvoice;
import com.phptravelsnative.Adapters.VerticalList;
import com.phptravelsnative.Models.Amenities_Model;
import com.phptravelsnative.Models.Model_Tour;
import com.phptravelsnative.Models.OverView;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.lib.Parallex.NotifyingScrollView;
import com.phptravelsnative.utality.lib.Parallex.ScrollViewFragment;

import java.util.ArrayList;
import java.util.Calendar;


public class Tour_Overview extends ScrollViewFragment {

    OverView overView;
    Model_Tour tour;
    Calendar calendar = Calendar.getInstance();
    private ProgressDialog dialog;
    TextView text_desc;
    Spinner child;
    Spinner adult;
    Spinner inflants;
    int value_day_in, value_year_in, value_month_in;
    TextView total_adult;
    TextView total_child;
    SharedPreferences sharedPreferences;
    public  final String MyPREFERENCES = "MyPrefs" ;
    TextView total_inflants;
    TextView date_from;

    public static Fragment newInstance(int position, OverView ov, Model_Tour t) {
        Tour_Overview fragment = new Tour_Overview();


        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putParcelable("ov", ov);
        args.putParcelable("tour", t);
        fragment.setArguments(args);
        return fragment;
    }

    public Tour_Overview() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPosition = getArguments().getInt(ARG_POSITION);
        overView = getArguments().getParcelable("ov");
        tour = getArguments().getParcelable("tour");

        dialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage(getString(R.string.loading));

        sharedPreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        View view = inflater.inflate(R.layout.tour_fragment, container, false);
        mScrollView = (NotifyingScrollView) view.findViewById(R.id.scrollview);

        TextView Adulttxt=(TextView)view.findViewById(R.id.adult_rate_par);
        Adulttxt.setText(getString(R.string.adult)+tour.getSymbolCurrency()+tour.getPerAdultPrice());
        TextView Childtxt=(TextView)view.findViewById(R.id.child_rate_par);
        Childtxt.setText(getString(R.string.child)+tour.getSymbolCurrency()+tour.getPerChildPrice());
        TextView Inflantsdtxt=(TextView)view.findViewById(R.id.inflants_rate_par);
        Inflantsdtxt.setText(getString(R.string.inflant)+tour.getSymbolCurrency()+tour.getPerInfantPrice());

        Spinner spinnerAdult=(Spinner)view.findViewById(R.id.adult_spinner);
        Spinner spinnerChild=(Spinner)view.findViewById(R.id.child_spinner);
        Spinner spinnerInflants=(Spinner)view.findViewById(R.id.spinner_inflants);

        Button update=(Button) view.findViewById(R.id.onupdate);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdate();
            }
        });

        date_from=(TextView)view.findViewById(R.id.date_from);

        date_from.setText(tour.getDate());

        View date_picker =view.findViewById(R.id.update_layout);
        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });


        ArrayList<Integer> adult_values=new ArrayList<>();
        ArrayList<Integer> child_array=new ArrayList<>();
        ArrayList<Integer> inflants_array=new ArrayList<>();
        for(int i=1;i<=tour.getMaxAdult();i++)
        {
            adult_values.add(i);
        }
        ArrayAdapter<Integer> sAdapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,adult_values);
        spinnerAdult.setAdapter(sAdapter);


        for(int i=1;i<=tour.getMaxChild();i++)
        {
            child_array.add(i);
        }
        sAdapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,child_array);
        spinnerChild.setAdapter(sAdapter);

        for(int i=1;i<=tour.getMaxInflants();i++)
        {
            inflants_array.add(i);
        }
        sAdapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,inflants_array);
        spinnerInflants.setAdapter(sAdapter);

        total_adult =(TextView)view.findViewById(R.id.total_adult);
        total_adult.setText(tour.getSymbolCurrency()+tour.getPerAdultPrice()*spinnerAdult.getSelectedItemId());



        total_child = (TextView)view.findViewById(R.id.total_child);
        total_child.setText(tour.getSymbolCurrency()+tour.getPerChildPrice()*spinnerAdult.getSelectedItemId());

        total_inflants = (TextView)view.findViewById(R.id.total_inflants);
        total_inflants.setText(tour.getSymbolCurrency()+tour.getPerInfantPrice()*spinnerAdult.getSelectedItemId());

        spinnerAdult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                total_adult.setText(tour.getSymbolCurrency()+tour.getPerAdultPrice()*(position+1));
                tour.setMaxAdult(position+1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerChild.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                total_child.setText(tour.getSymbolCurrency()+tour.getPerChildPrice()*(position+1));
                tour.setMaxChild(position+1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerInflants.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                total_inflants.setText(tour.getSymbolCurrency()+tour.getPerInfantPrice()*(position+1));
                tour.setMaxInflants(position+1);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

        text_desc = (TextView) view.findViewById(R.id.desc);

        TextView text_desc = (TextView) view.findViewById(R.id.desc);
        text_desc.setText(overView.getDesc());

        TextView text_policy = (TextView) view.findViewById(R.id.policy);
        text_policy.setText(overView.getPolicy());



        return view;
    }

    private void onUpdate()
    {
        Bundle bundle=new Bundle();
        bundle.putParcelable("tour_object",tour);
        if(sharedPreferences.getBoolean("Check_Login",true) && !sharedPreferences.getBoolean("coupons",false))
        {
            Intent intent=new Intent(getContext(), WebViewInvoice.class);
            intent.putExtra("check_type","tour");
            intent.putExtra("Check_Guest",false);

            intent.putExtras(bundle);
            startActivity(intent);

        }else
        {
            Intent intent=new Intent(getContext(), Invoice.class);
            intent.putExtra("check_type","tour");
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();

        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");

    }
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {


            value_day_in=dayOfMonth;
            value_month_in=monthOfYear;
            value_year_in=year;


            date_from.setText((monthOfYear+1)+"/"+dayOfMonth+"/"+year);

        }
    };
    public static class DatePickerFragment extends DialogFragment {
        DatePickerDialog.OnDateSetListener ondateSet;

        TextView textView;
        DatePickerDialog dateFragment;

        public DatePickerFragment() {
        }

        public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
            ondateSet = ondate;
        }

        private int year, month, day;
        Boolean b = false;

        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);
            year = args.getInt("year");
            month = args.getInt("month");
            day = args.getInt("day");
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            dateFragment = new DatePickerDialog(getActivity(), R.style.DialogTheme, ondateSet, year, month, day);
            textView = new TextView(getContext());
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(20, 20, 20, 20);
            dateFragment.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dateFragment.setCustomTitle(textView);
            return dateFragment;
        }

    }


    @Override
    public void onResume() {
        super.onResume();

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


