package com.smarthomebear.restaurantroulette;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    Toolbar toolbar;

    //Variables for the menu
    private DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    //Variables for the address
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView address;
    ImageButton renew;
    private final static int REQUEST_CODE=100;
    private static double lat;
    private static double lng;
    private static String fullAddress;
    private String street;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get current location
        address=findViewById(R.id.location);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        //Fragment open

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.fragment_container, new RouletteFragment());
        fragmentTransaction.commit();


        //set our toolbar as default
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //choice of the element in the menu
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

        //set Button for renew location
        renew=findViewById(R.id.toolbar_location_renew);
        renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });

    }
    //Query current location
    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location!=null){
                            Geocoder geocoder=new Geocoder(MainActivity.this, Locale.getDefault());
                            List<Address> addresses= null;
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                fullAddress=addresses.get(0).getAddressLine(0);
                                street=addresses.get(0).getThoroughfare();
                                lat=addresses.get(0).getLatitude();
                                lng=addresses.get(0).getLongitude();
                                address.setText(street);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else{

                            }
                        }
                    });

        }else{
            askPermission();

            }
        }
        //Ask for location authorization
        private void askPermission(){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        //If rejected, check again and report back
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else{
                Toast.makeText(this, R.string.req_permission, Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
//choice of the element in the menu
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
                AlertDialog.Builder bug=new AlertDialog.Builder(this);
                bug.setTitle(R.string.bug);
                bug.setMessage(R.string.bug_input);
                final EditText inputBug = new EditText(this);
                // Specify the type of input expected
                inputBug.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                bug.setView(inputBug);
                bug.setPositiveButton(R.string.report, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SendEmailTask task = new SendEmailTask("Fehler", inputBug.getText().toString());
                        task.execute();
                        Toast.makeText(MainActivity.this, getString(R.string.msg_sent), Toast.LENGTH_SHORT).show();
                    }
                });
                bug.create().show();
                break;
            case R.id.nav_questions:
                AlertDialog.Builder questions=new AlertDialog.Builder(this);
                questions.setTitle(R.string.questions+"?");
                questions.setMessage(R.string.msg_questions);
                final EditText inputQuestions = new EditText(this);
                // Specify the type of input expected
                inputQuestions.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                questions.setView(inputQuestions);
                questions.setPositiveButton(R.string.transmit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SendEmailTask task = new SendEmailTask("Frage", inputQuestions.getText().toString());
                        task.execute();
                        Toast.makeText(MainActivity.this, getString(R.string.msg_sent), Toast.LENGTH_SHORT).show();
                    }
                });
                questions.create().show();
                break;
            case R.id.nav_dsgvo:
                AlertDialog.Builder dsgvo=new AlertDialog.Builder(this);
                dsgvo.setTitle(R.string.privacy_policy);
                dsgvo.setMessage(R.string.msg_dsgvo);
                dsgvo.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dsgvo.create().show();
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
            Intent intent=new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    //making values available for other classes
    public static double getLat(){
        return lat;
    }
    public static double getLng(){
        return lng;
    }
    public static String getFullAddress(){
        return fullAddress;
    }

}