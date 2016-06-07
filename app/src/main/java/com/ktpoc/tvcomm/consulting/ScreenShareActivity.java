package com.ktpoc.tvcomm.consulting;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;



/*
 - when expert send push message, it dynamically receive the push message
 */

public class ScreenShareActivity extends Activity {

    public WebView mWebView;
    private ConsultingClient mClient;

    private final String _TAG = "[SCREEN SHARE]";

   // private Bridge mAndroidBridge;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        ViewManager.getInstance().addActivity(this);
        ViewManager.getInstance().printActivityListSofar();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IntentAction.DISCONNECT_FROM_CONSULTING);
        intentFilter.addCategory("android.intent.category.DEFAULT");
        registerReceiver(receiver, intentFilter);

        Bundle extras = getIntent().getExtras();
        String url = "";
        if(extras != null){
            url = extras.getString("message");
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            i.setPackage("com.android.chrome");
            startActivity(i);
        }else{
            //url is not passed
        }


    }

    private void checkFinalPage(){
        int currentActivityIdx = ViewManager.getInstance().getCurrentActivityIdx();

        if(currentActivityIdx == 0){

            Log.d(_TAG, "앱이 종료되어야 함");
            Toast toast = Toast.makeText(this, "컨설팅 서비스를 종료합니다", Toast.LENGTH_LONG);
            toast.show();

            moveTaskToBack(true);

            Intent i = new Intent(IntentAction.SEND_NOTI_FROM_CONSULTING);
            i.putExtra("consultingState", "exit");
            Log.d(_TAG, "send broadcast exit extra to M.C");
            sendBroadcast(i);

        }else if(currentActivityIdx > 0){
            //nothing to do
        }else{
            Log.d(_TAG, "current index is ackward : "+currentActivityIdx);
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(IntentAction.DISCONNECT_FROM_CONSULTING)){
                checkFinalPage();
                ViewManager.getInstance().removeActivity(ScreenShareActivity.this);
            }
        }
    };

    //0525
    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewManager.getInstance().addActivity(this);
        ViewManager.getInstance().addActivity(this);
        ViewManager.getInstance().printActivityListSofar();

        setContentView(R.layout.activity_webview);
        mWebView = (WebView)findViewById(R.id.web_view);
        String url = getIntent().getStringExtra("message");
        mClient = new ConsultingClient(url);
        mClient.setWebviewSettings(mWebView);
        mAndroidBridge = new Bridge();
        //mAndroidBridge.setOnResponseEventListener(listener);
        mClient.bridgeAndroid(mAndroidBridge, mWebView);
    }

    public  class Bridge {
        private final String _TAG = "[Bridge]";
        Handler handler = new Handler();

        @JavascriptInterface
        public void sendResponseFromJS(final String res) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d(_TAG, "JS sent a responses to this Android --> " + res);
//                    Intent intent = new Intent(ScreenShareActivity.this, MainActivity.class);
//                    intent.putExtra("EXTRA_FINISH", true);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
                    //moveTaskToBack(true);
                    ScreenShareActivity.this.finish();
                    ViewManager.getInstance().removeActivity(ScreenShareActivity.this);
                }
            });
        }
    }
*/

    @Override
    protected void onResume(){
        Log.d(_TAG, "onResume()");
        super.onResume();

    }


    @Override
    protected void onPause(){
        super.onPause();
        Log.d(_TAG, "onPause()");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(_TAG, "onStop()");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
        Log.d(_TAG, "onDestroy()");
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                checkFinalPage();
                ViewManager.getInstance().removeActivity(ScreenShareActivity.this);
                Log.d(_TAG, "BACK KEY PRESSED");

                break;
            default:
                break;
        }
        return false;
    }

}
