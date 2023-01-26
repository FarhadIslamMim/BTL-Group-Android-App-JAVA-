package com.oshnisoft.erp.btl.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * Created by tariqul.islam on 04-03-16.
 */

public class DateTimeUtils {

    /**
     * Example: 2015-09-07 02:56:03 PM
     */
    public static String FORMAT1 = "yyyy-MM-dd hh:mm:ss a";

    /**
     * Example: 07/09/2015
     */
    public static String FORMAT2 = "dd/MM/yyyy";

    /**
     * Example: Sep 07, 2015
     */
    public static String FORMAT3 = "MMM dd, yyyy";

    /**
     * Example: Monday
     */
    public static String FORMAT4 = "EEEE";

    /**
     * Example: 2015-10-11
     */
    public static String FORMAT5 = "yyyy-MM-dd";

    /**
     * Example: 2015-10-11 11:35:00
     */
    public static String FORMAT6 = "yyyy-MM-dd HH:mm:ss";

    /**
     * Example: Sep 07
     */
    public static String FORMAT7 = "MMM dd";


    public static String FORMAT_APP_DEFAULT = "dd-MM-yyyy HH:mm:ss";

    public static String FORMAT9 = "dd-MM-yyyy";

    public static String FORMAT10 = "dd";
    public static String FORMAT_TIME = "hh:mm";
    public static String FORMAT11 = "MM";

    public static String FORMAT12 = "MMM dd, yyyy HH:mm:ss";


    /**
     * Example: 30-10-2015-06-05-23
     */
    public static String FORMAT_FOR_SERVICE = "dd-MM-yyyy-HH-mm-ss";
    public static String FORMAT_FOR_ZONE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static String[] WEEK_DAY = {"sun", "mon", "tue", "wed", "thu", "fri", "sat"};
    public static String[] WEEK_DAY_1 = {"", "SU", "MO", "TU", "WE", "TH", "FR", "SA"};

    public static String[] MONTH_NAME = {
            "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"
    };
    public static String[] MONTH_NAME_SHORT = {
            "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL",
            "AUG", "SEP", "OCT", "NOV", "DEC"
    };


    public static String changeDateTimeFormat(String dateTime, String currentFormat, String expectedFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(currentFormat, Locale.ENGLISH);
        Date testDate = null;
        try {
            testDate = sdf.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
//            ErrorTrackingUtils.saveErrorToSend(ex);
        }
        SimpleDateFormat formatter = new SimpleDateFormat(expectedFormat, Locale.ENGLISH);
        String newFormat = formatter.format(testDate);
        return newFormat;
    }

    public static String[] monthInString = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


//    public static long convertToMilliseconds(String dateTime, String currentFormat){
//        SimpleDateFormat sdf = new SimpleDateFormat(currentFormat, Locale.ENGLISH);
//        try {
//            Date testDate = sdf.parse(dateTime);
//            return testDate.getTime();
//        }catch(Exception ex){
//            ex.printStackTrace();
//            ErrorTrackingUtils.saveErrorToSend(ex);
//        }
//        return 0;
//    }

    public static String getTodayDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = new Date(System.currentTimeMillis());
        String todaysDate = dateFormat.format(date);
        return todaysDate;
    }

    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_TIME, Locale.ENGLISH);
        Date date = new Date(System.currentTimeMillis());
        String todaysDate = dateFormat.format(date);
        return todaysDate;
    }

