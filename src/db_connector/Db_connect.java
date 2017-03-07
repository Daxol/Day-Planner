package db_connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import User.Task;
import User.TaskHandlerTime;

public class Db_connect {

	public static final String DRIVER = "org.sqlite.JDBC";
	public static final String DB_URL = "jdbc:sqlite:dayPlannerDB.db";

	private Connection sql_connection;
	private Statement sql_statement;

	public Db_connect() {
		try {
			Class.forName(Db_connect.DRIVER);
		} catch (ClassNotFoundException e) {
			System.err.println("there is no JDBC driver");
			e.printStackTrace();
		}

		try {
			sql_connection = DriverManager.getConnection(DB_URL);
			sql_statement = sql_connection.createStatement();
		} catch (SQLException e) {
			System.err.println("Error with open connection");
			e.printStackTrace();
		}
		createTables();
		check_task_status_db();
	}

	public boolean createTables() {
		String createTableTask = "CREATE TABLE IF NOT EXISTS Tasks " + "(id_task INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " description_of_task varchar(255), time_for_task_in_days int,"
				+ "start_task_date varchar(255),end_task_date varchar(255)," + "taskStatus int)";
		try {
			sql_statement.execute(createTableTask);
		} catch (SQLException e) {
			System.err.println("error: cant create table");
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public boolean insertTask(Task task) {
		try {
			PreparedStatement preparedStatement = sql_connection.prepareStatement(
					"insert into" + " tasks (description_of_task,time_for_task_in_days,start_task_date,"
							+ "taskStatus) values (?,?,?,?)");

			preparedStatement.setString(1, task.get_description_of_task());
			preparedStatement.setInt(2, task.get_time_for_task_in_days());
			preparedStatement.setString(3, task.get_start_task_date_String());
			preparedStatement.setInt(4, task.get_task_status());
			preparedStatement.execute();
		} catch (SQLException e) {
			System.err.println("error:cant add new task");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public List<Task> get_task_obj() {

		try {

			ResultSet result_set = sql_statement.executeQuery("select * from tasks");
			List<Task> list_tasks = new ArrayList<Task>();
			while (result_set.next()) {
				list_tasks.add(new Task(result_set.getInt(1), result_set.getString(2), result_set.getInt(3),
						result_set.getString(4), result_set.getString(5), result_set.getInt(6)));

			}

			list_tasks.sort(new CustomComparator() {
			});
			return list_tasks;

		} catch (SQLException e) {
			System.err.println("cant load task to list");
			e.printStackTrace();
		}
		return null;
	}

	private Boolean sql_operation(String query) {
		try {
			sql_statement.executeUpdate(query);

			return true;
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
			return false;
		}
	}

	public void delete_task_from_db(int id_task) {
		sql_operation("DELETE FROM tasks WHERE id_task =" + id_task + "");
	}

	private void update_task_from_db(int id_task, String position_for_update, String value) {
		sql_operation("UPDATE tasks SET " + position_for_update + "='" + value + "' WHERE id_task=" + id_task + "");
	}

	public void update_task_status_db(int id_task, int status) {
		update_task_from_db(id_task, "taskStatus", Integer.toString(status));
	}

	public void update_task_start_date_db(int id_task, String start_date) {
		update_task_from_db(id_task, "start_task_date", start_date);
	}

	public void update_task_end_date_db(int id_task, String end_date) {
		update_task_from_db(id_task, "end_task_date", end_date);
	}

	public void finish_task_db(Task task) {
		update_task_status_db(task.get_id_task(), task.get_task_status());
		if (task.get_task_status() != 3) {
			update_task_end_date_db(task.get_id_task(), TaskHandlerTime.dateTime_to_String(task.getEnd_task_date()));
		}
	}

	private void check_task_status_db() {
		for (Task task : get_task_obj()) {
			if (TaskHandlerTime.update_status(task)) {
				update_task_status_db(task.get_id_task(), task.get_task_status());
			}
		}
	}

	public void closeConnection() {
		try {
			sql_connection.close();
		} catch (SQLException e) {
			System.err.println("error: cant close connection");
			e.printStackTrace();
		}
	}

}

abstract class CustomComparator implements Comparator<Task> {
	@Override
	public int compare(Task o1, Task o2) {
		return o1.get_start_task_date().compareTo(o2.get_start_task_date());
	}
}