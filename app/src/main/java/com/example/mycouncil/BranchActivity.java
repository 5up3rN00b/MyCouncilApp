package com.example.mycouncil;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class BranchActivity extends AppCompatActivity {

    Button mBack;
    DrawerLayout d1;
    ActionBarDrawerToggle abdt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);

        d1 =findViewById(R.id.d1);
        abdt = new ActionBarDrawerToggle(this, d1, R.string.Open, R.string.Close);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        abdt.setDrawerIndicatorEnabled(true);

        d1.addDrawerListener(abdt);
        abdt.syncState();

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
                else if (id == R.id.viewPolls){
                    startActivity((new Intent(getApplicationContext(), ViewPollActivity.class)));
                }
                return true;
            }
        });

        mBack = findViewById(R.id.back);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
