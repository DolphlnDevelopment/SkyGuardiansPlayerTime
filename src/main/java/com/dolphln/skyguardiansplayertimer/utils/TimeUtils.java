package com.dolphln.skyguardiansplayertimer.utils;

import org.apache.commons.lang3.time.DurationFormatUtils;

public class TimeUtils {

    public static String formatTime(long seconds) {
        return DurationFormatUtils.formatDuration(seconds * 1000L, "d'd' H'h' m'm' s's'");
    }
}
