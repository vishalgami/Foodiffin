package com.example.foodiffin.ui.profile;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.foodiffin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Calendar;

public class ProfileFragment extends Fragment {
    EditText nameEditTextProfile,phoneEditTextSignup,dobEditTextProfile,emailEditTextProfile,passwordEditTextSignup;
    DatePickerDialog picker;
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;
    String userID;
    private FirebaseFirestore fStore;
    private DocumentReference documentReference;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.profile_fragment,container,false);

        dobEditTextProfile=root.findViewById(R.id.dobEditTextProfile);
        nameEditTextProfile=root.findViewById(R.id.nameEditTextProfile);
        phoneEditTextSignup=root.findViewById(R.id.phoneEditTextSignup);
        emailEditTextProfile=root.findViewById(R.id.emailEditTextProfile);
        passwordEditTextSignup=root.findViewById(R.id.passwordEditTextSignup);

        dobEditTextProfile.setInputType(InputType.TYPE_NULL);
        dobEditTextProfile.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(getActivity(),
                    (view, year1, monthOfYear, dayOfMonth) -> dobEditTextProfile.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
            picker.show();
        });
        setProfile();


        return root;
    }

    private void setProfile() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentUser.getUid();

        fStore = FirebaseFirestore.getInstance();
        documentReference = fStore.collection("users").document(userID);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String userName = task.getResult().getString("name");
                    nameEditTextProfile.setText(userName);
                    String dob = task.getResult().getString("dob");
                    dobEditTextProfile.setText(dob);
                    String phone = task.getResult().getString("phoneNumber");
                    phoneEditTextSignup.setText(phone);
                    String email = task.getResult().getString("email");
                    emailEditTextProfile.setText(email);
                    String password = task.getResult().getString("password");
                    passwordEditTextSignup.setText(password);
                }
            }
        });
    }
}