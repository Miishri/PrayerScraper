package com.prayer.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "identifier_id")
    private Long id;
    private String hijriCalendar;
    private String georgianCalendar;

    @OneToMany(mappedBy = "prayerIdentifier", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Prayer> dailyPrayers;
}
