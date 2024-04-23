package com.money.deliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.os.LocaleListCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseReference reference;
    FirebaseUser user;
    SliderView sliderView;
    SliderAdapter adapter;
    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;
    CardView superMarketCard, orderCard, resturantProductCard, customOrderCard, addResturantCard;
    String deviceID;
    ImageView languace;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        deviceID= Settings.Secure.getString ( getContentResolver (), Settings.Secure.ANDROID_ID );
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        adapter = new SliderAdapter(this);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        renewItems();

        clickListener();

    }
    public void setLocale(String lang) {
        if (lang.equals("ar")) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("ar"));
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"));
        }
    }
    private void clickListener() {

        languace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(MainActivity.this);

                dialog.setContentView(R.layout.languace);
                TextView ar = dialog.findViewById(R.id.ar);
                TextView en = dialog.findViewById(R.id.en);

                dialog.setCancelable(true);

                dialog.show();

                en.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setLocale("en");
                    }
                });
                ar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setLocale("ar");
                    }
                });
            }
        });

        customOrderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog();
            }
        });

        superMarketCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SuperMarketProduct.class));
            }
        });

        orderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogBox();
            }
        });

        resturantProductCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ResturantActivity.class));
            }
       });

        addResturantCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PharmacyActivity.class));
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.account:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        return true;

                    case R.id.home:

                        return true;

                    case R.id.chat:
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://wa.me/+201222676606"));
                        startActivity(intent);
                        return true;

                    case R.id.orders:
                        startActivity(new Intent(MainActivity.this, DataOrdersActivity.class));
                        return true;
                }
                return false;
            }
        });

    }

    private void initialize() {
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.navigation_bar);
        progressBar = findViewById(R.id.progress);
        sliderView = findViewById(R.id.imageSlider);
        customOrderCard = findViewById(R.id.customOrderCard);
        superMarketCard = findViewById(R.id.superMarketCard);
        resturantProductCard = findViewById(R.id.resturantProductCard);
        addResturantCard = findViewById(R.id.add_resturantCard);
        orderCard = findViewById(R.id.orderCard);
        languace = findViewById(R.id.languace);
    }
    public void renewItems() {
        List<SliderItem> sliderItemList = new ArrayList<>();
        //dummy data
        for (int i = 0; i < 3; i++) {
            SliderItem sliderItem = new SliderItem();
            sliderItem.setDescription("Slider Item " + i);
            if (i == 0) {
                sliderItem.setImageUrl("https://i.postimg.cc/c4h2NVQX/slider3.jpg");
            } else if (i == 1) {
                sliderItem.setImageUrl("https://i.postimg.cc/Yq05sw87/slider2.jpg");
            } else {
                sliderItem.setImageUrl("https://i.postimg.cc/qRQF1m3g/slider3.jpg");
            }
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }
    private void dialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.order_mandob)
                .setMessage(R.string.title_order_mandob)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        startActivity(new Intent(MainActivity.this, OrderActivity.class));
                    }
                }).create().show();
    }
    private void DialogBox() {
        Dialog dialog=new Dialog(this);

        dialog.setContentView(R.layout.dialog_box);
        TextView whatsapp = dialog.findViewById(R.id.whatsapp);

        dialog.setCancelable(true);

        dialog.show();
        clickOnConfirm(whatsapp,dialog);

    }

    private void clickOnConfirm(final TextView whatsapp,final Dialog dialog) {
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://wa.me/+201222676606"));
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.Exit)
                .setMessage(R.string.title_exit)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                        finishAffinity();
                        System.exit(0);
                    }
                }).create().show();
    }
}