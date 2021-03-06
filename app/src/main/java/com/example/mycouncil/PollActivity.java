package com.example.mycouncil;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mycouncil.Feedback.Choice;
import com.example.mycouncil.Feedback.Poll;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import static com.example.mycouncil.HomeActivity.pollList;

public class PollActivity extends AppCompatActivity {

    Button mCreate;
    EditText mQuestion, mOption1, mOption2, mOption3, mOption4;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        mCreate = findViewById(R.id.createPollButton);
        mQuestion = findViewById(R.id.questionEditText);
        mOption1 = findViewById(R.id.optionOneEditText);
        mOption2 = findViewById(R.id.optionTwoEditText);
        mOption3 = findViewById(R.id.optionThreeEditText);
        mOption4 = findViewById(R.id.optionFourEditText);

        if (LoginActivity.isCitizen){
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().findItem(R.id.poll).setVisible(false);
        }

        if (!LoginActivity.isCitizen){
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().findItem(R.id.poll).setVisible(true);
        }
        final Spinner branchSpinner = findViewById(R.id.branchPoll);
        branchSpinner.setVisibility(View.VISIBLE);
        ArrayAdapter<CharSequence> branchAdapter = ArrayAdapter.createFromResource(this, R.array.postBranches, android.R.layout.simple_spinner_item);
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(branchAdapter);
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuestion.getText().toString()!=null && mOption1.getText().toString() != null && mOption2.getText().toString() != null && mOption3.getText().toString() != null && mOption4.getText().toString() != null ){
                    ArrayList<Choice> choicesList = new ArrayList<>();
                    choicesList.add(new Choice(mOption1.getText().toString(), 0));
                    choicesList.add(new Choice(mOption2.getText().toString(), 0));
                    choicesList.add(new Choice(mOption3.getText().toString(), 0));
                    choicesList.add(new Choice(mOption4.getText().toString(), 0));
                    pollList.add(new Poll(mQuestion.getText().toString(), choicesList));
                    System.out.println(mQuestion.getText().toString() + " " + mOption1.getText().toString() + " " + mOption2.getText().toString()+ " " + mOption3.getText().toString() + " " + mOption4.getText().toString());
                }
                else{
                    Toast.makeText(PollActivity.this, "Please Enter All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
    }
}