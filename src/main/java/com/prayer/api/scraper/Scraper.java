package com.prayer.api.scraper;

import com.prayer.api.convertor.CalendarConverter;
import com.prayer.api.model.Prayer;
import com.prayer.api.model.PrayerIdentifier;
import com.prayer.api.repository.PrayerIdentifierRepository;
import com.prayer.api.repository.PrayerRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Component
public class Scraper {

    private PrayerIdentifierRepository prayerIdentifierRepository;
    private PrayerRepository prayerRepository;

    public Scraper(PrayerIdentifierRepository prayerIdentifierRepository, PrayerRepository prayerRepository, int month) {
        this.prayerIdentifierRepository = prayerIdentifierRepository;
        this.prayerRepository = prayerRepository;
        this.url = getMonthlyPrayerUrl(month);
        this.prayers = new ArrayList<>();
    }


    private String url;

    @Getter
    private List<PrayerIdentifier> prayers;
    @Getter
    private List<List<PrayerIdentifier>> yearlyPrayers;

    private final CalendarConverter calendarConverter = new CalendarConverter();

    private Document urlConnection;


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
        List<Prayer> dailyPrayerList = new ArrayList<>();

        int prayerCount = 1;
        int date = 1;

        for (Element prayerElement: getPrayerHtmlTable()) {
            if (dateExists(month, date)) {

                dailyPrayerList.add(tempPrayerHolder(prayerCount, prayerElement));

                if (isLastDailyPrayer(prayerCount)) {

                    PrayerIdentifier prayerIdentifier = prayerIdentifierRepository.save(PrayerIdentifier.builder()
                            .hijriCalendar(calendarConverter.convertToHijri(month, date))
                            .georgianCalendar(calendarConverter.convertToGeorgian(month, date))
                            .build());

                    dailyPrayerList.forEach(prayer -> prayer.setPrayerIdentifier(prayerIdentifier));
                    prayerRepository.saveAll(dailyPrayerList);


                    date++;

                    prayerCount = 1;
                    dailyPrayerList = new ArrayList<>();

                } else prayerCount++;

            }
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

    private Prayer tempPrayerHolder(int count, Element time) {
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

    public void loadYearlyPrayers() {
        this.yearlyPrayers = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            loadPrayers(i);
            this.yearlyPrayers.add(this.prayers);
            this.prayers = new ArrayList<>();
        }
    }

    public boolean dateExists(int month, int date) {
        try {
            LocalDate.of(LocalDate.now().getYear(), month, date);
        } catch (DateTimeException e) {
            System.out.println("THIS DATE DOES NOT EXIST");
            return false;
        }

        return true;
    }
}
