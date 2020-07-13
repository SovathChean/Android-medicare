package com.example.mankind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       //Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        loadFragment(new HomeFragment());

        //register context_menu
//        ImageView brain = findViewById(R.id.brain);
//       registerForContextMenu(brain);
        BottomNavigationView navigation = findViewById(R.id.bottomNav);
        navigation.setOnNavigationItemSelectedListener(this);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.toolbar_menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Toast.makeText(this, "Selected Item: " +item.getTitle(), Toast.LENGTH_SHORT).show();
//        switch (item.getItemId()){
//            case R.id.menu:
//                return true;
//            case R.id.notification:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.context_menu, menu);
//    }

//    public void onClick(View view)
//    {
//        Intent intent = new Intent(this, BlogActivity.class);
//        startActivity(intent);
//        finish();
//    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.home:
                fragment = new HomeFragment();
                break;

            case R.id.blog:
                fragment = new BlogsFragment();
                break;

            case R.id.more:
                fragment = new MoreFragment();
                break;

        }

        return loadFragment(fragment);
    }

    public boolean loadFragment(Fragment fragment)
    {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
