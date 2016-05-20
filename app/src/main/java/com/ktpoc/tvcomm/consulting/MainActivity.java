package com.ktpoc.tvcomm.consulting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends Activity{

    private final String _TAG = "[MAIN PAGE]";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ViewManager.getInstance().addActivity(this);
        ViewManager.getInstance().printActivityListSofar();

        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected  void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        Log.d(_TAG, "onNewIntent");
        if(intent != null){
            setIntent(intent);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        //TODO: check
        Intent i = new Intent(this, com.ktpoc.tvcomm.consulting.HomeActivity.class);
        startActivity(i);

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
}
