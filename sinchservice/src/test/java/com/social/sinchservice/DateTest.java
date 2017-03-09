package com.social.sinchservice;

import com.social.sinchservice.utils.DateUtils;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Date;

/**
 * Created by valgood on 3/6/2017.
 */

public class DateTest {

    @Test
    public void convertDatesTest() {
        String dateToConvert = "Fri Feb 24 22:57:51 EST 2017";

        Date newDate = DateUtils.convertStringToDate(dateToConvert);
        Assert.assertNotNull("Failed to get Date in Date Object ", newDate);

        String newDateStr = DateUtils.convertDateToString(newDate);
        Assert.assertNotNull("Failed to get Date in String ", newDateStr);
    }
}
