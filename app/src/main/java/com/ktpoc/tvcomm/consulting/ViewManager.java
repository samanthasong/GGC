package com.ktpoc.tvcomm.consulting;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.DefaultDatabaseErrorHandler;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by insam on 2016. 5. 13..
 */

public class ViewManager extends Application{
    private static ViewManager actListInstance = null;
    private ArrayList<Activity> mArrayList;
    private int mIdx;
    private String mCategory;
    private String mExpert;
    private String mRoomId;
    private String mHome;
    private String _TAG = "[VIEW MANAGER]";

    public static synchronized ViewManager getInstance() {
        if(actListInstance == null)
            actListInstance = new ViewManager();
        return actListInstance;
    }

    private ViewManager() {
        this.mArrayList = new ArrayList<>();
        mIdx = -1;
        mCategory = "";
        mExpert = "";
        mRoomId = "";
        mHome = "";
    }

    public void addActivity(Activity activity){
        this.mArrayList.add(activity);
        mIdx++;
    }

    public void removeActivity(Activity activity){
        Log.d(_TAG, "remove this [ " + mIdx +" ] activity" +activity.getClass().getName().toString());

        activity.finish();
        this.mArrayList.remove(activity);
        mIdx--;
    }


    public void removeAndFinishAllActivity(){
        for(int i = 0; i < mArrayList.size(); i++){
            mArrayList.get(i).moveTaskToBack(true);
            mArrayList.get(i).finish();
            mArrayList.remove(mArrayList.get(i));
//
//            Intent intent = new Intent("com.ktpoc.tvcomm.consulting.noti");
//            intent.putExtra("consultingState", "exit");
//            Log.d(_TAG, "send broadcast exit extra to M.C");
//            sendBroadcast(intent);
        }
        mIdx = -1;
    }

    public void printActivityListSofar() {
        for (int i = 0; i < mArrayList.size(); i++) {
            Log.d("[ACTIVITY LIST]", "[" + i + "]" + mArrayList.get(i).getComponentName().toString());
        }
    }

    public boolean isActivityAlive(Class<?> cls){
        String queryClass = cls.getName().toString();
        boolean result = false;
        for(int i = 0; i < mArrayList.size(); i++){
            String className = mArrayList.get(i).getClass().getName().toString();
            if(className.equals(queryClass)){
                result = true;
            }
        }
        return result;
    }

    public void launchActivity(Context context, Class<?> cls, Bundle extra){
        Intent i = new Intent(context, cls);
        i.putExtras(extra);
        context.startActivity(i);
    }

    public Activity getActivity(int i){
        return mArrayList.get(i);
    }

    public int getCurrentActivityIdx(){
        return mIdx;
    }

    public Activity getCurrentActivity(){
        return mArrayList.get(mIdx);
    }

    public String getParamVal(String key){
        String value = "";
        switch (key){
            case "CATEGORY":
                value = mCategory;
                break;
            case "EXPERT":
                value = mExpert;
                break;
            case "ROOMID":
                value = mRoomId;
                break;
            case "HOME":
                value = mHome;
            default:
                break;
        }
        return value;
    }

    public void setParamVal(String key, String value){
        switch (key){
            case "CATEGORY":
                mCategory = value;
                break;
            case "EXPERT":
                mExpert = value;
                break;
            case "ROOMID":
                mRoomId = value;
                break;
            case "HOME":
                mHome = value;
            default:
                break;
        }
    }
}
