package com.example.painapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.Locale;
import java.util.Map;


//Pop-ups, including prompt pictures, save pop-ups, add type pop-ups

public class DialogUtils {

    private Toast toast;



    //set password
    private final static String correct = "zhu";




    private static String chooseColor;
    private static int color;
    private ColorPickerActivity colorPickerActivity;

    protected void tipsDialog(Context context, String msg){

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_tips,null );
        TextView tipTextView = v.findViewById(R.id.tips_textview);
        tipTextView.setText(msg);

         Dialog tipsDialog = new Dialog(context, R.style.MyDialogStyle);
        tipsDialog.setContentView(R.layout.dialog_tips);
        tipsDialog.setCancelable(true);
        tipsDialog.setCanceledOnTouchOutside(true);

        tipsDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        Window window = tipsDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        //lp.gravity = Gravity.CENTER;
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.AppTheme);
        tipsDialog.show();
    }

    protected void savePasswordDialog(final Context context, final Map<String, Bitmap> map, final float proportion, final String filePath){


        final RWList RWList = new RWList();

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_password_save,null );

        final Dialog saveDialog = new Dialog(context, R.style.MyDialogStyle);
        saveDialog.setContentView(R.layout.dialog_password_save);
        saveDialog.setCancelable(false);
        saveDialog.setCanceledOnTouchOutside(false);

        saveDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

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

                    SaveJson saveJson = new SaveJson();

                    saveJson.exportJson(map,proportion,filePath,patientID.getText().toString());
                    saveDialog.dismiss();
                    toast = Toast.makeText(saveDialog.getContext(), "File saved successfully", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else if (string.isEmpty()){
                    toast = Toast.makeText(saveDialog.getContext(), "Patient ID cannot be empty!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else if (!string.equals(correct)){
                    toast = Toast.makeText(saveDialog.getContext(), "Wrong Password!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }

            }
        });
    }

    protected void addPasswordDialog(final Context context){

        final RWList RWList = new RWList();

        LayoutInflater inflater = LayoutInflater.from(context);
        final View v = inflater.inflate(R.layout.dialog_password_add,null );

        final Dialog addDialog = new Dialog(context, R.style.MyDialogStyle);
        addDialog.setContentView(R.layout.dialog_password_add);
        addDialog.setCancelable(false);
        addDialog.setCanceledOnTouchOutside(false);

        addDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        Window window = addDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        //lp.gravity = Gravity.CENTER;
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.AppTheme);
        addDialog.show();


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
                addDialog.dismiss();
            }
        });
        final Button confirm = v.findViewById(R.id.password_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                String string = password.getText().toString();
                if (string.equals(correct) && !typeName.getText().toString().isEmpty()){

                    RWList.writeList(context,typeName.getText().toString(),"typeList.txt");
                    RWList.writeList(context,chooseColor,"colorList.txt");
                    addDialog.dismiss();
                    toast = Toast.makeText(addDialog.getContext(), "Add Successfully!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    Intent intent = new Intent(context,PainTypeActivity.class);
                    context.startActivity(intent);
                    Activity activity = (Activity)context;
                    activity.finish();

                }
                else if (typeName.getText().toString().isEmpty()){
                    toast = Toast.makeText(addDialog.getContext(), "Type Name cannot be empty!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else if (!string.equals(correct)){
                    toast = Toast.makeText(addDialog.getContext(), "Wrong Password!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }

            }
        });
        Button displaycolor = (Button)v.findViewById(R.id.color);

        Button colorHex = (Button)v.findViewById(R.id.colorHex);
        colorPickerActivity = new ColorPickerActivity(context);
        colorHex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPickerActivity.show();
                LayoutInflater inflater = LayoutInflater.from(context);
                Button button = colorPickerActivity.getClose();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        colorPickerActivity.dismiss();
                        chooseColor = colorHex(colorPickerActivity.getColor());
                        color = colorPickerActivity.getColor();
                    }
                });
            }
        });
        colorPickerActivity.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                colorHex.setText(chooseColor);
                displaycolor.setBackgroundColor(color);
            }
        });
    }

    private String colorHex(int color){
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "#%02X%02X%02X%02X", a, r, g, b);

    }


}
