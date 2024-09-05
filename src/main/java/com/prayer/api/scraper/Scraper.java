package com.prayer.api.scraper;

import com.prayer.api.model.Prayer;
import com.prayer.api.model.PrayerIdentifier;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scraper {

    @Setter
    @Getter
    private String url;

    @Getter
    private List<PrayerIdentifier> prayers;

    private Document urlConnection;


    public Scraper(String url) {
        this.url = url;
        this.prayers = null;
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

    public Elements getPrayerHtmlTable() {
        return this.urlConnection.select("table.prayer-times * td:not(.prayertime-1)");
    }

    private List<Prayer> prayers() {
        List<PrayerIdentifier> monthPrayerList = new ArrayList<>();
        List<Prayer> dailyPrayers = new ArrayList<>();

        int prayerCount = 1;
        int date = 1;

        for (Element prayerElement: getPrayerHtmlTable()) {
            dailyPrayers.add(getTempPrayerHolder(prayerCount, prayerElement));

            if (isLastDailyPrayer(prayerCount)) {
                monthPrayerList.add(new PrayerIdentifier(date, dailyPrayers));
                date++;

                prayerCount = 1;
                dailyPrayers = new ArrayList<>();

            } else prayerCount++;

        }
        monthPrayerList.stream().forEach(System.out::println);

        return null;
    }

    public String getPrayerName(int count) {
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


    public static void main(String[] args) {
        Scraper scraper = new Scraper("https://www.muslimpro.com/en/find?coordinates=59.32932349999999%2C18.0685808&country_code=SE&country_name=Sweden&city_name=Stockholm&date=2024-09&convention=precalc");
        scraper.startConnection();

        scraper.prayers();
    }

}
