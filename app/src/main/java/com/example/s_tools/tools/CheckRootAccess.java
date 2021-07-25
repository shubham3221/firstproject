package com.example.s_tools.tools;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.File;
import java.io.IOException;

public class CheckRootAccess {

    public static final String TEST_KEYS="test-keys";
    public static final String PATHNAME="/system/app/Superuser.apk";
    public static final String COMMAND="su";
    public static final String BUSYBOX="stericson.busybox";

    public static boolean isRootedDevice(Context context) {

        boolean rootedDevice = false;
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains(TEST_KEYS)) {
            rootedDevice = true;
        }

        // check if /system/app/Superuser.apk is present
        try {
            File file = new File(PATHNAME);
            if (file.exists()) {
                rootedDevice = true;
            }
        } catch (Throwable e1) {
            //Ignore
        }

        //check if SU command is executable or not
        try {
            Runtime.getRuntime().exec(COMMAND);
            rootedDevice = true;
        } catch (IOException localIOException) {
            //Ignore
        }

        //check weather busy box application is installed
        String packageName =BUSYBOX; //Package for busy box app
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            rootedDevice = true;
        } catch (PackageManager.NameNotFoundException e) {
            //App not installed
        }

        return rootedDevice;
    }
}
