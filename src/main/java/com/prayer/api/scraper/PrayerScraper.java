package com.prayer.api.scraper;

import com.prayer.api.convertor.CalendarConverter;
import com.prayer.api.entity.Prayer;
import com.prayer.api.repository.PrayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class PrayerScraper {

    public static String MuslimProLink = "https://prayer-times.muslimpro.com/en/find?coordinates=59.32932349999999%2C18.0685808&country_code=SE&country_name=Sweden&city_name=Stockholm&date=2024-01&convention=precalc";
    public static String IslamiskaLink = "https://www.islamiskaforbundet.se/bonetider/";

    private final CalendarConverter calendarConverter = new CalendarConverter();
    private final PrayerRepository prayerRepository;
    private final ChromeDriver chromeDriver = new ChromeDriver(setChromeHeadless());

    public PrayerScraper(PrayerRepository prayerRepository) {
        this.prayerRepository = prayerRepository;
    }


    public void scrapeIslamiska() {
        chromeDriver.get(IslamiskaLink);

        driverTimeout(chromeDriver);

        closePopup();
        List<WebElement> monthDropdown = chromeDriver.findElements(By.cssSelector("#ifis_bonetider_page_months option"));
        int year = Integer.parseInt(chromeDriver.findElement(By.cssSelector("#ifis_bonetider_page_header span b i")).getText().substring(0, 4));

        for (int i = 1; i <= 12; i++) {
            int finalI = i;
            new WebDriverWait(chromeDriver, Duration.ofMillis(1000))
                    .ignoring(StaleElementReferenceException.class)
                    .until((WebDriver d) -> {
                        monthDropdown.get(finalI).click();

                        driverTimeout(chromeDriver);

                        scrapeAndPopulateIslamiskaPrayerData(finalI, year);
                        return true;
                    });
        }


        chromeDriver.quit();
        log.info("ISLAMISKA SCRAPED - YEARLY");
    }

    private void scrapeAndPopulateIslamiskaPrayerData(int month, int year) {
        List<String> prayerTable = chromeDriver.
                findElements(By.cssSelector("#ifis_bonetider_page_content #ifis_bonetider tr"))
                .stream()
                .map(WebElement::getText)
                .toList();

        List<Prayer> prayers = prayerTable
                .stream().map(element -> {
                    String[] prayer = element.split(" ");


                    return Prayer.builder()
                            .georgian(formatIslamiskaGeorgian(prayer, month, year))
                            .hijri(calendarConverter.convertToHijri(month, Integer.parseInt(prayer[0]), year))
                            .fajr(prayer[1])
                            .sunrise(prayer[2])
                            .zuhr(prayer[3])
                            .asr(prayer[4])
                            .maghrib(prayer[5])
                            .isha(prayer[6])
                            .scrapedSite("ISLAMISKA")
                            .build();
                }).toList();

        prayerRepository.saveAll(prayers);
    }

    private void closePopup() {
        chromeDriver.findElement(By.className("sgpb-popup-close-button-1")).click();
    }

    public void scrapeMuslimPro() {
        chromeDriver.get(MuslimProLink);

        driverTimeout(chromeDriver);
        acceptMuslimProCookies();
        int year = Integer.parseInt(chromeDriver.findElement(By.className("display-month")).getText().substring(8));

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

                            scrapeAndPopulatePrayerDataMuslimPro(d, finalI, year);
                            return true;
                        });

            } else {
                scrapeAndPopulatePrayerDataMuslimPro(chromeDriver, 1, year);
            }
        }


        log.info("MUSLIM PRO SCRAPED - YEARLY");
    }

    private void acceptMuslimProCookies() {
        WebElement acceptCookies = chromeDriver.findElement(By.id("onetrust-accept-btn-handler"));
        acceptCookies.click();
    }

    private void clickCalendar(WebDriver driver) {
        WebElement calendar = driver.findElement(By.className("calender-div"));
        calendar.click();
    }

    private void scrapeAndPopulatePrayerDataMuslimPro(WebDriver driver, int month, int year) {
        List<Prayer> prayers = driver.findElements(
                        By.cssSelector(".prayer-times * tr:not(:first-child)"))
                .stream()
                .map(WebElement::getText)
                .map(element -> {
                    String[] prayer = element.split(" ");

                    return Prayer.builder()
                            .georgian(formatPrayerGeorgian(prayer))
                            .hijri(formatMuslimProHijri(prayer, month, year))
                            .fajr(prayer[3])
                            .sunrise(prayer[4])
                            .zuhr(prayer[5])
                            .asr(prayer[6])
                            .maghrib(prayer[7])
                            .isha(prayer[8])
                            .scrapedSite("MUSLIM-PRO")
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

    private String formatIslamiskaGeorgian(String[] prayerTable, int month, int year) {
        return  calendarConverter.getMonth(month) + " " + calendarConverter.calculateDayOfWeek(Integer.parseInt(prayerTable[0]), month, year) + " " + prayerTable[0];
    }

    private String formatMuslimProHijri(String[] prayerTable, int month, int year) {
        return calendarConverter.convertToHijri(month, Integer.parseInt(prayerTable[1]), year);
    }

    private ChromeOptions setChromeHeadless() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        return options;
    }
}
