package com.example.womensafetyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;

public class FakeCallActivity extends AppCompatActivity {
    MediaPlayer mp;
    ImageButton call1,call2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_call);
        call1 = (ImageButton)findViewById(R.id.call1) ;
        call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mp.stop();
            }
        });

        call2 = (ImageButton)findViewById(R.id.call2) ;
        call2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.stop();
            }
        });
        mp = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        mp.setLooping(true);
        mp.start();
    }

    @Override
    public void onBackPressed() {
        mp.stop();
       FakeCallActivity.this.finishAffinity();
        Intent i=new Intent(FakeCallActivity.this, MainActivity.class);
        startActivity(i);
    }

}