package com.example.waldo.Utils;

import android.util.Log;

public class Utils {


    public static String getDate(String alldate){
        String months[] = {"Jan","Feb","Mar","Apr","May","Jun","July","Aug","Sep","Oct","Nov","Dec"};
        String[] val ={"01","02","03","04","05","06","07","08","09","10","11","12"};

        String month = alldate.substring(5,7);
        String year = alldate.substring(0,4);
        Log.e("month",""+month);
        Log.e("year",""+year);
        String date="";

        for(int j=0;j<months.length;j++){
            if(val[j].equalsIgnoreCase(month)){
                date = months[j] +" , " + year;
            }
        }
        return date;
    }
}
