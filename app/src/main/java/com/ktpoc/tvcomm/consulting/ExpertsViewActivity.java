package com.ktpoc.tvcomm.consulting;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class ExpertsViewActivity extends Activity {

    public WebView mWebView;
    private final String _TAG = "[SCHEDULE ACTIVITY]";
    ConsultingClient client;

    private String _url = "https://tvcomm.dlinkddns.com:3000/expertlistpage/amuzlab";

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
        toast.show();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            //String _category = extras.getString("category");

        }
        _url += "?index=";
        _url += "1";
        client = new ConsultingClient(_url);
        client.setWebviewSettings(mWebView);
        String currentUserInput = null;

    }
}
