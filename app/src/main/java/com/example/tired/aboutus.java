package com.example.tired;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

public class aboutus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        getSupportActionBar().setBackgroundDrawable(new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{getResources().getColor(R.color.customActionBarColor),
                        getResources().getColor(R.color.customActionBarColorDark)}));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}