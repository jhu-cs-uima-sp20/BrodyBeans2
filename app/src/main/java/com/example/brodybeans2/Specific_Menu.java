package com.example.brodybeans2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
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
    private ArrayList<String> menuCat;
    private static ArrayList<String> espresso = new ArrayList<>(Arrays.asList("Americano","Latte", "Mocha","Red Eye","Cappuccino","Macchiato Traditional","Espresso","Dirty Chai", "Black & White" , "Jitter Machine" , "Ferrero Rocher","Milky Way"));
    private static ArrayList<String> nonEspresso = new
            ArrayList<>(Arrays.asList("Drip Coffee", "Coffee Refill",
            "Chai Tea Latte" , "London Fog", "Hot Tea","Hot Chocolate", "Steamer", "Iced Coffee", "Iced Tea" ));
    private static ArrayList<String> breakfast = new ArrayList<>(Arrays.asList("Bagel w/ Cream Cheese" , "Bagel w/ Butter or Jelly",
            "Bagel w/ Hummus", "Egg & cheese", "Bacon, Egg & cheese", "Sausage, Egg & cheese", "Muffins & Pastries"));
    private static ArrayList<String> blendBev = new ArrayList<>(Arrays.asList("Mocha Java","Chocolate Chunk","Cookies & Cream","Toasted Coconut","Coffee Toffee","Java Chip", "Green Tea", "Fruit Smoothie"));
    private HashMap<String,  ArrayList<String>> menu;
    //ActionBar actionBar = getActionBar();
    private static ArrayList<Integer> bfastImages = new ArrayList<>(Arrays.asList(R.mipmap.bagel, R.mipmap.jel, R.mipmap.hum, R.mipmap.ec, R.mipmap.bec, R.mipmap.sec, R.mipmap.mufftwo));
    private static ArrayList<Integer> espressImages = new ArrayList<>(Arrays.asList(R.mipmap.hot_coffee, R.mipmap.latte, R.mipmap.mocah, R.mipmap.beancof, R.mipmap.cap, R.mipmap.machtwo, R.mipmap.espres, R.mipmap.tea,R.mipmap.widecaf,R.mipmap.upcaf, R.mipmap.heartcaf, R.mipmap.prettycof));
    private static ArrayList<Integer> nonepressImages = new ArrayList<>(Arrays.asList(R.drawable.iced_coffee_background, R.drawable.iced_coffee_background, R.drawable.iced_coffee_background, R.drawable.iced_coffee_background, R.drawable.iced_coffee_background, R.drawable.iced_coffee_background, R.drawable.iced_coffee_background, R.drawable.iced_coffee_background, R.drawable.iced_coffee_background));
    private static ArrayList<Integer> blendImages = new ArrayList<>(Arrays.asList(R.drawable.iced_coffee_background, R.drawable.iced_coffee_background, R.drawable.iced_coffee_background, R.drawable.iced_coffee_background, R.drawable.iced_coffee_background, R.drawable.iced_coffee_background, R.drawable.iced_coffee_background, R.drawable.iced_coffee_background));

    //espresso.add("latte");
    //espresso.add("cappicino");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setHomeButtonEnabled(true);

        context = getApplicationContext();
        //jsut so not null will fix in a little

        menu = new HashMap<String, ArrayList<String>>();

            menu.put("Espresso",espresso);
            menu.put("Non Espresso",nonEspresso);
            menu.put("Breakfast",breakfast);
            menu.put("Blended Beverage",blendBev);


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
        //specMenLayoutManager = new LinearLayoutManager(this);
        GridLayoutManager specMenLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        String cat = PreferenceManager.getDefaultSharedPreferences(context).getString("category", null);
        if (cat == "Breakfast") {
            specMenAdapter = new specMenAdapter(menuCat, bfastImages, context);
        } else if (cat == "Espresso") {
            specMenAdapter = new specMenAdapter(menuCat, espressImages, context);
        }else if (cat == "Non Espresso") {
            specMenAdapter = new specMenAdapter(menuCat, nonepressImages, context);
        }else if (cat == "Blended Beverage") {
            specMenAdapter = new specMenAdapter(menuCat, blendImages, context);
        }

        //orderItemAdapter = new OrderItemAdapter(itemList);

        specMenRecyclerView.setLayoutManager(specMenLayoutManager);
        specMenRecyclerView.setAdapter(specMenAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
