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

public class HomeActivity extends Activity {

    public WebView mWebView;
    private final String _TAG ="[HOME ACTIVITY]";

    //private final String _url = "https://amuzlab.iptime.org:3000/users/asdf";
    //private final String _url = "https://172.30.1.58:3000/consulting/amuzlab";
    //private final String _url = "https://tvcomm.dlinkddns.com:3000/users/expert_category_22?roomId=rjw0asgrrg";
    private final String _url = "http://www.google.co.kr";

    private String server_url;
    private ConsultingClient client;
    private String deviceAuthResult;
    private String tokenStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewManager.getInstance().addActivity(this);
        ViewManager.getInstance().printActivityListSofar();

        //String server_url = getResources().getString(R.string.consulting_server_url);
        setContentView(R.layout.activity_webview);

        client = new ConsultingClient(_url);
        mWebView = (WebView)findViewById(R.id.web_view);
        client.setWebviewSettings(mWebView);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.ktpoc.tvcomm.consulting.send.token");
        //for push msg test
        intentFilter.addAction("com.insun.send.msg.songsong");
        intentFilter.addCategory("android.intent.category.DEFAULT");
        registerReceiver(receiver, intentFilter);

        tokenStr = "";

    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.ktpoc.tvcomm.consulting.send.token")){
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

//        Bundle extras = getIntent().getExtras();
//        if(extras!=null){
//
//
//        }
        Toast toast = Toast.makeText(this, _TAG, Toast.LENGTH_SHORT);
        toast.show();

        String currentUserInput = null;


        deviceAuthResult = checkRegistration();
        registerToKPNS();
        switch (deviceAuthResult) {
            case "true": // Customer already registered once before
                Log.d(_TAG, "deviceAuthSelect result is -->" + deviceAuthResult);
                break;

            case "false": // Customer never registered
                Log.d(_TAG, "deviceAuthSelect result is -->" + deviceAuthResult);
                registerDialog();
                break;

            case "error":
                Log.d(_TAG, "deviceAuthSelect result is -->" + deviceAuthResult);
                break;

            default:
                break;
        }


        //client.bridgeUserEventToJS(currentUserInput, mWebView);
    }

    private String checkRegistration(){
        String result;

        server_url  = getResources().getString(R.string.consulting_server_url);
        AndroidHttp httpsClient = new AndroidHttp(server_url);

        Properties prop = new Properties();
       final String registrationId = ((TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

       // String registrationId = "testestest";
        prop.setProperty("registerid", registrationId);

        result = httpsClient.postCMS("deviceAuthSelect?", prop, true);
        return result;
    }

    private void registerDialog(){
        final AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(this);
        dlgBuilder.setMessage("GiGA Genie 컨설팅 서비스에 가입하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(HomeActivity.this, UserRegisterActivity.class);
                        intent.putExtra("token", tokenStr);
                        startActivity(intent);

                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = dlgBuilder.create();
        alertDialog.setTitle("GiGA Genie 컨설팅 서비스");
        alertDialog.show();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                this.finish();
                Log.d(_TAG, "BACK KEY PRESSED");

                break;
            default:
                break;
        }
        return false;
    }

    private Boolean registerToKPNS(){
        //KPNS 2.1.5
        ///*
        KPNSApis.requestInstance(this, new OnKPNSInitializeEventListener() {
            @Override
            public void onSuccessInitialize(KPNSApis kpnsApis) {
                kpnsApis.register("0WW4I105s0", "TVConsulting01");
                Log.d(_TAG, "KPNS API 초기화 성공");
                /*
                kpnsApis.getConnectionState();

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
                startService(new Intent(HomeActivity.this, PushClientService.class).setAction(Constant.ACTION_START_SERVICE));
            }
        });
        //*/
        //for KPNS 2.1.2

        //        KPNSApis.requestInstance(Context context, new OnKPNSInitializeEventListener() {
//
//            @Override
//            public void onSuccessInitialize(KPNSApis kpnsApis) {
//                kpnsApis.register("0WW4I105s0", "TVConsulting01"); //param: appId, cpId
//            }
//
//            @Override
//            public void onFailInitialize() {
//                Log.d(_TAG, "KPNS initializing failed!!!");
//                startService(new Intent(context, PushClientService.class).setAction(Constant.ACTION_START_SERVICE));
//                //phone state에 대한 퍼미션 있어야 pushClient로 register하라고 되어 있음 내일하장
////                iif ((context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
////                    startService(new Intent(MainActivity.this, PushClientService.class).setAction(Constant.ACTION_START_SERVICE));
////                } else { }
//            }
//        });
        return true;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    /*
   [USAGE -  Web Application에서의 Android's function call usage]
   - window.consultingAndroid.함수();
   - 이 떄 함수는 AndroidBridge 클래스 내 멤버메소드로용~
*/


    /* Android call the JS's function */

}
