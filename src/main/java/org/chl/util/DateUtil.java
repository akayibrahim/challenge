package org.chl.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static Date covertToDate(String strDate) {
        try {
            if (strDate.length() == 16) {
                return toDate(strDate);
            } else if (strDate.length() == 10) {
                strDate += " 00:00";
                return toDate(strDate);
            }
        } catch (ParseException e) {
            // TODO error handling
        }
        return null;
    }

    private static Date toDate(String strDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        Date date = formatter.parse(strDate);
        return date;
    }

    public static String toString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        return formatter.format(date);
    }
}
