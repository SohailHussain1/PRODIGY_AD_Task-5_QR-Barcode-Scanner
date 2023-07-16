package com.example.tired;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ouroption extends AppCompatActivity {
    private CardView num1,num3,num2,num4,num5,num6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_ouroption);
        num3= (CardView) findViewById(R.id.num3);
        num1= (CardView) findViewById(R.id.num1);
        num2= (CardView) findViewById(R.id.num2);
        num4= (CardView) findViewById(R.id.num4);
        num5= (CardView) findViewById(R.id.num5);
        num6= (CardView) findViewById(R.id.num6);
        num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),scannerview.class));
            }
        });
        num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ContactUs.class));
            }
        });
        num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Generate.class));
            }
        });
        num4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),aboutus.class));
            }
        });

        num5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String privacyPolicyUrl = "https://www.freeprivacypolicy.com/live/61f78676-3af8-408c-8d66-4a0907fef307";
                Uri uri = Uri.parse(privacyPolicyUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        num6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });


    }
}