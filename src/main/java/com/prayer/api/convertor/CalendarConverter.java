package com.prayer.api.convertor;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Year;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CalendarConverter {
    private final List<String> georgianMonths = Arrays.asList(
            "Jan", "Feb", "Mar", "Apr",
            "May", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec"
    );

    public String convertToHijri(String month, int day) {
        double jdn = calculateJDN(Year.now().getValue(), getMonthInt(month), day);
        double HIJRI_EPOCH = 1948439.5;
        double daysSinceHijriEpoch = jdn - HIJRI_EPOCH;

        int hijriYear = (int) Math.floor(daysSinceHijriEpoch / 354.366) + 1;

        double yearStart = HIJRI_EPOCH + (hijriYear - 1) * 354.366;
        double dayOfYear = jdn - yearStart;

        double HIJRI_MONTH_LENGTH = 29.53058867;
        int hijriMonth = (int) Math.floor(dayOfYear / HIJRI_MONTH_LENGTH) + 1;

        int hijriDay = (int) (dayOfYear % HIJRI_MONTH_LENGTH);
        if (hijriDay == 0) {
            hijriDay = (int) HIJRI_MONTH_LENGTH;
            hijriMonth -= 1;
        }

        if (hijriMonth > 12) {
            hijriMonth = 1;
            hijriYear += 1;
        } else if (hijriMonth < 1) {
            hijriMonth = 12;
            hijriYear -= 1;
        }

        return hijriDay + " " + getHijriMonthName(hijriMonth) + " " + hijriYear + " AH";
    }

    private double calculateJDN(int year, int month, int day) {
        int a = (14 - month) / 12;
        year = year + 4800 - a;
        month = month + 12 * a - 3;

        return day + ((153 * month + 2) / 5) + (365 * year) + (year / 4) - (year / 100) + (year / 400) - 32045;
    }

    private String getHijriMonthName(int month) {
        String[] hijriMonths = {
                "Muharram", "Safar", "Rabi'Al-Awwal", "Rabi'Al-Thani",
                "Jamada-Al-Awwal", "Jamada-Al-Thani", "Rajab", "Sha'ban",
                "Ramadan", "Shawwal", "Dhul-Qa'dah", "Dhul-Hijjah"
        };
        return hijriMonths[month - 1];
    }

    private int getMonthInt(String month) {
        return georgianMonths.indexOf(month) + 1;
    }
}