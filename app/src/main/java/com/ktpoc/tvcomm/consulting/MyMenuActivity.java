package com.ktpoc.tvcomm.consulting;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class MyMenuActivity extends Activity {

    public WebView mWebView;
    private final String _TAG = "[SCHEDULE ACTIVITY]";
    ConsultingClient client;

    private final String _url = "https://tvcomm.dlinkddns.com:3000/users/expert_category_22?roomId=hlsid6xgjz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewManager.getInstance().addActivity(this);

        //setContentView(R.layout.activity_webview);
       // mWebView = (WebView)findViewById(R.id.web_view);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Toast toast = Toast.makeText(this, _TAG, Toast.LENGTH_SHORT);
        toast.show();
//        Bundle extras = getIntent().getExtras();
//        if(extras != null){
//
//        }
//        client = new ConsultingClient(_url);
//        client.setWebviewSettings(mWebView);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setPackage("com.android.chrome");
        i.setData(Uri.parse(_url));

        startActivity(i);
        String currentUserInput = null;

    }
}
