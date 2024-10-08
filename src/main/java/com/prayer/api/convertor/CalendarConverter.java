package com.prayer.api.convertor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.Locale;

public class CalendarConverter {

    private final double HIJRI_MONTH_LENGTH = 29.5;
    private final double HIJRI_YEAR_LENGTH = 354.366;
    private final double HIJRI_EPOCH = 1948439.5;
    private final int CURRENT_YEAR = LocalDate.now().getYear();

    private final String[] hijriMonths = {
            "Muharram", "Safar", "Rabi'Al-Awwal", "Rabi'Al-Thani",
            "Jamada-Al-Awwal", "Jamada-Al-Thani", "Rajab", "Sha'ban",
            "Ramadan", "Shawwal", "Dhul-Qa'dah", "Dhul-Hijjah"
    };

    private final String[] georgianMonths = {
            "Jan", "Feb", "Mar", "Apr",
            "May", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec"
    };


    private double calculateJDN(int month, int day) {
        LocalDate date = LocalDate.of(CURRENT_YEAR, month, day);
        int y = date.getYear();
        int m = date.getMonthValue();
        int d = date.getDayOfMonth();

        int a = (14 - m) / 12;
        y = y + 4800 - a;
        m = m + 12 * a - 3;

        return d + ((153 * m + 2) / 5) + (365 * y) + (y / 4) - (y / 100) + (y / 400) - 32045;
    }

    public String convertToHijri(int month, int day) {
        double jdn = calculateJDN(month, day);

        double daysSinceHijriEpoch = jdn - HIJRI_EPOCH;

        int hijriYear = (int) Math.round(((double)(CURRENT_YEAR - 622) * 33)/32);

        double remainingDays = daysSinceHijriEpoch % HIJRI_YEAR_LENGTH;
        int hijriMonth = (int) (remainingDays / HIJRI_MONTH_LENGTH) + 1;
        int hijriDay = (int) (remainingDays % HIJRI_MONTH_LENGTH) + 1;

        return hijriDay + " " + getHijriMonth(hijriMonth) + " " + hijriYear + " AH";
    }

    private String getHijriMonth(int month) {
        return hijriMonths[month - 1];
    }

    public String convertToGeorgian(int month, int date) {
        String stringDate = String.format("%d-%d-%d", CURRENT_YEAR, month, date);

        return getGeorgianDay(stringDate) + " " + getGeorgianMonth(month) + " " + date + " " + CURRENT_YEAR;
    }

    private String getGeorgianDay(String inputDate){
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(inputDate);
        } catch (ParseException e) {
            System.out.println("EXCEPTION WHILE PARSING GEORGIAN DATE: " + inputDate);
        }
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date).substring(0, 3);
    }

    public boolean isLeapYear(int year) {
        return YearMonth.of(year, 2).isLeapYear();
    }

    private String getGeorgianMonth(int month) {
        return georgianMonths[month - 1];
    }

}
