package com.prayer.api.scraper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScraperTest {

    private final String testUrl = "https://www.muslimpro.com/en/find?coordinates=59.32932349999999%2C18.0685808&country_code=SE&country_name=Sweden&city_name=Stockholm&date=2024-09&convention=precalc";
    private final Scraper scraper = new Scraper(testUrl);

    @BeforeEach
    void setUp() {
        scraper.startConnection();
    }

    @Test
    void startConnection() {

    }

    @Test
    void getPrayerHtmlTable() {
    }

    @Test
    void getCurrentPrayer() {
    }

    @Test
    void getUrl() {
    }

    @Test
    void getUrlConnection() {
    }

    @Test
    void setUrl() {
    }

    @Test
    void setPrayers() {
    }

    @Test
    void setUrlConnection() {
    }
}