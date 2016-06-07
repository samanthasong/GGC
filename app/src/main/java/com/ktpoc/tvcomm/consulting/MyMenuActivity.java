package com.ktpoc.tvcomm.consulting;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.Toast;

public class MyMenuActivity extends Activity {

    public WebView mWebView;
    private final String _TAG = "[SCHEDULE ACTIVITY]";
    ConsultingClient client;

    private String _url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewManager.getInstance().addActivity(this);
        ViewManager.getInstance().printActivityListSofar();

        setContentView(R.layout.activity_webview);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            _url = extras.getString("message");
            client = new ConsultingClient(_url);
        }
        mWebView = (WebView)findViewById(R.id.web_view);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Toast toast = Toast.makeText(this, _TAG, Toast.LENGTH_SHORT);
        toast.show();
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                //this.finish();
                checkFinalPage();
                ViewManager.getInstance().removeActivity(MyMenuActivity.this);
                Log.d(_TAG, "BACK KEY PRESSED");

                break;
            default:
                break;
        }
        return false;
    }
}
