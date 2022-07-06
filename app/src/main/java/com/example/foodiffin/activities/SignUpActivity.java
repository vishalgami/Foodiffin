package com.example.foodiffin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodiffin.HomepageNavigationActivity;
import com.example.foodiffin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {
    EditText dobEditTextSignup,nameEditTextSignup,phoneEditTextSignup,emailEditTextSignup,passwordEditTextSignup;;
    DatePickerDialog picker;
    TextView signInTextViewSignUp;
    Button buttonSignup;
    private FirebaseFirestore fStore;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private static final String USER = "user";
    private User user;
    String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signInTextViewSignUp=findViewById(R.id.signInTextViewSignUp);
        emailEditTextSignup = findViewById(R.id.emailEditTextSignup);
        passwordEditTextSignup = findViewById(R.id.passwordEditTextSignup);
        nameEditTextSignup = findViewById(R.id.nameEditTextSignup);
        phoneEditTextSignup = findViewById(R.id.phoneEditTextSignup);
        dobEditTextSignup=(EditText) findViewById(R.id.dobEditTextSignup);
        buttonSignup = findViewById(R.id.buttonSignup);

//        Instance for firebase
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        /*------------------ Date of birth Picker listener -----------------------*/
        dobEditTextSignup.setInputType(InputType.TYPE_NULL);
        dobEditTextSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(SignUpActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dobEditTextSignup.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditTextSignup.getText().toString();
                String password = passwordEditTextSignup.getText().toString();
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(SignUpActivity.this, "Enter email and password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = nameEditTextSignup.getText().toString();
                String phoneNumber = phoneEditTextSignup.getText().toString();
                String dob = dobEditTextSignup.getText().toString();
                User user = new User(email,password,dob,phoneNumber,name);

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((task) -> {
                                if (task.isSuccessful()) {
                                    userId = mAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = fStore.collection("users").document(userId);
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("Register User", "createUserWithEmail:success");
                                            FirebaseUser currentUser = mAuth.getCurrentUser();
                                            currentUser.sendEmailVerification()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("Email", "Email sent.");
                                                            }
                                                        }
                                                    });
                                            Intent signInIntent = new Intent(SignUpActivity.this, LogInActivity.class);
                                            startActivity(signInIntent);
                                            Toast.makeText(SignUpActivity.this, "Registered Successfully.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Register User", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                        });
            }
        });

        signInTextViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = new Intent(SignUpActivity.this,LogInActivity.class);
                startActivity(signInIntent);
            }
        });



    }
}