package com.example.foodiffin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodiffin.HomepageNavigationActivity;
import com.example.foodiffin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    TextView signUpBtn;
    EditText emailEditText,passwordEditText;
    private FirebaseAuth mAuth;
    public String email,password;
    private final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpBtn=findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(view -> startActivity(new Intent(LogInActivity.this,SignUpActivity.class)));
        mAuth = FirebaseAuth.getInstance();
    }

    public void homepageCall(View view) {

        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(LogInActivity.this, "Enter email and password",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(mAuth.getCurrentUser().isEmailVerified())
                            {
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            }else {
                                Toast.makeText(LogInActivity.this, "Please verify your email.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.w(TAG,"createUserWithEmail:failure", task.getException());
                            Toast.makeText(LogInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void onStart(){
        super.onStart();
//        FirebaseUser user = mAuth.getCurrentUser();
//        if(user != null)
//        {
//            updateUI(user);
//        }
    }

    private void updateUI(FirebaseUser user) {
        Intent profileIntent = new Intent(LogInActivity.this, HomepageNavigationActivity.class);
        profileIntent.putExtra("email",user.getEmail());
        startActivity(profileIntent);
    }
}