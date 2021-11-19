package com.be.solo.qr.code.scanner.SoloActivities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;

import com.be.solo.qr.code.scanner.R;
import com.be.solo.qr.code.scanner.SoloModels.SoloFavoriteModel;
import com.be.solo.qr.code.scanner.SoloUtils.SoloQRCreated;
import com.be.solo.qr.code.scanner.SoloUtils.SoloSharedPreference;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SoloQRActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 101;
    private boolean IS_FAVOURITE =false ;
    private ImageView imageView, icon;
    private TextView time, title;
    private Toolbar toolbar;
    private LinearLayout btn_save, btn_share, btn_print;
    private String permissoin_for;
    private boolean saved = false;
    private int color , icons;
    private Menu menu ;
    String title_string;
   private SoloSharedPreference sharedPref = new SoloSharedPreference();

    private AdView adView;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu=menu;
        getMenuInflater().inflate(R.menu.favourite_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.favourite_favourite:
            {
                manageFavourities(title_string,color,icons);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        MobileAds.initialize(SoloQRActivity.this, initializationStatus -> {
        });
        admobBannerAds();
        soloInit();
        soloToolbar();
        soloTimeCode();
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
    private void manageFavourities(String text, int color,int iconss) {

        if (IS_FAVOURITE) {

            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_result_black_24dp));
            List<SoloFavoriteModel> favoriteList = sharedPref.getFavorites(this);
            favoriteList.remove(favoriteList.size() - 1);
            sharedPref.saveFavorites(this, favoriteList);
            IS_FAVOURITE = false;
        } else {

            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.remove_fav));

            sharedPref.addTofavourite(this, text,color,iconss);
            IS_FAVOURITE = true;
        }

    }


    private void soloClickListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissoin_for = "share";
                permissions(permissoin_for);

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_save.setEnabled(false);
                permissoin_for = "save";
                permissions(permissoin_for);
            }
        });
        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soloPrintQR();
            }
        });
    }

    private void soloTimeCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        time.setText(currentDateandTime);
    }

    private void soloToolbar() {
        setSupportActionBar(toolbar);

        imageView.setImageBitmap(SoloQRCreated.imageDrawable);

        Intent i = getIntent();
        title_string = i.getStringExtra("title");
        String header = i.getStringExtra("header");
        title.setText(title_string);
        toolbar.setTitle(header);
        icons=Integer.parseInt(i.getStringExtra("icon"));
        color=Integer.parseInt(i.getStringExtra("color"));
        Glide.with(this).load(icons).into(icon);
        icon.setBackgroundResource(color);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void soloInit() {
        imageView = findViewById(R.id.image);
        icon = findViewById(R.id.icon);
        time = findViewById(R.id.time);
        title = findViewById(R.id.title);
        btn_print = findViewById(R.id.btn_print);
        btn_save = findViewById(R.id.btn_save);
        btn_share = findViewById(R.id.btn_share);
        toolbar = findViewById(R.id.toolbar);
        sharedPref = new SoloSharedPreference();

    }

    private void soloPrintQR() {

        PrintHelper photoPrinter = new PrintHelper(this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

        //print
        photoPrinter.printBitmap("QRCode", SoloQRCreated.imageDrawable, new PrintHelper.OnPrintFinishCallback() {
            @Override
            public void onFinish() {
            }
        });

    }

    private void saveQR() {
        if (saved)
        {

        }
        else {
            OutputStream outputStream = null;
            Uri uri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver contentResolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "image_" + System.currentTimeMillis() / 1000 + ".jpg");
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);


                try {
                    outputStream = contentResolver.openOutputStream(uri);
                   
                    Bitmap bitmap1 = Bitmap.createBitmap(SoloQRCreated.imageDrawable);
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

                if (SoloQRCreated.imageDrawable.getWidth() > SoloQRCreated.imageDrawable.getHeight()) {
                    String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                    File file = new File(imagesDir, "image_" + System.currentTimeMillis() + ".jpg");
                    try {
                        outputStream = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap1 = Bitmap.createBitmap(SoloQRCreated.imageDrawable);
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                            bitmap1, "image_" + System.currentTimeMillis(), null);
                    uri = Uri.parse(path);
                } else {
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                            SoloQRCreated.imageDrawable, "image_" + System.currentTimeMillis(), null);
                    uri = Uri.parse(path);
                }
                Log.d("filteredUri__", uri + " = camera");

            }

        }
    }

    private void shareQR() {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        OutputStream outputStream;
        BitmapDrawable draw = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = draw.getBitmap();

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/QRCode");
            dir.mkdirs();

        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        File outFile = new File(dir, fileName);
        try {
            outputStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(outFile));
            sendBroadcast(intent);

        } catch (IOException e) {
//            Toast.makeText(QRActivity.this, "Go to Device Settings>Device>Applications>Application Manager>QRCode>Permissions>Enable Storage permission!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }//            Toast.makeText(QRActivity.this, "Go to Device Settings>Device>Applications>Application Manager>QRCode>Permissions>Enable Storage permission!" , Toast.LENGTH_LONG).show();


        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpg");
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outFile));
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share QRCode Via"));
    }


    private void permissions(String permissoin_type) {
        checkPermissions(
                STORAGE_PERMISSION_CODE,permissoin_type);
    }

    private void checkPermissions(int storagePermissionCode, String permissoin_type) {
        if (ContextCompat.checkSelfPermission(SoloQRActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(SoloQRActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        } else {
            if (permissoin_type.equals("save")) {
                saveQR();
            }
            if (permissoin_type.equals("share")) {
                shareQR();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (permissoin_for.equals("save")) {
                    saveQR();
                }
                if (permissoin_for.equals("share")) {
                    shareQR();
                }
                Toast.makeText(SoloQRActivity.this,
                        "Storage Permission Granted! QRCode Saved.",
                        Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(SoloQRActivity.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_LONG)
                        .show();
            }
        }
    }
}
