package com.wordpress.helpmevishal.helpmevishalapp;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private WebView mWebView;
    ProgressBar progressBar;
    WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE}, 1);
        } else {
            addDeveloperContact();
        }
        mWebView = (WebView) findViewById(R.id.myWebView);
        webSettings = mWebView.getSettings();
        if (!DetectConnection.checkInternetConnection(this)) {
            Snackbar.make(mWebView, "No Internet! Please turn it on...", Snackbar.LENGTH_LONG).show();
        } else {
            webSettings.setJavaScriptEnabled(true);
            mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            mWebView.getSettings().setAppCacheEnabled(true);
            mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webSettings.setDomStorageEnabled(true);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSettings.setUseWideViewPort(true);
            //webSettings.setSavePassword(true);
            //webSettings.setSaveFormData(true);
            //webSettings.setEnableSmoothTransition(true);
            mWebView.setWebViewClient(new MyWebViewClient());
            mWebView.setWebChromeClient(new WebChromeClientDemo());

            mWebView.loadUrl("https://helpmevishal.wordpress.com/news-feeds-uploads/");
            mWebView.setDownloadListener(new DownloadListener() {
                public void onDownloadStart(String url, String userAgent,
                                            String contentDisposition, String mimetype,
                                            long contentLength) {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "HMV-Doc");
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");
                    Snackbar.make(mWebView,"Downloading File...",Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        deleteCache(this);
        clearApplicationData();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            deleteCache(this);
            clearApplicationData();
            super.onBackPressed();
            deleteCache(this);
            clearApplicationData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                if (!DetectConnection.checkInternetConnection(this)) {
                    Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_LONG).show();
                } else {
                    mWebView.reload();
                }
                break;
            case R.id.action_settings1:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey! I found an amazing homework app HelpMeVishal. Download it to have free access to all our Lab Practicals, Class Notes, Assignments and many other interesting stuffs.   https://play.google.com/store/apps/details?id=com.wordpress.helpmevishal.helpmevishalapp");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.action_settings2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, 1);
                } else {
                    callWhatsApp();
                }
                break;
            case R.id.action_settings:
                startActivity(new Intent(getApplicationContext(), Request.class));
                break;
            case R.id.action_settings4:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://helpmevishal.wordpress.com/"));
                startActivity(browserIntent);
                break;
            case R.id.action_settings5:
                startActivity(new Intent(getApplicationContext(), About.class));
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_news) {
            if (!DetectConnection.checkInternetConnection(this)) {
                Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_LONG).show();
            } else {
                mWebView.loadUrl("https://helpmevishal.wordpress.com/news-feeds-uploads/");
            }

        } else if (id == R.id.nav_lab) {
            if (!DetectConnection.checkInternetConnection(this)) {
                Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_LONG).show();
            } else {
                mWebView.loadUrl("https://helpmevishal.wordpress.com/labs-work/");
            }

        } else if (id == R.id.nav_notes) {
            if (!DetectConnection.checkInternetConnection(this)) {
                Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_LONG).show();
            } else {
                mWebView.loadUrl("https://helpmevishal.wordpress.com/class-notes/");
            }

        } else if (id == R.id.nav_downloads) {
            if (!DetectConnection.checkInternetConnection(this)) {
                Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_LONG).show();
            } else {
                mWebView.loadUrl("https://helpmevishal.wordpress.com/downloads/");
            }

        } else if (id == R.id.nav_about) {
            startActivity(new Intent(getApplicationContext(), About.class));

        } else if (id == R.id.nav_whatsapp) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey! I found an amazing homework app HelpMeVishal. Download it to have free access to all our Lab Practicals, Class Notes, Assignments and many other interesting stuffs.   https://play.google.com/store/apps/details?id=com.wordpress.helpmevishal.helpmevishalapp");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.nav_request) {
            startActivity(new Intent(getApplicationContext(), Request.class));
        } else if (id == R.id.nav_results) {
            if (!DetectConnection.checkInternetConnection(this)) {
                Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_LONG).show();
            } else {
                mWebView.loadUrl("http://www.m.ptuexam.com/LoginMe.aspx");
                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.m.ptuexam.com/LoginMe.aspx"));
                //startActivity(browserIntent);
            }
        } else if (id == R.id.nav_attedance) {
            if (!DetectConnection.checkInternetConnection(this)) {
                Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_LONG).show();
            } else {
                mWebView.loadUrl("http://erp.cgc.edu.in/");
            }

        } else if (id == R.id.nav_web) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://helpmevishal.wordpress.com/"));
            startActivity(browserIntent);
            //startActivity(new Intent(getApplicationContext(), website.class));
        } else if (id == R.id.nav_cgcnews) {
            if (!DetectConnection.checkInternetConnection(this)) {
                Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_LONG).show();
            } else {
                mWebView.loadUrl("https://www.facebook.com/CGCPunjab");
            }
        } else if (id == R.id.nav_facebook) {
            if (!DetectConnection.checkInternetConnection(this)) {
                Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_LONG).show();
            } else {
                mWebView.loadUrl("https://www.facebook.com/");
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class DetectConnection {
        public static boolean checkInternetConnection(Context context) {

            ConnectivityManager con_manager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (con_manager.getActiveNetworkInfo() != null
                    && con_manager.getActiveNetworkInfo().isAvailable()
                    && con_manager.getActiveNetworkInfo().isConnected()) {
                return true;
            } else {
                return false;
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            progressBar.setProgress(100);
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebChromeClientDemo extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {
            progressBar.setProgress(progress);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callWhatsApp();
                } else {
                    Toast.makeText(MainActivity.this, "Permission not granted !!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void callWhatsApp(){
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

    public void addDeveloperContact(){
        String Name = "Vishal Singh";
        String Phone = "+917696324435";
        String Email = "Kushwahz.vishalSingh@gmail.com";

        ArrayList<ContentProviderOperation> ops = new ArrayList < ContentProviderOperation > ();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        if (Name != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            Name).build());
        }

        //------------------------------------------------------ Mobile Number
        if (Phone != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, Phone)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        //------------------------------------------------------ Email
        if (Email != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, Email)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }

        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }
    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));

                }
            }
        }
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

}
