package com.example.brodybeans2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Adapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

public class Specific_Menu extends AppCompatActivity {

    private Context context;
    private RecyclerView specMenRecyclerView;
    private RecyclerView.Adapter specMenAdapter;
    private RecyclerView.LayoutManager specMenLayoutManager;


    // this is hard coded
    private ArrayList<String> espresso;
    private ArrayList<String> menuCat;
    private static ArrayList<String> nonEspresso = new
            ArrayList<>(Arrays.asList("Drip Coffee", "Coffee Refill",
            "Chai Tea Latte" , "London Fog", "Hot Tea","Hot Chocolate", "Steamer", "Iced Coffee", "Iced Tea" ));
    private static ArrayList<String> breakfast = new ArrayList<>(Arrays.asList("Bagel w/ Cream Cheese" , "Bagel w/ Butter or Jelly",
            "Bagel w/ Hummus", "Egg & cheese"));
    private ArrayList<String> blendBev;
    private HashMap<String,  ArrayList<String>> menu;

    //espresso.add("latte");
    //espresso.add("cappicino");



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = getApplicationContext();
        //jsut so not null will fix in a little
        espresso = new ArrayList<String>();
        espresso.add("coffee");
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
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String cat = PreferenceManager.getDefaultSharedPreferences(context).getString("category", null);
        System.out.print("saved pref is   " + cat);
        if (cat != null) {
            menuCat = menu.get(cat);
            //System.out.print(menuCat);
        }
        setContentView(R.layout.activity_specific__menu);
        buildRecyclerView();


    }


    public void buildRecyclerView() {
        specMenRecyclerView =findViewById(R.id.specMenList);

        //mRecyclerView.setHasFixedSize(true);
        specMenLayoutManager = new LinearLayoutManager(this);
        specMenAdapter = new specMenAdapter(menuCat);
        //orderItemAdapter = new OrderItemAdapter(itemList);

        specMenRecyclerView.setLayoutManager(specMenLayoutManager);
        specMenRecyclerView.setAdapter(specMenAdapter);

    }
}
