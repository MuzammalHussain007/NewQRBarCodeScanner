package com.be.solo.qr.code.scanner.SoloActivities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.be.solo.qr.code.scanner.R;
import com.be.solo.qr.code.scanner.SoloModels.Scanned;
import com.be.solo.qr.code.scanner.SoloModels.SoloFavoriteModel;
import com.be.solo.qr.code.scanner.SoloModels.SoloHistoryModel;
import com.be.solo.qr.code.scanner.SoloUtils.SoloSharedPreference;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SoloScanResultActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TextView result, time, title;
    private List<SoloFavoriteModel> favorites;
    private List<SoloHistoryModel> history;
    private List<Scanned> scannedList;
    SoloSharedPreference sharedPref = new SoloSharedPreference();
    private boolean IS_FAVOURITE = false;
    private Menu menu;
    private SoloSharedPreference soloSharedPreference ;

    private InterstitialAd mInterstitialAd;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        MobileAds.initialize(SoloScanResultActivity.this, initializationStatus -> {
        });
        admobBannerAds();
        admobInterstitialAds();
        soloInit();
        soloSetToolbar();
        soloOther();
        soloClickListener();
        soloaction();

    }

    private void soloaction() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        Scanned scanned = new Scanned("Scanned QR Code",currentDateandTime);
        scannedList.add(scanned);
        soloSharedPreference.saveScannedHistory(getApplicationContext(),scannedList);
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
                                finish();
                            }
                        });
                    }
                });
    }

    private void soloClickListener() {
//        sharedPref.addToHistory(this, result.getText().toString());  //TODO SOLO RESULT HISTORY

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(SoloScanResultActivity.this);
                } else {
                    finish();
                }
            }
        });
    }

    private void soloOther() {
        Intent intent = getIntent();
        String scaning_result = intent.getStringExtra("scaning_result");
        result.setText(scaning_result);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        time.setText(currentDateandTime);
    }

    private void soloSetToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void soloInit() {
        toolbar = findViewById(R.id.toolbar);
        result = findViewById(R.id.result);
        time = findViewById(R.id.time);
        title = findViewById(R.id.title);
        soloSharedPreference = new SoloSharedPreference();
        scannedList = new ArrayList<>();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scan_result_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.favourite_toolbar:
                manageFavourities();
                return true;
            case R.id.copy:
                copyText();
                return true;
            case R.id.share:
                shareText();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void manageFavourities() {

        if (IS_FAVOURITE) {

            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_result_black_24dp));

            List<SoloFavoriteModel> favoriteList = sharedPref.getFavorites(this);
            favoriteList.remove(favoriteList.size() - 1);
            sharedPref.saveFavorites(this, favoriteList);
            IS_FAVOURITE = false;
        } else {

            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.remove_fav));

            sharedPref.addTofavourite(this, result.getText().toString(),R.color.texts_color,R.drawable.ic_uil_texts);
            IS_FAVOURITE = true;
        }

    }

    private void copyText() {

        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) SoloScanResultActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Text Copied", result.getText());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(SoloScanResultActivity.this, "Text Copied", Toast.LENGTH_LONG).show();
    }

    private void shareText() {

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, result.getText().toString());
        startActivity(Intent.createChooser(share, "Share Status Via"));
    }

}
