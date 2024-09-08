package com.prayer.api.scraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prayer.api.convertor.CalendarConverter;
import com.prayer.api.model.Prayer;
import com.prayer.api.model.PrayerIdentifier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class Scraper {

    private String url;

    @Getter
    private List<PrayerIdentifier> prayers;
    @Getter
    private List<List<PrayerIdentifier>> yearlyPrayers;

    private final CalendarConverter calendarConverter = new CalendarConverter();

    private Document urlConnection;


    public Scraper(int month) {
        this.url = getMonthlyPrayerUrl(month);
        this.prayers = new ArrayList<>();
    }

    public boolean startConnection() {
        try {
            urlConnection = Jsoup.connect(url).get();
        }
        catch (IOException exception) {
            System.out.println("Error While Connecting to URL: " + exception.getMessage());
            return false;
        }

        return true;
    }

    Elements getPrayerHtmlTable() {
        return this.urlConnection.select("table.prayer-times * td:not(.prayertime-1)");
    }

    public void loadPrayers(int month) {
        List<Prayer> dailyPrayers = new ArrayList<>();

        int prayerCount = 1;
        int date = 1;

        for (Element prayerElement: getPrayerHtmlTable()) {
            dailyPrayers.add(getTempPrayerHolder(prayerCount, prayerElement));

            if (isLastDailyPrayer(prayerCount)) {

                prayers.add(PrayerIdentifier.builder()
                        .identifierDay(date)
                        .hijriCalendar(calendarConverter.convertToHijri(month, date))
                        .georgianCalendar(calendarConverter.convertToGeorgian(month, date))
                        .weeklyPrayers(dailyPrayers)
                        .build());

                date++;

                prayerCount = 1;
                dailyPrayers = new ArrayList<>();

            } else prayerCount++;

        }
    }


    private String getPrayerName(int count) {
        return switch (count) {
            case 1 -> "Fajr";
            case 2 -> "Sunrise";
            case 3 -> "Zuhr";
            case 4 -> "Asr";
            case 5 -> "Maghrib";
            case 6 -> "Isha";
            default -> "ERROR OCCURRED WHEN COUNTING";
        };
    }

    private Prayer getTempPrayerHolder(int count, Element time) {
        return Prayer.builder()
                .prayerName(getPrayerName(count))
                .prayerTime(time.text()).build();
    }

    private boolean isLastDailyPrayer(int count) {
        return count == 6;
    }

    public String getMonthlyPrayerUrl(int month) {
        return "https://prayer-times.muslimpro.com/id/Waktu-sholat-Stockholm-Sweden-2673730?date=2024-" + month + "&convention=precalc";
    }

    public void loadYearPrayers() {
        this.yearlyPrayers = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            loadPrayers(i);
            this.yearlyPrayers.add(this.prayers);
            this.prayers = new ArrayList<>();
        }
    }
}
