package com.prayer.api.loader;

import com.prayer.api.repository.PrayerIdentifierRepository;
import com.prayer.api.repository.PrayerRepository;
import com.prayer.api.scraper.Scraper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class SalahLoader implements CommandLineRunner {

    private final PrayerIdentifierRepository prayerIdentifierRepository;
    private final PrayerRepository prayerRepository;

    public SalahLoader(PrayerIdentifierRepository prayerIdentifierRepository, PrayerRepository prayerRepository) {
        this.prayerIdentifierRepository = prayerIdentifierRepository;
        this.prayerRepository = prayerRepository;
    }

    @Override
    public void run(String... args) {
        Scraper scraper = new Scraper( prayerIdentifierRepository,prayerRepository, 1);

        if (prayerRepository.findAll().isEmpty()) {
            scraper.startConnection();
            scraper.loadYearlyPrayers();

        }
    }
}
