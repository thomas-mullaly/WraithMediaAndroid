package com.wraithmedia.media.mediatimeformatter.test;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.wraithmedia.media.MediaTimeFormatter;

public class FormatMediaTimeShould extends TestCase {

    private MediaTimeFormatter mMediaTimeFormatter;

    protected void setUp() {
        mMediaTimeFormatter = new MediaTimeFormatter();
    }

    public void testReturn0Colon00WhenPassedATimeOfZero() {
        final String EXPECTED_VALUE = "0:00";

        String result = mMediaTimeFormatter.formatMediaTime(0);

        Assert.assertEquals(EXPECTED_VALUE, result);
    }

    public void testReturn0Colon01WhenPassedATimeOfOneSecond() {
        final String EXPECTED_VALUE = "0:01";

        String result = mMediaTimeFormatter.formatMediaTime(1);

        Assert.assertEquals(EXPECTED_VALUE, result);
    }

    public void testReturn0Colon02WhenPassedATimeOfTwoSeconds() {
        final String EXPECTED_VALUE = "0:02";

        String result = mMediaTimeFormatter.formatMediaTime(2);

        Assert.assertEquals(EXPECTED_VALUE, result);
    }

    public void testReturn1Colon00WhenPassedATimeOfSixtySeconds() {
        final String EXPECTED_VALUE = "1:00";

        String result = mMediaTimeFormatter.formatMediaTime(60);

        Assert.assertEquals(EXPECTED_VALUE, result);
    }

    public void testReturn10Colon55WhenPassedATimeOfSixHundredAndFiftyFiveSeconds() {
        final String EXPECTED_VALUE = "10:55";

        String result = mMediaTimeFormatter.formatMediaTime(655);

        Assert.assertEquals(EXPECTED_VALUE, result);
    }

    public void testShouldThrowIllegalArgumentExceptionWhenPassedANegativeTime() {
        boolean thrown = false;

        try {
            mMediaTimeFormatter.formatMediaTime(-1);
        } catch (IllegalArgumentException ex) {
            thrown = true;
        }

        Assert.assertEquals(true, thrown);
    }
}
