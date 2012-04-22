package com.wraithmedia.media;

public class MediaTimeFormatter {
    private final String COLON_SEPARATOR = ":";
    private final String COLON_ZERO_SEPARATOR = ":0";

    public String formatMediaTime(int timeInSeconds) throws IllegalArgumentException {
        if (timeInSeconds < 0) throw new IllegalArgumentException();

        int numberOfMinutes = timeInSeconds / 60;

        int remainingSeconds = timeInSeconds - (numberOfMinutes * 60);

        String separator = COLON_SEPARATOR;
        if (remainingSeconds < 10) {
            separator = COLON_ZERO_SEPARATOR;
        }
        return numberOfMinutes + separator + remainingSeconds;
    }
}
