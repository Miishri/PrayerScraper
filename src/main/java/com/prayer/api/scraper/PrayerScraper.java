package com.prayer.api.scraper;

import ch.qos.logback.core.html.CssBuilder;
import com.prayer.api.convertor.CalendarConverter;
import com.prayer.api.entity.DailyPrayer;
import com.prayer.api.repository.PrayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class PrayerScraper {

    public static String MuslimProLink = "https://prayer-times.muslimpro.com/en/find?coordinates=59.32932349999999%2C18.0685808&country_code=SE&country_name=Sweden&city_name=Stockholm&date=2024-01&convention=precalc";
    public static String IslamiskaLink = "https://www.islamiskaforbundet.se/bonetider/";

    private final CalendarConverter calendarConverter = new CalendarConverter();

    private final PrayerRepository prayerRepository;

    public PrayerScraper(PrayerRepository prayerRepository) {
        this.prayerRepository = prayerRepository;
    }


//    public List<DailyPrayer> scrapeIslamiska() {
//        chromeDriver.get(IslamiskaLink);
//
//        driverTimeout(chromeDriver);
//
//        String prayerTable = chromeDriver.findElement(
//                By.id("ifis_bonetider")
//        ).getText();
//
//
//        return null;
//    }

    public void scrapeMuslimPro() {
        ChromeDriver chromeDriver = new ChromeDriver(setChromeHeadless());
        chromeDriver.get(MuslimProLink);

        driverTimeout(chromeDriver);
        acceptMuslimProCookies(chromeDriver);

        By selector = By.cssSelector(".month-picker-month-table a");

        for (int i = 1; i <= 12; i++) {
            if (i != 1) {
                int finalI = i;
                new WebDriverWait(chromeDriver, Duration.ofMillis(1500))
                        .ignoring(StaleElementReferenceException.class)
                        .until((WebDriver d) -> {
                            clickCalendar(d);
                            WebElement htmlElement = d.findElement(By.className("button-" + finalI));

                            htmlElement.click();
                            driverTimeout(d);

                            scrapeAndPopulatePrayerDataMuslimPro(d);
                            return true;
                        });

            } else {
                scrapeAndPopulatePrayerDataMuslimPro(chromeDriver);
            }
        }

        chromeDriver.quit();
        log.info("MUSLIM PRO SCRAPED - YEARLY");
    }

    private void acceptMuslimProCookies(WebDriver chromeDriver) {
        WebElement acceptCookies = chromeDriver.findElement(By.id("onetrust-accept-btn-handler"));
        acceptCookies.click();
    }


    private void clickCalendar(WebDriver chromeDriver) {
        WebElement calendar = chromeDriver.findElement(By.className("calender-div"));
        calendar.click();
    }

    private void scrapeAndPopulatePrayerDataMuslimPro(WebDriver driver) {
        List<DailyPrayer> prayers = driver.findElements(
                        By.cssSelector(".prayer-times * tr:not(:first-child)"))
                .stream()
                .map(WebElement::getText)
                .map(element -> {
                    String[] prayer = element.split(" ");

                    return DailyPrayer.builder()
                            .georgian(formatPrayerGeorgian(prayer))
                            .hijri(formatPrayerHijri(prayer))
                            .fajr(prayer[3])
                            .sunrise(prayer[4])
                            .zuhr(prayer[5])
                            .asr(prayer[6])
                            .maghrib(prayer[7])
                            .isha(prayer[8])
                            .build();
                })
                .toList();

        prayerRepository.saveAll(prayers);
    }

    private void driverTimeout(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
    }

    private String formatPrayerGeorgian(String[] prayerTable) {
        return  prayerTable[2] + " " + prayerTable[0] + " " + prayerTable[1];
    }

    private String formatPrayerHijri(String[] prayerTable) {
        return  calendarConverter.convertToHijri(prayerTable[2], Integer.parseInt(prayerTable[1]));
    }

    private ChromeOptions setChromeHeadless() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        return options;
    }
}
