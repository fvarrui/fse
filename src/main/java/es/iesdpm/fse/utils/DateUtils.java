package es.iesdpm.fse.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static LocalDate dateToLocalDate(Date date) {
		if (date == null)
			return null;
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date localDateToDate(LocalDate localDate) {
		if (localDate == null)
			return null;
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static int lastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, 0);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static int getWeekday(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);
		int weekday = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return (weekday == 0) ? 7 : weekday;
	}

	public static boolean isWeekend(int year, int month, int day) {
		int weekday = getWeekday(year, month, day);
		return (weekday == 6 || weekday == 7);
	}

	public static boolean isWeekend(LocalDate date) {
		return isWeekend(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
	}

	public static int getWeekday(LocalDate date) {
		return getWeekday(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
	}

}