//    public static String getTodaysDay(){
//        String weekdays[] = new DateFormatSymbols(Locale.ENGLISH).getWeekdays();
//        InvoiceDetail weekdays[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)];
//    }


    public static int getValueFromLetter(String l) {
        l = l.toLowerCase();
        if (l.equals("a")) return 1;
        else if (l.equals("b")) return 2;
        else if (l.equals("c")) return 3;
        else if (l.equals("d")) return 4;
        else if (l.equals("e")) return 5;
        else if (l.equals("f")) return 6;
        else if (l.equals("g")) return 7;
        else if (l.equals("h")) return 8;
        else if (l.equals("i")) return 9;
        else if (l.equals("j")) return 10;
        else if (l.equals("k")) return 11;
        else if (l.equals("l")) return 12;
        else if (l.equals("m")) return 13;
        else if (l.equals("n")) return 14;
        else if (l.equals("o")) return 15;
        else if (l.equals("p")) return 16;
        else if (l.equals("q")) return 17;
        else if (l.equals("r")) return 18;
        else if (l.equals("s")) return 19;
        else if (l.equals("t")) return 20;
        else if (l.equals("u")) return 21;
        else if (l.equals("v")) return 22;
        else if (l.equals("w")) return 23;
        else if (l.equals("x")) return 24;
        else if (l.equals("y")) return 25;
        else if (l.equals("z")) return 26;
        else return 27;
    }

    public static String getToday(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }


    public static String getNextDay(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +1);
        return dateFormat.format(calendar.getTime());
    }

    public static String getNextTwoDay(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +2);
        return dateFormat.format(calendar.getTime());
    }

    public static String getYesterday(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return dateFormat.format(calendar.getTime());
    }

    public static String getPrevTwoDayDate(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -2);
        return dateFormat.format(calendar.getTime());
    }

    public static String getWeeksFirstDay(String format, int weekStartDay) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.setFirstDayOfWeek(weekStartDay);
        c.set(Calendar.DAY_OF_WEEK, weekStartDay);
        return dateFormat.format(c.getTime());
    }

    public static String getPrevWeeksFirstDay(String format, int weekStartDay) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.setFirstDayOfWeek(weekStartDay);
        c.set(Calendar.DAY_OF_WEEK, weekStartDay);
        c.add(Calendar.WEEK_OF_MONTH, -1);
        return dateFormat.format(c.getTime());
    }

    public static String getPrevWeeksLastDay(String format, int weekEndDay) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.setFirstDayOfWeek(weekEndDay);
        c.set(Calendar.DAY_OF_WEEK, weekEndDay);
        return dateFormat.format(c.getTime());
    }

    public static String getMonthsFirstDay(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
//        c.setFirstDayOfWeek(Calendar.SATURDAY);
        c.set(Calendar.DAY_OF_MONTH, 1);
//        Log.i(tag, "Date: " + dateFormat.format(c.getTime()));
        return dateFormat.format(c.getTime());
    }

    public static String getPrevMonthsFirstDay(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
//        c.setFirstDayOfWeek(Calendar.SATURDAY);
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 1);
//        Log.i(tag, "Date: " + dateFormat.format(c.getTime()));
        return dateFormat.format(c.getTime());
    }

    public static String getPrevMonthsLastDay(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
//        c.setFirstDayOfWeek(Calendar.SATURDAY);
//        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 0);
//        Log.i(tag, "Date: " + dateFormat.format(c.getTime()));
        return dateFormat.format(c.getTime());
    }

    public static String getTodaysDateTime(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format); // "yyyy-MM-dd HH:mm:ss"
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        String todaysDate = dateFormat.format(date);
        //  Log.d(TAG, todaysDate + "");
        return todaysDate;
    }

    /**
     * Gets previous or past day, based on current day.
     * Use this method to calculate the date which is x days past or after today.
     * To calculate the date which is 5 day before today: getDateFromToday(-5, MMM dd, yyyy)
     *
     * @param numberOfDays -ve or +ve for previous or later days.
     * @param format
     * @return the calculated date
     */
    public static String getDateFromToday(int numberOfDays, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, numberOfDays);
        return dateFormat.format(calendar.getTime());
    }

    public static Date getSystemDate() {
        return new Date();
    }

    public static long getSystemTimeInMillis() {
        return new Date().getTime();
    }

//    public static String getServerTime(Context context, String format){
//        Calendar c = Calendar.getInstance();
//        long timeDiff = SharedPrefUtils.getTimeDiff(context);
//        SystemUtils.log("time diff: " + timeDiff + ", current: " + (new Date().getTime() - timeDiff));
//        c.setTimeInMillis(new Date().getTime() - timeDiff);
//        String str = convertSecondsToDate((c.getTimeInMillis() / 1000), format);
//        return str;
//    }

