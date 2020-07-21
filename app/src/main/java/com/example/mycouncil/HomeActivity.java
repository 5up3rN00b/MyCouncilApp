package com.example.mycouncil;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    Button mLogout;
    DrawerLayout d1;
    ActionBarDrawerToggle abdt;
    ListView homeListView;
    ImageView title;
    SwipeRefreshLayout swiper_no_swiping;
    public static ArrayList<Post> postList = new ArrayList<>();
    public static ArrayList<Poll> pollList = new ArrayList<com.example.mycouncil.Feedback.Poll>();

    public static HashMap<Integer, String> idToNameMap = new HashMap<>();

    public static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        homeListView = findViewById(R.id.homeListView);
//        homeListView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        final Spinner branchSpinner = findViewById(R.id.branchSpinner);
        branchSpinner.setVisibility(View.VISIBLE);
        ArrayAdapter<CharSequence> branchAdapter = ArrayAdapter.createFromResource(this, R.array.branchNames, android.R.layout.simple_spinner_item);
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(branchAdapter);

        title = findViewById(R.id.imageView4);

        ListView homePostList = findViewById(R.id.homeListView);

        swiper_no_swiping = findViewById(R.id.swipethismuthafucka);


        swiper_no_swiping.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetPostsTask().execute();
            }
        });


        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    title.setBackgroundResource(R.drawable.home);
                    PostListAdapter adapter = new PostListAdapter(HomeActivity.this, R.layout.listview_layout, postList);
                    homePostList.setAdapter(adapter);
                }

                if(position == 1){
                    title.setBackgroundResource(R.drawable.police);
                    ArrayList<Post> policeList = new ArrayList<>();
                    for(Post p : postList) {
                        if (p.getBranch().equals("Police Department")) {
                            policeList.add(p);
                        }
                    }
                    PostListAdapter adapter = new PostListAdapter(HomeActivity.this, R.layout.listview_layout, policeList);
                    homePostList.setAdapter(adapter);

                }

                if(position == 2){
                    title.setBackgroundResource(R.drawable.fire);
                    ArrayList<Post> fireList = new ArrayList<>();
                    for(Post p : postList) {
                        if (p.getBranch().equals("Fire Department")) {
                            fireList.add(p);
                        }
                    }
                    PostListAdapter adapter = new PostListAdapter(HomeActivity.this, R.layout.listview_layout, fireList);
                    homePostList.setAdapter(adapter);

                }

                if(position == 3){
                    title.setBackgroundResource(R.drawable.city);
                    ArrayList<Post> cityList = new ArrayList<>();
                    for(Post p : postList) {
                        if (p.getBranch().equals("City Council")) {
                            cityList.add(p);
                        }
                    }
                    PostListAdapter adapter = new PostListAdapter(HomeActivity.this, R.layout.listview_layout, cityList);
                    homePostList.setAdapter(adapter);
                }

                if(position == 4) {
                    title.setBackgroundResource(R.drawable.education1);
                    ArrayList<Post> eduList = new ArrayList<>();
                    for (Post p : postList) {
                        if (p.getBranch().equals("Education Department")) {
                            eduList.add(p);
                        }
                    }
                    PostListAdapter adapter = new PostListAdapter(HomeActivity.this, R.layout.listview_layout, eduList);
                    homePostList.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("Techie","----------------------------citizenship2----------------------------");
            }
        });
        System.out.println(LoginActivity.isCitizen);


        if (LoginActivity.isCitizen){
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().findItem(R.id.poll).setVisible(false);
            navigationView.getMenu().findItem(R.id.viewPolls).setVisible(true);
        }

        if (!LoginActivity.isCitizen){
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().findItem(R.id.poll).setVisible(true);
            navigationView.getMenu().findItem(R.id.viewPolls).setVisible(true);
        }


        d1 =findViewById(R.id.d1);
        abdt = new ActionBarDrawerToggle(this, d1, R.string.Open, R.string.Close);

        abdt.setDrawerIndicatorEnabled(true);

        d1.addDrawerListener(abdt);
        abdt.syncState();


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                else if (id == R.id.createPolls){
                    startActivity((new Intent(getApplicationContext(), PollActivity.class)));
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    class PostListAdapter extends ArrayAdapter <Post> {
        private ArrayList<Post> list;
        private Context postContext;
        int postResource;

        public PostListAdapter(@NonNull Context context, int resource, ArrayList<Post> objects){
            super(context, resource, objects);
            postContext = context;
            postResource = resource;
            list = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String title = list.get(position).getTitle();
            String body = list.get(position).getDescription();

            String branchNameEnter = list.get(position).getBranch();
            Integer userID = list.get(position).getUserId();

            LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(postResource, parent, false);

            TextView postTitle, bodyText, name, branchName;
            Button upvote,downvote;
            ImageView pfp;

            postTitle = convertView.findViewById(R.id.postTitleTextView);
            bodyText = convertView.findViewById(R.id.postBodyTextView);
            name = convertView.findViewById(R.id.namePostTextView);
            branchName = convertView.findViewById(R.id.branchNamePostTextView);
            upvote = convertView.findViewById(R.id.upvote);
            downvote = convertView.findViewById(R.id.downvote);
            pfp = convertView.findViewById(R.id.profilePictureImageView);

            postTitle.setText(title);
            bodyText.setText(body);
            int randomP = (int)(Math.random() * 4) + 1;
            if(randomP == 1){
                pfp.setImageResource(R.drawable.ellipse12);
            } else if(randomP == 2){
                pfp.setImageResource(R.drawable.mymantropo);
            } else if(randomP == 3){
                pfp.setImageResource(R.drawable.elizabeth);
            } else {
                pfp.setImageResource(R.drawable.susan);
            }

            name.setText(idToNameMap.get(userID));

            branchName.setText(branchNameEnter);


            int post_id = list.get(position).getPostId();

            if (LoginActivity.upvoteClicked.get(position)) {
                upvote.setBackgroundResource(R.drawable.arrowup_blue);
            } else {
                upvote.setBackgroundResource(R.drawable.arrowup);
            }

            if (LoginActivity.downvoteClicked.get(position)) {
                downvote.setBackgroundResource(R.drawable.arrowdown_blue);
            } else {
                downvote.setBackgroundResource(R.drawable.arrowdown);
            }

            TextView upvoteCounter;
            TextView downvoteCounter;

            upvoteCounter = convertView.findViewById(R.id.upvoteCounter);
            downvoteCounter = convertView.findViewById(R.id.downvoteCounter);
            upvoteCounter.setText(String.valueOf(list.get(position).getUpvotes()));
            downvoteCounter.setText(String.valueOf(list.get(position).getDownvotes()));

            upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LoginActivity.upvoteClicked.get(position)) {
                        upvote.setBackgroundResource(R.drawable.arrowup);
                        list.get(position).subUpvotes();
                        new UpdateVotesTask(-1, 0, post_id).execute();
                    } else {
                        upvote.setBackgroundResource(R.drawable.arrowup_blue);
                        list.get(position).addUpvotes();
                        new UpdateVotesTask(1, 0, post_id).execute();
                    }

                    LoginActivity.upvoteClicked.set(position, !LoginActivity.upvoteClicked.get(position));
                    upvoteCounter.setText(Integer.toString(list.get(position).getUpvotes()));
                }
            });

            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LoginActivity.downvoteClicked.get(position)) {
                        downvote.setBackgroundResource(R.drawable.arrowdown);
                        list.get(position).subDownvotes();
                        new UpdateVotesTask(0, -1, post_id).execute();
                    } else {
                        downvote.setBackgroundResource(R.drawable.arrowdown_blue);
                        list.get(position).addDownvotes();
                        new UpdateVotesTask(0, 1, post_id).execute();
                    }

                    LoginActivity.downvoteClicked.set(position, !LoginActivity.downvoteClicked.get(position));
                    downvoteCounter.setText(Integer.toString(list.get(position).getDownvotes()));
                }
            });

            return convertView;
        }
    }

    class UpdateVotesTask extends AsyncTask<Void, Void, Void> {
        private String text;
        private int upvotes, downvotes, id;

        public UpdateVotesTask(int upvotes, int downvotes, int id) {
            this.upvotes = upvotes;
            this.downvotes = downvotes;
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                HttpClient httpclient = HttpClients.createDefault();
                HttpPost httppost = new HttpPost("http://IP:8008/posts/add.php");

                List<NameValuePair> params = new ArrayList<NameValuePair>(4);
                params.add(new BasicNameValuePair("updateVotes", "true"));
                params.add(new BasicNameValuePair("upvotes", Integer.toString(upvotes)));
                params.add(new BasicNameValuePair("downvotes", Integer.toString(downvotes)));
                params.add(new BasicNameValuePair("id", Integer.toString(id)));

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
            Log.d(TAG, "Added upvotes and downvotes");
        }
    }

    class GetPostsTask extends AsyncTask<Void, Void, Void> {
        private String text;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                HttpClient httpclient = HttpClients.createDefault();
                HttpGet httpget = new HttpGet("http://IP:8008/posts/get.php?all=true");

                //Execute and get the response.
                HttpResponse response = httpclient.execute(httpget);
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
            HomeActivity.postList = new ArrayList<>();
            String[] posts = text.split("<br>");

            for (int i = 0; i < posts.length; i++) {
                String[] attributes = posts[i].split("\\|");
                //System.out.println(Arrays.toString(attributes));
                HomeActivity.postList.add(new Post(attributes[2], attributes[3], attributes[6], Integer.parseInt(attributes[1]), Integer.parseInt(attributes[0]), Integer.parseInt(attributes[4]), Integer.parseInt(attributes[5])));

                if (i >= LoginActivity.upvoteClicked.size()) {
                    LoginActivity.upvoteClicked.add(false);
                }

                if (i >= LoginActivity.downvoteClicked.size()) {
                    LoginActivity.downvoteClicked.add(false);
                }

                Collections.sort(HomeActivity.postList, new Comparator<Post>() {
                    @Override
                    public int compare(Post o1, Post o2) {
                        return o2.getTotalVotes() - o1.getTotalVotes();
                    }
                });
            }

            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

