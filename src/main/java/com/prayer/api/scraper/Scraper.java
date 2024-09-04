package com.prayer.api.scraper;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class Scraper {

    private String url;
    private List<HashMap<String, String>> prayers;

    private Document urlConnection;


    public Scraper(String url) {
        this.url = url;
        this.prayers = null;
    }

    public boolean startConnection() {
        try {
            urlConnection = Jsoup.connect(url).get();
        }
        catch (IOException exception) {
            System.out.println("Error While Connecting to URL: " + exception.getMessage());
            return false;
        }

        return true;
    }

    public Elements getPrayerHtmlTable() {
        return this.urlConnection.select("table.prayer-times");
    }

    public Element getCurrentPrayer() {
        return this.urlConnection.select("table.prayer-times.active").first();
    }

    private List<HashMap<String, String>> getPrayers() {

        return prayers;
    }
}
