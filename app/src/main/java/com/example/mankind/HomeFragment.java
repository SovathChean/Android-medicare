package com.example.mankind;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        //Make a reference to recyclerView
        recyclerView = view.findViewById(R.id.home_recycle);
        progressBar = view.findViewById(R.id.progress_bar);
        //create linear manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        loadBlogs();

        return view;

    }
    public void loadBlogs() {

        showLoading(true);
        String url = "https://my.api.mockaroo.com/testing.json?key=9e083da0";


        StringRequest Request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                Blog[] blogs = gson.fromJson(response, Blog[].class);
                HomeAdapter homeAdapter = new HomeAdapter(getContext(), blogs);
                recyclerView.setAdapter(homeAdapter);
                showLoading(false);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Something wrong with connection", Toast.LENGTH_SHORT).show();
                Log.d("blog", "Something wrong with "+ error.getMessage());

                showLoading(false);
            }
        });

        Volley.newRequestQueue(getActivity()).add(Request);
    }
    public void showLoading(boolean state)
    {
        if(state){

            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
        else {
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
