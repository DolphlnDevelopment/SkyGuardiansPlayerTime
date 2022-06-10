package com.dolphln.skyguardiansplayertimer.core;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class CustomTimestamp {

    private LocalDateTime time;
    private ZoneId zoneId = TimeZone.getTimeZone("GMT").toZoneId();

    public CustomTimestamp() {
        this.time = getDateTimeNow();
    }

    public CustomTimestamp(long timestamp) {
        this.time = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), zoneId);
    }

    public long toTimestamp() {
        return this.time.atZone(zoneId).toEpochSecond();
    }

    private LocalDateTime getDateTimeNow() {
        return LocalDateTime.now(zoneId);
    }

    private long getTimestampNow() {
        return getDateTimeNow().atZone(zoneId).toEpochSecond();
    }

    public long getDifference() {
        return getTimestampNow()-toTimestamp();
    }

    @Override
    public String toString() {
        System.out.println(this.time.toString());
        return this.time.format(DateTimeFormatter.ofPattern("dd'd' HH'h' mm'm' ss's'"));
    }

}
