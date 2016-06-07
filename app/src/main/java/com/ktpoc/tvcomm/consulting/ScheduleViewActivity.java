package com.ktpoc.tvcomm.consulting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
        client = new ConsultingClient(_url);
        client.setWebviewSettings(mWebView);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Toast toast = Toast.makeText(this, _TAG, Toast.LENGTH_SHORT);
        toast.show();

        String currentUserInput = null;
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
               // this.finish();
                checkFinalPage();
                ViewManager.getInstance().removeActivity(ScheduleViewActivity.this);
                Log.d(_TAG, "BACK KEY PRESSED");

                break;
            default:
                break;
        }
        return false;
    }
}
