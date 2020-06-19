package com.example.mycouncil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mycouncil.Users.Citizen;
import com.example.mycouncil.Users.Leader;
import com.example.mycouncil.Users.User;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Button mRegister, mLogin;
    private EditText loginEmailAddress, loginPassword;
    private static final String TAG = "LoginActivity";

    public static String emailText, passwordText;
    public static User user;
    public static boolean isCitizen;

    public static ArrayList<Boolean> upvoteClicked = new ArrayList<>();
    public static ArrayList<Boolean> downvoteClicked = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mRegister = findViewById(R.id.register);
        mLogin = findViewById(R.id.loginButton);

        loginEmailAddress = findViewById(R.id.loginEmailAddress);
        loginPassword = findViewById(R.id.loginPassword);


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailText = loginEmailAddress.getText().toString();
                passwordText = loginPassword.getText().toString();
                new LoginTask().execute();
            }
        });
    }

    class LoginTask extends AsyncTask<Void, Void, Void> {
        private String text;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                HttpClient httpclient = HttpClients.createDefault();
                HttpPost httppost = new HttpPost("http://73.71.24.214:8008/users/get.php");

                List<NameValuePair> params = new ArrayList<NameValuePair>(5);
                params.add(new BasicNameValuePair("email", LoginActivity.emailText));
                params.add(new BasicNameValuePair("password", LoginActivity.passwordText));
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
            if (!text.equals("Email does not exist") && !text.equals("Login failed")) {
                String[] params = text.split(":");
                Log.d(TAG, Arrays.toString(params));

                if (params[0].equals("Citizen")) {
                    LoginActivity.user = new Citizen(params[2], params[3], params[4], Integer.parseInt(params[1]), Integer.parseInt(params[5]));
                    LoginActivity.isCitizen = true;
                } else if (params[0].equals("Leader")) {
                    LoginActivity.user = new Leader(params[2], params[4], params[5], params[3], Integer.parseInt(params[1]), Integer.parseInt(params[6]));
                    LoginActivity.isCitizen = false;
                }

                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
