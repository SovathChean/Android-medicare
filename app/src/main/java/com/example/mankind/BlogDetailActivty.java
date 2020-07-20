package com.example.mankind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.view.SimpleDraweeView;

public class BlogDetailActivty extends AppCompatActivity {
    private TextView Bbody, Btitle, Bauthor;
    private SimpleDraweeView img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_blog_item);

        getIncomingIntent();
    }

    private void getIncomingIntent(){
        Log.d("OnClick", "welcome to blog detail");
        if(getIntent().hasExtra("image_url") && getIntent().hasExtra("title")
          && getIntent().hasExtra("body")&&getIntent().hasExtra("author")
        ){
            String imageUrl = getIntent().getStringExtra("image_url");
            String title = getIntent().getStringExtra("title");
            String body= getIntent().getStringExtra("body");
            String author = getIntent().getStringExtra("author");


            setImage(imageUrl, title, body, author);
        }
    }
    private void setImage(String imageUrl, String title, String body, String author){
        Btitle = findViewById(R.id.title);
        Bbody = findViewById(R.id.body);
        Bauthor = findViewById(R.id.author);
        img = findViewById(R.id.blog_item);

        img.setImageURI(imageUrl);
        Btitle.setText(title);
        Bbody.setText(body);
        Bauthor.setText(author);

    }


}
