package com.ktpoc.tvcomm.consulting;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class ScreenShareActivity extends Activity {

    public WebView mWebView;
    private final String _TAG = "[SCREEN SHARE";

    private final String _url = "http://google.co.kr";
    private ConsultingClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewManager.getInstance().addActivity(this);
        ViewManager.getInstance().printActivityListSofar();


        setContentView(R.layout.activity_webview);
        mWebView = (WebView)findViewById(R.id.web_view);
        String url = getIntent().getStringExtra("message");
        mClient = new ConsultingClient(url);
        mClient.setWebviewSettings(mWebView);
    }

    @Override
    protected void onResume(){
        super.onResume();
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        Log.d("[ScreenSHARE]","wait for time");


//                    }
//                }, 5000);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}
