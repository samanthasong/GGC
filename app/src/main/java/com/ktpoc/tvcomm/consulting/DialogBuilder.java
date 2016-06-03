package com.ktpoc.tvcomm.consulting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.WindowManager;

/**
 * Created by insam on 2016. 5. 30..
 */
public class DialogBuilder {

    private boolean mRetVal;


    public boolean exitAppDialog(Context context){

        mRetVal = false;
        final AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(context);
        dlgBuilder.setMessage("GiGA Genie 컨설팅 서비스를 종료 하시겠습니까")
                .setCancelable(false).setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRetVal = true;
                        dialog.dismiss();
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mRetVal = false;
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = dlgBuilder.create();
        alertDialog.setTitle("GiGA Genie 컨설팅 서비스");
//        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();

        return mRetVal;
    }

    public boolean registerServiceDialog(Context context){
        mRetVal = true;
        final AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(context);
        dlgBuilder.setMessage("GiGA Genie 컨설팅 서비스에 가입하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mRetVal = true;
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                mRetVal = false;
            }
        });
        AlertDialog alertDialog = dlgBuilder.create();
        alertDialog.setTitle("GiGA Genie 컨설팅 서비스");
        alertDialog.show();
        return  mRetVal;
    }


}
