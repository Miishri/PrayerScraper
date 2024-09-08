package com.prayer.api.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@Table(name="prayer")
@NoArgsConstructor
@AllArgsConstructor
public class Prayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prayer_id")
    private Long id;
    private String prayerName;
    private String prayerTime;

    @ManyToOne
    @JoinColumn(name = "identifier_id")
    private PrayerIdentifier prayerIdentifier;
}
