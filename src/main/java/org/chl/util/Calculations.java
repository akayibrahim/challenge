package org.chl.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Calculations {
    public static String calculateUntilDate(Date untilDate) {
        String untilDateStr = "LAST ";
        long diff = getDateDiff(DateUtil.getCurrentDatePlusThreeHour(), untilDate,TimeUnit.DAYS);
        long diffOfHours = getDateDiff(DateUtil.getCurrentDatePlusThreeHour(), untilDate, TimeUnit.HOURS);
        if (diff == 0) {
            untilDateStr += diffOfHours + " HOURS!";
        } else if (diff <= 7) {
            untilDateStr += diff + " DAYS!";
        } else {
            untilDateStr += diff / 7 + " WEEKS!";
        }
        return untilDateStr;
    }

    public static String calculateInsertTime(Date insertDate) {
        String insertDateStr = "";
        long diff = getDateDiff(insertDate, DateUtil.getCurrentDatePlusThreeHour(), TimeUnit.DAYS);
        if (diff == 0) {
            long diffOfHours = getDateDiff(insertDate, DateUtil.getCurrentDatePlusThreeHour(),TimeUnit.HOURS);
            if (diffOfHours == 0) {
                long diffOfMin = getDateDiff(insertDate, DateUtil.getCurrentDatePlusThreeHour(), TimeUnit.MINUTES);
                insertDateStr = diffOfMin + " MINUTES AGO";
            } else {
                insertDateStr = diffOfHours + " HOURS AGO";
            }
        } else {
            SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yyyy");
            insertDateStr = dt1.format(insertDate);
        }
        return insertDateStr;
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) {
        Date dt = DateUtil.getCurrentDatePlusThreeHour();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, -1);
        dt = c.getTime();
        calculateUntilDate(dt);
    }
}
