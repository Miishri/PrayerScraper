package com.prayer.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@AllArgsConstructor
@Data
public class PrayerIdentifier {
    private int day;
    private String hijriCalendar;
    private List<Prayer> prayer;
}
