package com.be.solo.qr.code.scanner.SoloActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.be.solo.qr.code.scanner.R;
import com.bumptech.glide.Glide;


public class SoloTextDetailActivity extends AppCompatActivity {

    private TextView btn_mail,btn_message,content,time,title;
    private Toolbar toolbar;
    private Intent intent;
    private int color ,icon;
    private ImageView iconImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texts);

        soloInit();
        soloSetToolbar();
        soloOthers();
        soloListener();
    }

    private void soloListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        btn_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail(intent.getStringExtra("content"));
            }
        });
    }

    private void soloOthers() {
        intent = getIntent();
        time.setText(intent.getStringExtra("date"));
        title.setText(intent.getStringExtra("content"));
        content.setText(intent.getStringExtra("content"));
        Glide.with(getApplicationContext()).load(icon).into(iconImage);
        iconImage.setBackgroundResource(color);

    }

    private void soloSetToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void soloInit() {
        toolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.title);
        time = findViewById(R.id.time);
        content = findViewById(R.id.content);
        btn_mail = findViewById(R.id.mail);
        btn_message = findViewById(R.id.message);
        color = Integer.parseInt(getIntent().getStringExtra("color"));
        icon = Integer.parseInt(getIntent().getStringExtra("icon"));
        iconImage = findViewById(R.id.icon);
     }

    private void sendMessage() {

        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", "");
                sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(sendIntent);

    }

    private void sendMail(String contents) {

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT,contents);
        startActivity(Intent.createChooser(share, "Share Status Via"));
    }
}
