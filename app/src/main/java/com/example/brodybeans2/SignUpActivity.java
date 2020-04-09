package com.example.brodybeans2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
     private Button callLogIn;

     private FirebaseAuth fAuth;
     private FirebaseDatabase fDatabase;
    private DatabaseReference fDatabaseRef;

     private TextInputLayout mPhoneNumber, mPassword, mEmail, mFullName;
     private Button mSignUp;
     private String userID;

     private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        progressBar = findViewById(R.id.sign_up_progress_bar);

        mPhoneNumber = findViewById(R.id.phone_number);
        mPassword = findViewById(R.id.password);
        mEmail = findViewById(R.id.sign_up_email);
        mFullName = findViewById(R.id.name);
        mSignUp = findViewById(R.id.register);

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        fDatabaseRef = fDatabase.getReference().child("users");

        if(fAuth.getCurrentUser() != null) {
            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = mPassword.getEditText().getText().toString().trim();
                final String email = mEmail.getEditText().getText().toString().trim();
                final String fullName = mFullName.getEditText().getText().toString();
                final String phoneNumber = mPhoneNumber.getEditText().getText().toString();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required.");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password must be at least 6 characters long.");
                    return;
                }

                if (!containsNum(password)) {
                    mPassword.setError("Password must contain at least one number.");
                    return;
                }

                //Progress bar visible
                progressBar.setVisibility(View.VISIBLE);
                //Register in Firebase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                          if (task.isSuccessful()) {
                              Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                              Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                              userID = fAuth.getCurrentUser().getUid();

                              User user = new User(userID, fullName, email, phoneNumber);
                              fDatabaseRef.push().setValue(user);
                              startActivity(intent);
                              finish();
                          } else {
                              Toast.makeText(SignUpActivity.this, "Error!: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                              progressBar.setVisibility(View.INVISIBLE);
                          }
                    }
                });

            }
        });

        callLogIn = findViewById(R.id.login_screen);
        callLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(SignUpActivity.this,LogInActivity.class);
                //startActivity(intent);
                finish();
            }
        });
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
