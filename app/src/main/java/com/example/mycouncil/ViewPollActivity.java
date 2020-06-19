package com.example.mycouncil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mycouncil.Feedback.Choice;
import com.example.mycouncil.Feedback.Poll;
import com.example.mycouncil.Feedback.Post;
import com.google.android.material.navigation.NavigationView;

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
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class ViewPollActivity extends AppCompatActivity {

    ArrayList <Poll> pollList = new ArrayList<>();
    ArrayList <Choice> listOfChoices = new ArrayList<>();
    DrawerLayout d1;
    ActionBarDrawerToggle abdt;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_polls);

        listOfChoices.add(new Choice ("A", 4));
        listOfChoices.add(new Choice ("B", 23));
        listOfChoices.add(new Choice ("C", 43));
        listOfChoices.add(new Choice ("D", 15));
        listOfChoices.add(new Choice ("E", 15));

        pollList.add(new Poll("Test", listOfChoices));
        pollList.add(new Poll("TEst 2", listOfChoices));

        if (LoginActivity.isCitizen){
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().findItem(R.id.poll).setVisible(false);
        }

        if (!LoginActivity.isCitizen){
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().findItem(R.id.poll).setVisible(true);
        }

        for (Poll p: pollList){
//            System.out.println(p.getTitle());
        }



        final NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);

        d1 =findViewById(R.id.d1);
        abdt = new ActionBarDrawerToggle(this, d1, R.string.Open, R.string.Close);

        abdt.setDrawerIndicatorEnabled(true);

        d1.addDrawerListener(abdt);
        abdt.syncState();

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
        ListView viewPollList = findViewById(R.id.viewPollList);
        PollListAdapter adapter = new PollListAdapter(ViewPollActivity.this, R.layout.poll_layout, pollList);
        viewPollList.setAdapter(adapter);
    }

class PollListAdapter extends ArrayAdapter<Poll> {
    private ArrayList<Poll> list;
    private Context pollContext;
    int pollResource;

    public PollListAdapter(@NonNull Context context, int resource, ArrayList<Poll> objects){
        super(context, resource, objects);
        pollContext = context;
        pollResource = resource;
        list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String question = list.get(position).getTitle();
        String optionOne = list.get(position).getChoice(0).getDescription();
        String optionTwo = list.get(position).getChoice(1).getDescription();
        String optionThree = list.get(position).getChoice(2).getDescription();
        String optionFour = list.get(position).getChoice(3).getDescription();
        String noneOfTheAboveOption = list.get(position).getChoice(4).getDescription();

        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(pollResource, parent, false);

        TextView pollTitle;
        RadioButton one, two, three, four, five;
        ProgressBar pOne, pTwo, pThree, pFour, pFive;

        pollTitle = convertView.findViewById(R.id.pollTitleTextView);
        one = convertView.findViewById(R.id.pollOption1);
        two = convertView.findViewById(R.id.pollOption2);
        three = convertView.findViewById(R.id.pollOption3);
        four = convertView.findViewById(R.id.pollOption4);
        five = convertView.findViewById(R.id.noneOfTheAboveOption);

        pOne = convertView.findViewById(R.id.pollOption1ProgressBar);
        pTwo = convertView.findViewById(R.id.pollOption2ProgressBar);
        pThree = convertView.findViewById(R.id.pollOption3ProgressBar);
        pFour = convertView.findViewById(R.id.pollOption4ProgressBar);
        pFive = convertView.findViewById(R.id.pollOption5ProgressBar);

        pollTitle.setText(question);
        one.setText(optionOne);
        two.setText(optionTwo);
        three.setText(optionThree);
        four.setText(optionFour);
        five.setText(noneOfTheAboveOption);

        one.setOnClickListener(view -> {
            pOne.setProgress(pOne.getProgress() + 10);
            one.setChecked(true);
            two.setChecked(false);
            three.setChecked(false);
            four.setChecked(false);
            five.setChecked(false);
        });

        two.setOnClickListener(view -> {
            pTwo.setProgress(pTwo.getProgress()+10);
            two.setChecked(true);
            three.setChecked(false);
            four.setChecked(false);
            five.setChecked(false);
            one.setChecked(false);
        });

        three.setOnClickListener(view -> {
            pThree.setProgress(pThree.getProgress()  + 10);
            three.setChecked(true);
            two.setChecked(false);
            four.setChecked(false);
            five.setChecked(false);
            one.setChecked(false);
        });

        four.setOnClickListener(view -> {
            pFour.setProgress(pFour.getProgress() + 10);
            four.setChecked(true);
            two.setChecked(false);
            three.setChecked(false);
            five.setChecked(false);
            one.setChecked(false);

        });

        five.setOnClickListener(view -> {
            pFive.setProgress(pFive.getProgress() + 10);
            five.setChecked(true);
            two.setChecked(false);
            three.setChecked(false);
            four.setChecked(false);
            one.setChecked(false);
        });

        System.out.println(one.getText());

        System.out.println(pollTitle.getText());

        return convertView;
    }
}




}