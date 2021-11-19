package com.be.solo.qr.code.scanner.SoloActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class SoloWifiActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RadioGroup radioGroup;
    private EditText ssid,password;
    private TextView create_qr;

    private AdView adView;
    private InterstitialAd mInterstitialAd;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        MobileAds.initialize(SoloWifiActivity.this, initializationStatus -> {
        });
        admobBannerAds();
        admobInterstitialAds();
        soloIDS();
        radioGroup.clearCheck();
        soloClickListener();
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
                                createQR(radioButton);
                            }
                        });
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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = radioGroup.findViewById(i);
                if (radioButton.getText().equals("WPA/WPA2")) {
                    radioButton.setBackgroundResource(R.color.blue);
                    radioGroup.findViewById(R.id.wep).setBackgroundResource(0);
                    radioGroup.findViewById(R.id.none).setBackgroundResource(0);
                } else if (radioButton.getText().equals("WEP")) {
                    radioButton.setBackgroundResource(R.color.blue);
                    radioGroup.findViewById(R.id.wpa).setBackgroundResource(0);
                    radioGroup.findViewById(R.id.none).setBackgroundResource(0);
                } else if (radioButton.getText().equals("None")) {
                    radioButton.setBackgroundResource(R.color.blue);
                    radioGroup.findViewById(R.id.wpa).setBackgroundResource(0);
                    radioGroup.findViewById(R.id.wep).setBackgroundResource(0);
                }
            }
        });

        create_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int type = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(type);

                if (ssid.getText().toString().isEmpty() || password.getText().toString().isEmpty() ){
                    Toast.makeText(SoloWifiActivity.this,"Enter Correct Data!",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (mInterstitialAd!=null){
                        mInterstitialAd.show(SoloWifiActivity.this);
                    }else {
                        createQR(radioButton);
                    }
                }

            }
        });
    }

    private void soloIDS() {
        toolbar = findViewById(R.id.toolbar);
        radioGroup = findViewById(R.id.radioGroup);
        ssid = findViewById(R.id.ssid);
        password = findViewById(R.id.password);
        create_qr = findViewById(R.id.create_wifi);
        RadioButton radioButton =  findViewById(R.id.wpa);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioButton.setChecked(true);
    }

    private void createQR(RadioButton radioButton) {

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(
                    "ssid :" + ssid.getText().toString() +
                            ", password :" + password.getText().toString() +
                            ", type :" + radioButton.getText(),
                    BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            List<String> result_list = new ArrayList<>();
            result_list.add(ssid.getText().toString());
            result_list.add(password.getText().toString());
            result_list.add(radioButton.getText().toString());
            String result_string = new Gson().toJson(result_list);

            SoloSharedPreference sharedPref = new SoloSharedPreference();
            sharedPref.addToHistory(this,result_string,R.color.wifi_color,R.drawable.ic_wifi);
            showQR(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showQR(Bitmap bitmap) {

        SoloQRCreated.imageDrawable = bitmap;
        SoloQRCreated.icon = R.drawable.wifi;
        Intent i = new Intent(this, SoloQRActivity.class);
        i.putExtra("title",ssid.getText().toString());
        i.putExtra("header","WiFi");
        i.putExtra("icon",String.valueOf(R.drawable.ic_wifi));
        i.putExtra("color",String.valueOf(R.color.wifi_color));
        startActivity(i);
        finish();
    }
}
