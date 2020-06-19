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

    ArrayList<Poll> pollList = new ArrayList<>();
    ArrayList<Choice> listOfChoices = new ArrayList<>();
    ArrayList<Choice> listOfChoices1 = new ArrayList<>();
    DrawerLayout d1;
    ActionBarDrawerToggle abdt;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_polls);

        listOfChoices.add(new Choice("Funding Schools", 3));
        listOfChoices.add(new Choice("Create new parks", 1));
        listOfChoices.add(new Choice("Improve roads/ infrastructure", 0));
        listOfChoices.add(new Choice("Homeless Shelters", 1));
        listOfChoices.add(new Choice("Building residential neighborhoods", 0));

        listOfChoices1.add(new Choice("Not at all", 3));
        listOfChoices1.add(new Choice("Could be better", 1));
        listOfChoices1.add(new Choice("It's alright", 0));
        listOfChoices1.add(new Choice("I like it", 1));
        listOfChoices1.add(new Choice("It's the best!", 5));

        pollList.add(new Poll("What should we spend our additional budget on?", listOfChoices));
        pollList.add(new Poll("How satisfied are you with your community?", listOfChoices1));

        if (LoginActivity.isCitizen) {
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().findItem(R.id.poll).setVisible(false);
        }

        if (!LoginActivity.isCitizen) {
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().findItem(R.id.poll).setVisible(true);
        }

        for (Poll p : pollList) {
//            System.out.println(p.getTitle());
        }


        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);

        d1 = findViewById(R.id.d1);
        abdt = new ActionBarDrawerToggle(this, d1, R.string.Open, R.string.Close);

        abdt.setDrawerIndicatorEnabled(true);

        d1.addDrawerListener(abdt);
        abdt.syncState();

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.home) {
//                    Toast.makeText(HomeActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                } else if (id == R.id.post) {
//                    Toast.makeText(HomeActivity.this, "Post", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), CreateActivity.class));
                } else if (id == R.id.logout) {
//                    Toast.makeText(HomeActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else if (id == R.id.poll) {
                    startActivity(new Intent(getApplicationContext(), PollActivity.class));
                } else if (id == R.id.viewPolls) {
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

        public PollListAdapter(@NonNull Context context, int resource, ArrayList<Poll> objects) {
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

            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(pollResource, parent, false);

            TextView pollTitle;
            RadioButton one, two, three, four, five;
            ProgressBar pOne, pTwo, pThree, pFour, pFive;
            Integer op1, op2, op3, op4, op5;
            TextView t1, t2, t3, t4, t5;

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

            t1 = convertView.findViewById(R.id.percentDisplay1);
            t2 = convertView.findViewById(R.id.percentDisplay2);
            t3 = convertView.findViewById(R.id.percentDisplay3);
            t4 = convertView.findViewById(R.id.percentDisplay4);
            t5 = convertView.findViewById(R.id.percentDisplay5);

            pollTitle.setText(question);
            one.setText(optionOne);
            two.setText(optionTwo);
            three.setText(optionThree);
            four.setText(optionFour);
            five.setText(noneOfTheAboveOption);

            Integer totalVotes = list.get(position).getTotalVotes();

            op1 = 100 * list.get(position).getChoice(0).getVotes() / totalVotes;
            op2 = 100 * list.get(position).getChoice(1).getVotes() / totalVotes;
            op3 = 100 * list.get(position).getChoice(2).getVotes() / totalVotes;
            op4 = 100 * list.get(position).getChoice(3).getVotes() / totalVotes;
            op5 = 100 * list.get(position).getChoice(4).getVotes() / totalVotes;





            one.setOnClickListener(view -> {
                list.get(position).getChoice(0).addVote();
                pOne.setProgress(100 * list.get(position).getChoice(0).getVotes() / list.get(position).getTotalVotes());
                pTwo.setProgress(100 * list.get(position).getChoice(1).getVotes() / list.get(position).getTotalVotes());
                pThree.setProgress(100 * list.get(position).getChoice(2).getVotes() / list.get(position).getTotalVotes());
                pFour.setProgress(100 * list.get(position).getChoice(3).getVotes() / list.get(position).getTotalVotes());
                pFive.setProgress(100 * list.get(position).getChoice(4).getVotes() / list.get(position).getTotalVotes());
                t1.setVisibility(View.VISIBLE);
                t2.setVisibility(View.VISIBLE);
                t3.setVisibility(View.VISIBLE);
                t4.setVisibility(View.VISIBLE);
                t5.setVisibility(View.VISIBLE);
                t1.setText(100 * list.get(position).getChoice(0).getVotes() / list.get(position).getTotalVotes() + "%");
                t2.setText(100 * list.get(position).getChoice(1).getVotes() / list.get(position).getTotalVotes() + "%");
                t3.setText(100 * list.get(position).getChoice(2).getVotes() / list.get(position).getTotalVotes() + "%");
                t4.setText(100 * list.get(position).getChoice(3).getVotes() / list.get(position).getTotalVotes() + "%");
                t5.setText(100 * list.get(position).getChoice(4).getVotes() / list.get(position).getTotalVotes() + "%");
                one.setChecked(true);
                two.setChecked(false);
                three.setChecked(false);
                four.setChecked(false);
                five.setChecked(false);
            });


            two.setOnClickListener(view -> {
                list.get(position).getChoice(1).addVote();
                pOne.setProgress(100 * list.get(position).getChoice(0).getVotes() / list.get(position).getTotalVotes());
                pTwo.setProgress(100 * list.get(position).getChoice(1).getVotes() / list.get(position).getTotalVotes());
                pThree.setProgress(100 * list.get(position).getChoice(2).getVotes() / list.get(position).getTotalVotes());
                pFour.setProgress(100 * list.get(position).getChoice(3).getVotes() / list.get(position).getTotalVotes());
                pFive.setProgress(100 * list.get(position).getChoice(4).getVotes() / list.get(position).getTotalVotes());
                t1.setVisibility(View.VISIBLE);
                t2.setVisibility(View.VISIBLE);
                t3.setVisibility(View.VISIBLE);
                t4.setVisibility(View.VISIBLE);
                t5.setVisibility(View.VISIBLE);
                t1.setText(100 * list.get(position).getChoice(0).getVotes() / list.get(position).getTotalVotes() + "%");
                t2.setText(100 * list.get(position).getChoice(1).getVotes() / list.get(position).getTotalVotes() + "%");
                t3.setText(100 * list.get(position).getChoice(2).getVotes() / list.get(position).getTotalVotes() + "%");
                t4.setText(100 * list.get(position).getChoice(3).getVotes() / list.get(position).getTotalVotes() + "%");
                t5.setText(100 * list.get(position).getChoice(4).getVotes() / list.get(position).getTotalVotes() + "%");
                two.setChecked(true);
                three.setChecked(false);
                four.setChecked(false);
                five.setChecked(false);
                one.setChecked(false);
            });

            three.setOnClickListener(view -> {
                list.get(position).getChoice(2).addVote();
                pOne.setProgress(100 * list.get(position).getChoice(0).getVotes() / list.get(position).getTotalVotes());
                pTwo.setProgress(100 * list.get(position).getChoice(1).getVotes() / list.get(position).getTotalVotes());
                pThree.setProgress(100 * list.get(position).getChoice(2).getVotes() / list.get(position).getTotalVotes());
                pFour.setProgress(100 * list.get(position).getChoice(3).getVotes() / list.get(position).getTotalVotes());
                pFive.setProgress(100 * list.get(position).getChoice(4).getVotes() / list.get(position).getTotalVotes());
                t1.setVisibility(View.VISIBLE);
                t2.setVisibility(View.VISIBLE);
                t3.setVisibility(View.VISIBLE);
                t4.setVisibility(View.VISIBLE);
                t5.setVisibility(View.VISIBLE);
                t1.setText(100 * list.get(position).getChoice(0).getVotes() / list.get(position).getTotalVotes() + "%");
                t2.setText(100 * list.get(position).getChoice(1).getVotes() / list.get(position).getTotalVotes() + "%");
                t3.setText(100 * list.get(position).getChoice(2).getVotes() / list.get(position).getTotalVotes() + "%");
                t4.setText(100 * list.get(position).getChoice(3).getVotes() / list.get(position).getTotalVotes() + "%");
                t5.setText(100 * list.get(position).getChoice(4).getVotes() / list.get(position).getTotalVotes() + "%");
                pThree.setProgress(op3);
                three.setChecked(true);
                two.setChecked(false);
                four.setChecked(false);
                five.setChecked(false);
                one.setChecked(false);
            });

            four.setOnClickListener(view -> {
                list.get(position).getChoice(3).addVote();
                pOne.setProgress(100 * list.get(position).getChoice(0).getVotes() / list.get(position).getTotalVotes());
                pTwo.setProgress(100 * list.get(position).getChoice(1).getVotes() / list.get(position).getTotalVotes());
                pThree.setProgress(100 * list.get(position).getChoice(2).getVotes() / list.get(position).getTotalVotes());
                pFour.setProgress(100 * list.get(position).getChoice(3).getVotes() / list.get(position).getTotalVotes());
                pFive.setProgress(100 * list.get(position).getChoice(4).getVotes() / list.get(position).getTotalVotes());
                t1.setVisibility(View.VISIBLE);
                t2.setVisibility(View.VISIBLE);
                t3.setVisibility(View.VISIBLE);
                t4.setVisibility(View.VISIBLE);
                t5.setVisibility(View.VISIBLE);
                t1.setText(100 * list.get(position).getChoice(0).getVotes() / list.get(position).getTotalVotes() + "%");
                t2.setText(100 * list.get(position).getChoice(1).getVotes() / list.get(position).getTotalVotes() + "%");
                t3.setText(100 * list.get(position).getChoice(2).getVotes() / list.get(position).getTotalVotes() + "%");
                t4.setText(100 * list.get(position).getChoice(3).getVotes() / list.get(position).getTotalVotes() + "%");
                t5.setText(100 * list.get(position).getChoice(4).getVotes() / list.get(position).getTotalVotes() + "%");
                four.setChecked(true);
                two.setChecked(false);
                three.setChecked(false);
                five.setChecked(false);
                one.setChecked(false);

            });

            five.setOnClickListener(view -> {


                list.get(position).getChoice(4).addVote();
                pOne.setProgress(100 * list.get(position).getChoice(0).getVotes() / list.get(position).getTotalVotes());
                pTwo.setProgress(100 * list.get(position).getChoice(1).getVotes() / list.get(position).getTotalVotes());
                pThree.setProgress(100 * list.get(position).getChoice(2).getVotes() / list.get(position).getTotalVotes());
                pFour.setProgress(100 * list.get(position).getChoice(3).getVotes() / list.get(position).getTotalVotes());
                pFive.setProgress(100 * list.get(position).getChoice(4).getVotes() / list.get(position).getTotalVotes());
                t1.setVisibility(View.VISIBLE);
                t2.setVisibility(View.VISIBLE);
                t3.setVisibility(View.VISIBLE);
                t4.setVisibility(View.VISIBLE);
                t5.setVisibility(View.VISIBLE);
                t1.setText(100 * list.get(position).getChoice(0).getVotes() / list.get(position).getTotalVotes() + "%");
                t2.setText(100 * list.get(position).getChoice(1).getVotes() / list.get(position).getTotalVotes() + "%");
                t3.setText(100 * list.get(position).getChoice(2).getVotes() / list.get(position).getTotalVotes() + "%");
                t4.setText(100 * list.get(position).getChoice(3).getVotes() / list.get(position).getTotalVotes() + "%");
                t5.setText(100 * list.get(position).getChoice(4).getVotes() / list.get(position).getTotalVotes() + "%");
                five.setChecked(true);
                two.setChecked(false);
                three.setChecked(false);
                four.setChecked(false);
                one.setChecked(false);

            });


            return convertView;
        }
    }


}