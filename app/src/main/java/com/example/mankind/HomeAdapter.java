package com.example.mankind;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends RecyclerView.Adapter<BlogViewHolder> {
    //data set
    private Blog[] blogs;
    private Blog blog;
    private Context mContext;

    public HomeAdapter(Context mContext, Blog[] blogs) {
        this.blogs = blogs;
        this.mContext = mContext;
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
        blog = blogs[position];
        holder.bind(blog);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("OnClick", blog.getBody());
                Intent intent = new Intent(mContext, BlogDetailActivty.class);
                intent.putExtra("image_url", blog.getPhoto());
                intent.putExtra("title", blog.getTitle());
                intent.putExtra("body", blog.getBody());
                intent.putExtra("author", blog.getAuthor());

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
