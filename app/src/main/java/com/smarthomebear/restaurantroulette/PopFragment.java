package com.smarthomebear.restaurantroulette;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class PopFragment extends Fragment {
        private List<String> selectedItems = new ArrayList<>();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_pop, container, false);

            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button preferencesButton = view.findViewById(R.id.preferences_button);
            preferencesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // Do something with the selected items
                            Toast.makeText(getActivity(), "Selected items: " + selectedItems.toString(),
                                    Toast.LENGTH_SHORT).show();
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



