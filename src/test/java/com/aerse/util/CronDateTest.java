package com.aerse.util;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class CronDateTest {

	private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
	private CronDate date;
	
	@Test
	public void testEveryMinute() {
		date = new CronDate("* * * * *");
		assertDate("2013 01 01 00:01:00", "2013 01 01 00:00:00");
		assertDate("2013 01 01 00:03:00", "2013 01 01 00:02:30");
	}

	@Test
	public void testEveryMinuteInMonth() {
		date = new CronDate("* * * 1 *");
		assertDate("2013 01 01 00:01:00", "2013 01 01 00:00:00");
		assertDate("2013 01 01 00:02:00", "2013 01 01 00:01:00");
		assertDate("2013 01 01 01:00:00", "2013 01 01 00:59:00");
		assertDate("2013 01 02 00:00:00", "2013 01 01 23:59:00");
		assertDate("2014 01 01 00:00:00", "2013 01 31 23:59:00");
	}
	
	@Test
	public void testMonthDayHourMinute() {
		date = new CronDate("0 0 1 1 *");
		assertDate("2014 01 01 00:00:00", "2013 12 31 09:59:59");
		assertDate("2014 01 01 00:00:00", "2013 01 01 09:59:59");
	}
	
	@Test
	public void testOverflowFromMinuteToHours() {
		date = new CronDate("* * 1 * *");
		assertDate("2013 01 01 10:00:00", "2013 01 01 09:59:59");
	}

	@Test
	public void testOverflowFromMinuteToDays() {
		date = new CronDate("* * * 1 *");
		assertDate("2013 01 02 00:00:00", "2013 01 01 23:59:59");
	}

	@Test
	public void testOnlyHour() {
		date = new CronDate("* 10 * * *");
		assertDate("2013 01 01 10:00:00", "2013 01 01 09:59:59");
		assertDate("2013 01 01 10:03:00", "2013 01 01 10:02:59");
		assertDate("2013 01 02 10:00:00", "2013 01 01 10:59:59");
	}

	@Test
	public void testMinuteHourInterval() {
		date = new CronDate("*/5 10 * * *");
		assertDate("2013 01 01 10:00:00", "2013 01 01 09:04:01");
		assertDate("2013 01 01 10:05:00", "2013 01 01 10:01:00");
		assertDate("2013 01 01 10:40:00", "2013 01 01 10:36:00");
		assertDate("2013 01 02 10:00:00", "2013 01 01 10:59:00");
	}

	@Test
	public void testMinuteHourEnum() {
		date = new CronDate("15 10,13 * * *");
		assertDate("2013 01 01 10:15:00", "2013 01 01 09:04:00");
		assertDate("2013 01 01 13:15:00", "2013 01 01 10:15:59");
		assertDate("2013 01 01 13:15:00", "2013 01 01 10:16:00");
		assertDate("2013 01 02 10:15:00", "2013 01 01 13:20:00");
	}

	@Test
	public void testMinuteInterval1() {
		date = new CronDate("*/5 * * * *");
		assertDate("2013 01 01 00:05:00", "2013 01 01 00:04:00");
		assertDate("2013 01 01 00:10:00", "2013 01 01 00:05:00");
		assertDate("2013 01 01 00:10:00", "2013 01 01 00:06:00");
		assertDate("2013 01 01 01:00:00", "2013 01 01 00:56:00");
	}

	@Test
	public void testMinuteInterval2() {
		date = new CronDate("10-20/5 * * * *");
		assertDate("2013 01 01 00:10:00", "2013 01 01 00:04:00");
		assertDate("2013 01 01 00:15:00", "2013 01 01 00:10:00");
		assertDate("2013 01 01 00:20:00", "2013 01 01 00:16:00");
		assertDate("2013 01 01 01:10:00", "2013 01 01 00:21:00");
	}

	@Test
	public void testMinuteEnum() {
		date = new CronDate("10,35 * * * *");
		assertDate("2013 01 01 00:10:00", "2013 01 01 00:05:00");
		assertDate("2013 01 01 00:35:00", "2013 01 01 00:10:00");
		assertDate("2013 01 01 00:35:00", "2013 01 01 00:11:00");
		assertDate("2013 01 01 01:10:00", "2013 01 01 00:35:00");
		assertDate("2013 01 01 01:10:00", "2013 01 01 00:36:00");
	}

	@Test
	public void testMinuteRange() {
		date = new CronDate("10-20 * * * *");
		assertDate("2013 01 01 00:10:00", "2013 01 01 00:05:00");
		assertDate("2013 01 01 00:11:00", "2013 01 01 00:10:00");
		assertDate("2013 01 01 00:12:00", "2013 01 01 00:11:00");
		assertDate("2013 01 01 01:10:00", "2013 01 01 00:20:00");
	}

	@Test
	public void testMinuteExplicit() {
		date = new CronDate("10 * * * *");
		assertDate("2013 01 01 00:10:00", "2013 01 01 00:05:00");
		assertDate("2013 01 01 01:10:00", "2013 01 01 00:10:00");
		assertDate("2013 01 01 01:10:00", "2013 01 01 00:15:00");
	}

	private void assertDate(String expected, String current) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
		sdf.setTimeZone(GMT);
		try {
			Date currentDate = sdf.parse(current);
			Date expectedDate = sdf.parse(expected);
			Date result = date.getNext(currentDate.getTime());
			Calendar resultCal = Calendar.getInstance();
			resultCal.setTimeZone(GMT);
			resultCal.setTimeInMillis(result.getTime());
			Calendar expectedCal = Calendar.getInstance();
			expectedCal.setTimeZone(GMT);
			expectedCal.setTime(expectedDate);

			assertEquals(expectedCal.get(Calendar.YEAR), resultCal.get(Calendar.YEAR));
			assertEquals(expectedCal.get(Calendar.MONTH), resultCal.get(Calendar.MONTH));
			assertEquals(expectedCal.get(Calendar.DAY_OF_MONTH), resultCal.get(Calendar.DAY_OF_MONTH));
			assertEquals(expectedCal.get(Calendar.HOUR_OF_DAY), resultCal.get(Calendar.HOUR_OF_DAY));
			assertEquals(expectedCal.get(Calendar.MINUTE), resultCal.get(Calendar.MINUTE));
			assertEquals(expectedCal.get(Calendar.SECOND), resultCal.get(Calendar.SECOND));
		} catch (ParseException e) {
			throw new RuntimeException();
		}
	}

}
