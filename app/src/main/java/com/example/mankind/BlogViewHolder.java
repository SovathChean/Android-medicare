package com.example.mankind;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

public class BlogViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout parentLayout;
    private TextView title, body, timestamp, author;
    private SimpleDraweeView photo;

    public BlogViewHolder(@NonNull View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        body = (TextView) itemView.findViewById(R.id.body);
        author = (TextView) itemView.findViewById(R.id.author);
        photo = itemView.findViewById((R.id.img_sender));
        parentLayout = itemView.findViewById(R.id.blog_list);
    }

    public void bind(Blog blogs){
        title.setText(blogs.getTitle());
        body.setText(blogs.getBody());
        author.setText(blogs.getAuthor());
        photo.setImageURI(blogs.getPhoto());
    }
}
