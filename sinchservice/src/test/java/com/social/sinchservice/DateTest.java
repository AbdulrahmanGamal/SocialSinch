package com.social.sinchservice;

import android.os.Build;
import com.social.sinchservice.utils.DateUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

/**
 * Created by valgood on 3/6/2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = "src/main/AndroidManifest.xml",
        packageName = "com.social.sinchservice")
public class DateTest {

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
}
