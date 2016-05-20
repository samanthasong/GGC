package com.ktpoc.tvcomm.consulting;

import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;


/**
 * Created by insam on 2016. 5. 2..
 */

public class AndroidBridge {

    private final String _TAG = "[AndroidBridge]";
    Handler handler = new Handler();

    @JavascriptInterface
    public void sendResponseFromJS(final String res) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(_TAG, "JS sent a responses to this Android --> " + res);
                //TODO: Insert code when dealing with Consulting TV Web Application RESPONSES(res)
            }
        });
    }
}
