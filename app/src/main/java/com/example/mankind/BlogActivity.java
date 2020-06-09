package com.example.mankind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class BlogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        //Make a reference to recyclerView
        RecyclerView recyclerView = findViewById(R.id.recycle_view);

        //create linear manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Blog[] blogs = loadBlogs();
        BlogAdapter blogAdapter = new BlogAdapter(blogs);
        recyclerView.setAdapter(blogAdapter);
    }

    private Blog[] loadBlogs() {
        Blog blog1 = new Blog();
        blog1.setTitle("Covid19");
        blog1.setAuthor("Sovath");
        blog1.setBody("kjsdfjksdjkfs");


        Blog blog2 = new Blog();
        blog2.setTitle("Mar");
        blog2.setAuthor("Sovath Chean");
        blog2.setBody("kjsdfjksdjkfs");


        Blog blog3 = new Blog();
        blog3.setTitle("Dolpomin");
        blog3.setAuthor("Jim Kwid");
        blog3.setBody("ksdf");


       return new Blog[]{blog1, blog2, blog3};
    }
}
