package com.mapia.util;

/**
 * Created by daehyun on 15. 6. 8..
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils
{
    private static SimpleDateFormat hhmmss;
    private static SimpleDateFormat sdfMMdd;
    private static SimpleDateFormat sdfMMddhhmm;
    private static SimpleDateFormat sdfyyyyMMdd;
    private static SimpleDateFormat sdfyyyyMMddDot;

    static {
        DateUtils.hhmmss = new SimpleDateFormat("hhmmss");
        DateUtils.sdfMMdd = new SimpleDateFormat("MM/dd");
        DateUtils.sdfMMddhhmm = new SimpleDateFormat("MM\uc6d4 dd\uc77c a hh:mm");
        DateUtils.sdfyyyyMMdd = new SimpleDateFormat("yyyy\ub144 MM\uc6d4 dd\uc77c");
        DateUtils.sdfyyyyMMddDot = new SimpleDateFormat("yyyy.MM.dd");
    }

    public static String getFriendlyTimeForEnd(final long n) {
        return getFriendlyTimeForEnd(new Date(n));
    }

    public static String getFriendlyTimeForEnd(final String s) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            return getFriendlyTimeForEnd(simpleDateFormat.parse(s));
        }
        catch (ParseException ex) {
            return null;
        }
    }

    public static String getFriendlyTimeForEnd(final Date date) {
        final long n = (Calendar.getInstance().getTime().getTime() - date.getTime()) / 1000L / 60L;
        final long n2 = n / 60L;
        if (n2 / 24L > 365L) {
            return DateUtils.sdfyyyyMMdd.format(date);
        }
        if (n2 > 24L) {
            return DateUtils.sdfMMddhhmm.format(date);
        }
        final StringBuffer sb = new StringBuffer();
        if (n2 == 0L) {
            long n3 = n;
            if (n <= 0L) {
                n3 = 1L;
            }
            sb.append(String.valueOf(n3) + "\ubd84 \uc804");
            return sb.toString();
        }
        sb.append(String.valueOf(n2) + "\uc2dc\uac04 \uc804");
        return sb.toString();
    }

    public static String getFriendlyTimeForKorean(final long n) {
        return getFriendlyTimeForKorean(new Date(n));
    }

    public static String getFriendlyTimeForKorean(final String s) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            return getFriendlyTimeForKorean(simpleDateFormat.parse(s));
        }
        catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static String getFriendlyTimeForKorean(final Date date) {
        final StringBuffer sb = new StringBuffer();
        final long n = (Calendar.getInstance().getTime().getTime() - date.getTime()) / 1000L;
        long n2;
        if (n >= 60L) {
            n2 = n % 60L;
        }
        else {
            n2 = n;
        }
        final long n3 = n / 60L;
        long n4;
        if (n3 >= 60L) {
            n4 = n3 % 60L;
        }
        else {
            n4 = n3;
        }
        final long n5 = n3 / 60L;
        long n6;
        if (n5 >= 24L) {
            n6 = n5 % 24L;
        }
        else {
            n6 = n5;
        }
        final long n7 = n5 / 24L;
        long n8;
        if (n7 >= 30L) {
            n8 = n7 % 30L;
        }
        else {
            n8 = n7;
        }
        final long n9 = n7 / 30L;
        long n10;
        if (n9 >= 12L) {
            n10 = n9 % 12L;
        }
        else {
            n10 = n9;
        }
        final long n11 = n9 / 12L;
        if (n11 > 0L) {
            sb.append(n11 + "\ub144");
        }
        else if (n10 > 0L) {
            sb.append(n10 + "\uc6d4");
        }
        else if (n8 > 0L) {
            sb.append(n8 + "\uc77c");
        }
        else if (n6 > 0L) {
            sb.append(n6 + "\uc2dc\uac04");
        }
        else if (n4 > 0L) {
            sb.append(n4 + "\ubd84");
        }
        else {
            long n12 = n2;
            if (n2 < 0L) {
                n12 = 0L;
            }
            sb.append(n12 + "\ucd08");
        }
        sb.append(" \uc804");
        return sb.toString();
    }

    public static String getFriendlyTimeForNoti(final long n) {
        return getFriendlyTimeForNoti(new Date(n));
    }

    private static String getFriendlyTimeForNoti(final Date date) {
        final StringBuffer sb = new StringBuffer();
        final long n = (Calendar.getInstance().getTime().getTime() - date.getTime()) / 1000L;
        long n2;
        if (n >= 60L) {
            n2 = n % 60L;
        }
        else {
            n2 = n;
        }
        final long n3 = n / 60L;
        long n4;
        if (n3 >= 60L) {
            n4 = n3 % 60L;
        }
        else {
            n4 = n3;
        }
        final long n5 = n3 / 60L;
        long n6;
        if (n5 >= 24L) {
            n6 = n5 % 24L;
        }
        else {
            n6 = n5;
        }
        final long n7 = n5 / 24L;
        long n8;
        if (n7 >= 30L) {
            n8 = n7 % 30L;
        }
        else {
            n8 = n7;
        }
        long n9 = n7 / 30L;
        if (n9 >= 12L) {
            n9 %= 12L;
        }
        if (n9 > 0L || n8 > 0L) {
            sb.append(DateUtils.sdfMMdd.format(date));
        }
        else if (n6 > 0L) {
            sb.append(n6 + "\uc2dc\uac04");
            sb.append(" \uc804");
        }
        else if (n4 > 0L) {
            sb.append(n4 + "\ubd84");
            sb.append(" \uc804");
        }
        else {
            long n10 = n2;
            if (n2 < 0L) {
                n10 = 0L;
            }
            sb.append(n10 + "\ucd08");
            sb.append(" \uc804");
        }
        return sb.toString();
    }

    public static String getHHmm() {
        return new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
    }

    public static String getSimpleDate(final long n) {
        return DateUtils.sdfyyyyMMddDot.format(new Date(n));
    }

    public static SimpleDateFormat getSimpleDateFormatForAnalogClock() {
        return DateUtils.hhmmss;
    }

    private static String getTimeString(final long n) {
        if (n > 9L) {
            return "" + n;
        }
        return "0" + n;
    }

    public static String getYyyyMMddHHmmss() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
    }
}