package com.sconnecting.driverapp.base;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by TrungDao on 8/2/16.
 */

public class DateTimeHelper {


    public static Boolean isToday(Date date){
        return(date.getYear() == new Date().getYear() && date.getMonth() == new Date().getMonth() && date.getDate() == new Date().getDate() );

    }
    public static Boolean isCurrentYear(Date date){

        return date.getYear() == new Date().getYear();

    }


    public static Boolean isCurrentMonth(Date date){

        return (date.getYear() == new Date().getYear() && date.getMonth() == new Date().getMonth());

    }


    public static Boolean isYesterday(Date date){

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return isToday(cal.getTime());
    }


    public static Boolean isTomorrow(Date date){

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return isToday(cal.getTime());
    }

    public static Boolean isExpired(Date date, Integer extraMinutes){

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        long elapsedTime = new Date().getTime() - date.getTime();

        return elapsedTime < 0 && Math.abs(elapsedTime) >= (extraMinutes*60*1000 );

    }

    public static Boolean isSince(Date date,Integer extraMinutes){

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        long elapsedTime = new Date().getTime() - date.getTime();

        return  elapsedTime < (extraMinutes*60*1000 );

    }


    public static Boolean isNow(Date date,Integer extraMinutes){

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        long elapsedTime = new Date().getTime() - date.getTime();

        return (elapsedTime < 0 && Math.abs(elapsedTime) <= (3*60*1000))
                || (Math.abs(elapsedTime) <= (extraMinutes*60*1000 ));


    }


    public static String toString(Date date,String strFormat){
        SimpleDateFormat format = new SimpleDateFormat(strFormat);

        String strDate = format.format(date);

        return strDate;
    }

    public static String toVietnamese(Date date){

        String strPickupTime = "";
        if(isNow(date,5)){

            strPickupTime = "ngay bây giờ";

        }else{

            long seconds = (date.getTime() - new Date().getTime())/1000;


            if ((date.getDate() == new Date().getDate()) && ( (seconds > 0) || Math.abs(seconds)<=60)){

                long hours =  seconds / 3600;
                long minutes =  Math.round((seconds % 3600) / 60);

                if( hours >= 1){
                    strPickupTime = String.format("sau %d giờ, %d phút", hours, minutes );
                }else{
                    strPickupTime =  String.format("sau %d phút", minutes ) ;
                }

            }else {

                String strDate = toString(date,"HH:mm");

                if(isToday(date)){
                    strPickupTime = strDate + " hôm nay";

                }else{
                    String strDate2 = toString(date,"dd/MM");
                    strPickupTime = strDate + " ngày " + strDate2;
                }

            }
        }

        return strPickupTime;
    }


}
