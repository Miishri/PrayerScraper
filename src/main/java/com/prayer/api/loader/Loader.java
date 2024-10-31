package com.prayer.api.loader;

import com.prayer.api.repository.PrayerRepository;
import com.prayer.api.scraper.PrayerScraper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
            scraper.scrapeMuslimPro();
        }
    }
}
