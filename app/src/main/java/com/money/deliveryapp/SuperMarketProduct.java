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
import java.util.Random;

public class SuperMarketProduct extends AppCompatActivity {
    TextView orderbtn;
    TextInputLayout kind_product, FullName, phone, Adrress, note, name_superMarket;
    ProgressBar progressBar;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    String deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_market_product);

        auth=FirebaseAuth.getInstance ();
        user=auth.getCurrentUser ();
        deviceID= Settings.Secure.getString ( getContentResolver (), Settings.Secure.ANDROID_ID );
        reference= FirebaseDatabase.getInstance ().getReference ().child ( "Users" ).child(deviceID);

        intialization();

        onClick();

    }
    private void intialization() {
        orderbtn = findViewById(R.id.orderbtn);
        kind_product = findViewById(R.id.kind_product);
        FullName = findViewById(R.id.FullName);
        phone = findViewById(R.id.phone);
        Adrress = findViewById(R.id.Adrress);
        note = findViewById(R.id.note);
        name_superMarket = findViewById(R.id.name_superMarket);
        progressBar = findViewById(R.id.progressBar);
    }
    private void onClick() {
        orderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = FullName.getEditText().getText().toString();
                String phone1 = phone.getEditText().getText().toString();
                String kind_product1 = kind_product.getEditText().getText().toString();
                String Adrress1 = Adrress.getEditText().getText().toString();

                if (name1.isEmpty()) {
                    FullName.setError("يرجي كتابة الاسم");
                } else if (phone1.isEmpty()) {
                    phone.setError("يرجي كتابة رقمك");
                } else if (Adrress1.isEmpty()) {
                    Adrress.setError("يرجي كتابة العنوان بالتفصيل");
                } else if (kind_product1.isEmpty()) {
                    kind_product.setError("يرجي كتابة نوع المنتج الذي سوف ترسلة");
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    OrderDone(name1, phone1, kind_product1, Adrress1);
                }

            }
        });
    }
    private void OrderDone(String name, String phone, String kind, String address) {
        Random random = new Random();
        int rr = random.nextInt(100);
        HashMap map = new HashMap();
        String note1;
        if (note.getEditText().getText().toString().isEmpty()) {
            note1 = "لا يوجد ملاحظات";
        } else {
            note1 = note.getEditText().getText().toString();
        }
        String name_supermarket;
        if (name_superMarket.getEditText().getText().toString().isEmpty()) {
            name_supermarket = "لا يوجد اسم سوبر ماركت";
        } else {
            name_supermarket = name_superMarket.getEditText().getText().toString();
        }
        map.put("name", name);
        map.put("phone", phone);
        map.put("kind_product", kind);
        map.put("address", address);
        map.put("note", note1);
        map.put("name_SuperMarket", name_supermarket);
        map.put("none", "جاري التنفيذ");
        reference.child("Orders").child(String.valueOf(rr)).updateChildren(map);
        Toast.makeText(this, "تم طلب مندوب وفي انتظار الرد.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SuperMarketProduct.this, DataOrdersActivity.class));
        progressBar.setVisibility(View.GONE);
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