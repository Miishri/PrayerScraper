package com.prayer.api.scraper;

import com.prayer.api.convertor.CalendarConverter;
import com.prayer.api.model.PrayerIdentifier;
import com.prayer.api.repository.PrayerIdentifierRepository;
import com.prayer.api.repository.PrayerRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import java.time.Duration;
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

        this.prayers = new ArrayList<>();
    }

    private String url;

    @Getter
    private List<PrayerIdentifier> prayers;
    @Getter
    private List<List<PrayerIdentifier>> yearlyPrayers;

    private final CalendarConverter calendarConverter = new CalendarConverter();

    public List<String> scrapePrayerWebsiteMuslim() {
        WebDriver chromeDriver = new ChromeDriver();
        chromeDriver.navigate().to("https://prayer-times.muslimpro.com/id/Waktu-sholat-Adzan-Stockholm-Sweden-2673730?date=2024-10&convention=precalc");



        return null;
    }

    private void websiteLoading(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
    }
}
