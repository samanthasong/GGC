package com.ktpoc.tvcomm.consulting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by insam on 2016. 4. 21..
 */
public class PushReceiver extends BroadcastReceiver{

//TODO:
    private final String _TAG = "[SONG-Push Receiver BR]";
    @Override
    public void onReceive(Context context, Intent intent) {

        //TODO: receiving msg and processing msg ~~ popup triggering~
        String action = intent.getAction();


        /* On REGISTER */
        if("com.tta.push.intent.receive.REGISTRATION".equals(action)){ //version 2.x
            try{
                byte[] token = intent.getByteArrayExtra("token");
                String error = intent.getStringExtra("error");
                String tokenStr = new String(token, "UTF-8");
                Log.d(_TAG, "PUSH REGISTRATION Token STR is  --> " + tokenStr + ", error -->" + error);


                Intent i = new Intent(IntentAction.PASS_TOKEN_FROM_CONSULTING); //receive it in
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.putExtra("token", tokenStr);
                context.sendBroadcast(i);

            }catch (Exception e){
                e.printStackTrace();
            }
        } else if ("com.ktpns.pa.receive.REGISTRATION".equals(action)){ //version 1.x
            Log.d(_TAG, "PUSH REGISTRATION result  --> " + intent.getIntExtra("result", -1));
            Log.d(_TAG, "PUSH REGISTRATION regId  --> " + intent.getStringExtra("regId"));
            Log.d(_TAG, "PUSH REGISTRATION detail  --> " + intent.getStringExtra("detail"));
        }

        /* On CHECK STATUS */
        if("com.tta.push.intent.receive.STATUS_OF_SERVICE".equals(action)){ //version 2.x
            try{
                int status = intent.getIntExtra("status", -2);
                switch (status){
                    case 0:
                        Log.d(_TAG, "STATUS OF PUSH --> disconnected!");
                        break;
                    case 1:
                        Log.d(_TAG, "STATUS OF PUSH --> Connected!");
                        break;
                    case -1:
                        Log.d(_TAG, "STATUS OF PUSH --> unregistered package!");
                        break;
                    default:
                        Log.d(_TAG, "STATUS OF PUSH --> UNKNOWN");
                        break;

                }

            }catch (Exception e){
                e.printStackTrace();
            }
        } else if ("com.ktpns.pa.receive.SERVICE_STATUS".equals(action)){ //version 1.x
            Log.d(_TAG, "STATUS OF PUSH status  --> " + intent.getStringExtra("status"));
            Log.d(_TAG, "STATUS OF PUSH details  --> " + intent.getStringExtra("datail"));
        }

        /* on RECEIVE ACTUAL PUSH MESSAGE */
        if("com.tta.push.intent.receive.MESSAGE".equals(action)){ //version 2.x

            int type = intent.getIntExtra("type", -2);
            byte[] message = intent.getByteArrayExtra("message");
            boolean needAck = intent.getBooleanExtra("needAck", false);
            short transactionId = intent.getShortExtra("transactionId", (short)0);
            String strMsg;
            try{
                strMsg = new String(message, "UTF-8");
                switch (type) {
                    case 0:
                        Log.d(_TAG, "PUSH MESSAGE type --> Broadcasting Notice");
                        break;
                    case 1:
                        Log.d(_TAG, "receive.MESSAGE type --> General Push Message");

                        String msg = strMsg;

                        if(msg.contains("action_client")){ // Expert finishes consulting then notify it via PUSH MSG
                            String closeAction = strMsg.substring(19);
                            Log.d(_TAG, "Action client message is -> "+ closeAction);

                            if(closeAction.equals("close")){
                                // close the screen Activity
                                Log.d(_TAG, "Action client message is -> ");
                                Intent i = new Intent("com.ktpoc.tvcomm.consulting.send.session.off");
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                context.sendBroadcast(i);

                            }else{
                                //do nothing
                            }
                        }else if(msg.contains("room_url")){ // Expert starts consulting and notify it via PUSH MSG
                            String url = strMsg.substring(14);
                            //check if it is redundant push message (KPNS)
                            boolean underConsulting = ViewManager.getInstance().isActivityAlive(ScreenShareActivity.class);
                            Log.d(_TAG, "IS CONSULTING UNDER GOING? --> "+ underConsulting);
                            if(underConsulting == false){
                                Intent i = new Intent(context, ConsultingPopUpActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("message", url);
                                context.startActivity(i);
                            }else{
                                //do nothing
                            }

                        }
                        Log.d(_TAG, "receive.MESSAGE is " + msg);

                        break;
                    default:
                        Log.d(_TAG, "receive.MESSAGE type --> UNKNOWN");
                        break;
                }
                if(needAck){
                    intent = new Intent("com.tta.push.intent.send.ACK");
                    intent.putExtra("appId", "0WW4I105s0");
                    intent.putExtra("transactionId", transactionId);
                    context.sendBroadcast(intent);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("com.ktpns.pa.receive.MESSAGE".equals(action)){ //version 1.x
            String msgId = intent.getStringExtra("id");
            String msg1 = intent.getStringExtra("alert");
            String msg2 = intent.getStringExtra("url");
            String msg3 = intent.getStringExtra("msg");
            Log.d(_TAG, "PUSH MESSAGE ID --> " + msgId);
            Log.d(_TAG, "PUSH MESSAGE alert --> " + msg1);
            Log.d(_TAG, "PUSH MESSAGE url --> " +msg2);
            Log.d(_TAG, "PUSH MESSAGE msg --> " + msg3);
        }
        /* on RECEIVE REQUEST TO RE-REGISTER referencing 2.1.5 doc */
        if("com.tta.push.intent.receive.RE_REGISTER".equals(action)){
            Log.d(_TAG, "RECEIVE REQUEST TO RE-REGISTER");
            //KPNSApis.register("0WW4I105s0", "TVConsulting01");
        }else if("com.tta.push.intent.receive.UNREGISTERED".equals(action)){
            //KPNSApis.register("0WW4I105s0", "TVConsulting01");
            Log.d(_TAG, "RECEIVE REQUEST TO UNREGISTERED");
        }
         /* on RECEIVE SERVICE_UNAVAILABLE referencing 2.1.5 doc */
        if ("com.tta.push.intent.receive.SERVICE_UNAVAILABLE".equals(action)) {
            String reason = intent.getStringExtra("reason");
            Log.d(_TAG,"SERVICE_UNAVAILABLE reason --> " + reason);
        }
    }
}
