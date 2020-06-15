package com.example.mycouncil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class RegistrationActivity extends AppCompatActivity {

    Button mHome, mLogin,mCitizen, mPolitican;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mHome = findViewById(R.id.home);
        mLogin = findViewById(R.id.login);
        mCitizen = findViewById(R.id.citizen);
        mPolitican = findViewById(R.id.political_figure);

        final Spinner spinner = findViewById(R.id.jobs);
        spinner.setVisibility(View.GONE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.jobs, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        mPolitican.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                mPolitican.setBackgroundResource(R.drawable.political_color);
                mPolitican.setTextColor(Color.WHITE);
                mCitizen.setBackgroundResource(R.drawable.citizen);
                mCitizen.setTextColor(Color.parseColor("#707070"));
            }
        });

        mCitizen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.GONE);
                mPolitican.setBackgroundResource(R.drawable.citizen);
                mPolitican.setTextColor(Color.parseColor("#707070"));
                mCitizen.setBackgroundResource(R.drawable.political_color);
                mCitizen.setTextColor(Color.WHITE);
            }
        });
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}
