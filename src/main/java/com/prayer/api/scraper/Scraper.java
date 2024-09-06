package com.prayer.api.scraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prayer.api.convertor.HijriCalendarConverter;
import com.prayer.api.model.Prayer;
import com.prayer.api.model.PrayerIdentifier;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Scraper {

    @Setter
    @Getter
    private String url;

    @Getter
    private List<PrayerIdentifier> prayers;

    private HijriCalendarConverter hijriCalendarConverter = new HijriCalendarConverter();

    private Document urlConnection;

    public Scraper(String url) {
        this.url = url;
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

    private Elements getPrayerHtmlTable() {
        return this.urlConnection.select("table.prayer-times * td:not(.prayertime-1)");
    }

    public void loadPrayers(int month) {
        List<Prayer> dailyPrayers = new ArrayList<>();

        int prayerCount = 1;
        int date = 1;

        for (Element prayerElement: getPrayerHtmlTable()) {
            dailyPrayers.add(getTempPrayerHolder(prayerCount, prayerElement));

            if (isLastDailyPrayer(prayerCount)) {
                prayers.add(new PrayerIdentifier(
                        date,
                        hijriCalendarConverter.convertToHijri(month, date),
                        dailyPrayers));
                date++;

                prayerCount = 1;
                dailyPrayers = new ArrayList<>();

            } else prayerCount++;

        }
    }

    private String getPrayerName(int count) {
        switch (count){
            case 1:
                return "Fajr";
            case 2:
                return "Sunrise";
            case 3:
                return "Zuhr";
            case 4:
                return "Asr";
            case 5:
                return "Maghrib";
            case 6:
                return "Isha";
            default:
                return "ERROR OCCURRED WHEN COUNTING";
        }
    }

    private Prayer getTempPrayerHolder(int count, Element time) {
        return Prayer.builder()
                .name(getPrayerName(count))
                .time(time.text()).build();
    }

    private boolean isLastDailyPrayer(int count) {
        return count == 6 ? true : false;
    }
    public boolean populatePrayerJson() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(new File("src/main/resources/prayers/prayers.json"), this.prayers);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR WHEN POPULATING JSON FILE PRAYERS DATA: " + e.getMessage());
        }

        return false;
    }


    public static void main(String[] args) {
        Scraper scraper = new Scraper("https://prayer-times.muslimpro.com/id/Waktu-sholat-Stockholm-Sweden-2673730");

        scraper.startConnection();
        scraper.loadPrayers(9);
        scraper.populatePrayerJson();
    }

}
