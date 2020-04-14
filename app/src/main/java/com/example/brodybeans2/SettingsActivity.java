package com.example.brodybeans2;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private TextInputLayout mNewPassword, mNewUsername, mCurPassword;
    private Switch notifToggle;
    private Button updatePswd;

    //database fields
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;

    private String uID;
    private String curUsername;
    private String curPassword;
    private String newPassword;

    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mNewPassword = findViewById(R.id.new_password);
        mCurPassword = findViewById(R.id.cur_password);
        mNewUsername = findViewById(R.id.new_username);
        updatePswd = findViewById(R.id.change_pswd_btn);

        //get the current FireBase user-- idk if this is necessary
        user = FirebaseAuth.getInstance().getCurrentUser();

        //mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");

        //curUsername = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        //curPassword = FirebaseAuth.getInstance().getCurrentUser().ge();


        mFirebaseAuth = FirebaseAuth.getInstance();
        uID = mFirebaseAuth.getCurrentUser().getUid();
        mUsersDatabaseReference = mFirebaseDatabase.getInstance().getReference().child("users");




        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.child("userID").getValue(String.class).equals(uID)) {
                    curUsername = dataSnapshot.child("fullName").getValue(String.class);

                    //set the text of settings box to current username
                    mNewUsername.setHint(curUsername);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.child("userID").getValue(String.class).equals(uID)) {
                    curUsername = dataSnapshot.child("fullName").getValue(String.class);

                    //set the text of settings box to current username
                    mNewUsername.setHint(curUsername);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        mUsersDatabaseReference.addChildEventListener(mChildEventListener);


        updatePswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                curPassword = mCurPassword.getEditText().getText().toString().trim();
                newPassword = mNewPassword.getEditText().getText().toString().trim();

                //check if current password field is empty
                if (TextUtils.isEmpty(curPassword)) {
                    mCurPassword.setError("Current password required");
                    return;
                }

                if (TextUtils.isEmpty(newPassword)) {
                    mNewPassword.setError("Input new password");
                    return;
                }

                if (newPassword.length() < 6) {
                    mNewPassword.setError("Password must be at least 6 characters long.");
                    return;
                }

                if (!containsNum(newPassword)) {
                    mNewPassword.setError("Password must contain at least one number.");
                    return;
                }

                if (curPassword.equals(newPassword)) {
                    mNewPassword.setError("New password field should not match old password field!");
                    return;
                }

                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), curPassword);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SettingsActivity.this, "Password updated",Toast.LENGTH_LONG).show();
                                        //Log.d(TAG, );
                                    } else {
                                        Toast.makeText(SettingsActivity.this, "Error password not updated",Toast.LENGTH_LONG).show();
                                        //Log.d(TAG, );
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(SettingsActivity.this, "Authentication failed. Make sure current password" +
                                    " field is correct.",Toast.LENGTH_LONG).show();
                            //Log.d(TAG, "Error auth failed");
                        }
                    }
                });
            }
        });




        //change it in the database and it should update automatically via ^


    }

    public boolean containsNum(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.contains(String.valueOf(i))) {
                return true;
            }
        }
        return false;
    }

}