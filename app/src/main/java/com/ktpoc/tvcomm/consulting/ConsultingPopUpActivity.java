package com.ktpoc.tvcomm.consulting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class ConsultingPopUpActivity extends Activity {

    private PopupDialog mPopupDlg;
    private String mMessage;
    private String _TAG = "[POPUP]";
    private boolean isConfirmBtn;

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
                    "컨설팅 연결 요청이 왔습니다. 수락하시겠어요? "+mMessage,
                    leftClickListener,
                    rightClickListener);
            mPopupDlg.show();
            isConfirmBtn = true;
        }
    }

    View.OnClickListener leftClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) { //confirm

            mPopupDlg.dismiss();

            //1. calling GiGA Conference Application TO CONNECT
            Intent intent_to_gcf = new Intent(IntentAction.REQ_CONFERENCE_START_FROM_CONSULTING);
            sendBroadcast(intent_to_gcf);

            //TODO: MC한테 상태 정보 업데이트
            Intent intent_to_mc = new Intent(IntentAction.SEND_NOTI_FROM_CONSULTING);
            intent_to_mc.putExtra("consultingState", "connected");
            sendBroadcast(intent_to_mc);

            //2. this app execute CONNECTION
//            Intent i = new Intent(ConsultingPopUpActivity.this, ScreenShareActivity.class);
//            i.putExtra("message", mMessage);
//            startActivity(i);
//            Log.d(_TAG, "message is " + mMessage);

            //ConsultingPopUpActivity.this.finish();
            checkFinalPage();
            ViewManager.getInstance().removeActivity(ConsultingPopUpActivity.this);

        }
    };

    View.OnClickListener rightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) { //cancellation
            mPopupDlg.dismiss();

//            ConsultingPopUpActivity.this.finish();
            checkFinalPage();
            ViewManager.getInstance().removeActivity(ConsultingPopUpActivity.this);
        }
    };

    private void checkFinalPage(){
        int currentActivityIdx = ViewManager.getInstance().getCurrentActivityIdx();

        if(currentActivityIdx == 0){

            Log.d(_TAG, "앱이 종료되어야 함");
            Toast toast = Toast.makeText(this, "컨설팅 서비스를 종료합니다", Toast.LENGTH_LONG);
            toast.show();

            moveTaskToBack(true);

            Intent i = new Intent(IntentAction.SEND_NOTI_FROM_CONSULTING);
            i.putExtra("consultingState", "exit");
            Log.d(_TAG, "send broadcast exit extra to M.C");
            sendBroadcast(i);

        }else if(currentActivityIdx > 0){
            //nothing to do
        }else{
            Log.d(_TAG, "current index is ackward : "+currentActivityIdx);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_CENTER:  // 확인 키
                if(isConfirmBtn) { // leftButtonEventListener = OK
                    mPopupDlg.dismiss();

                    //1. calling GiGA Conference Application TO CONNECT
                    Intent intent_to_gcf = new Intent(IntentAction.REQ_CONFERENCE_START_FROM_CONSULTING);
                    sendBroadcast(intent_to_gcf);

                    //TODO: MC한테 상태 정보 업데이트
                    Intent intent_to_mc = new Intent(IntentAction.SEND_NOTI_FROM_CONSULTING);
                    intent_to_mc.putExtra("consultingState", "connected");
                    sendBroadcast(intent_to_mc);

                   // ConsultingPopUpActivity.this.finish();
                    checkFinalPage();
                    ViewManager.getInstance().removeActivity(ConsultingPopUpActivity.this);

                }else { // rightButtonEventListener = CANCEL
                    mPopupDlg.dismiss();

                //    ConsultingPopUpActivity.this.finish();
                    checkFinalPage();
                    ViewManager.getInstance().removeActivity(ConsultingPopUpActivity.this);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT: // 오른쪽 키

                if(isConfirmBtn == true){
                    isConfirmBtn = false;
                }else{
                    isConfirmBtn = true;
                }
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                if(isConfirmBtn == true){
                    isConfirmBtn = false;
                }else{
                    isConfirmBtn = true;
                }
                break;
            case KeyEvent.KEYCODE_F1:
                mPopupDlg.dismiss();

              //  ConsultingPopUpActivity.this.finish();
                checkFinalPage();
                ViewManager.getInstance().removeActivity(ConsultingPopUpActivity.this);
            default:
                break;
        }
        return false;
    }



    @Override
    protected void onPause(){
        super.onPause();
        Log.d(_TAG, "onPause()");
    }

    @Override
    protected void onStop(){
        super.onStop();

        Log.d(_TAG, "onStop()");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(_TAG, "onDestroy()");
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
