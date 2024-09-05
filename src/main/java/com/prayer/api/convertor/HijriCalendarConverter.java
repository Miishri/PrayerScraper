package com.prayer.api.convertor;

public class HijriCalendarConverter {

    public double calculateJDN(int year, int month, int day) {
        int a = (14 - month) / 12;
        int y = year + 4800 - a;
        int m = month + 12 * a - 3;

        double JDN = day + ((153 * m + 2) / 5) + (365 * y) + (y / 4) - (y / 100) + (y / 400) - 32045;

        return JDN;
    }

    public String convertToHijri(int year, int month, int day) {
        double jdn = calculateJDN(year, month, day);

        double daysSinceHijriEpoch = jdn - 1948439.5;

        int hijriYear = (int) (daysSinceHijriEpoch / 354.366);

        double remainingDays = daysSinceHijriEpoch % 354.366;
        int hijriMonth = (int) (remainingDays / 29.5) + 1;
        int hijriDay = (int) (remainingDays % 29.5) + 1;

        return hijriDay + " " + getHijriMonthName(hijriMonth) + " " + hijriYear + " AH";
    }

    // Method to get the name of the Hijri month
    private static String getHijriMonthName(int month) {
        String[] hijriMonths = {"Muharram", "Safar", "Rabi' Al-Awwal", "Rabi' Al-Thani",
                "Jamada Al-Awwal", "Jamada Al-Thani", "Rajab", "Sha'ban",
                "Ramadan", "Shawwal", "Dhul-Qa'dah", "Dhul-Hijjah"};
        return hijriMonths[month - 1];
    }

    public static void main(String[] args) {
        int year = 2023;
        int month = 9;
        int day = 5;

        String hijriDate = convertToHijri(year, month, day);
        System.out.println("Hijri Date: " + hijriDate);
    }
}
