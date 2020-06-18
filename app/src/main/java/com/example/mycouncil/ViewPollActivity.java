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

    ListView viewPollList;
    ArrayList <Poll> pollList = new ArrayList<>();
    ArrayList <Choice> listOfChoices = new ArrayList<>();



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

        for (Poll p: pollList){
            System.out.println(p.getTitle());
        }

        viewPollList = findViewById(R.id.seepolls);
        PollListAdapter ladapter = new PollListAdapter(this, R.layout.poll_layout, pollList);
        viewPollList.setAdapter(ladapter);

    }

class PollListAdapter extends ArrayAdapter<Post> {
    private ArrayList<Poll> list;
    private Context pollContext;
    int pollResource;

    public PollListAdapter(@NonNull Context context, int resource, ArrayList<Poll> objects){
        super(context, resource);
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
        String optionFive = list.get(position).getChoice(4).getDescription();

        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(pollResource, parent, false);

        TextView pollTitle;
        Button one, two, three, four, five;

        pollTitle = convertView.findViewById(R.id.pollTitleTextView);
        one = convertView.findViewById(R.id.pollOption1);
        two = convertView.findViewById(R.id.pollOption2);
        three = convertView.findViewById(R.id.pollOption3);
        four = convertView.findViewById(R.id.pollOption4);
        five = convertView.findViewById(R.id.noneOfTheAboveOption);

        pollTitle.setText(question);
        one.setText(optionOne);
        two.setText(optionTwo);
        three.setText(optionThree);
        four.setText(optionFour);
        five.setText(optionFive);

        System.out.println(one);

        System.out.println(pollTitle.getText());

        return convertView;
    }
}
}