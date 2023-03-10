package com.smarthomebear.restaurantroulette;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.imageView2);
    }

    public void start(View view) {
        Animation rotateImage= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
        imageView.startAnimation(rotateImage);
    }

}