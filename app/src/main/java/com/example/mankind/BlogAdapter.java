package com.example.mankind;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogViewHolder> {

    //data set
    private Blog[] blogs;

    public BlogAdapter(Blog[] blogs) {
        this.blogs = blogs;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View blogItems = layoutInflater.inflate(R.layout.activity_blog_list, parent, false);

        return new BlogViewHolder(blogItems);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
      Blog blog = blogs[position];
      holder.bind(blog);
    }

    @Override
    public int getItemCount() {
        return blogs.length;
    }
}
