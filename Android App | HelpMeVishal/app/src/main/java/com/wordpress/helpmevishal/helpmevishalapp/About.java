package com.wordpress.helpmevishal.helpmevishalapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        AdView adView = (AdView) findViewById(R.id.a_adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);
        findViewById(R.id.vishal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
                } else {
                    Uri uri = Uri.parse("smsto:" + "+917696324435");
                    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                    i.putExtra("sms_body", "Hi");
                    i.setPackage("com.whatsapp");
                    try{
                        startActivity(i);
                    } catch (Exception e){
                        Toast.makeText(About.this, "Please refresh your WhatsApp Contact first", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        findViewById(R.id.shamli).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
                } else {
                    Uri uri = Uri.parse("smsto:" + "+917696324435");
                    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                    i.putExtra("sms_body", "Hi");
                    i.setPackage("com.whatsapp");
                    try {
                        startActivity(i);
                    } catch (Exception e) {
                        Toast.makeText(About.this, "Please refresh your WhatsApp Contact first", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
