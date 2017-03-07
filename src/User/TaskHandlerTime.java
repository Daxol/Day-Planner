package User;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TaskHandlerTime {

	protected static String get_dateTime_formatted_today() {
		return dateTime_to_String(new DateTime());
	}

	public static DateTimeFormatter dateTimeFormatter() {
		try {
			return DateTimeFormat.forPattern("yyyy-MM-dd");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public static DateTime string_to_DateTime(String date_to_parse) {
		if (date_to_parse == null)
			return null;
		try {
			return dateTimeFormatter().parseDateTime(date_to_parse);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
		return null;
	}

	public static String dateTime_to_String(DateTime dateTime_to_parse) {
		if (dateTime_to_parse == null)
			return null;
		try {
			return dateTime_to_parse.toString(dateTimeFormatter());
		} catch (Exception e) {
			System.err.println(e);
		}
		return null;
	}

	public static int days_after_start_task(Task task) {

		return Days.daysBetween(new DateTime(task.get_start_task_date()), new DateTime()).getDays();
	}

	public static Boolean update_status(Task task) {
		if (new DateTime().isAfter(task.get_start_task_date())
				&& task.get_time_for_task_in_days() > days_after_start_task(task) && task.get_task_status() != 2
				&& task.get_task_status() != 1) {
			task.start_task_status();
			return true;
		} else if (task.get_time_for_task_in_days() <= days_after_start_task(task) && task.get_task_status() != 3) {
			task.fail_task();
			return true;

		} else
			return false;
	}

}