//    public static long getServerTimeMillis(Context context){
//        String format = FORMAT_APP_DEFAULT;
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(new Date().getTime() - (SharedPrefUtils.getTimeDiff(context)));
//        String str = convertSecondsToDate((c.getTimeInMillis() / 1000), format);
//        return convertToMilliseconds(str, format);
//    }

    public static boolean isSameDay(Date firstDate, Date lastDate) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(firstDate);
        cal2.setTime(lastDate);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isSameMonth(Date firstDate, Date lastDate) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(firstDate);
        cal2.setTime(lastDate);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }

    public static class TimeAgo {

        public static final List<Long> times = Arrays.asList(
                TimeUnit.DAYS.toMillis(365),
                TimeUnit.DAYS.toMillis(30),
                TimeUnit.DAYS.toMillis(1),
                TimeUnit.HOURS.toMillis(1),
                TimeUnit.MINUTES.toMillis(1),
                TimeUnit.SECONDS.toMillis(1)
        );
        public static final List<String> timesString = Arrays.asList("year", "month", "day", "hour", "minute", "second");

        public static String getHumanReadable(long duration) {

            StringBuffer res = new StringBuffer();
            for (int i = 0; i < TimeAgo.times.size(); i++) {
                Long current = TimeAgo.times.get(i);
                long temp = duration / current;
                if (temp > 0) {
                    res.append(temp).append(" ").append(TimeAgo.timesString.get(i)).append(temp > 1 ? "s" : "").append(" ago");
                    break;
                }
            }
            if ("".equals(res.toString()))
                return "0 second ago";
            else
                return res.toString();
        }
    }

    /**
     * Get a diff between two dates
     *
     * @param firstDate the oldest date
     * @param lastDate  the newest date
     * @param timeUnit  the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date firstDate, Date lastDate, TimeUnit timeUnit) {
        long diffInMillies = lastDate.getTime() - firstDate.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static long getDateDiffWithSystem(String givenDate, String givenDateFormat, TimeUnit timeUnit) {
        SimpleDateFormat sdf = new SimpleDateFormat(givenDateFormat, Locale.ENGLISH);
        try {
            Date date = sdf.parse(givenDate);
            Date systemDate = new Date();
            long diffInMillis = systemDate.getTime() - date.getTime();

            return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getMonth(String date, String format) {
        Date d = null;
        try {
            d = new SimpleDateFormat(format, Locale.ENGLISH).parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            return new SimpleDateFormat("MMMM").format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMonthYear(String date, String format) {
        Date d = null;
        try {
            d = new SimpleDateFormat(format, Locale.ENGLISH).parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            return new SimpleDateFormat("MM-dddd").format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getYear(String date, String format) {
        Date d = null;
        try {
            d = new SimpleDateFormat(format, Locale.ENGLISH).parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            return new SimpleDateFormat("yyyy").format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getDaysRemaining(String fromDate, String toDate, String format) {
        long totalSeconds = 0;
        try {
            totalSeconds = getDateDiff(new SimpleDateFormat(format, Locale.ENGLISH).parse(fromDate), new SimpleDateFormat(format, Locale.ENGLISH).parse(toDate), TimeUnit.SECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (int) (totalSeconds / 86400);
    }

    public static int getHoursRemaining(String fromDate, String toDate, String format) {
        long totalSeconds = 0;
        try {
            totalSeconds = getDateDiff(new SimpleDateFormat(format, Locale.ENGLISH).parse(fromDate), new SimpleDateFormat(format, Locale.ENGLISH).parse(toDate), TimeUnit.SECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (int) (totalSeconds % 86400) / 3600;
    }

    public static int getMinutesRemaining(String fromDate, String toDate, String format) {
        long totalSeconds = 0;
        try {
            totalSeconds = getDateDiff(new SimpleDateFormat(format, Locale.ENGLISH).parse(fromDate), new SimpleDateFormat(format, Locale.ENGLISH).parse(toDate), TimeUnit.SECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (int) (totalSeconds % 3600) / 60;
    }

    public static int getSecondsRemaining(String fromDate, String toDate, String format) {
        long totalSeconds = 0;
        try {
            totalSeconds = getDateDiff(new SimpleDateFormat(format, Locale.ENGLISH).parse(fromDate), new SimpleDateFormat(format, Locale.ENGLISH).parse(toDate), TimeUnit.SECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (int) (totalSeconds % 60);
    }

    public static long convertDateToSeconds(String date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);
        try {
            return formatter.parse(date).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String convertSecondsToDate(long seconds, String outputFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(outputFormat, Locale.ENGLISH);
        return sdf.format(new Date(seconds * 1000));
    }

    public static Date convertStringToDate(String string, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        try {
            return sdf.parse(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String addSecondsToDate(String originalDate, String format, int seconds) {
//        String serverTimeSync = serverTimeFile.toString();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(originalDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.SECOND, seconds);
        return sdf.format(c.getTime());
    }

    public static String getHourMinutes(String hourMinutes) {
        String hourAndMinutes, hourMinAm[];
        if (hourMinutes == null) {
            hourAndMinutes = "00:00";
        } else {
            hourMinAm = hourMinutes.split(":");
            if (hourMinAm[2].equalsIgnoreCase("AM")) {
                hourAndMinutes = hourMinAm[0] + ":" + hourMinAm[1];
            } else {
                hourAndMinutes = String.valueOf(Integer.valueOf(hourMinAm[0]) + 12) + ":" + hourMinAm[1];
            }
        }

        return hourAndMinutes;
    }

    public static String[] getAmPm(String hourMinutes, boolean isMorning) {
        String hourAndMinutes[] = new String[3], hourMinAm[];
        if (hourMinutes == null || hourMinutes.equalsIgnoreCase("00:00")) {
            hourAndMinutes[0] = isMorning ? "08" : "05";
            hourAndMinutes[1] = "30";
            hourAndMinutes[2] = isMorning ? "AM" : "PM";
        } else {
            hourMinAm = hourMinutes.split(":");
            if (Integer.valueOf(hourMinAm[0]) > 12) {
                hourAndMinutes[0] = get12HourFormat(Integer.valueOf(hourMinAm[0]) - 12);
                hourAndMinutes[1] = hourMinAm[1];
                hourAndMinutes[2] = "PM";
            } else {
                hourAndMinutes[0] = hourMinAm[0];
                hourAndMinutes[1] = hourMinAm[1];
                hourAndMinutes[2] = "AM";
            }
        }

        return hourAndMinutes;
    }

    public static String get12HourFormat(int hour) {
        String hourString;
        if (hour < 10) {
            hourString = "0" + hour;
        } else {
            hourString = String.valueOf(hour);
        }
        return hourString;
    }

    public static int[] getSplitDate(String date) {
        int[] dateInt = new int[3];
        String[] dateStr = date.split("-");
        for (int i = 0; i < 3; i++) {
            dateInt[i] = Integer.valueOf(dateStr[i]);
        }
        return dateInt;

    }

    public static int dayOfMonthPos;


    public static String getMonthForInt(int num) {
        String month = "wrong";
        switch (num) {
            case 1:
                month = "JAN";
                break;
            case 2:
                month = "FEB";
                break;
            case 3:
                month = "MAR";
                break;
            case 4:
                month = "APR";
                break;
            case 5:
                month = "MAY";
                break;
            case 6:
                month = "JUN";
                break;
            case 7:
                month = "JUL";
                break;
            case 8:
                month = "AUG";
                break;
            case 9:
                month = "SEP";
                break;
            case 10:
                month = "OCT";
                break;
            case 11:
                month = "NOV";
                break;
            case 12:
                month = "DEC";
                break;
            default:
                break;
        }
        /*DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 1 && num <= 12 ) {
            month = months[num-1];
        }*/
        return month;
    }

    public static String getFullMonthForInt(int num) {
        if (num < 1 && num > 12) {
            return "Unknown";
        } else {
            return MONTH_NAME[num - 1];
        }
    }

    public static String getMonthNumber(int num) {
        String month = "wrong";
        if (num > 9) {
            //10,11,12
            month = String.valueOf(num);
        } else {
            //01,02,03
            month = "0" + String.valueOf(num);
        }

        return month;
    }

    public static String getMonthYear(int num, int year) {
        String month = "wrong";
        if (num > 9) {
            //10,11,12
            month = String.valueOf(num);
        } else {
            //01,02,03
            month = "0" + num;
        }
        return month + "-" + year;
    }

    public static int getMonthOrDayWithoutZero(String num) {
        if (num.startsWith("0")) {
            return Integer.valueOf(num.substring(1));
        } else {
            return Integer.valueOf(num);
        }
    }


    public static String getDayMonthYear(int day, int monthN, int year) {
        String month = "00";
        String date = "00";
        if (monthN > 9) {
            //10,11,12
            month = String.valueOf(monthN);
        } else {
            //01,02,03
            month = "0" + monthN;
        }
        if (day > 9) {
            //10,11,12
            date = String.valueOf(day);
        } else {
            //01,02,03
            date = "0" + day;
        }

        return date + "-" + month + "-" + year;
    }

    public static String getDayFormDate(String date) {
        String day = date.split("-")[0];
        if (day.startsWith("0")) {
            return day.substring(1);
        } else {
            return day;
        }
    }

    public static int getMonth() {

        return Calendar.getInstance().get(Calendar.MONTH);
    }

    public static boolean isLastDay(int day, Calendar cal) {
        if (day == cal.getActualMaximum(Calendar.DAY_OF_MONTH))
            return true;
        else
            return false;
    }


}
