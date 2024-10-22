package com.prayer.api.controller;

import com.prayer.api.model.PrayerIdentifier;
import com.prayer.api.repository.PrayerIdentifierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/prayer")
public class PrayerController {

    private final PrayerIdentifierRepository prayerRepository;
    private long currentDay = LocalDate.now().getDayOfYear();

    @GetMapping()
    public PrayerIdentifier getCurrentPrayer() {
        return prayerRepository.findById(currentDay).get();
    }

    @GetMapping("/{prayerId}")
    public PrayerIdentifier getPrayerById(@PathVariable Long prayerId) {
        return prayerRepository.findById(prayerId).orElse(prayerRepository.findById(currentDay).get());
    }

    @GetMapping("/all")
    public List<PrayerIdentifier> getAllPrayers() {
        return prayerRepository.findAll();
    }
}
