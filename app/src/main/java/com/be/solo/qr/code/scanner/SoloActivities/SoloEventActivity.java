package com.be.solo.qr.code.scanner.SoloActivities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SoloEventActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView begin_time, end_time, create_qr;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private EditText title, location, description;

    private AdView adView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        MobileAds.initialize(SoloEventActivity.this, initializationStatus -> {
        });
        admobBannerAds();
        admobInterstitialAds();
        soloInit();
        soloSetToolbar();
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
                                soloCreateQR();
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

        begin_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soloSelectBeginDate(0);
            }
        });
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soloSelectBeginDate(1);
            }
        });

        create_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (title.getText().toString().isEmpty() || location.getText().toString().isEmpty() ||
                        description.getText().toString().isEmpty() || begin_time.getText().toString().isEmpty() ||
                        end_time.getText().toString().isEmpty()) {
                    Toast.makeText(SoloEventActivity.this, "Enter Correct Data!", Toast.LENGTH_SHORT).show();
                } else {
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(SoloEventActivity.this);
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
        begin_time = findViewById(R.id.begin_time);
        end_time = findViewById(R.id.end_time);
        title = findViewById(R.id.title);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        create_qr = findViewById(R.id.create_event);
    }

    private void soloCreateQR() {

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(
                    "title :" + title.getText().toString() +
                            ", location :" + location.getText().toString() +
                            ", description :" + description.getText().toString() +
                            ", begin_time :" + begin_time.getText().toString() +
                            ", end_time :" + end_time.getText().toString(),
                    BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            List<String> result_list = new ArrayList<>();
            result_list.add(title.getText().toString());
            result_list.add(location.getText().toString());
            result_list.add(description.getText().toString());
            result_list.add(begin_time.getText().toString());
            result_list.add(end_time.getText().toString());
            String result_string = new Gson().toJson(result_list);

            SoloSharedPreference sharedPref = new SoloSharedPreference();
            sharedPref.addToHistory(this, result_string,R.color.event_color,R.drawable.calenderss);
            soloShowQR(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void soloShowQR(Bitmap bitmap) {

        SoloQRCreated.imageDrawable = bitmap;
        SoloQRCreated.icon = R.drawable.calendar;
        Intent i = new Intent(this, SoloQRActivity.class);
        i.putExtra("title", title.getText().toString());
        i.putExtra("header", "Event");
        i.putExtra("icon",String.valueOf(R.drawable.calenderss));
        i.putExtra("color",String.valueOf(R.color.event_color));

        startActivity(i);
        finish();
    }

    private void soloSelectBeginDate(final int value) {

        final Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog StartTime = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                if (value == 0) {
                    begin_time.setText(dateFormat.format(newDate.getTime()));
                }
                if (value == 1) {
                    end_time.setText(dateFormat.format(newDate.getTime()));
                }

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        StartTime.show();
    }
}
