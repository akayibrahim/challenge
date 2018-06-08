package org.chl.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static Date covertToDate(String strDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date date = formatter.parse(strDate);
            return date;
        } catch (ParseException e) {
            // TODO error handling
        }
        return null;
    }
}
