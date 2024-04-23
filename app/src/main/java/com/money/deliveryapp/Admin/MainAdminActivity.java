package com.money.deliveryapp.Admin;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.money.deliveryapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainAdminActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Fetch> list = new ArrayList<>();
    AdpterData adpterData;
    SearchView searchView;
    TextView textView;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fileList(newText);
                return true;
            }
        });

        auth=FirebaseAuth.getInstance ();
        user=auth.getCurrentUser ();
        recyclerView = findViewById(R.id.rec);
        textView = findViewById(R.id.text55);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adpterData = new AdpterData(list,this);
        recyclerView.setAdapter(adpterData);
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        root.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Fetch fetch = new Fetch();
                    fetch.setName(snapshot1.child("name").getValue().toString());
                    fetch.setPassword(snapshot1.child("password").getValue().toString());
                    fetch.setEmail(snapshot1.child("email").getValue().toString());
                    fetch.setMobile(snapshot1.child("phone").getValue().toString());
                    fetch.setUid(snapshot1.child("uid").getValue().toString());
                    fetch.setImage(snapshot1.child("image").getValue().toString());
                    list.add(fetch);
                }
                adpterData.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void fileList(String text) {
        List<Fetch> filteredList = new ArrayList<>();
        for (Fetch fetch : list){
            if (fetch.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(fetch);
            }
        }
        if (filteredList.isEmpty()){
            recyclerView.setVisibility(GONE);
            textView.setVisibility(VISIBLE);
        }else{
            adpterData.setFilteredList(filteredList);
            textView.setVisibility(GONE);
            recyclerView.setVisibility(VISIBLE);
        }
    }
}