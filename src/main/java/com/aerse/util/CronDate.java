package com.aerse.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CronDate {

	private final static TimeZone GMT = TimeZone.getTimeZone("GMT");
	private final String pattern;
	private final List<CronDateEntry> entries = new ArrayList<>();
	private final List<Integer> resetFields = new ArrayList<>();
	private Integer overflowField = null;
	private Integer minimumIncrementField = null;

	public CronDate(String pattern) {
		this.pattern = pattern;
		String[] parts = pattern.split(" ");
		if (parts.length != 5) {
			throw new IllegalArgumentException("invalid pattern: " + pattern + " expected: 5");
		}
		List<Integer> minutes = null;
		List<Integer> hours = null;
		List<Integer> months = null;
		List<Integer> days = null;
		if (!parts[0].equals("*")) {
			minutes = fill(parts[0], 0, 59);
			overflowField = Calendar.HOUR_OF_DAY;
		} else {
			minimumIncrementField = Calendar.MINUTE;
		}
		if (!parts[1].equals("*")) {
			hours = fill(parts[1], 0, 23);
			overflowField = Calendar.DAY_OF_MONTH;
		} else if (minimumIncrementField == null) {
			minimumIncrementField = Calendar.HOUR_OF_DAY;
		}
		if (!parts[2].equals("*")) {
			days = fill(parts[2], 1, 31);
			overflowField = Calendar.MONTH;
		}
		if (!parts[3].equals("*")) {
			months = fill(parts[3], 1, 12);
			overflowField = Calendar.YEAR;
		}
		if (minimumIncrementField != null && overflowField != null && minimumIncrementField <= overflowField) {
			minimumIncrementField = null;
		}
		if (minutes == null) {
			resetFields.add(Calendar.MINUTE);
			if (hours == null) {
				resetFields.add(Calendar.HOUR_OF_DAY);
				if (days == null) {
					resetFields.add(Calendar.DAY_OF_MONTH);
				}
			}
		}
		fillEntries(minutes, hours, days, months);
		Collections.sort(entries);
	}

	private void fillEntries(List<Integer> minutes, List<Integer> hours, List<Integer> days, List<Integer> months) {
		List<Integer> dummy = new ArrayList<>(1);
		dummy.add(null);
		if (minutes == null) {
			minutes = dummy;
		}
		if (hours == null) {
			hours = dummy;
		}
		if (days == null) {
			days = dummy;
		}
		if (months == null) {
			months = dummy;
		}
		for (Integer minute : minutes) {
			for (Integer hour : hours) {
				for (Integer day : days) {
					for (Integer month : months) {
						entries.add(new CronDateEntry(minute, hour, day, null, month));
					}
				}
			}
		}
	}

	private static List<Integer> fill(String minute, int min, int max) {
		List<Integer> result = new ArrayList<>();
		if (minute.contains("/")) {
			String[] parts = minute.split("/");
			Integer start;
			Integer end;
			if (parts[0].contains("-")) {
				String[] interval = parts[0].split("-");
				start = Integer.parseInt(interval[0]);
				end = Integer.parseInt(interval[1]);
			} else if (parts[0].contains("*")) {
				start = min;
				end = max;
			} else {
				throw new IllegalArgumentException("invalid slash argument: " + parts[0]);
			}
			Integer increment = Integer.parseInt(parts[1]);
			for (int i = start; i <= end; i += increment) {
				result.add(i);
			}
		} else if (minute.contains("-")) {
			String[] parts = minute.split("-");
			Integer start = Integer.parseInt(parts[0]);
			Integer end = Integer.parseInt(parts[1]);
			for (int i = start; i <= end; i++) {
				result.add(i);
			}
		} else if (minute.contains(",")) {
			String[] parts = minute.split(",");
			for (String cur : parts) {
				result.add(Integer.parseInt(cur));
			}
		} else {
			result.add(Integer.parseInt(minute));
		}
		return result;
	}

	public Date getNext(long currentMillis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(GMT);
		cal.setTimeInMillis(currentMillis);
		CronDateEntry calEntry = new CronDateEntry(cal.get(Calendar.MINUTE), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.DAY_OF_WEEK), cal.get(Calendar.MONTH) + 1);
		int index = Collections.binarySearch(entries, calEntry);
		if (index < 0) {
			index = -(index + 1);
			if (index == 0) {
				resetFields(cal);
			}
		} else {
			if (matchWithIncrementedMinimumIncField(cal, entries.get(index))) {
				cal.add(minimumIncrementField, 1);
			} else {
				index++;
			}
		}
		if (index > entries.size() - 1) {
			if (overflowField != null) {
				cal.add(overflowField, 1);
			}
			resetFields(cal);
			index = 0;
		}
		CronDateEntry resultEntry = entries.get(index);
		setEntry(cal, resultEntry);
		return cal.getTime();
	}

	private static void setEntry(Calendar cal, CronDateEntry resultEntry) {
		if (resultEntry.getMonth() != null) {
			cal.set(Calendar.MONTH, resultEntry.getMonth() - 1);
		}
		if (resultEntry.getDay() != null) {
			cal.set(Calendar.DAY_OF_MONTH, resultEntry.getDay());
		}
		if (resultEntry.getHour() != null) {
			cal.set(Calendar.HOUR_OF_DAY, resultEntry.getHour());
		}
		if (resultEntry.getMinute() != null) {
			cal.set(Calendar.MINUTE, resultEntry.getMinute());
		}
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
	}

	private void resetFields(Calendar cal) {
		for (Integer curReset : resetFields) {
			int resetTo;
			if (curReset == Calendar.DAY_OF_MONTH) {
				resetTo = 1;
			} else {
				resetTo = 0;
			}
			cal.set(curReset, resetTo);
		}
	}

	private boolean matchWithIncrementedMinimumIncField(Calendar cur, CronDateEntry entry) {
		if (minimumIncrementField == null) {
			return false;
		}
		Calendar inc = Calendar.getInstance();
		inc.setTimeZone(GMT);
		inc.setTimeInMillis(cur.getTimeInMillis());
		setEntry(inc, entry);
		inc.add(minimumIncrementField, 1);
		if (entry.getMonth() != null && !compare(Calendar.MONTH, inc, entry.getMonth() - 1)) {
			return false;
		}
		if (entry.getDay() != null && !compare(Calendar.DAY_OF_MONTH, inc, entry.getDay())) {
			return false;
		}
		if (entry.getHour() != null && !compare(Calendar.HOUR_OF_DAY, inc, entry.getHour())) {
			return false;
		}
		if (entry.getMinute() != null && !compare(Calendar.MINUTE, inc, entry.getMinute())) {
			return false;
		}
		return true;
	}

	private static boolean compare(int field, Calendar f, int value) {
		if (f.get(field) != value) {
			return false;
		}
		return true;
	}

	public String getPattern() {
		return pattern;
	}
}
