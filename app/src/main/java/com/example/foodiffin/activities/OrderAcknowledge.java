package com.example.foodiffin.activities;

import static javax.mail.internet.InternetAddress.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.foodiffin.HomepageNavigationActivity;
import com.example.foodiffin.R;
import com.example.foodiffin.models.HomepageFoodListingModel;
import com.example.foodiffin.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;


import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class OrderAcknowledge extends AppCompatActivity {

    Button btnOrderAgain;
    TextView orderNumber,orderDate,orderDescription,orderTotal;
    HomepageFoodListingModel homepageFoodListingModel = null;
    int orderNumberGenerate;
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;
    String userID,email,name;
    private FirebaseFirestore fStore;
    private DocumentReference documentReference;
    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    Random r = new Random( System.currentTimeMillis() );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_acknowledge);

        orderNumber = findViewById(R.id.orderNumber);
        orderDate = findViewById(R.id.orderDate);
        orderDescription = findViewById(R.id.orderDescription);
        orderTotal = findViewById(R.id.orderTotal);
        btnOrderAgain = findViewById(R.id.btnOrderAgain);

        final Object object = getIntent().getSerializableExtra("detail");
        if(object instanceof HomepageFoodListingModel)
        {
            homepageFoodListingModel = (HomepageFoodListingModel) object;
        }
        if(homepageFoodListingModel != null)
        {
            orderNumberGenerate = (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
            orderDescription.setText(homepageFoodListingModel.getFoodTitle().toUpperCase(Locale.ROOT));
            orderTotal.setText("$ "+Math.round(homepageFoodListingModel.getTotalAmount()*100.0)/100.0);
            orderNumber.setText("#"+orderNumberGenerate);
            orderDate.setText(formatter.format(date));
            getUserMail();
        }

        btnOrderAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(OrderAcknowledge.this, HomepageNavigationActivity.class);
                startActivity(homeIntent);
            }
        });
    }

    private void sendMail(String email,String name) {
        try {
            String stringSenderEmail = "foodiffinadm@gmail.com";
            String stringReceiverEmail = email;
            String stringPasswordSenderEmail = "foodiffin";

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

            mimeMessage.setSubject("Confirmation Order from FOODIFFIN");
            mimeMessage.setText("Hello, "+ name +"\n\nThanks for your Order from FOODIFFIN on " + formatter.format(date) + ". \nOrder Confirmation Number: #" + orderNumberGenerate + ".\n" +
                    "Your order is " + homepageFoodListingModel.getFoodTitle() + " that includes " + homepageFoodListingModel.getFoodMenu() + ".\n" +
                    "Total Amount: $" + Math.round(homepageFoodListingModel.getTotalAmount()*100.0)/100.0 + ".\n\n" +
                    "Thanks & Regards\nFOODIFFIN");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        }

    private String getUserMail() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentUser.getUid();

        fStore = FirebaseFirestore.getInstance();
        documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    email = task.getResult().getString("email");
                    name = task.getResult().getString("name");
                    sendMail(email,name);
                }
            }
        });
        return email;
    }
}