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

public class SoloURLActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText url;
    private TextView create_qr;

    private AdView adView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);
        MobileAds.initialize(SoloURLActivity.this, initializationStatus -> {
        });
        admobBannerAds();
        admobInterstitialAds();
        soloIds();
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

    private void soloListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        create_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (url.getText().toString().isEmpty()) {
                    Toast.makeText(SoloURLActivity.this, "Enter Correct Data!", Toast.LENGTH_SHORT).show();
                } else {
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(SoloURLActivity.this);
                    } else {
                        soloCreateQR();
                    }
                }
            }
        });
    }

    private void soloToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void soloIds() {
        toolbar = findViewById(R.id.toolbar);
        url = findViewById(R.id.url);
        create_qr = findViewById(R.id.create_url);
    }

    private void soloCreateQR() {

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(
                    "url :" + url.getText().toString(),
                    BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            List<String> result_list = new ArrayList<>();
            result_list.add(url.getText().toString());
            String result_string = new Gson().toJson(result_list);

            SoloSharedPreference sharedPref = new SoloSharedPreference();
            sharedPref.addToHistory(this, result_string,R.color.location_color,R.drawable.ic_url);
            showQR(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showQR(Bitmap bitmap) {

        SoloQRCreated.imageDrawable = bitmap;
        SoloQRCreated.icon = R.drawable.url;
        Intent i = new Intent(this, SoloQRActivity.class);
        i.putExtra("title", url.getText().toString());
        i.putExtra("header", "Url");
        i.putExtra("icon",String.valueOf(R.drawable.ic_url));
        i.putExtra("color",String.valueOf(R.color.location_color));

        startActivity(i);
        finish();
    }
}
