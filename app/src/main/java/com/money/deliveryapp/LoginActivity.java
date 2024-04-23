package com.money.deliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.money.deliveryapp.Admin.AdminLoginActivity;

public class LoginActivity extends AppCompatActivity {
    ProgressBar progressBar;
    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser user;
    TextView Signup_tv1, forgot, admin;
    AppCompatButton login;
    TextInputLayout email_et, pa_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
        auth = FirebaseAuth.getInstance();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        user = auth.getCurrentUser();

        if (user != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        clickListener();

    }

    private void clickListener() {
        Signup_tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, AdminLoginActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_et.getEditText().getText().toString().trim();
                String pass = pa_et.getEditText().getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    email_et.setError("يرجي كتابة البريد الألكتروني");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    pa_et.setError("يرجي كتابة كلمة المرور");
                    return;
                }

                signIn(email, pass);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetActivity.class));

            }
        });

    }

    private void signIn(String email, final String password) {
        progressBar.setVisibility(View.VISIBLE);
        login.setVisibility(View.GONE);
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (email.equals("admin@gmail.com") && password.equals("admin@123")) {
                                progressBar.setVisibility(View.GONE);
                                login.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this, "خطأ في كلمة المرور والبريد الالكتروني", Toast.LENGTH_SHORT).show();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                login.setVisibility(View.VISIBLE);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            login.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void initialize() {
        progressBar = findViewById(R.id.progressBar);
        Signup_tv1 = findViewById(R.id.Signup_tv1);
        email_et = findViewById(R.id.email_et);
        admin = findViewById(R.id.admin);
        pa_et = findViewById(R.id.pa_et);
        forgot = findViewById(R.id.textView5);
        login = findViewById(R.id.Login);
    }

}