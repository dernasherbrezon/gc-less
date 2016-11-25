package com.aerse.util;

class CronDateEntry implements Comparable<CronDateEntry> {

	private final Integer minute;
	private final Integer hour;
	private final Integer day;
	private final Integer dayOfWeek;
	private final Integer month;

	CronDateEntry(Integer minute, Integer hour, Integer day, Integer dayOfWeek, Integer month) {
		super();
		this.minute = minute;
		this.hour = hour;
		this.day = day;
		this.dayOfWeek = dayOfWeek;
		this.month = month;
	}

	public Integer getMinute() {
		return minute;
	}

	public Integer getHour() {
		return hour;
	}

	public Integer getDay() {
		return day;
	}

	public Integer getDayOfWeek() {
		return dayOfWeek;
	}

	public Integer getMonth() {
		return month;
	}

	@Override
	public int compareTo(CronDateEntry o) {
		if (month != null && o.month != null) {
			int monthCompareResult = month.compareTo(o.month);
			if (monthCompareResult != 0) {
				return monthCompareResult;
			}
		}
		boolean hasDay = day != null && o.day != null;
		boolean hasDayOfWeek = dayOfWeek != null && o.dayOfWeek != null;
		if (hasDay && !hasDayOfWeek) {
			int dayCompareResult = day.compareTo(o.day);
			if (dayCompareResult != 0) {
				return dayCompareResult;
			}
		}
		if (!hasDay && hasDayOfWeek) {
			int dayOfWeekCompareResult = dayOfWeek.compareTo(o.dayOfWeek);
			if (dayOfWeekCompareResult != 0) {
				return dayOfWeekCompareResult;
			}
		}
		if (hasDay && hasDayOfWeek) {
			int dayCompareResult = day.compareTo(o.day);
			int dayOfWeekCompareResult = dayOfWeek.compareTo(o.dayOfWeek);
			if (dayCompareResult != 0 && dayOfWeekCompareResult != 0) {
				return dayCompareResult;
			}
		}
		if (hour != null && o.hour != null) {
			int hourCompareResult = hour.compareTo(o.hour);
			if (hourCompareResult != 0) {
				return hourCompareResult;
			}
		}
		if (minute != null && o.minute != null) {
			int minuteCompareResult = minute.compareTo(o.minute);
			if (minuteCompareResult != 0) {
				return minuteCompareResult;
			}
		}
		return 0;
	}
}
