package com.ktpoc.tvcomm.consulting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;

public class ConsultingPopUpActivity extends Activity {

    private PopupDialog mPopupDlg;
    private String mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewManager.getInstance().addActivity(this);
        ViewManager.getInstance().printActivityListSofar();


        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            mMessage = extra.getString("message");
            mPopupDlg = new PopupDialog(this,
                    "GiGA Genie 컨설팅 서비스",
                    "컨설팅 연결 요청이 왔습니다 연결하시겠습니까?",
                    leftClickListener,
                    rightClickListener);
            mPopupDlg.show();
        }

    }



    View.OnClickListener leftClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mPopupDlg.dismiss();
            Intent i = new Intent(ConsultingPopUpActivity.this, ScreenShareActivity.class);
            i.putExtra("message", mMessage);
            Log.d("[POPUP]", "message is " + mMessage);
            ConsultingPopUpActivity.this.finish();
            startActivity(i);

        }
    };

    View.OnClickListener rightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPopupDlg.dismiss();
//            ConsultingPopUpActivity.this.finish();
//            ViewManager.getInstance().removeActivity(ConsultingPopUpActivity.this);
        }
    };

    @Override
    protected void onPause(){
        super.onPause();
//        this.finish(); //안먹힘
//        ViewManager.getInstance().removeActivity(ConsultingPopUpActivity.this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        this.finish(); //안먹힘
        ViewManager.getInstance().removeActivity(ConsultingPopUpActivity.this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }


//    private Button mConfirmBtn, mCancelBtn;
//    private TextView mTextView;
//    private String mMessage;
//
//    private final String _TAG = "[SONG-POPUP ACTIVITY]";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_consulting_pop_up);
//
//        ViewManager.getInstance().addActivity(this);
//        ViewManager.getInstance().printActivityListSofar();
//
//        Bundle extra = getIntent().getExtras();
//        if (extra != null) {
//            mMessage = extra.getString("message");
//        }
//        setPopUpContent();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//    }
//
//    private void setPopUpContent(){
//        mConfirmBtn = (Button)findViewById(R.id.confirm_button);
//        mCancelBtn = (Button)findViewById(R.id.cancel_button);
//        mTextView = (TextView)findViewById(R.id.text_view);
//
//        mTextView.setText(mMessage);
//
//        mConfirmBtn.setOnClickListener(this);
//        mCancelBtn.setOnClickListener(this);
//    }
//
//    public void onClick(View v){
//        switch (v.getId()){
//            case R.id.confirm_button:
//
//                //TODO: url passing
//
//                Toast toast = Toast.makeText(this, "URL RECEIVED --> " + mMessage, Toast.LENGTH_SHORT);
//                toast.show();
//
//                //moveTaskToBack(true);
//                this.finish();
//                break;
//            case R.id.cancel_button:
//                Log.d(_TAG, "cancel button pressed");
//                //moveTaskToBack(true);
//                this.finish();
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    protected void onPause(){
//        //ViewManager.getInstance().removeActivity(this);
//        Log.d(_TAG, "onPause called");
//        super.onPause();
//
//    }
//
//    @Override
//    protected void onStop(){
//        //ViewManager.getInstance().removeActivity(this);
//        Log.d(_TAG, "onStop called");
//        super.onStop();
//
//    }

}
