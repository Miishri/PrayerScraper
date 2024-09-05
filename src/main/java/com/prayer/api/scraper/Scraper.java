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

public class Scraper {

    @Setter
    @Getter
    private String url;

    @Getter
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

    private List<HashMap<String, String>> prayers() {



        return prayers;
    }


    public static void main(String[] args) {
        Scraper scraper = new Scraper("https://www.muslimpro.com/en/find?coordinates=59.32932349999999%2C18.0685808&country_code=SE&country_name=Sweden&city_name=Stockholm&date=2024-09&convention=precalc");
        scraper.startConnection();

        System.out.println(
            scraper.getPrayerHtmlTable()
        );
    }

}
