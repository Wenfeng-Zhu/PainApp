package com.example.painapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.Map;

public class DialogUtils {
    private static TextView tipTextView;
    private static Dialog tipsDialog;
    private static Dialog passwordDialog;
    private static Dialog saveDialog;
    private static ImageView tipsImageView;
    public boolean add_correct = false;
    public boolean save_correct = false;
    private Toast toast;
    private String typename;
    private String patientid;
    private final static String correct = "zhu";


    public void showCompleteDialog(Context context, String msg){

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_tips,null );// 得到加载view
        tipTextView = v.findViewById(R.id.tips_textview);// 提示文字
        tipTextView.setText(msg);// 设置加载信息
        //tipsImageView = v.findViewById(R.id.tips_image);
        //tipsImageView.setBackgroundResource(R.drawable.popup_image);
        tipsDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        tipsDialog.setContentView(R.layout.dialog_tips);
        tipsDialog.setCancelable(true); // 是否可以按“返回键”消失
        tipsDialog.setCanceledOnTouchOutside(true); // 点击加载框以外的区域

        tipsDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        /**
         *将显示Dialog的方法封装在这里面
         */
        Window window = tipsDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        //lp.gravity = Gravity.CENTER;
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.AppTheme);
        tipsDialog.show();
    }

    public void savePasswordDialog(final Context context, final Map<String, Bitmap> map, final float proportion, final String filePath){
        final RWList RWList = new RWList();

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_password_save,null );

        saveDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        saveDialog.setContentView(R.layout.dialog_password_save);
        saveDialog.setCancelable(false); // 是否可以按“返回键”消失
        saveDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域

        saveDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

        Window window = saveDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        //lp.gravity = Gravity.CENTER;
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.AppTheme);
        saveDialog.show();

        final EditText password = v.findViewById(R.id.password_save);
        final EditText patientID = v.findViewById(R.id.editTextNumberSigned);

        Switch showswitch = v.findViewById(R.id.save_switch);
        showswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        Button cancel = v.findViewById(R.id.save_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_correct = true;
                saveDialog.dismiss();
            }
        });
        final Button confirm = v.findViewById(R.id.save_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                String string = password.getText().toString();
                if (string.equals(correct) &&  !patientID.getText().toString().isEmpty()){
                    patientid = patientID.getText().toString();
                    SaveJson saveJson = new SaveJson();
                    System.out.println("测试节点——————2————————");

                    saveJson.exportJson(map,proportion,filePath,patientid);
                    saveDialog.dismiss();
                    toast = Toast.makeText(saveDialog.getContext(), "File saved successfully", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    save_correct = true;
                }
                else if (string.isEmpty()){
                    save_correct = false;
                    toast = Toast.makeText(saveDialog.getContext(), "Patient ID cannot be empty!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else if (!string.equals(correct)){
                    save_correct = false;
                    toast = Toast.makeText(saveDialog.getContext(), "Wrong Password!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }

            }
        });
    }

    public void passwordDialog(final Context context){
        final RWList RWList = new RWList();

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_password,null );

        passwordDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        passwordDialog.setContentView(R.layout.dialog_password);
        passwordDialog.setCancelable(false); // 是否可以按“返回键”消失
        passwordDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域

        passwordDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

        Window window = passwordDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        //lp.gravity = Gravity.CENTER;
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.AppTheme);
        passwordDialog.show();


        final EditText typeName = v.findViewById(R.id.typeName);
        final EditText password = v.findViewById(R.id.password_add);

        Switch showswitch = v.findViewById(R.id.password_switch);
        showswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        Button cancel = v.findViewById(R.id.password_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_correct = true;
                passwordDialog.dismiss();
            }
        });
        final Button confirm = v.findViewById(R.id.password_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                String string = password.getText().toString();
                if (string.equals(correct) && !typeName.getText().toString().isEmpty()){
                    typename = typeName.getText().toString();

                    RWList.writeList(context,typename,"typeList");

                    add_correct = true;
                    passwordDialog.dismiss();
                    toast = Toast.makeText(passwordDialog.getContext(), "Add Successfully!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    Intent intent = new Intent(context,PainTypeActivity.class);
                    context.startActivity(intent);
                    Activity activity = (Activity)context;
                    activity.finish();

                }
                else if (typeName.getText().toString().isEmpty()){
                    add_correct = false;
                    toast = Toast.makeText(passwordDialog.getContext(), "Type Name cannot be empty!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else if (!string.equals(correct)){
                    add_correct = false;
                    toast = Toast.makeText(passwordDialog.getContext(), "Wrong Password!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }

            }
        });
    }
}
