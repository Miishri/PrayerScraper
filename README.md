# Stockholm Prayers API

This API provides real-time access to prayer times and annual prayer data for Stockholm.

## Endpoints

### 1. Get Current Prayer

**URL**: `/current`

**Method**: `GET`

**Description**: Returns the prayer times for a specific day of the year (1st January to 31st December). If the provided day is outside this range, the endpoint will return the current prayer.

**Example Request**:
```bash
GET GET http://localhost:7860/prayer/150
```

**Example Response**
```bash
{
      "id": 256,
      "hijriCalendar": "9 Rabi'Al-Awwal 1446 AH",
      "georgianCalendar": "Thu Sep 12 2024",
      "dailyPrayers": [
            {
                  "id": 1531,
                  "prayerName": "Fajr",
                  "prayerTime": "06:01"
            },
            {
                  "id": 1532,
                  "prayerName": "Sunrise",
                  "prayerTime": "08:29"
            },
            {
                  "id": 1533,
                  "prayerName": "Zuhr",
                  "prayerTime": "12:01"
            },
            {
                  "id": 1534,
                  "prayerName": "Asr",
                  "prayerTime": "13:08"
            },
            {
                  "id": 1535,
                  "prayerName": "Maghrib",
                  "prayerTime": "15:23"
            },
            {
                  "id": 1536,
                  "prayerName": "Isha",
                  "prayerTime": "17:35"
            }
      ]
}
```


### 2. Get Prayer for a Specific Day

**URL**: `/prayer/{day}`

**Method**: `GET`

**Description**: Returns the current prayer time in real time.

**Example Request**:
```bash
GET http://localhost:7860/prayer/150
```

**Example Response**
```bash
{
      "id": 150,
      "hijriCalendar": "21 Dhul-Qa'dah 1446 AH",
      "georgianCalendar": "Wed May 29 2024",
      "dailyPrayers": [
            {
                  "id": 895,
                  "prayerName": "Fajr",
                  "prayerTime": "05:41"
            },
            {
                  "id": 896,
                  "prayerName": "Sunrise",
                  "prayerTime": "08:00"
            },
            {
                  "id": 897,
                  "prayerName": "Zuhr",
                  "prayerTime": "12:06"
            },
            {
                  "id": 898,
                  "prayerName": "Asr",
                  "prayerTime": "13:36"
            },
            {
                  "id": 899,
                  "prayerName": "Maghrib",
                  "prayerTime": "16:02"
            },
            {
                  "id": 900,
                  "prayerName": "Isha",
                  "prayerTime": "18:05"
            }
      ]
}
```


### 3. Get All Prayers for the Year

**URL**: `/prayers`

**Method**: `GET`

**Description**: Returns the list of all prayer times for the entire year.

**Example Request**:
```bash
GET http://localhost:7860/prayers
```

**Example Response**
```bash
{

      "id": 1,
      "hijriCalendar": "20 Jamada-Al-Thani 1446 AH",
      "georgianCalendar": "Mon Jan 1 2024",
      "dailyPrayers": [
            {
                  "id": 1,
                  "prayerName": "Fajr",
                  "prayerTime": "06:05"
            },
            {
                  "id": 2,
                  "prayerName": "Sunrise",
                  "prayerTime": "08:37"
            },
            {
                  "id": 3,
                  "prayerName": "Zuhr",
                  "prayerTime": "11:56"
            },
            {
                  "id": 4,
                  "prayerName": "Asr",
                  "prayerTime": "12:54"
            },
            {
                  "id": 5,
                  "prayerName": "Maghrib",
                  "prayerTime": "15:04"
            },
            {
                  "id": 6,
                  "prayerName": "Isha",
                  "prayerTime": "17:21"
            }
            ...
      ]

}
```


## Running the Application

1. Make sure the correct version of Java is installed (17).
2. Run this command to start up the application.
```bash
./mvnw spring-boot:run 
```
3. The application is responding to request from
```bash
http://localhost:7860/
```
