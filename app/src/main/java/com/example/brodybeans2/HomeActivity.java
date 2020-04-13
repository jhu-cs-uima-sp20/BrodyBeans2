package com.example.brodybeans2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mOrdersDatabaseReference;
    private ChildEventListener mChildEventListener;

    private FirebaseAuth mFirebaseAuth;

    private User mUser;
    private String userId;

    private Button newOrderBtn;
    private TextView welcomeMsg;
    private Context context;
    private DataSnapshot ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("orders");
//        if (mOrdersDatabaseReference == null) {
//            PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).edit().putString("status", "false").apply();
//        }
        Log.d("Database REf",mOrdersDatabaseReference.toString());

        if (PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).contains("status")) {
            if (PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).getString("status", null).equals("true")) {

                Intent i = new Intent(this, OrdersActivity.class);
                startActivity(i);

            }
        }

        setContentView(R.layout.activity_home);



        mFirebaseAuth = FirebaseAuth.getInstance();
        userId = mFirebaseAuth.getCurrentUser().getUid();
        mUsersDatabaseReference = mFirebaseDatabase.getInstance().getReference().child("users");

        welcomeMsg = (TextView) findViewById(R.id.welcome_msg);

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("USER ID user", dataSnapshot.child("userID").getValue(String.class));
                Log.d("USER ID auth", userId);
                if (dataSnapshot.child("userID").getValue(String.class).equals(userId)) {
                    mUser = new User(userId,
                            dataSnapshot.child("fullName").getValue(String.class),
                            dataSnapshot.child("email").getValue(String.class),
                            dataSnapshot.child("phoneNumber").getValue(String.class));

                    welcomeMsg.setText("Welcome, " +  mUser.getFullName() + "!");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mUsersDatabaseReference.addChildEventListener(mChildEventListener);




        newOrderBtn = (Button) findViewById(R.id.new_order_btn);
        newOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenuActivity();
                /*Toast.makeText(HomeActivity.this, "Button Clicked",
                        Toast.LENGTH_SHORT).show();

                 */
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Menu options
        if (id == R.id.cart) {
            context = getApplicationContext();
            PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_settings) {
            signOut();
            return true;
        }
        else if (id == R.id.settings) {
            settings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(HomeActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();
    }

    public void settings() {
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivity(intent);
        //finish();
    }

    public void openMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

}

