package com.example.s_tools;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ContactUs extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        textView = findViewById(R.id.contacttext);
//        imp_msg1.setText("\u25CF" + " Upcomming Updates Only On evrsh.blogspot.com");
//        imp_msg2.setText("\u25CF" + " Any Query? Message Us.");
//        imp_msg3.setText("\u25CF" + " Use VPN if Something Don't Work (ex: Initializing,Movies,Generate Token)");
//
        textView.setText("\u25CF" + " Upcomming Updates Only On evrsh.blogspot.com \n \\u25CF\" + \" Any Query? Message Us. \n" +
                "");
    }
}