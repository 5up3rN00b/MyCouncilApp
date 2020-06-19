package com.example.mycouncil;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mycouncil.Feedback.Post;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class SplashActivity extends AppCompatActivity{
    private static int SPLASH_SCREEN = 4000;
    private static final String TAG = "SplashActivity";
    private boolean first = false;
    Animation topAnim, bottomAnim;
    ImageView title, descrip, flag;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        title = findViewById(R.id.council);
        descrip = findViewById(R.id.communication);
        flag = findViewById(R.id.flag_logo);

        title.setAnimation(topAnim);
        descrip.setAnimation(bottomAnim);
        flag.setAnimation(bottomAnim);

        new GetUsers().execute();
        new GetPostsTask().execute();
    }

    class GetUsers extends AsyncTask<Void, Void, Void> {
        private String text;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                HttpClient httpclient = HttpClients.createDefault();
                HttpGet httpget = new HttpGet("http://73.71.24.214:8008/users/get.php?all=true");

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

            HomeActivity.idToNameMap = new HashMap<>();
            String[] posts = text.split("<br>");

            for (int i = 0; i < posts.length; i++) {
                String[] attributes = posts[i].split("\\|");
                //System.out.println(Arrays.toString(attributes));
                HomeActivity.idToNameMap.put(Integer.parseInt(attributes[0]), attributes[1]);
            }

            if (first) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                first = true;
            }
        }
    }

    class GetPostsTask extends AsyncTask<Void, Void, Void> {
        private String text;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                HttpClient httpclient = HttpClients.createDefault();
                HttpGet httpget = new HttpGet("http://73.71.24.214:8008/posts/get.php?all=true");

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

            if (first) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                first = true;
            }
        }
    }
}
