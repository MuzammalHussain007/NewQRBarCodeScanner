package com.be.solo.qr.code.scanner.SoloActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.be.solo.qr.code.scanner.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

public class SoloURLsDetailActivity extends AppCompatActivity {

    private TextView btn_open_ulr,content,time,title;
    private Toolbar toolbar;
    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urls);
        MobileAds.initialize(SoloURLsDetailActivity.this, initializationStatus -> {
        });
        admobBannerAds();
        soloIDS();
        soloToolbar();
        soloListener();
    }
    @SuppressLint("MissingPermission")
    private void admobBannerAds() {
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d("__showAd", "onAdFailed");
            }
        });
    }

    private void soloListener() {
        final Intent intent = getIntent();
        time.setText(intent.getStringExtra("date"));
        title.setText(intent.getStringExtra("content"));
        content.setText(intent.getStringExtra("content"));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_open_ulr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl(intent.getStringExtra("content"));
            }
        });
    }

    private void soloToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void soloIDS() {
        toolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.title);
        time = findViewById(R.id.time);
        content = findViewById(R.id.content);
        btn_open_ulr = findViewById(R.id.open_url);
    }

    private void openUrl(String content) {

        Uri uri = Uri.parse(content); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
