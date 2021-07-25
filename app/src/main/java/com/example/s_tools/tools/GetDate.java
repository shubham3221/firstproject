package com.example.s_tools.tools;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class GetDate {

    public static final String TAG="//";
    public static final String TODAY="Today";
    public static final String YESTERDAY="Yesterday";
    public static final String DAYS_AGO7="7 Days Ago";
    public static final String DAYS_AGO30="30 Days Ago";

    public static String getdate(String modified) {
        String[] separated=modified.split(" ");

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date date=null;
        Calendar calendar=Calendar.getInstance(TimeZone.getDefault());
        try {
            date=format.parse(separated[0]);
            calendar.setTime(date);
//            Log.i(TAG, "getdate: "+calendar.get(Calendar.YEAR));
//            Log.i(TAG, "getdate: "+calendar.get(Calendar.DAY_OF_MONTH));
//            Log.i(TAG, "getdate: "+new SimpleDateFormat("MMM").format(calendar.getTime()));
        } catch (ParseException e) {
            Log.i(TAG, "GetDate class: exception: " + e.getMessage());
            e.printStackTrace();
        }
        return calendar.get(Calendar.DAY_OF_MONTH) + " " + new SimpleDateFormat("MMM").format(calendar.getTime());

    }

    public static String convertAmPm(String time) {
        SimpleDateFormat dateFormatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=null;
        try {
            date=dateFormatter.parse(time.replace("T", " "));
        } catch (ParseException e) {
            e.printStackTrace();
        }

// Get time from date
        SimpleDateFormat timeFormatter=new SimpleDateFormat("h:mm a");
        String displayValue=timeFormatter.format(date);

        return displayValue;
    }


    public static String covertTimeToTextForMyWebsite(String dataDate) {


        String convTime=null;

        String suffix="Ago";

        try {
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date pasTime=null;
            if (!dataDate.contains("T")) {
                pasTime=dateFormat.parse(dataDate.replace(" ", "T"));
            } else {
                pasTime=dateFormat.parse(dataDate);
            }


            Date nowTime=new Date();
            nowTime.setTime(nowTime.getTime() - 330 * 60000);
            long dateDiff=nowTime.getTime() - pasTime.getTime();

            long second=TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute=TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour=TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day=TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime=second + " Seconds " + suffix;
            } else if (minute < 60) {
                convTime=minute + " Minutes " + suffix;
            } else if (hour < 24) {
                convTime=hour + " Hours " + suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    convTime=(day / 360) + " Years " + suffix;
                } else if (day > 30) {
                    convTime=(day / 30) + " Months " + suffix;
                } else {
                    convTime=(day / 7) + " Week " + suffix;
                }
            } else if (day < 7) {
                convTime=day + " Days " + suffix;
            }

        } catch (ParseException e) {
            return dataDate;
        }

        return convTime;
    }

    public static String covertTimeToTextForWebsite(String dataDate) {


        String convTime=null;

        String prefix="";
        String suffix="Ago";

        try {
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date pasTime=null;
            if (!dataDate.contains("T")) {
                pasTime=dateFormat.parse(dataDate.replace(" ", "T"));
            } else {
                pasTime=dateFormat.parse(dataDate);
            }


            Date nowTime=new Date();
            nowTime.setTime(nowTime.getTime());
            long dateDiff=nowTime.getTime() - pasTime.getTime();

            long second=TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute=TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour=TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day=TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime=second + " Seconds " + suffix;
            } else if (minute < 60) {
                convTime=minute + " Minutes " + suffix;
            } else if (hour < 24) {
                convTime=hour + " Hours " + suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    convTime=(day / 360) + " Years " + suffix;
                } else if (day > 30) {
                    convTime=(day / 30) + " Months " + suffix;
                } else {
                    convTime=(day / 7) + " Week " + suffix;
                }
            } else if (day < 7) {
                convTime=day + " Days " + suffix;
            }

        } catch (ParseException e) {
            return dataDate;
        }
        return convTime;
    }
    public static String covertTimeToTextForGoogle(String dataDate) {


        String convTime=null;
        String specialData=null;

        String prefix="";
        String suffix="Ago";

        try {
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date pasTime=null;
            if (!dataDate.contains("T")) {
                pasTime=dateFormat.parse(dataDate.replace(" ", "T"));
            } else {
                pasTime=dateFormat.parse(dataDate);
            }


            Date nowTime=new Date();
            nowTime.setTime(nowTime.getTime());
            long dateDiff=nowTime.getTime() - pasTime.getTime();

            long second=TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute=TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour=TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day=TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime=second + " Seconds " + suffix;
            } else if (minute < 60) {
                convTime=minute + " Minutes " + suffix;
            } else if (hour < 24) {
                convTime=hour + " Hours " + suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    specialData=new SimpleDateFormat("MMM dd,yyyy h:mm a").format(pasTime);
                    convTime=(day / 360) + " Years " + suffix;
                } else if (day > 30) {
                    specialData=new SimpleDateFormat("MMM dd,yyyy h:mm a").format(pasTime);
                    convTime=(day / 30) + " Months " + suffix;
                } else {
                    convTime=(day / 7) + " Week " + suffix;
                }
            } else if (day < 7) {
                convTime=day + " Days " + suffix;
            }

        } catch (ParseException e) {
            return dataDate;
        }
        if (specialData!=null){
            return specialData;
        }
        return convTime;
    }
    public static String covertTimeToTextFor_premium_header(String dataDate) {


        String convTime=null;

        String prefix="";
        String suffix="Ago";

        try {
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date pasTime=null;
            if (!dataDate.contains("T")) {
                pasTime=dateFormat.parse(dataDate.replace(" ", "T"));
            } else {
                pasTime=dateFormat.parse(dataDate);
            }


            Date nowTime=new Date();
            nowTime.setTime(nowTime.getTime());
            long dateDiff=nowTime.getTime() - pasTime.getTime();

            long second=TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute=TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour=TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day=TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime=" Today ";
            } else if (minute < 60) {
                convTime=" Today ";
            } else if (hour < 24) {
                convTime=" Today ";
            } else if (day >= 7) {
                if (day > 360) {
                    convTime=(day / 360) + " Years " + suffix;
                } else if (day > 30) {
                    convTime=(day / 30) + " Months " + suffix;
                } else {
                    convTime=(day / 7) + " Week " + suffix;
                }
            } else if (day < 7) {
                convTime=day + " Days " + suffix;
            }

        } catch (ParseException e) {
            return dataDate;
        }

        return convTime;
    }
    public static Long getTimeLong(String dataDate) {
        Date pasTime=null;

        try {
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            if (!dataDate.contains("T")) {
                pasTime=dateFormat.parse(dataDate.replace(" ", "T"));
            } else {
                pasTime=dateFormat.parse(dataDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pasTime.getTime();
    }

    public static String getFormattedDate(String dataDate) {
        Date pasTime=null;

        try {
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            if (!dataDate.contains("T")) {
                pasTime=dateFormat.parse(dataDate.replace(" ", "T"));
            } else {
                pasTime=dateFormat.parse(dataDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long smsTimeInMilis=pasTime.getTime();


        Calendar smsTime=Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now=Calendar.getInstance();

        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return TODAY;
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return YESTERDAY;
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 7) {
            return DAYS_AGO7;
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 30) {
            return DAYS_AGO30;
        } else return null;
    }


}

