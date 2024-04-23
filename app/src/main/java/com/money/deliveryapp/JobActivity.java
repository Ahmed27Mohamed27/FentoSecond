package com.money.deliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class JobActivity extends AppCompatActivity {
    TextView orderbtn;
    TextInputLayout FullName, phone, Adrress;
    ProgressBar progressBar;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    String deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        auth=FirebaseAuth.getInstance ();
        user=auth.getCurrentUser ();
        deviceID= Settings.Secure.getString ( getContentResolver (), Settings.Secure.ANDROID_ID );
        reference= FirebaseDatabase.getInstance ().getReference ().child ( "Users" ).child(deviceID);

        intialization();

        onClick();

    }
    private void intialization() {
        orderbtn = findViewById(R.id.orderbtn);
        FullName = findViewById(R.id.FullName);
        phone = findViewById(R.id.phone);
        Adrress = findViewById(R.id.Adrress);
        progressBar = findViewById(R.id.progressBar);
    }
    private void onClick() {
        orderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = FullName.getEditText().getText().toString();
                String phone1 = phone.getEditText().getText().toString();
                String Adrress1 = Adrress.getEditText().getText().toString();

                if (name1.isEmpty()) {
                    FullName.setError("يرجي كتابة الاسم");
                } else if (phone1.isEmpty()) {
                    phone.setError("يرجي كتابة رقمك");
                } else if (Adrress1.isEmpty()) {
                    Adrress.setError("يرجي كتابة العنوان بالتفصيل");
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    OrderDone(name1, phone1, Adrress1);
                }

            }
        });
    }
    private void OrderDone(String name, String phone, String address) {
        HashMap map = new HashMap();
        map.put("name", name);
        map.put("phone", phone);
        map.put("address", address);
        map.put("none", "جاري التنفيذ");
        reference.child("Job").updateChildren(map);
        Toast.makeText(this, "تم ارسال طلبك بنجاح سوف يتم التواصل معك من الدعم الفني.", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
        finish();
    }

    @Override
    protected void onStart() {
        getData();
        super.onStart();
    }
    private void getData() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                String phone1 = snapshot.child("phone").getValue().toString();
                FullName.getEditText().setText(name);
                phone.getEditText().setText(phone1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}