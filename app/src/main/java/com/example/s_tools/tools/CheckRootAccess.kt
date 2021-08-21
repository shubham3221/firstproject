package com.example.s_tools.tools

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.io.File
import java.io.IOException

object CheckRootAccess {
    const val TEST_KEYS = "test-keys"
    const val PATHNAME = "/system/app/Superuser.apk"
    const val COMMAND = "su"
    const val BUSYBOX = "stericson.busybox"
    fun isRootedDevice(context: Context): Boolean {
        var rootedDevice = false
        val buildTags = Build.TAGS
        if (buildTags != null && buildTags.contains(TEST_KEYS)) {
            rootedDevice = true
        }

        // check if /system/app/Superuser.apk is present
        try {
            val file = File(PATHNAME)
            if (file.exists()) {
                rootedDevice = true
            }
        } catch (e1: Throwable) {
            //Ignore
        }

        //check if SU command is executable or not
        try {
            Runtime.getRuntime().exec(COMMAND)
            rootedDevice = true
        } catch (localIOException: IOException) {
            //Ignore
        }

        //check weather busy box application is installed
        val packageName = BUSYBOX //Package for busy box app
        val pm = context.packageManager
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            rootedDevice = true
        } catch (e: PackageManager.NameNotFoundException) {
            //App not installed
        }
        return rootedDevice
    }
}