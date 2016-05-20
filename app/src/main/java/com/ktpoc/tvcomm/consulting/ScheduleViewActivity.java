package com.ktpoc.tvcomm.consulting;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class ScheduleViewActivity extends Activity {

    public WebView mWebView;
    private final String _TAG = "[SCHEDULE ACTIVITY]";
    ConsultingClient client;

    private final String _url = "https://amuzlab.iptime.org:3000/users/asdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mWebView = (WebView)findViewById(R.id.web_view);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Toast toast = Toast.makeText(this, _TAG, Toast.LENGTH_SHORT);
//        Bundle extras = getIntent().getExtras();
//        if(extras != null){
//
//        }
        client = new ConsultingClient(_url);
        client.setWebviewSettings(mWebView);
        String currentUserInput = null;

    }

}
