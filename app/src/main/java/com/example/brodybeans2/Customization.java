package com.example.brodybeans2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class Customization extends AppCompatActivity {

    Button addToCart;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization);

        addToCart = findViewById(R.id.addToCart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = getApplicationContext();

                //hard coded as an example of the data we should store
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("category", "Espresso").apply();
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("size", "small").apply();
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("temp", "iced").apply();
                Intent intent = new Intent(v.getContext(), CartActivity.class);
                startActivity(intent);
            }
        });

    }


}
