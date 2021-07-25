package com.example.s_tools.tools;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    public static boolean isvalidName(String name,Context context){
        if (name.isEmpty() || name.length() < 3 || name.contains(" ")){
            Toast.makeText(context, "Invalid Name", Toast.LENGTH_SHORT).show();
            return false;
        }return true;
    }
    public static boolean isValidEmailId(String email,Context context){
        boolean matches=Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{4}|[\\w-]{4,}))@" + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,3})$").matcher(email).matches();
        if (matches){
            return true;
        }
        Toast.makeText(context, "Invlaid Email", Toast.LENGTH_SHORT).show();
        return matches;
    }
    public static boolean isValidPassword(String string, Context context){
        String PATTERN;
            //PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
            PATTERN = "^[a-zA-Z@#$%0-10]\\w{4,12}$";




        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(string);
        if (!matcher.matches()){
            Toast.makeText(context, "Invalid Password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return matcher.matches();
    }
    public static boolean isPassMatched(String pass,String confirmpass,Context context){
        if (pass.contains(confirmpass)){
            return true;
        }
        Toast.makeText(context, "Password not matched", Toast.LENGTH_SHORT).show();
        return false;
    }

}
