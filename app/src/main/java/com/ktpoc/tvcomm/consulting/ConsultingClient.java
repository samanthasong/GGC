package com.ktpoc.tvcomm.consulting;

import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by insam on 2016. 5. 2..
 */
public class ConsultingClient {
    private final String _TAG = "[WEBVIEW]";
    private final String THIS_ANDROID_APP = "ConsultingAndroid";

    private String mUrl;

    public ConsultingClient(String url){
        mUrl = url;

    }
    public void setWebviewSettings(final WebView webview){

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


        /* for more outstanding performance kkkkkkkkk*/
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        Log.d(_TAG, "CHROME VERSION : " + webview.getSettings().getUserAgentString());
    }
//
//    public void bridgeAndroid(ScreenShareActivity.Bridge androidBridge, WebView webView){
//        webView.addJavascriptInterface(androidBridge, THIS_ANDROID_APP);
//    }



//    public void bridgeUserEventToJS(String userInput, WebView wv) {
//        userInput = "USER EVENT IS : Pointing 2nd Div object";
//        wv.loadUrl("javascript:onReceiveFromAndroid('USER EVENT SENDING...");
//    }


}
