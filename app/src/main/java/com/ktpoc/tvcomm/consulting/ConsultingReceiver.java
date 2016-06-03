package com.ktpoc.tvcomm.consulting;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import com.ktpns.lib.service.PushClientService;
import com.ktpns.lib.util.Constant;

import java.util.List;
import java.util.StringTokenizer;

public class ConsultingReceiver extends BroadcastReceiver {
//    public ConsultingReceiver() {
//    }

    private String _TAG = "[Consulting BR]";


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        //컨설팅 앱 실행 요청
        if (IntentAction.START_APP_FROM_MC.equals(action)) {
            try {
                Intent i = new Intent(context, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //전문가 리스트 요청
        if (IntentAction.SHOW_EXPERT_LIST_PAGE_FROM_MC.equals(action)) {
            try {
//                String category = intent.getStringExtra("category");

                //TODO: Do something with category and expert

                //TODO: set Extras and flag
                Intent i  = new Intent(context,ExpertsViewActivity.class);
                ViewManager.getInstance().setParamVal("CATEGORY", "expertlistpage");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            } catch (Exception e){
                e.printStackTrace();
            }
        }

        //전문가 스케줄 요청
        if (IntentAction.SHOW_SCHEDULE_PAGE_FROM_MC.equals(action)) {
            try {
                String expert = intent.getStringExtra("name");

                //TODO: Do something with expert
                Intent i = new Intent(context, ScheduleViewActivity.class);
                //TODO: set Extras and flag
                i.putExtra("expert", expert);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //menu 이동 명령
        if (IntentAction.SHOW_PAGE_FROM_MC.equals(action)) {
            try {
                String  actId = intent.getStringExtra("actId");
                Log.d(_TAG, "menu 명령 income with " + actId);
                if(actId.contains("502")){ // back key event
                    ViewManager.getInstance().removeActivity(ViewManager.getInstance().getCurrentActivity());
//                    int curIndex = ViewManager.getInstance().getCurrentActivityIdx();
//                    if (curIndex == 0 ){
//
//                        //TODO: 종료 합니다 && noti to MC
////                        Toast toast = Toast.makeText(context, "컨설팅 서비스를 종료합니다", Toast.LENGTH_LONG);
////                        toast.show();
//                        ViewManager.getInstance().removeActivity(ViewManager.getInstance().getActivity(curIndex), ViewManager.getInstance().getActivity(curIndex).this);
//
//                        Log.d(_TAG, "send broadcast to M.C from finish");
//
//                    }else {
//                        Activity currentActivity = ViewManager.getInstance().getCurrentActivity();
//                        ViewManager.getInstance().removeActivity(currentActivity, currentActivity.this);
//                        currentActivity.finish();
//                    }

                }else if (actId == "504"){ //mymenu

                    Intent i = new Intent(context, MyMenuActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);

                }else{
                    ;
                }
                //TODO: process activity index with actId
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //종료요청
        if (IntentAction.FINISH_FROM_MC.equals(action)) {
            try {
                Intent i = new Intent(IntentAction.SEND_NOTI_FROM_CONSULTING);
                i.putExtra("consultingState", "exit");
                context.sendBroadcast(i);
                Log.d(_TAG, "send broadcast to M.C from finish");

                Toast toast = Toast.makeText(context, "컨설팅 서비스를 종료합니다", Toast.LENGTH_LONG);
                toast.show();

                ViewManager.getInstance().removeAndFinishAllActivity();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (IntentAction.STOP_FROM_MC.equals(action)) {
            try {
                //service로 가든 main activity 로 가든..
                //TODO: destroy consulting app
                Intent i = new Intent("com.ktpoc.tvcomm.consulting.noti");
                i.putExtra("consultingState", "exit");
                context.sendBroadcast(i);
                Log.d(_TAG, "send broadcast to M.C from stop");

                Toast toast = Toast.makeText(context, "컨설팅 서비스를 종료합니다", Toast.LENGTH_LONG);
                toast.show();

                ViewManager.getInstance().removeAndFinishAllActivity();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* is it right ???  */
        if (IntentAction.SERVICE_CHECK_FROM_MC.equals(action)) {
            try {
                if (isRunningService(context, PushClientService.class) == false) {
                    context.startService(new Intent(context, PushClientService.class).setAction(Constant.ACTION_START_SERVICE));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(intent.getAction().equals("com.insun.send.msg.songsong")){
            //푸쉬 메세지 테스트용
            Intent i = new Intent(context, ConsultingPopUpActivity.class);
            i.putExtra("message", "https://www.naver.com");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }



    public static boolean isRunningService(Context context, Class<?> cls) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (info != null) {
            for(ActivityManager.RunningServiceInfo serviceInfo : info) {
                ComponentName compName = serviceInfo.service;
                String className = compName.getClassName();

                if(className.equals(cls.getName())) {
                    isRunning = true;
//                    String testString = "AM_RUNNING_SERVICE is KPNS SERVICE";

//                    Toast toast = Toast.makeText(context, testString, Toast.LENGTH_SHORT);
//                    toast.show();
                    break;
                }
            }
        }
        return isRunning;
    }
}
