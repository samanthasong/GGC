package com.ktpoc.tvcomm.consulting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.Toast;

import com.ktpns.lib.KPNSApis;
import com.ktpns.lib.OnKPNSInitializeEventListener;
import com.ktpns.lib.service.PushClientService;
import com.ktpns.lib.util.Constant;

import java.util.Properties;


public class MainActivity extends Activity{

    public WebView mWebView;
    private final String _TAG ="[MAIN ACTIVITY]";

    private  String _url;
    private String api_url;
    private ConsultingClient client;
    private String deviceAuthResult;
    private String tokenStr;
    private boolean isShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewManager.getInstance().setParamVal("HOME", "homepage");
        ViewManager.getInstance().addActivity(this);
        ViewManager.getInstance().printActivityListSofar();

        //String server_url = getResources().getString(R.string.consulting_server_url);

        setContentView(R.layout.activity_webview);
        mWebView = (WebView)findViewById(R.id.web_view);
        _url = getResources().getString(R.string.consulting_server_url);
        _url += ViewManager.getInstance().getParamVal("HOME");
        client = new ConsultingClient(_url);
        client.setWebviewSettings(mWebView);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IntentAction.PASS_TOKEN_FROM_CONSULTING);
        intentFilter.addCategory("android.intent.category.DEFAULT");
        registerReceiver(receiver, intentFilter);

        //0602
        Intent i = new Intent(IntentAction.SEND_NOTI_FROM_CONSULTING);
        i.putExtra("consultingState", "normal");
        Log.d(_TAG, "send broadcast normal extra to M.C");
        sendBroadcast(i);

        tokenStr = "";
        isShown = false;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(IntentAction.PASS_TOKEN_FROM_CONSULTING)){
                Bundle extras = intent.getExtras();
                if(extras != null){
                    tokenStr = extras.getString("token");
                    Log.d(_TAG, "token string is --> " + tokenStr);
                }
            }
        }
    };

    @Override
    protected void onResume(){
        super.onResume();

        String currentUserInput = null;


        deviceAuthResult = checkRegistration();
        registerToKPNS();
        switch (deviceAuthResult) {
            case "true": // Customer already registered once before
                Log.d(_TAG, "deviceAuthSelect result is -->" + deviceAuthResult);
                break;

            case "false": // Customer never registered
                Log.d(_TAG, "deviceAuthSelect result is -->" + deviceAuthResult);
                if(!isShown){
                    registerDialog();
                }
                break;

            case "error":
                Log.d(_TAG, "deviceAuthSelect result is -->" + deviceAuthResult);
                break;

            default:
                break;
        }

    }

    private String checkRegistration(){
        String result;

        api_url  = getResources().getString(R.string.consulting_api_url);
        AndroidHttp httpsClient = new AndroidHttp(api_url);

        Properties prop = new Properties();
        final String registrationId = ((TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

        // String registrationId = "testestest";
        prop.setProperty("registerid", registrationId);

        result = httpsClient.postCMS("deviceAuthSelect?", prop, true);
        return result;
    }

    private void registerDialog(){
        DialogBuilder builder = new DialogBuilder();

        if(builder.registerServiceDialog(this)){
            Intent intent = new Intent(MainActivity.this, UserRegisterActivity.class);
            intent.putExtra("token", tokenStr);
            startActivity(intent);
        }else {
            // 취소 누르면 STAY 죵
        }
        isShown = true;
    }

    private Boolean registerToKPNS(){
        //KPNS 2.1.5

        KPNSApis.requestInstance(this, new OnKPNSInitializeEventListener() {
            @Override
            public void onSuccessInitialize(KPNSApis kpnsApis) {
                kpnsApis.register("0WW4I105s0", "TVConsulting01");
                Log.d(_TAG, "KPNS API 초기화 성공");
                /*
                kpnsApis.getConnectionState(
                if (Prefs.isMainLibApp(UserRegisterActivity.this) == true) {
                    Log.d(_TAG, "THIS IS CONNECTED TO KPNS SERVER");
                } else {
                    Log.d(_TAG, "ANOTHER APP CONNECTED TO KPNS SERVER");
                }
                */
            }

            @Override
            public void onFailInitialize() {
                Log.d(_TAG, "KPNS API 초기화 실패");
                startService(new Intent(MainActivity.this, PushClientService.class).setAction(Constant.ACTION_START_SERVICE));
            }
        });

        return true;
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
                ViewManager.getInstance().removeActivity(MainActivity.this);


//                if(ViewManager.getInstance().removeActivity(MainActivity.this)){ // 마지막 남은 화면일 때
//                    DialogBuilder builder = new DialogBuilder();
//                    if( builder.exitAppDialog(this) ){ // 나가겠다고 했을 때
//                        ViewManager.getInstance().removeAndFinishAllActivity();
//
//                    }else{
//                        //do nothing
//                    }
//                }else{// 마지막 남은 화면은 아닐 때
//
//                }
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

