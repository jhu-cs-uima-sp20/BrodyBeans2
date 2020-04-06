package com.example.brodybeans2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    Button espressoBtn;
    Button nonEspressoBtn;
    Button blendedBevBtn;
    Button bfastBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        espressoBtn = findViewById(R.id.espresso);
        nonEspressoBtn = findViewById(R.id.non_espresso);
        blendedBevBtn = findViewById(R.id.blended_bev);
        bfastBtn = findViewById(R.id.breakfast);


        espressoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CartActivity.class);
                startActivity(intent);
            }
        });


        nonEspressoBtn =  findViewById(R.id.non_espresso);

        nonEspressoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CartActivity.class);
                startActivity(intent);
            }
        });

        blendedBevBtn = findViewById(R.id.blended_bev);

        blendedBevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CartActivity.class);
                startActivity(intent);
            }
        });


        bfastBtn =  findViewById(R.id.breakfast);

        bfastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CartActivity.class);
                startActivity(intent);
            }
        });




    }
}
