package com.ktpoc.tvcomm.consulting;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ktpns.lib.KPNSApis;
import com.ktpns.lib.OnKPNSInitializeEventListener;
import com.ktpns.lib.service.PushClientService;
import com.ktpns.lib.util.Constant;

import java.util.Properties;

public class UserRegisterActivity extends Activity {

    private String _TAG = "[USER REGISTER]";

    private EditText mPhoneEditTxt, mNameEditTxt;
    private Button mRegisterBtn;
    private RadioGroup mDeviceradioGroup;
    private RadioButton mGenieRadioBtn, mPhoneRadioBtn;
    private String m_server_url;
    private String result;
    private AndroidHttp mHttpsClient;
    private Properties mProperties;
    private String mRegistrationId;

    private String tokenStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        ViewManager.getInstance().addActivity(this);
        ViewManager.getInstance().printActivityListSofar();

        mPhoneEditTxt = (EditText)findViewById(R.id.phone_edit_text);
        mNameEditTxt = (EditText)findViewById(R.id.name_edit_text);
        mRegisterBtn= (Button)findViewById(R.id.register_button);
        mDeviceradioGroup = (RadioGroup)findViewById(R.id.device_radio_group);
        mGenieRadioBtn = (RadioButton)findViewById(R.id.genie_radio_btn);
        mPhoneRadioBtn = (RadioButton)findViewById(R.id.phone_radio_btn);

        m_server_url = getResources().getString(R.string.consulting_server_url);
        mHttpsClient = new AndroidHttp(m_server_url);
        mProperties = new Properties();
        //mRegistrationId = getResources().getString(R.string.imei);
        mRegistrationId = ((TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

       tokenStr = getIntent().getStringExtra("token");
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        Log.d(_TAG, "onNewIntent");
        if(intent != null){
            setIntent(intent);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        mPhoneEditTxt.setText("0103330132");
        mNameEditTxt.setText("insami");
    }
    @Override
    protected void onResume(){

        super.onResume();
        Log.d(_TAG, "onResume called");
        if(tokenStr == ""){
            //registerToKPNS();
        }

    }

    public void onClickRegister(View v) {

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.loading_bar);
        progressBar.setVisibility(View.VISIBLE);

        if (tokenStr != "") {

            String phoneNum, nickName, deviceType;

            phoneNum = mPhoneEditTxt.getText().toString();
            nickName = mNameEditTxt.getText().toString();
            deviceType = "2";

            mProperties.setProperty("registerid", mRegistrationId);
            mProperties.setProperty("tokenvalue", tokenStr);
            if (mDeviceradioGroup.getCheckedRadioButtonId() == R.id.phone_radio_btn) {
                deviceType = "1"; //default is 2 (Genie box)
            }else if(mDeviceradioGroup.getCheckedRadioButtonId() == R.id.genie_radio_btn) {
                deviceType = "2";
            }
            mProperties.setProperty("devicetype", deviceType);
            mProperties.setProperty("nickname", nickName);
            mProperties.setProperty("phone", phoneNum);

            result = mHttpsClient.postCMS("deviceInsert?", mProperties, false);
            Log.d(_TAG, "deviceInsert result is --> " + result);
            switch (result) {
                case "true":
                    Toast toast = Toast.makeText(this, "REGISTER TO CMS SUCCEED", Toast.LENGTH_SHORT);
                    toast.show();
                    progressBar.setVisibility(View.INVISIBLE);
                    break;
                case "false":
                    progressBar.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;
            }

        }
    }



    @Override
    protected void onPause(){
        super.onPause();

    }

    @Override
    protected void onDestroy(){


        super.onDestroy();
    }
}
