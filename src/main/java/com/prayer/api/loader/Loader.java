package com.prayer.api.loader;

import com.prayer.api.repository.PrayerRepository;
import com.prayer.api.scraper.PrayerScraper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleStateException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Loader implements CommandLineRunner {

    private final PrayerRepository prayerRepository;
    private final PrayerScraper scraper;

    public Loader(PrayerRepository prayerRepository, PrayerScraper scraper) {
        this.prayerRepository = prayerRepository;
        this.scraper = scraper;
    }

    @Override
    public void run(String... args) {
        if (prayerRepository.count() == 0) {
            try {
                scraper.scrapeMuslimPro();
                scraper.scrapeIslamiska();
            } catch (Exception e) {
                System.out.println("ERROR OCCURED WHEN FETCHING DATA FROM THE SITES: " + e.getMessage());
            }
        }
    }
}
