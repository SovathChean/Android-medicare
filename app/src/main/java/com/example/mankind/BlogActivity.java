package com.example.mankind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class BlogActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        //Make a reference to recyclerView
        recyclerView = findViewById(R.id.recycle_view);

        //create linear manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

         loadBlogs();

    }

    public void loadBlogs() {
        String url = "https://my.api.mockaroo.com/testing.json?key=9e083da0";

        StringRequest Request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                Blog[] blogs = gson.fromJson(response, Blog[].class);
                BlogAdapter blogAdapter = new BlogAdapter(BlogActivity.this, blogs);
                recyclerView.setAdapter(blogAdapter);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BlogActivity.this, "Something wrong with connection", Toast.LENGTH_SHORT).show();
                Log.d("blog", "Something wrong with "+ error.getMessage());
            }
        });

        Volley.newRequestQueue(this).add(Request);
    }
}
