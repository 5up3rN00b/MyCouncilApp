package com.example.mycouncil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {
    Button mHome, mLogin, mCitizen, mPolitician, mRegister;
    private EditText zipcode, first_name, last_name, email, password;
    private static final String TAG = "RegistrationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mHome = findViewById(R.id.home);
        mLogin = findViewById(R.id.login);
        mRegister = findViewById(R.id.register);

        zipcode = findViewById(R.id.zipcode);
        first_name = findViewById(R.id.firstName);
        last_name = findViewById(R.id.lastName);
        email = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);

        // TODO New thread
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        mCitizen = findViewById(R.id.citizen);
        mPolitician = findViewById(R.id.political_figure);

        final Spinner spinner = findViewById(R.id.jobs);
        spinner.setVisibility(View.GONE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.jobs, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        mPolitician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
            }
        });

        mCitizen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.GONE);
            }
        });
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PostTask().execute();
            }
        });
    }

    class PostTask extends AsyncTask<Void, Void, Void> {
        private String text;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                HttpClient httpclient = HttpClients.createDefault();
                HttpPost httppost = new HttpPost("http://73.71.24.214:8008/users/add.php");

                List<NameValuePair> params = new ArrayList<NameValuePair>(5);
                runOnUiThread(new Runnable() {
                    public void run() {
                        params.add(new BasicNameValuePair("zipcode", zipcode.getText().toString()));
                        params.add(new BasicNameValuePair("first-name", first_name.getText().toString()));
                        params.add(new BasicNameValuePair("last-name", last_name.getText().toString()));
                        params.add(new BasicNameValuePair("email", email.getText().toString()));
                        params.add(new BasicNameValuePair("password", password.getText().toString()));
                    }
                });
                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

                //Execute and get the response.
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();

                InputStream inputStream = entity.getContent();
                text = "";
                text = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
            } catch (Exception e) {
                Log.d(TAG, "Exception Caught: " + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG, text);
            if (text.equals("Registered successfully")) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            } else {
                Toast.makeText(RegistrationActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

