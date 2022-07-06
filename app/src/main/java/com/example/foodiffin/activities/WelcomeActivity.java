package com.example.foodiffin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.foodiffin.R;

public class WelcomeActivity extends AppCompatActivity {

    TextView logInText;
    TextView signUpText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        logInText=findViewById(R.id.logInTextView);
        signUpText=findViewById(R.id.signUpTextView);

        //Button listener at welcome screen
        logInText.setOnClickListener(view -> startActivity(new Intent(WelcomeActivity.this,LogInActivity.class)));

        signUpText.setOnClickListener(view -> startActivity(new Intent(WelcomeActivity.this,SignUpActivity.class)));
    }
}