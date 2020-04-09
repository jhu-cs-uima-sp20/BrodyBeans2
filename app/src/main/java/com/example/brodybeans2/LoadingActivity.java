package com.example.brodybeans2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class LoadingActivity<animation> extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    private AnimationDrawable animation;
    private Animation topAnim, bottomAnim;
    private ImageView meme;
    private TextView name, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);

        meme = (ImageView)findViewById(R.id.coffee_anim);
        animation = (AnimationDrawable)meme.getDrawable();

        animation.start();

        //Animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        name = findViewById(R.id.name);
        slogan = findViewById(R.id.slogan);

        name.setAnimation(topAnim);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this, LogInActivity.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View,String>(meme, "logo_image");
                pairs[1] = new Pair<View,String>(name, "logo_text");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoadingActivity.this,pairs);
                    startActivity(intent, options.toBundle());
                    finish();
                }
            }
        },SPLASH_SCREEN);
    }
}
