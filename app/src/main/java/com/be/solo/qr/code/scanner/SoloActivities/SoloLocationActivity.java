package com.be.solo.qr.code.scanner.SoloActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.be.solo.qr.code.scanner.R;
import com.be.solo.qr.code.scanner.SoloUtils.SoloQRCreated;
import com.be.solo.qr.code.scanner.SoloUtils.SoloSharedPreference;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SoloLocationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText latitude,longitude,query;
    private TextView create_qr;

    private AdView adView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        MobileAds.initialize(SoloLocationActivity.this, initializationStatus -> {
        });
        admobBannerAds();
        soloInit();
        soloSetToolbar();
        soloClickListener();
        admobInterstitialAds();
    }

    private void admobInterstitialAds() {
        InterstitialAd.load(this,
                getString(R.string.admob_interstitial_ad_id),
                new AdRequest.Builder().build(),
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                    }

                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                soloCreateQR();
                            }
                        });
                    }
                });
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

    private void soloClickListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        create_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (latitude.getText().toString().isEmpty() || longitude.getText().toString().isEmpty() ||
                        query.getText().toString().isEmpty() )
                {
                    Toast.makeText(SoloLocationActivity.this,"Enter Correct Data!",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (mInterstitialAd !=null){
                        mInterstitialAd.show(SoloLocationActivity.this);
                    }else {
                        soloCreateQR();
                    }
                }

            }
        });
    }

    private void soloSetToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void soloInit() {
        toolbar = findViewById(R.id.toolbar);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        query = findViewById(R.id.query);
        create_qr = findViewById(R.id.create_location);
    }

    private void soloCreateQR() {

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(
                    "latitude :" + latitude.getText().toString() +
                            ", longitude :" + longitude.getText().toString() +
                            ", query :"+ query.getText().toString(),
                    BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            List<String> result_list = new ArrayList<>();
            result_list.add(latitude.getText().toString());
            result_list.add(longitude.getText().toString());
            result_list.add(query.getText().toString());

            String result_string = new Gson().toJson(result_list);
            SoloSharedPreference sharedPref = new SoloSharedPreference();
            sharedPref.addToHistory(this,result_string,R.color.location_color,R.drawable.ic_location);
            soloShowQR(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void soloShowQR(Bitmap bitmap) {

        SoloQRCreated.imageDrawable = bitmap;
        SoloQRCreated.icon = R.drawable.location;
        Intent i = new Intent(this, SoloQRActivity.class);
        i.putExtra("title",query.getText().toString());
        i.putExtra("header","Location");
        i.putExtra("icon",String.valueOf(R.drawable.ic_location));
        i.putExtra("color",String.valueOf(R.color.location_color));

        startActivity(i);
        finish();
    }
}
