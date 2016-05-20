package com.ktpoc.tvcomm.consulting;

import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by insam on 2016. 5. 2..
 */
public class ConsultingClient {
    private final String _TAG = "[WEBVIEW]";
    private final String THIS_ANDROID_APP = "[CONSULTING ANDROID]";

    private String mUrl;

    public ConsultingClient(String url){
        mUrl = url;

    }
    public void setWebviewSettings(WebView webview){

        webview.loadUrl(mUrl);
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
                    Log.d(_TAG, "Permission request");
                    request.grant(request.getResources());
//                    }
//                });

            }


//
//            @Override
//            public void onPermissionRequest(final PermissionRequest request) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        request.grant(request.getResources());
//                        Log.d(_TAG, "Permission request");
//                    }
//                });
//            }

        });

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
//        webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webview.setWebContentsDebuggingEnabled(true);
        webview.addJavascriptInterface(new AndroidBridge(), THIS_ANDROID_APP);
        Log.d(_TAG, "CHROME VERSION : " + webview.getSettings().getUserAgentString());
    }

    public String refactorUrl(Bundle extra){


        return mUrl;

    }

    public void bridgeUserEventToJS(String userInput, WebView wv) {
        userInput = "USER EVENT IS : Pointing 2nd Div object";
        wv.loadUrl("javascript:onReceiveFromAndroid('USER EVENT SENDING...");
    }

}
