package com.social.backendless;

import android.os.Build;

import com.example.jorgevalbuena.backendless.BuildConfig;
import com.social.backendless.utils.DateUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

/**
 * Unit Test for DateUtils class
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = "src/main/AndroidManifest.xml",
        packageName = "com.social.sinchservice")
public class DateUtilsTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        // Empty for now
    }
    @Test
    public void convertDatesTest() {
        String dateToConvert = "Fri Feb 24 22:57:51 EST 2017";

        Date newDate = DateUtils.convertStringToDate(dateToConvert);
        Assert.assertNotNull("Failed to get Date in Date Object ", newDate);

        String newDateStr = DateUtils.convertDateToString(newDate);
        Assert.assertNotNull("Failed to get Date in String ", newDateStr);
    }

    @Test
    public void convertDateToLastSeenFormat() {
        //long time ago
        String lastSeenFormat = DateUtils.convertDateToLastSeenFormat("Mon Jul 25 11:26:32 EDT 2016");
        Assert.assertEquals("Last Seen date not equals", lastSeenFormat, "Jul 25, 2016");
        //yesterday
        lastSeenFormat = DateUtils.convertDateToLastSeenFormat("Mon Mar 18 11:26:32 EDT 2017");
        Assert.assertEquals("Last Seen date not equals", lastSeenFormat, "yesterday at 11:26 AM");
        //today
        lastSeenFormat = DateUtils.convertDateToLastSeenFormat("Sun Mar 19 11:26:32 EDT 2017");
        Assert.assertEquals("Last Seen date not equals", lastSeenFormat, "today at 11:26 AM");

    }
}