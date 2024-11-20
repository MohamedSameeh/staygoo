package com.phptravelsnative.Activities;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.phptravelsnative.Models.ExpediaExtra;
import com.phptravelsnative.Models.Hotel_data;
import com.phptravelsnative.Models.car_model;
import com.phptravelsnative.Network.Post.ExpediaBooking;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Extra.Constant;
import com.phptravelsnative.utality.Views.SingleTonRequest;
import com.phptravelsnative.utality.Views.TourSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Card extends Drawer {

    private ViewGroup cardFront;
    private ViewGroup cardBack;
    boolean isBackShowing = false;

    EditText cvv;
    EditText card_number;
    EditText card_name;
    EditText date;
    TextView amount;
    String card_types[];
    String card_codes[];
    TextView card_number_preview;
    TextView card_name_preview;
    TextView card_date_preview;
    TextView card_cvv_preview;
    ProgressDialog pd;
    Button book;
    ExpediaExtra expediaExtra;
    Hotel_data hotel_data;
    TourSpinner creditCard;


    LinearLayout mRelativeLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.activity_card_activity);
        View inflated = stub.inflate();

        pd = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        pd.setIndeterminate(true);
        pd.setCancelable(true);
        pd.setMessage(getString(R.string.loading));


        mRelativeLayout=(LinearLayout)inflated.findViewById(R.id.main_layout);

        Intent intent=getIntent();
        cardFront = (ViewGroup) inflated.findViewById(R.id.card_preview_front);
        cardBack = (ViewGroup) inflated.findViewById(R.id.card_preview_back);
        expediaExtra=intent.getParcelableExtra("expedia");
        hotel_data=intent.getParcelableExtra("hotel_object");

        getCards();

        cvv= (EditText) inflated.findViewById(R.id.cvc);
        card_number=(EditText)inflated.findViewById(R.id.card_number);
        card_name=(EditText)inflated.findViewById(R.id.card_name);
        date=(EditText)inflated.findViewById(R.id.expiry_date);
        creditCard=(TourSpinner) inflated.findViewById(R.id.tour_type);
        book=(Button)inflated.findViewById(R.id.book);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickHotelSelect();
            }
        });
        card_number_preview=(TextView)findViewById(R.id.card_preview_number);
        card_cvv_preview=(TextView)findViewById(R.id.card_preview_cvc);
        card_date_preview=(TextView)findViewById(R.id.card_preview_expiry);
        card_name_preview=(TextView)findViewById(R.id.card_preview_name);
        amount=(TextView)findViewById(R.id.payment_amount);

        amount.setText(expediaExtra.getTotal_price());

        if(!hotel_data.getChild().split("900")[1].equals("0")) {

            cvv.setText("123");
            card_number.setText("5401999999999999");
            date.setText("12/2025");
            creditCard.setText("Master Card");
            creditCard.setTag("CA");

        }
        hotel_data.setChild(hotel_data.getChild().split("900")[0]);

        Log.d("child",hotel_data.getChild());


        card_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text=s.toString();
                for(int i=s.length();i<16;i++)
                {
                    text=text+"X";
                }
                card_number_preview.setText(text);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cvv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showBack();
            }
        });
        card_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showFront();
            }
        });
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showFront();
            }
        });
        card_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showFront();
            }
        });


        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()) {
                    expediaExtra.setExpairy(date.getText().toString());
                    expediaExtra.setInput_card_number(card_number.getText().toString());
                    expediaExtra.setInput_card_cvw(cvv.getText().toString());
                    expediaExtra.setExpairy(date.getText().toString());
                    expediaExtra.setInput_card_type(creditCard.getTag().toString());
                    showExpediaUrl(expediaExtra, hotel_data);
                }
            }
        });



    }

    private boolean validate() {


        if(date.getText().toString().equals("")) {
            date.setError(getString(R.string.spefiy_date));
            return false;
        }else if(cvv.getText().toString().length()<3)
        {
            cvv.setError(getString(R.string.cvv_number));
            return false;
        }else if(card_number.getText().toString().length()<16)
        {
            card_number.setError(getString(R.string.cvv_number));
            return false;
        }
        return true;
    }


    private void showExpediaUrl(final ExpediaExtra expediaExtra, Hotel_data hotel_data) {

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    pd.dismiss();

                    JSONObject main_json = new JSONObject(response);
                    JSONObject json_object = main_json.getJSONObject("HotelRoomReservationResponse");
                    car_model cm=new car_model();
                    Bundle b=new Bundle();
                    Intent intent=new Intent(Card.this,ExpediaCongratulations.class);
                    if(json_object.has("EanWsError"))
                    {
                        JSONObject error_object=json_object.getJSONObject("EanWsError");
                        cm.setDepositePrice("PresentationMessage : "+error_object.getString("presentationMessage"));
                        cm.setDropOfTime("VerboseMessage : "+error_object.getString("verboseMessage"));
                        cm.setTaxPrice("CATEGORY : "+error_object.getString("category"));
                        intent.putExtra("congrats","success");
                        b.putParcelable("cm",cm);
                        intent.putExtras(b);

                    }else
                    {
                        cm.setPickupDate(json_object.getString("confirmationNumbers"));
                        cm.setDropOfDate(json_object.getString("itineraryId"));
                        cm.setTaxPrice("Total Nightly Rate: "+json_object.getJSONObject("RateInfo").getJSONObject("ChargeableRateInfo").getString("@currencyCode")+" "+json_object.getJSONObject("RateInfo").getJSONObject("ChargeableRateInfo").getString("@nightlyRateTotal"));
                        cm.setTotalPrice("Total Amount : "+json_object.getJSONObject("RateInfo").getJSONObject("ChargeableRateInfo").getString("@currencyCode")+" "+json_object.getJSONObject("RateInfo").getJSONObject("ChargeableRateInfo").getString("@total"));
                        cm.setDepositePrice("Checkin Instructions : "+json_object.getString("checkInInstructions"));
                        cm.setDropOfTime("Non-refundable : "+json_object.getString("cancellationPolicy"));
                        intent.putExtra("congrats","unsuccess");
                        b.putParcelable("cm",cm);
                        intent.putExtras(b);
                    }
                    pd.dismiss();
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ExpediaBooking expediabooking=new ExpediaBooking(expediaExtra,hotel_data,listener);
        RequestQueue requestQueue = SingleTonRequest.getmInctance(this).getRequestQueue();
        requestQueue.add(expediabooking);
        pd.show();

    }


    private void showBack() {
        if (!isBackShowing) {
            Animator cardFlipLeftIn = AnimatorInflater.loadAnimator(getBaseContext(), R.animator.card_flip_left_in);
            cardFlipLeftIn.setTarget(cardFront);
            cardFlipLeftIn.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    cardFront.setVisibility(View.GONE);
                    cardBack.setVisibility(View.VISIBLE);
                    isBackShowing = true;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            cardFlipLeftIn.start();
        }
    }



    private void showFront() {
        if (isBackShowing) {
            Animator cardFlipRightIn = AnimatorInflater.loadAnimator(getBaseContext(), R.animator.card_flip_right_in);
            cardFlipRightIn.setTarget(cardBack);
            cardFlipRightIn.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    cardBack.setVisibility(View.GONE);
                    cardFront.setVisibility(View.VISIBLE);
                    isBackShowing = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            cardFlipRightIn.start();

        }

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        if(id==1)
        {
            return checkDate.ShowDailog(listener_data) ;

        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener listener_data=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


            if(month<10)
                day_to="0"+month;
            else
                day_to= ""+month;

            date.setText(day_to+"/"+year);

        }
    };
    public void OnClickHotelSelect()
    {
        showDialog(1);

    }
    private void getCards() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constant.domain_name + "expedia/getCardTypes?appKey=" + Constant.key+"&customerSessionId="+expediaExtra.getSessionId()+"&hotelId="+hotel_data.getId()

                , new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                JSONObject parentObject = null;
                try {
                    parentObject = new JSONObject(response.toString());
                    JSONObject main_object=parentObject.getJSONObject("HotelPaymentResponse");
                    JSONArray jsonArray=main_object.getJSONArray("PaymentType");

                    card_types=new String[jsonArray.length()];
                    card_codes=new String[jsonArray.length()];

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        card_codes[i]=jsonObject.getString("code");
                        card_types[i]=jsonObject.getString("name");
                    }

                } catch (JSONException e) {

                    Log.d("abcwwwwd", e.getMessage());
                }
                creditCard.setVisibility(View.VISIBLE);
                creditCard.listAdapter(card_types,card_codes);


            }
        }, null);

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

}
