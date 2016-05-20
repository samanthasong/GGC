package com.ktpoc.tvcomm.consulting;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ktpns.lib.service.PushClientService;
import com.ktpns.lib.util.Constant;

import java.util.List;

public class ConsultingReceiver extends BroadcastReceiver {
//    public ConsultingReceiver() {
//    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        //컨설팅 앱 실행 요청
        if ("com.ktpoc.tvcomm.consulting.start".equals(action)) {
            try {
                Log.d("HOHO", "start msg received");
                //String stateCode = intent.getStringExtra("stateCode");
                //TODO: Do something with stateCode
                Intent i = new Intent(context, MainActivity.class);
                //TODO: set Extras and flag
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //전문가 리스트 요청
        if ("com.ktpoc.tvcomm.consulting.information".equals(action)) {
            try {
                String category = intent.getStringExtra("category");

                //TODO: Do something with category and expert
                Intent i = new Intent(context, com.ktpoc.tvcomm.consulting.ExpertsViewActivity.class);
                //TODO: set Extras and flag
//                i.putExtra("category", category);
//                i.putExtra("expert", expert);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //전문가 스케줄 요청
        if ("com.ktpoc.tvcomm.consulting.schedule".equals(action)) {
            try {
                String expert = intent.getStringExtra("name");

                //TODO: Do something with expert
                Intent i = new Intent(context, com.ktpoc.tvcomm.consulting.ScheduleViewActivity.class);
                //TODO: set Extras and flag
                i.putExtra("expert", expert);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //menu 이동 명령
        if ("com.ktpoc.tvcomm.consulting.menu".equals(action)) {
            try {
                String actId = intent.getStringExtra("actId"); //(이전메뉴 502, 마이 메뉴 504 협의중

                //service로 가든 main activity 로 가든..

                //TODO: process activity index with actId
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if ("com.ktpoc.tvcomm.finish.consult".equals(action)) {
            try {
                //service로 가든 main activity 로 가든..
                //TODO: destroy consulting app
                ViewManager.getInstance().removeAllActivity();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /* is it right ???  */
        if ("com.ktpoc.tvcomm.service.check".equals(action)) {
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
            Intent i = new Intent(context, com.ktpoc.tvcomm.consulting.ConsultingPopUpActivity.class);
            i.putExtra("message", "https://www.naver.com");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public static boolean isRunningService(Context context, Class<?> cls) {
        boolean isRunning = false;

//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();


//        if (processInfos != null) {
//            for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
////                if (processInfo.getClass().getName()!=null) {
//
//                    String str = processInfo.processName.getClass().toString();
//                    Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
//                    toast.show();
//                    return isRunning;
//               // }
//            }
//        }
//        return isRunning;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (info != null) {
            for(ActivityManager.RunningServiceInfo serviceInfo : info) {
                ComponentName compName = serviceInfo.service;
                String className = compName.getClassName();

                if(className.equals(cls.getName())) {
                    isRunning = true;
                    String testString = "AM_RUNNING_SERVICE is KPNS SERVICE";

                    Toast toast = Toast.makeText(context, testString, Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
            }
        }
        return isRunning;
    }
}
