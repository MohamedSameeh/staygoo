package com.phptravelsnative.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import com.phptravelsnative.Models.car_model;
import com.phptravelsnative.R;

public class ExpediaCongratulations extends Drawer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        Intent intent=getIntent();
        car_model cm = intent.getParcelableExtra("cm");

        if (cm == null) {
            // التعامل مع الحالة عندما لا يتم تمرير car_model بشكل صحيح
            // يمكنك إظهار رسالة خطأ أو إنهاء النشاط
            finish();  // إنهاء النشاط إذا كانت car_model null
            return;
        }

        String congrats = intent.getStringExtra("congrats");
        if (congrats != null && congrats.equals("success"))
        {
            stub.setLayoutResource(R.layout.expedia_error);
            View inflated = stub.inflate();
            cm=intent.getParcelableExtra("cm");
            TextView itrator=(TextView) inflated.findViewById(R.id.categroy);
            itrator.setText(cm.getTaxPrice().replace("_"," "));

            TextView nightly=(TextView)inflated.findViewById(R.id.presentationMessage);
            nightly.setText(cm.getDepositePrice());

            TextView toatl=(TextView)inflated.findViewById(R.id.verboseMessage);
            toatl.setText(cm.getDropOfTime());
            Button closeButton = (Button) inflated.findViewById(R.id.close);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }else
        {
            stub.setLayoutResource(R.layout.expedia_congragulation);
            View inflated = stub.inflate();
            cm=intent.getParcelableExtra("cm");

            TextView itrator=(TextView) inflated.findViewById(R.id.itrater_id);
            itrator.setText(cm.getDropOfDate());

            TextView nightly=(TextView)inflated.findViewById(R.id.total_nightly);
            nightly.setText(cm.getTaxPrice());

            TextView toatl=(TextView)inflated.findViewById(R.id.total_rate);
            toatl.setText(cm.getTotalPrice());

            TextView check_instruction=(TextView)inflated.findViewById(R.id.checkInstrauction);
            check_instruction.setText(cm.getDepositePrice());

            TextView cancellation=(TextView)inflated.findViewById(R.id.cancellationPolicy);
            cancellation.setText(cm.getDropOfTime());

            Button closeButton = (Button) inflated.findViewById(R.id.close);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MainLayout.class);
                    intent.putExtra("CheckLayout", "MainList");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);                }
            });
        }
    }
}
