package com.wordpress.helpmevishal.helpmevishalapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Request extends AppCompatActivity implements View.OnClickListener {
    ImageView whatsapp_ic, mail_ic;
    LinearLayout whatsapp, mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        AdView adView = (AdView) findViewById(R.id.r_adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);
        whatsapp = (LinearLayout) findViewById(R.id.l_whatsapp);
        whatsapp.setOnClickListener(this);
        mail = (LinearLayout) findViewById(R.id.l_mail);
        mail.setOnClickListener(this);
        whatsapp_ic = (ImageView) findViewById(R.id.imageView4);
        whatsapp_ic.setOnClickListener(this);
        mail_ic = (ImageView)findViewById(R.id.imageView5);
        mail_ic.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.l_mail:Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: helpmevishal@gmail.com"));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));
                break;
            case R.id.imageView5:
                emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: helpmevishal@gmail.com"));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));
                break;
            case R.id.l_whatsapp :
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
                        Toast.makeText(this, "Please refresh your WhatsApp Contact first", Toast.LENGTH_LONG).show();
                    }
                }

                break;
            case R.id.imageView4 :
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
                        Toast.makeText(this, "Please refresh your WhatsApp Contact first", Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }
}
