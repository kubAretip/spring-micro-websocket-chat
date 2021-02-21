package pl.kubaretip.chatservice.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public final class DateUtils {

    public static final String DATE_PATTERN = "dd.MM.yyyy HH:mm:ss";

    public static OffsetDateTime convertStringDateToOffsetTime(String time) {
        var localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(DATE_PATTERN));
        var zoneOffset = TimeZone.getDefault().toZoneId().getRules().getOffset(localDateTime);
        return localDateTime.atOffset(zoneOffset);
    }
}
