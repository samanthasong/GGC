package com.ktpoc.tvcomm.consulting;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by insam on 2016. 5. 17..
 */

public class PopupDialog extends Dialog{

    private TextView mTitleView;
    private TextView mContentView;
    private Button mLeftButton;
    private Button mRightButton;
    private String mTitle;
    private String mContent;
    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;

    public PopupDialog(Context context, String title,View.OnClickListener singleListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle = title;
        this.mLeftClickListener = singleListener;
    }

    public PopupDialog(Context context, String title, String content, View.OnClickListener leftListener, View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        this.mTitle = title;
        this.mContent = content;
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
//        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
//        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        lpWindow.dimAmount = 0.0f;
//        getWindow().setAttributes(lpWindow);


        setContentView(R.layout.activity_consulting_pop_up);
        mTitleView = (TextView) findViewById(R.id.text_view);
        mContentView = (TextView) findViewById(R.id.txt_content);
        mLeftButton = (Button) findViewById(R.id.confirm_button);
        mRightButton = (Button) findViewById(R.id.cancel_button);

        mTitleView.setText(mTitle);
        mContentView.setText(mContent);

        if (mLeftClickListener != null && mRightClickListener != null) {
            mLeftButton.setOnClickListener(mLeftClickListener);
            mRightButton.setOnClickListener(mRightClickListener);
        } else if (mLeftClickListener != null&& mRightClickListener == null) {
            mLeftButton.setOnClickListener(mLeftClickListener);
        } else {

        }
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);


}

