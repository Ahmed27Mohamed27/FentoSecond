package com.money.deliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout r_name,r_email,r_password,r_phone;
    Button register;
    TextView goToLogin;
    ProgressBar progressBar;
    FirebaseAuth auth;
    String deviceID;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_register );
        FirebaseApp.initializeApp(RegisterActivity.this);

        auth=FirebaseAuth.getInstance ();

        initialize();
        clickListener ();
        deviceID= Settings.Secure.getString ( getContentResolver (), Settings.Secure.ANDROID_ID );

        goToLogin.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( RegisterActivity.this,LoginActivity.class ) );
            }
        });

    }

    private void clickListener()
    {
        register.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                String name=r_name.getEditText().getText().toString().trim();
                String email=r_email.getEditText().getText().toString().trim();
                String pass=r_password.getEditText().getText().toString().trim();
                String phone=r_phone.getEditText().getText().toString().trim();

                if (name.isEmpty ())
                {
                    r_name.setError ( "يرجي كتابة الاسم" );
                    return;
                }
                if (email.isEmpty () || !email.endsWith(".com"))
                {
                    r_email.setError ( "يرجي كتابة البريد الألكتروني" );
                    return;
                }
                if (phone.isEmpty() || phone.length()<10)
                {
                    r_phone.setError ("يرجي كتابة رقم الهاتف");
                    return;
                }
                if (pass.isEmpty () || pass.length()<6)
                {
                    Toast.makeText(RegisterActivity.this, "يرجي كتابة 6 (او اكثر) احرف او ارقام!!", Toast.LENGTH_SHORT).show();

                    return;
                }

                createAccount(email,pass );

            }
        } );


    }

    private void createAccount(final String email, final String pass) {

        progressBar.setVisibility(View.VISIBLE);
        register.setVisibility(View.GONE);

        auth.createUserWithEmailAndPassword ( email,pass )
                .addOnCompleteListener ( new OnCompleteListener<AuthResult> () {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful ())
                        {
                            final FirebaseUser user=auth.getCurrentUser ();
                            assert user != null;

                            updateUi(user,email,pass);

                        } else {
                            progressBar.setVisibility ( View.GONE );
                            register.setVisibility(View.VISIBLE);
                            Toast.makeText ( RegisterActivity.this, "Error"+task.getException ().getMessage (), Toast.LENGTH_SHORT ).show ();
                        }
                    }
                } );


    }

    private void updateUi(FirebaseUser user,String email,String password) {
        @SuppressLint("HardwareIds")
        String deviceID= Settings.Secure.getString ( getContentResolver (), Settings.Secure.ANDROID_ID );

        UserModel userModel=new UserModel();
        userModel.setName(r_name.getEditText().getText().toString().trim());
        userModel.setEmail(email);
        userModel.setPhone(r_phone.getEditText().getText().toString().trim());
        userModel.setImage("");
        userModel.setPassword(password);
        userModel.setUid(deviceID);

        DatabaseReference reference= FirebaseDatabase.getInstance ().getReference ().child ( "Users" );
        reference.child ( deviceID ).setValue ( userModel )
                .addOnCompleteListener ( new OnCompleteListener<Void> () {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful ())
                        {
                            startActivity ( new Intent ( RegisterActivity.this,LoginActivity.class ) );
                            finish ();
                            Toast.makeText(RegisterActivity.this, "تم تسجيل دخولك بنجاح", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText ( RegisterActivity.this, "Error"+task.getException ().getMessage (), Toast.LENGTH_SHORT ).show ();
                        }
                        progressBar.setVisibility ( View.GONE );
                        register.setVisibility ( View.VISIBLE );
                    }
                } );
    }
    private void initialize()
    {
        goToLogin = findViewById(R.id.Signin_tv);
        r_name = findViewById(R.id.FullName);
        r_phone = findViewById(R.id.phone);
        r_email = findViewById(R.id.email_et1);
        r_password = findViewById(R.id.pa_et1);
        register = findViewById(R.id.SignUp);
        progressBar = findViewById(R.id.progressBar4);
    }
}