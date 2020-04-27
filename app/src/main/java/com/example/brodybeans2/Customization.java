package com.example.brodybeans2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    TextInputEditText cust;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization);


        addToCart = findViewById(R.id.addToCart);


            //code to check if this checkbox is checked!


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tempCount = 0;
                int sizeCount = 0;
                context = getApplicationContext();

                cold = (CheckBox)findViewById(R.id.icedCB);
                if(cold.isChecked()){
                    tempCount++;
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
                else {
                    Intent intent = new Intent(v.getContext(), CartActivity.class);
                    startActivity(intent);
                }
            }
        });

    }


}
