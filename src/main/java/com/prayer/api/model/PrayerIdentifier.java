package com.prayer.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@Data
@Table(name="yearly_prayers")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PrayerIdentifier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "identifier_id")
    private Long id;
    private int identifierDay;
    private String hijriCalendar;
    private String georgianCalendar;

    @OneToMany(mappedBy = "prayerIdentifier", cascade = CascadeType.ALL)
    private List<Prayer> weeklyPrayers;
}
