package com.smarthomebear.restaurantroulette;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    ImageView imageViewRoulette;
    Button filterButton;
    Animation rotateImage;

    Toolbar toolbar;

    private DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set our toolbar as default
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set Hamburger icon
        drawer=findViewById(R.id.drawer_Layout);
        toggle=new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Set roulette image as variable
        imageViewRoulette=findViewById(R.id.roulette);

        //Make filter button clickable
        filterButton=findViewById(R.id.filter);
        filterButton.setOnClickListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_roulette:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RouletteFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Lets close only the menu when going back
    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void start(View view) {
        //Rotate image
        rotateImage= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
        imageViewRoulette.startAnimation(rotateImage);
    }

    public void onClick(View v){

        startActivity(new Intent(this, MapsActivity.class));
    }
}