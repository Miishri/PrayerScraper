package com.prayer.api.controller;

import com.prayer.api.model.PrayerIdentifier;
import com.prayer.api.repository.PrayerIdentifierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/5075690944")
public class PrayerController {

    private final PrayerIdentifierRepository prayerRepository;

    @GetMapping("/43725")
    public PrayerIdentifier getCurrentPrayer() {
        long currentDay = LocalDate.now().getDayOfYear();
        return prayerRepository.findAll().get(0);
    }
}
