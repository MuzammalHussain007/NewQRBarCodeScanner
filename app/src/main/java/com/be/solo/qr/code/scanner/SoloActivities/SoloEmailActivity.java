package com.be.solo.qr.code.scanner.SoloActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class SoloEmailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText mail_to, subject, message;
    private TextView create_qr;
    private ImageView imageView;

    private AdView adView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        MobileAds.initialize(SoloEmailActivity.this, initializationStatus -> {
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
                                if (mail_to.getText().toString().isEmpty() || subject.getText().toString().isEmpty() ||
                                        message.getText().toString().isEmpty()) {
                                    Toast.makeText(SoloEmailActivity.this, "Enter Correct Data!", Toast.LENGTH_SHORT).show();
                                } else {
                                    soloCreateQR();
                                }
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
                if (mail_to.getText().toString().isEmpty() || subject.getText().toString().isEmpty() ||
                        message.getText().toString().isEmpty()) {
                    Toast.makeText(SoloEmailActivity.this, "Enter Correct Data!", Toast.LENGTH_SHORT).show();
                } else {
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(SoloEmailActivity.this);
                    } else {
                        soloCreateQR();
                    }
                }
            }
        });
    }

    private void soloSetToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void soloInit() {
        toolbar = findViewById(R.id.toolbar);
        mail_to = findViewById(R.id.mail_to);
        subject = findViewById(R.id.subject);
        message = findViewById(R.id.message);
        create_qr = findViewById(R.id.create_mail);
        imageView = findViewById(R.id.image);
    }

    private void soloCreateQR() {

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(
                    "email_to :" + mail_to.getText().toString() +
                            ", mail_subject :" + subject.getText().toString() + ", mail_content :" + message.getText().toString(),
                    BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            List<String> result_list = new ArrayList<>();
            result_list.add(mail_to.getText().toString());
            result_list.add(subject.getText().toString());
            result_list.add(message.getText().toString());
            String result_string = new Gson().toJson(result_list);

            SoloSharedPreference sharedPref = new SoloSharedPreference();
            sharedPref.addToHistory(this, result_string,R.color.email_color,R.drawable.ic_email);
            soloShowQR(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void soloShowQR(Bitmap bitmap) {

        imageView.setImageBitmap(bitmap);
        SoloQRCreated.imageDrawable = bitmap;
        SoloQRCreated.icon = R.drawable.email;
        Intent i = new Intent(this, SoloQRActivity.class);
        i.putExtra("title", mail_to.getText().toString());
        i.putExtra("header", "Email");
        i.putExtra("icon",String.valueOf(R.drawable.ic_email));
        i.putExtra("color",String.valueOf(R.color.email_color));
        startActivity(i);
        finish();
    }
}
