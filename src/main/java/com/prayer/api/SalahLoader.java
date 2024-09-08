package com.prayer.api;

import com.prayer.api.repository.PrayerRepository;
import com.prayer.api.scraper.Scraper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class SalahLoader implements CommandLineRunner {

    private final PrayerRepository prayerRepository;

    public SalahLoader(PrayerRepository prayerRepository) {
        this.prayerRepository = prayerRepository;
    }

    @Override
    public void run(String... args) {
        Scraper scraper = new Scraper(1);

        if (prayerRepository.findAll().isEmpty()) {
            scraper.startConnection();
            scraper.loadYearPrayers();
            scraper.getYearlyPrayers().forEach(prayerRepository::saveAll);
        }
    }
}
