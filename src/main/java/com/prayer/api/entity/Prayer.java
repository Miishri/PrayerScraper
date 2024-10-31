package com.prayer.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Prayer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private Long id;
    private String hijri;
    private String georgian;

    private String fajr;
    private String sunrise;
    private String zuhr;
    private String asr;
    private String maghrib;
    private String isha;

    private String scrapedSite;
}
