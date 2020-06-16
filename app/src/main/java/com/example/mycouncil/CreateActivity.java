package com.example.mycouncil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mycouncil.Feedback.Post;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import static com.example.mycouncil.HomeActivity.postList;

public class CreateActivity extends AppCompatActivity {
    Button mCreate;
    DrawerLayout d1;
    ActionBarDrawerToggle abdt;
    EditText title, bodyText;
    String theTitle, body;

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

        mCreate = findViewById(R.id.createPostButton);
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theTitle = title.getText().toString();
                body = bodyText.getText().toString();
                postList.add(new Post(theTitle, body, 0,0));
                title.setText("");
                bodyText.setText("");
            }
        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.home){
//                    Toast.makeText(CreateActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class ));
                }
                if (id == R.id.branches){
//                    Toast.makeText(CreateActivity.this, "Branches", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), BranchActivity.class ));
                }
                if (id == R.id.post){
//                    Toast.makeText(CreateActivity.this, "Post", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), CreateActivity.class ));
                }
                if (id == R.id.logout){
                    Toast.makeText(CreateActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class ));
                }
                return true;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


}
