package com.money.deliveryapp.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.money.deliveryapp.R;

import java.util.HashMap;

public class Info extends AppCompatActivity {

    TextView name, email, password, number, deviceID;
    Button btnHagz;
    FirebaseUser user;
    FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        btnHagz = findViewById(R.id.btnHagz);
        btnHagz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Info.this,InfoOrder.class);
                i.putExtra("uid", deviceID.getText().toString());
                startActivity(i);
            }
        });

        name = findViewById(R.id.txtName);
        email = findViewById(R.id.txtCountry);
        password = findViewById(R.id.txtId);

        name = findViewById(R.id.name);
        number = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        deviceID = findViewById(R.id.deviceID);

        String s = getIntent().getExtras().getString("name");
        name.setText(s);
        String n = getIntent().getExtras().getString("email");
        email.setText(n);
        String m = getIntent().getExtras().getString("phone");
        number.setText(m);
        String o = getIntent().getExtras().getString("password");
        password.setText(o);
        String a = getIntent().getExtras().getString("uid");
        deviceID.setText(a);

        findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Users");
                HashMap hashMap = new HashMap();
                hashMap.remove("");
                root.child(deviceID.getText().toString()).setValue(hashMap);
                finish();
                Toast.makeText(Info.this, "تم", Toast.LENGTH_SHORT).show();
            }
        });
    }
}