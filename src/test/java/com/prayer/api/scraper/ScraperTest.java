package com.prayer.api.scraper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScraperTest {

    private final String testUrl = "https://www.muslimpro.com/en/find?coordinates=59.32932349999999%2C18.0685808&country_code=SE&country_name=Sweden&city_name=Stockholm&date=2024-09&convention=precalc";
    private final Scraper scraper = new Scraper(testUrl);

    @BeforeEach
    void setUp() {
        scraper.startConnection();
    }

    @Test
    void startConnection() {
        assertThat(scraper.startConnection()).isTrue();
    }

    @Test
    void getPrayerHtmlTable() {
        assertThat(scraper.getPrayerHtmlTable()).isNotEmpty();
    }

    @Test
    void getCurrentPrayer() {}

    @Test
    void getUrl() {
        assertThat(scraper.getUrl()).isEqualTo(testUrl);
    }

    @Test
    void setUrl() {
        String testUrl = "test.com";
        scraper.setUrl(testUrl);
        assertThat(scraper.getUrl()).isEqualTo(testUrl);
    }
}