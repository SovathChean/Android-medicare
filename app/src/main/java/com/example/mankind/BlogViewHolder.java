package com.example.mankind;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class BlogViewHolder extends RecyclerView.ViewHolder {

    private TextView title, body, timestamp, author;

    public BlogViewHolder(@NonNull View itemView) {
        super(itemView)
        title = (TextView) itemView.findViewById(R.id.title);
        body = (TextView) itemView.findViewById(R.id.body);
        author = (TextView) itemView.findViewById(R.id.author);
    }

    public void bind(Blog blogs){
        title.setText(blogs.getTitle());
        body.setText(blogs.getBody());
        author.setText(blogs.getAuthor());
    }
}
