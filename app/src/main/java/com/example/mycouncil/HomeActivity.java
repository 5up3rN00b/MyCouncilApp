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
import android.widget.ListView;
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



public class HomeActivity extends AppCompatActivity {

    Button mLogout;
    DrawerLayout d1;
    ActionBarDrawerToggle abdt;
    ListView homeListView;
    public static ArrayList<Post> postList = new ArrayList<>();
    public static ArrayList<Post> pollList = new ArrayList<>();
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
                if (id == R.id.branches){
//                    Toast.makeText(HomeActivity.this, "Branches", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), BranchActivity.class ));
                }
                if (id == R.id.post){
//                    Toast.makeText(HomeActivity.this, "Post", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), CreateActivity.class ));
                }
                if (id == R.id.logout){
//                    Toast.makeText(HomeActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class ));
                }
                return true;
            }
        });
        ListView homePostList = findViewById(R.id.homeListView);
        PostListAdapter adapter = new PostListAdapter(this, R.layout.listview_layout, postList);
        homePostList.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    class PostListAdapter extends ArrayAdapter<Post> {
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
            String body = list.get(position).getTitle();
            System.out.println(title+ " "+ body);

            LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(postResource, parent, false);

            TextView postTitle, bodyText, name, branchName;
            Button upvote,downvote;

            postTitle = convertView.findViewById(R.id.postTitleTextView);
            bodyText = convertView.findViewById(R.id.postBodyTextView);
            name = convertView.findViewById(R.id.namePostTextView);
            branchName = convertView.findViewById(R.id.branchNameTextView);
            upvote = convertView.findViewById(R.id.upvote);
            downvote = convertView.findViewById(R.id.downvote);

            postTitle.setText(title);
            bodyText.setText(body);
            name.setText("Jeff Bezos");
            //branchName.setText("Police");

            upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.get(position).addUpvotes();
                }
            });

            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.get(position).addDownvotes();
                }
            });

            return convertView;

        }
    }

}

