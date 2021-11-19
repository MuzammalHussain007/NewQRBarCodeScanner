package com.be.solo.qr.code.scanner.SoloActivities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.be.solo.qr.code.scanner.BuildConfig;
import com.be.solo.qr.code.scanner.R;
import com.be.solo.qr.code.scanner.SoloFragments.SoloCreateFragment;
import com.be.solo.qr.code.scanner.SoloFragments.SoloFavouritesFragment;
import com.be.solo.qr.code.scanner.SoloFragments.SoloHistoryFragment;
import com.be.solo.qr.code.scanner.SoloFragments.BeScanFragment;
import com.be.solo.qr.code.scanner.SoloUtils.SoloSharedPreference;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


public class SoloMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private LinearLayout linearLayout;
    private String TAG = "this";
    private SoloSharedPreference soloSharedPreference;

    //Admob Ads Integration
    private InterstitialAd mInterstitialAd;
    private boolean isExitApp = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, initializationStatus -> {
        });
        soloInit();
        soloOtherStuff();
        soloClickListener();
        admobInterstitialAds();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setAppicationMode();
    }

    private void setAppicationMode() {
        int val = soloSharedPreference.getsaveSoloMode(getApplicationContext());
        if (val == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
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
                                if (isExitApp) {
                                    finishAffinity();
                                }
                            }
                        });
                    }
                });
    }

    private void soloOtherStuff() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        loadFragment(new BeScanFragment());
        bottomNavigationView.setSelectedItemId(R.id.scan);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setCheckedItem(R.id.home);
    }

    private void soloClickListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        loadFragment(new BeScanFragment());
                        bottomNavigationView.setSelectedItemId(R.id.scan);
                        break;
                    case R.id.favourite:
                        loadFragment(new SoloFavouritesFragment());
                        bottomNavigationView.setSelectedItemId(R.id.favourite);
                        break;
                    case R.id.history:
                        loadFragment(new SoloHistoryFragment());
                        bottomNavigationView.setSelectedItemId(R.id.invisible);
                        break;
                    case R.id.privacyPolicy:
                        privacyPolicy();
                        break;
                    case R.id.change_theme: {
                        showDialog();
                        break;
                    }
                    case R.id.share:
                        shareApp();
                        break;
                    case R.id.more_apps:
                        moreApps();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.create:
                        loadFragment(new SoloCreateFragment());
                        navigationView.setCheckedItem(R.id.invisible_drawer);
                        break;
                    case R.id.scan:
                        loadFragment(new BeScanFragment());
                        navigationView.setCheckedItem(R.id.home);
                        break;
                    case R.id.favourite:
                        loadFragment(new SoloFavouritesFragment());
                        navigationView.setCheckedItem(R.id.favourite);
                        break;

                }
                return true;
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(SoloMainActivity.this);
        alBuilder.setTitle("Choose Theme");
        String[] list = {"Light Mode", "Dark Mode"};
        alBuilder.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        showLightMode();
                        break;
                    }
                    case 1: {
                        showDarkMode();
                        break;
                    }
                }
            }
        });
        alBuilder.create().show();
    }

    private void showDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        soloSharedPreference.saveSoloMode(getApplicationContext(), AppCompatDelegate.MODE_NIGHT_YES);
    }

    private void showLightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        soloSharedPreference.saveSoloMode(getApplicationContext(), AppCompatDelegate.MODE_NIGHT_NO);


    }

    private void privacyPolicy() {
//        Uri uri = Uri.parse("https://www.solovisionllc.com/privacy");
        Uri uri = Uri.parse("https://docs.google.com/document/d/1KgjT7j7C8kK_nABbS9pX41ZekxHDMv2mgOeDS1nX1Wg/edit?usp=sharing");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void soloInit() {
        bottomNavigationView = findViewById(R.id.bottomNav);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        linearLayout = findViewById(R.id.banner_container);
        soloSharedPreference = new SoloSharedPreference();
    }

    private void moreApps() {

        Uri uri = Uri.parse("https://play.google.com/store/apps/developer?id=SoLoVision");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void loadFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        existConfirmation();
    }

    private void existConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SoloMainActivity.this, R.style.CustomDialogTheme);
        builder.setTitle("Do You Really want to Exit!");
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_exit_button, null);
        Button yes, no, rateUs;
        yes = view.findViewById(R.id.button_cancel_yes);
        no = view.findViewById(R.id.button_cancel_no);
        rateUs = view.findViewById(R.id.button_cancel_rateApp);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        rateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(SoloMainActivity.this);
                    isExitApp = true;
                } else {
                    finishAffinity();
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    private void shareApp() {

        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "QR Scanner");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
