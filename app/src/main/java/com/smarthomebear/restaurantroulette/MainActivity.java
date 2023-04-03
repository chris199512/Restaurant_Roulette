package com.smarthomebear.restaurantroulette;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    Toolbar toolbar;

    private DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Fragment open

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.fragment_container, new RouletteFragment());
        fragmentTransaction.commit();

        //Popup

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

        //Start Roulette Fragment on first start
        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new RouletteFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_roulette);



        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_roulette:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RouletteFragment()).commit();
                break;
            case R.id.nav_maps:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MapsFragment()).commit();
                break;
            case R.id.nav_favorite:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FavoriteFragment()).commit();
                break;
            case R.id.nav_bug:
                Toast.makeText(this, "Bug", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_questions:
                Toast.makeText(this, "Q&A", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_dsgvo:
                AlertDialog.Builder alert=new AlertDialog.Builder(this);
                alert.setTitle("Datenschutzerklärung");
                alert.setMessage("Hier steht Text");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.create().show();
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

}