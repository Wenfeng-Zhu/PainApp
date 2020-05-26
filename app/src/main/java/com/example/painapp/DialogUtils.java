package com.example.painapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogUtils {
    private static TextView tipTextView;
    private static Dialog ProgressDialog;
    private static ImageView tipsImageView;
    public void showCompleteDialog(Context context, String msg){

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_tips,null );// 得到加载view
        tipTextView = v.findViewById(R.id.tips_textview);// 提示文字
        tipTextView.setText(msg);// 设置加载信息
        //tipsImageView = v.findViewById(R.id.tips_image);
        //tipsImageView.setBackgroundResource(R.drawable.prompt_image);


        ProgressDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        ProgressDialog.setContentView(R.layout.dialog_tips);
        ProgressDialog.setCancelable(true); // 是否可以按“返回键”消失
        ProgressDialog.setCanceledOnTouchOutside(true); // 点击加载框以外的区域

        ProgressDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        /**
         *将显示Dialog的方法封装在这里面
         */
        Window window = ProgressDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        //lp.gravity = Gravity.CENTER;
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.AppTheme);
        ProgressDialog.show();
    }
}
