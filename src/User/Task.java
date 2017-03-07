package User;

import org.joda.time.DateTime;

public class Task {

	private int id_task;
	private String description_of_task;
	private int task_status;
	private DateTime start_task_date;
	private DateTime end_task_date;
	private int time_for_task_in_days;

	public Task(String description_of_task, int time_for_task_in_days, String start_task_date_string) {
		this.description_of_task = description_of_task;
		this.start_task_date = TaskHandlerTime.string_to_DateTime(start_task_date_string);
		this.time_for_task_in_days = time_for_task_in_days;
		task_status = 0;

	}

	public Task(int id, String description_of_task, int time_for_task_in_days, String start_task_date,
			String end_task_date, int task_status) {
		this.id_task = id;

		this.description_of_task = description_of_task;
		this.task_status = task_status;
		this.start_task_date = TaskHandlerTime.string_to_DateTime(start_task_date);
		this.end_task_date = TaskHandlerTime.string_to_DateTime(end_task_date);
		this.time_for_task_in_days = time_for_task_in_days;

	}

	public String toString() {
		return " - description: " + description_of_task + " - status: " + task_status + " - start date: "
				+ get_start_task_date_String() + " - time for task: " + time_for_task_in_days;

	}

	protected void is_time_for_task_finished() {
		if (time_for_task_in_days <= TaskHandlerTime.days_after_start_task(this)) {
			fail_task();
		}

	}

	public void fail_task() {
		this.task_status = 3;

	}

	public DateTime get_start_task_date() {
		return start_task_date;
	}

	public String get_start_task_date_String() {
		return TaskHandlerTime.dateTime_to_String(start_task_date);
	}

	public void set_start_task_date() {
		this.start_task_date = new DateTime();
	}

	public DateTime getEnd_task_date() {
		return end_task_date;
	}

	public void set_end_task_date(DateTime end_task_date) {
		this.end_task_date = end_task_date;
	}

	public String get_description_of_task() {
		return description_of_task;
	}

	public void set_description_of_task(String description_of_task) {
		this.description_of_task = description_of_task;
	}

	public void start_task_status() {
		this.task_status = 1;

	}

	public int get_task_status() {
		return task_status;
	}

	public String get_task_status_string() {
		String status_string = "";
		switch (this.task_status) {
		case 0:
			status_string = "oczekuj¹ce";
			break;
		case 1:
			status_string = "rozpoczête";
			break;
		case 2:
			status_string = "zakoñczone";
			break;
		case 3:
			status_string = "nieukoñczone";
		}
		return status_string;
	}

	public void finish_task() {
		this.task_status = 2;
		set_end_task_date(new DateTime());

	}

	public int get_time_for_task_in_days() {
		return time_for_task_in_days;
	}

	/**
	 * @return the id_task(id is generate in DB)
	 */
	public int get_id_task() {
		return id_task;
	}

	/**
	 * @param id_task
	 *            should be generated in DB
	 */
	public void set_id_task(int id_task) {
		this.id_task = id_task;
	}

}
