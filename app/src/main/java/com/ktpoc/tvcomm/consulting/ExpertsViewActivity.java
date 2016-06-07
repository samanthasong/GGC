package com.ktpoc.tvcomm.consulting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class ExpertsViewActivity extends Activity {

    public WebView mWebView;
    private final String _TAG = "[SCHEDULE ACTIVITY]";
    ConsultingClient client;

    private String _url;
    private String _path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewManager.getInstance().addActivity(this);
        ViewManager.getInstance().printActivityListSofar();
        _path =  ViewManager.getInstance().getParamVal("CATEGORY");
        if(_path != ""){
            _url = getResources().getString(R.string.consulting_server_url);
            _url += _path;
        }
        setContentView(R.layout.activity_webview);
        mWebView = (WebView)findViewById(R.id.web_view);
        client = new ConsultingClient(_url);
        client.setWebviewSettings(mWebView);

        mWebView.addJavascriptInterface(new Bridge(), "consultingAndroid");

    }


    public  class Bridge {
        private final String _TAG = "[Bridge]";
        Handler handler = new Handler();

        @JavascriptInterface
        public void sendResponseFromJS(final String res) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    DialogBuilder builder = new DialogBuilder();
                    if(builder.exitAppDialog(ExpertsViewActivity.this) == true){
                        ViewManager.getInstance().removeAndFinishAllActivity();
                        Intent i = new Intent(IntentAction.SEND_NOTI_FROM_CONSULTING);
                        i.putExtra("consultingState", "exit");
                        Log.d(_TAG, "send broadcast exit extra to M.C");
                        sendBroadcast(i);
                        Log.d(_TAG, "exit key code received");
                    }
                }
            });
        }
    }


    @Override
    protected void onResume(){
        super.onResume();


        String currentUserInput = null;

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
                checkFinalPage();
                ViewManager.getInstance().removeActivity(ExpertsViewActivity.this);
                Log.d(_TAG, "BACK KEY PRESSED");

                break;
            case KeyEvent.KEYCODE_F1:
                DialogBuilder builder = new DialogBuilder();
                if( builder.exitAppDialog(this) ){
                    ViewManager.getInstance().removeAndFinishAllActivity();
                    Intent i = new Intent(IntentAction.SEND_NOTI_FROM_CONSULTING);
                    i.putExtra("consultingState", "exit");
                    Log.d(_TAG, "send broadcast exit extra to M.C");
                    sendBroadcast(i);
                }else{
                    //do nothing
                }

            default:
                break;
        }
        return false;
    }

}
