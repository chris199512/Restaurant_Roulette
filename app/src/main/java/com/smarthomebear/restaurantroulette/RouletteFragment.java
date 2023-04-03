package com.smarthomebear.restaurantroulette;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class RouletteFragment extends Fragment {

    ImageView imageViewRoulette;
    Button filterButton;
    Animation rotateImage;

    //set filter variables
    private boolean nowOpenFilter=true;
    private int distanceFilter=5000;//in meters
    private int priceFilter=0;
    private double ratingFilter=1.0;

    private List<String> selectedItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roulette, container, false);

        filterButton = view.findViewById(R.id.filter);
        imageViewRoulette=view.findViewById(R.id.roulette);

        imageViewRoulette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation rotateImage=AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate);
                imageViewRoulette.startAnimation(rotateImage);
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPreferencesDialog();
            }
        });

        return view;
    }


    private void showPreferencesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Ernährungspräferenzen auswählen")
                .setMultiChoiceItems(R.array.preferences, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                String[] preferences = getResources().getStringArray(R.array.preferences);
                                String selectedItem = preferences[which];
                                if (isChecked) {
                                    selectedItems.add(selectedItem);
                                } else {
                                    selectedItems.remove(selectedItem);
                                }
                            }
                        })
                //slider
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Do something with the selected items
                        Toast.makeText(getActivity(), "Selected items: " + selectedItems.toString(),
                                Toast.LENGTH_SHORT).show();
                        //nowOpenFilter=wert von der auswahl;
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        builder.create().show();
    }
}


