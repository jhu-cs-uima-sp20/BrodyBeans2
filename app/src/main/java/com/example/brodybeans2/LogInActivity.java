package com.example.brodybeans2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
    private Button callSignUp;

    private ProgressBar progressBar;

    private ImageView mImage;
    private TextView mLogoText, mGreetingText;
    private TextInputLayout mEmail, mPassword;
    private Button mLogInButton;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in);

        progressBar = findViewById(R.id.log_in_progress_bar);

        callSignUp = findViewById(R.id.signup_screen);
        mImage = findViewById(R.id.logo_image);
        mLogoText = findViewById(R.id.logo_name);
        mGreetingText = findViewById(R.id.slogan_name);
        mEmail = findViewById(R.id.log_in_email);
        mPassword = findViewById(R.id.password);
        mLogInButton = findViewById(R.id.go);

        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null) {
            if (fAuth.getCurrentUser().getUid().equals("tTrf1lio89S1SVI2tiiYAvxZqLG2")) {
                Intent intent = new Intent(LogInActivity.this, CafeHomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }

        mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = mPassword.getEditText().getText().toString().trim();
                String email = mEmail.getEditText().getText().toString();

                Log.d("BAD EMAIL!",email);

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required.");
                    return;
                }

                //Progress bar visible
                progressBar.setVisibility(View.VISIBLE);
                //Register in Firebase
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LogInActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                            if (fAuth.getCurrentUser().getUid().equals("tTrf1lio89S1SVI2tiiYAvxZqLG2")) {
                                Intent intent = new Intent(LogInActivity.this, CafeHomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(LogInActivity.this, "Error!: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });


        callSignUp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View,String>(mImage,"logo_image");
                pairs[1] = new Pair<View,String>(mLogoText, "logo_text");
                pairs[2] = new Pair<View,String>(mGreetingText, "logo_greeting");
                pairs[3] = new Pair<View,String>(mEmail, "username_tran");
                pairs[4] = new Pair<View,String>(mPassword, "mPassword_tran");
                pairs[5] = new Pair<View,String>(mLogInButton, "button_tran");
                pairs[6] = new Pair<View,String>(callSignUp, "login_signup_tran");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LogInActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                }

            }
        });
    }
}
