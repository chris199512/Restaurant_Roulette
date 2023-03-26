package com.smarthomebear.restaurantroulette;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class RouletteFragment extends Fragment implements View.OnClickListener{

    ImageView imageViewRoulette;
    Button filterButton;
    Animation rotateImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_roulette, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Set roulette image as variable
        imageViewRoulette=getView().findViewById(R.id.roulette);

        //Make filter button clickable
        filterButton=getView().findViewById(R.id.filter);
        filterButton.setOnClickListener(this);
    }


    public void onClick(View v){

    }

}
