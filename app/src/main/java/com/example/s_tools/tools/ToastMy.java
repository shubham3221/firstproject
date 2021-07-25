package com.example.s_tools.tools;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s_tools.R;

public class ToastMy {

    //Length Of Toast
    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;


    public static void successToast(Context context,String msg,int length){
        successToast(context,msg,length,Gravity.BOTTOM);
    }

    public static void successToast(Context context,String msg,int length,int gravity){
            View layout = LayoutInflater.from(context).inflate(R.layout.layout_toast,null,false);
        RelativeLayout relContainer = layout.findViewById(R.id.relContainer);
        relContainer.setBackgroundResource(R.drawable.bg_success_toast);
        ImageView toastImg = layout.findViewById(R.id.toastImg);
        toastImg.setImageResource(R.drawable.fbtoast_done);
        toastImg.setColorFilter(Color.GREEN);
        TextView toastMsg = layout.findViewById(R.id.toastMsg);
        toastMsg.setText(msg);
        Toast toast = new Toast(context);
        toast.setDuration(length);
        //toast.setGravity(gravity,0,0);
        toast.setView(layout);
        toast.show();
    }


    public static void errorToast(Context context,String msg,int length){
        errorToast(context,msg,length,Gravity.BOTTOM);
    }

    public static void errorToast(Context context,String msg,int length,int gravity){
        View layout = LayoutInflater.from(context).inflate(R.layout.layout_toast,null,false);
        RelativeLayout relContainer = layout.findViewById(R.id.relContainer);
        relContainer.setBackgroundResource(R.drawable.bg_error_toast);
        ImageView toastImg = layout.findViewById(R.id.toastImg);
        toastImg.setImageResource(R.drawable.fbtoast_close_white);
        toastImg.setColorFilter(Color.RED);
        TextView toastMsg = layout.findViewById(R.id.toastMsg);
        toastMsg.setText(msg);
        Toast toast = new Toast(context);
        toast.setDuration(length);
//        toast.setGravity(gravity,0,0);
        toast.setView(layout);
        toast.show();
    }







}