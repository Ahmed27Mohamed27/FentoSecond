package com.money.deliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class DataOrdersActivity extends AppCompatActivity {
    DatabaseReference reference;
    String deviceID;
    RelativeLayout re;
    LinearLayout rere;
    TextView phone, state, address, kind_product, name, no, cancel_hagz;
    RecyclerView recyclerView;
    AdapterData adapter = new AdapterData();
    ArrayList<ModelDataOrders> list = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_orders);

        recyclerView = findViewById(R.id.rec);
        cancel_hagz = findViewById(R.id.cancel_hagz);
        phone = findViewById(R.id.phone);
        kind_product = findViewById(R.id.kind_product);
        address = findViewById(R.id.address);
        name = findViewById(R.id.name);
        state = findViewById(R.id.state);
        rere = findViewById(R.id.rere);
        re = findViewById(R.id.re);
        no = findViewById(R.id.no);

        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(deviceID);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setList(list);

        show_data();

    }

    private void show_data() {

        reference.child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("none").exists()) {

                        String name1 = snapshot.child("name").getValue().toString();
                        String phone1 = snapshot.child("phone").getValue().toString();
                        String address1 = snapshot.child("address").getValue().toString();
                        String kind1 = snapshot.child("kind_product").getValue().toString();

                        String v = snapshot.child("none").getValue().toString();
                        if (v.equals("ملغي")) {
                            state.setText("ملغي");
                            state.setBackgroundResource(R.drawable.background_hagz_red);
                            cancel_hagz.setVisibility(View.GONE);
                        } else if (v.equals("تم التنفيذ")) {
                            state.setText("تم التنفيذ");
                            state.setBackgroundResource(R.drawable.background_hagz_green);
                            cancel_hagz.setVisibility(View.GONE);
                        } else if (v.equals("جاري التنفيذ")) {
                            state.setText("جاري التنفيذ");
                            state.setBackgroundResource(R.drawable.background_hagz_green);
                        }

                        name.setText(name1);
                        phone.setText(phone1);
                        address.setText(address1);
                        kind_product.setText(kind1);

                        cancel_hagz(name1, phone1, address1, kind1);

                    } else {
                        reference.child("Orders").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    ModelDataOrders fetch = new ModelDataOrders();
                                    fetch.setName(snapshot1.child("name").getValue().toString());
                                    fetch.setPhone(snapshot1.child("phone").getValue().toString());
                                    fetch.setNone(snapshot1.child("none").getValue().toString());
                                    String v = snapshot1.child("none").getValue().toString();
                                    if (v.equals("ملغي")) {
                                        state.setText("ملغي");
                                        state.setBackgroundResource(R.drawable.background_hagz_red);
                                        cancel_hagz.setVisibility(View.GONE);
                                    } else if (v.equals("تم التنفيذ")) {
                                        state.setText("تم التنفيذ");
                                        state.setBackgroundResource(R.drawable.background_hagz_green);
                                        cancel_hagz.setVisibility(View.GONE);
                                    } else if (v.equals("جاري التنفيذ")) {
                                        state.setText("جاري التنفيذ");
                                        state.setBackgroundResource(R.drawable.background_hagz_green);
                                    }
                                    fetch.setKind(snapshot1.child("kind_product").getValue().toString());
                                    fetch.setAddress(snapshot1.child("address").getValue().toString());
                                    list.add(fetch);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                } else {
                    re.setVisibility(View.GONE);
                    rere.setVisibility(View.GONE);
                    no.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DataOrdersActivity.this, "Unable to fetch data" + error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        reference.child("Orders").child("no").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        if (snapshot1.exists()) {
                            ModelDataOrders fetch = new ModelDataOrders();
                            fetch.setName(snapshot1.child("name").getValue().toString());
                            fetch.setPhone(snapshot1.child("phone").getValue().toString());
                            fetch.setNone(snapshot1.child("none").getValue().toString());
                            fetch.setKind(snapshot1.child("kind").getValue().toString());
                            fetch.setAddress(snapshot1.child("address").getValue().toString());
                            no.setVisibility(View.GONE);
                            list.add(fetch);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void cancel_hagz(String name, String phone, String address, String kind) {

        cancel_hagz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                int rr = random.nextInt(100);
                ModelDataOrders fetch = new ModelDataOrders();
                HashMap map = new HashMap();
                fetch.setName(name);
                fetch.setAddress(address);
                fetch.setKind(kind);
                fetch.setPhone(phone);
                fetch.setNone("ملغي");
                map.put("none", "ملغي");
                reference.child("Orders").child("no").child(String.valueOf(rr)).setValue(fetch);
                reference.child("Orders").updateChildren(map);
                Toast.makeText(DataOrdersActivity.this, "تم الغاء الحجز بنجاح", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DataOrdersActivity.this, MainActivity.class));
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}