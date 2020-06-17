package com.example.mycouncil;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mycouncil.Feedback.Post;
import com.example.mycouncil.Users.Citizen;
import com.example.mycouncil.Users.Leader;
import com.google.android.material.navigation.NavigationView;

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
import java.util.Arrays;
import java.util.List;

import static com.example.mycouncil.HomeActivity.postList;

public class CreateActivity extends AppCompatActivity {
    private Button mCreate;
    private DrawerLayout d1;
    private ActionBarDrawerToggle abdt;
    private EditText title, bodyText;
    private String theTitle, body;

    public static final String TAG = "CreateActivity";
    public static int id;
    public static String titleText, bodyTexts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_poll);
        setContentView(R.layout.activity_post);
        d1 =findViewById(R.id.d1);
        abdt = new ActionBarDrawerToggle(this, d1, R.string.Open, R.string.Close);

        abdt.setDrawerIndicatorEnabled(true);

        d1.addDrawerListener(abdt);
        abdt.syncState();

        title = findViewById(R.id.postTitleEditText);
        bodyText = findViewById(R.id.bodyTextEditText);


        final Spinner branchSpinner = findViewById(R.id.branch);
        branchSpinner.setVisibility(View.VISIBLE);
        ArrayAdapter<CharSequence> branchAdapter = ArrayAdapter.createFromResource(this, R.array.postBranches, android.R.layout.simple_spinner_item);
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(branchAdapter);

        mCreate = findViewById(R.id.createPostButton);
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleText = title.getText().toString();
                bodyTexts = bodyText.getText().toString();

                title.setText("");
                bodyText.setText("");

                if (LoginActivity.isCitizen) {
                    id = ((Citizen) LoginActivity.user).getId();
                } else {
                    id = ((Leader) LoginActivity.user).getId();
                }

                new CreateTask().execute();
            }
        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!LoginActivity.isCitizen){
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().findItem(R.id.poll).setVisible(true);
        }

        final NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.home){
//                    Toast.makeText(HomeActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class ));
                }
                else if (id == R.id.post){
//                    Toast.makeText(HomeActivity.this, "Post", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), CreateActivity.class ));
                }
                else if (id == R.id.logout){
//                    Toast.makeText(HomeActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class ));
                }
                else if (id == R.id.poll){
                    startActivity(new Intent(getApplicationContext(), PollActivity.class ));
                }
                return true;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    class CreateTask extends AsyncTask<Void, Void, Void> {
        private String text;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                HttpClient httpclient = HttpClients.createDefault();
                HttpPost httppost = new HttpPost("http://73.71.24.214:8008/posts/add.php");

                List<NameValuePair> params = new ArrayList<NameValuePair>(5);
                params.add(new BasicNameValuePair("user-id", Integer.toString(CreateActivity.id)));
                params.add(new BasicNameValuePair("title", CreateActivity.titleText));
                params.add(new BasicNameValuePair("description", CreateActivity.bodyTexts));
                params.add(new BasicNameValuePair("upvotes", Integer.toString(0)));
                params.add(new BasicNameValuePair("downvotes", Integer.toString(0)));
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
            if (text.length() > 0) {

                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            } else {
                Toast.makeText(CreateActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
