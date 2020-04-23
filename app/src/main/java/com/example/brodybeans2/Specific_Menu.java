package com.example.brodybeans2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

public class Specific_Menu extends AppCompatActivity {

    private Context context;

    // this is hard coded
    private ArrayList<String> espresso;
    private ArrayList<String> menuCat;
    private ArrayList<String> nonEspresso;
    private  ArrayList<String> breakfast;
    private ArrayList<String> blendBev;
    private HashMap<String,  ArrayList<String>> menu;

    //espresso.add("latte");
    //espresso.add("cappicino");



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = getApplicationContext();
        espresso = new ArrayList<String>();
        espresso.add("coffee");
        nonEspresso = new ArrayList<String>();
        nonEspresso.add("tea");
        breakfast = new ArrayList<String>();
        breakfast.add("Bagel");
        blendBev = new ArrayList<String>();
        blendBev.add("Java Chip");

        menu = new HashMap<String, ArrayList<String>>();

            //menu.put("Espresso",espresso);
            menu.put("Non Espresso",nonEspresso);
           // menu.put("Breakfast",breakfast);
           // menu.put("Blended Beverage",blendBev);




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific__menu);

        String cat = PreferenceManager.getDefaultSharedPreferences(context).getString("category", null);
        if (cat != null) {
            menuCat = menu.get(cat);
        }

    }
}
