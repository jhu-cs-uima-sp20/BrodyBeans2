package com.example.brodybeans2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class Customization extends AppCompatActivity {

    Button addToCart;
    private Context context;
    public String size;
    public String temp;
    public String notes;
    CheckBox cold;
    CheckBox hot;
    CheckBox sml;
    CheckBox med;
    CheckBox lrg;
    EditText cust;
    Switch fav_switch;
    boolean hasSpace = true;
    int wasFav = 0;
    int isFavNow = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization);


        addToCart = findViewById(R.id.addToCart);
        fav_switch = findViewById(R.id.fav_switch);

        // check if the item was a favorite item of the user
        String item = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("item", null);
        for (int i = 0; i < 4; i++) {
            if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("fav_" + i, "N/A").equals(item)) {
                wasFav = 1;
                fav_switch.setChecked(true);
                String size = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("fav_"+i+"size", "N/A");
                if (size.equals("Small, ")) {
                    sml = (CheckBox)findViewById(R.id.smallCB);
                    sml.setChecked(true);
                } else if (size.equals("Medium, ")) {
                    med = (CheckBox)findViewById(R.id.mediumCB);
                    med.setChecked(true);
                } else if (size.equals("Large, ")) {
                    lrg = (CheckBox)findViewById(R.id.LargeCB);
                    lrg.setChecked(true);
                }
                String temp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("fav_"+i+"temp", "N/A");
                if (temp.equals("Cold")) {
                    cold = (CheckBox)findViewById(R.id.icedCB);
                    cold.setChecked(true);
                } else if (temp.equals("Hot")) {
                    hot = (CheckBox)findViewById(R.id.hotCB);
                    hot.setChecked(true);
                }
                cust = findViewById(R.id.customInput);
                cust.setText(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("fav_"+i+"custom", "N/A"));
                break;
            }
        }

        //code to check if this checkbox is checked!
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tempCount = 0;
                int sizeCount = 0;
                String custom;
                boolean coldCheck = false;
                context = getApplicationContext();
                String cat = PreferenceManager.getDefaultSharedPreferences(context).getString("category", null);
                String item = PreferenceManager.getDefaultSharedPreferences(context).getString("item", null);
                cust = findViewById(R.id.customInput);
                custom = cust.getText().toString();
                //System.out.println("the inputted customizations are " + custom );
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("custom", custom).apply();

                // check if the item was is now a favorite item of the user
                if (fav_switch.isChecked()) {
                    isFavNow = 1;
                }

                cold = (CheckBox)findViewById(R.id.icedCB);
                if(cold.isChecked()){
                    tempCount++;
                    coldCheck = true;
                    temp = "Cold";
                }
                hot = (CheckBox)findViewById(R.id.hotCB);
                if(hot.isChecked()){
                    tempCount++;
                    temp = "Hot";
                }
                sml = (CheckBox)findViewById(R.id.smallCB);
                if(sml.isChecked()){
                    sizeCount++;
                    size = "Small, ";
                }
                med = (CheckBox)findViewById(R.id.mediumCB);
                if(med.isChecked()){
                    sizeCount++;
                    size = "Medium, ";
                }
                lrg = (CheckBox)findViewById(R.id.LargeCB);
                if(lrg.isChecked()){
                    sizeCount++;
                    size = "Large, ";
                }
                cust = findViewById(R.id.customInput);
                //notes = cust.getText();


                //get the text from the modification section
                //set it to the other text box


                //hard coded as an example of the data we should store
                //PreferenceManager.getDefaultSharedPreferences(context).edit().putString("category", "Espresso").apply();
                // which ever box is checked
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("size", size).apply();
                //which ever box is checked
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("temp", temp).apply();
                //what ever additional notes are written
                //PreferenceManager.getDefaultSharedPreferences(context).edit().putString("notes", "small").apply();

                if (sizeCount != 1){
                    //throw new toast
                    Toast.makeText(v.getContext(), "Please select exactly 1 size",Toast.LENGTH_SHORT).show();

                }
                else if (tempCount != 1){
                    //throw new toast
                    Toast.makeText(v.getContext(), "Please select exactly 1 temperature",Toast.LENGTH_SHORT).show();
                }
                else if (cat == "Blended Beverage" && coldCheck != true ){
                    //throw new toast
                    Toast.makeText(v.getContext(), "Blended beverages must be cold",Toast.LENGTH_SHORT).show();
                } else if (custom.length() > 40) {
                    Toast.makeText(v.getContext(), "please limit customization to 40 characters",Toast.LENGTH_SHORT).show();
                } else {
                    addOrRemoveFav(wasFav, isFavNow, context, cat, item, size, temp, custom);
                    if (hasSpace) {
                        Intent intent = new Intent(v.getContext(), CartActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

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

    // handle the favorites here
    public void addOrRemoveFav (int wasFav, int isFavNow, Context cxt, String cat, String item, String size, String temp, String custom) {
        // add a favorite item
        if (wasFav == 0 && isFavNow == 1) {
            // assume no space to add new item in this case
            hasSpace = false;
            for (int i = 0; i < 4; i++) {
                if (PreferenceManager.getDefaultSharedPreferences(cxt).getString("fav_" + i, "N/A").equals("N/A")) {
                    PreferenceManager.getDefaultSharedPreferences(cxt).edit().putString("fav_"+i, item).apply();
                    PreferenceManager.getDefaultSharedPreferences(cxt).edit().putString("fav_"+i+"cat", cat).apply();
                    PreferenceManager.getDefaultSharedPreferences(cxt).edit().putString("fav_"+i+"size", size).apply();
                    PreferenceManager.getDefaultSharedPreferences(cxt).edit().putString("fav_"+i+"temp", temp).apply();
                    PreferenceManager.getDefaultSharedPreferences(cxt).edit().putString("fav_"+i+"custom", custom).apply();
                    hasSpace = true;
                    break;
                }
            }
            if (!hasSpace) {
                hasSpace = false;
                Toast.makeText(cxt,"Need to remove an old favorite item before adding a new one", Toast.LENGTH_SHORT).show();
            }
        }
        // remove a favorite item
        else if (wasFav == 1 && isFavNow == 0) {
            for (int i = 0; i < 4; i++) {
                if (PreferenceManager.getDefaultSharedPreferences(cxt).getString("fav_" + i, "N/A").equals(item)) {
                    PreferenceManager.getDefaultSharedPreferences(cxt).edit().putString("fav_"+i, "N/A").apply();
                    PreferenceManager.getDefaultSharedPreferences(cxt).edit().putString("fav_"+i+"cat", "N/A").apply();
                    PreferenceManager.getDefaultSharedPreferences(cxt).edit().putString("fav_"+i+"size", "N/A").apply();
                    PreferenceManager.getDefaultSharedPreferences(cxt).edit().putString("fav_"+i+"temp", "N/A").apply();
                    PreferenceManager.getDefaultSharedPreferences(cxt).edit().putString("fav_"+i+"custom", "N/A").apply();
                }
            }
        }
    }


}
