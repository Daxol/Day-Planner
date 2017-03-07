package Gui;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import org.joda.time.DateTime;
import User.Task;
import db_connector.Db_connect;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.TextArea;
import javafx.util.Callback;

public class GuiLayoutListTask extends VBox {

	static Label lbl_name_of_task;
	static Label lbl_description_of_task;
	static Label lbl_status_of_task;
	static Label lbl_rating_of_task;
	static Label lbl_days_for_task;
	static Label lbl_delete_task;
	static Label lbl_finish_task;

	static Text txt_day_today;
	static Text txt_day_tomorrow;

	static VBox main_vbox_container;
	static HBox row_of_task_list;
	static ScrollPane scroll_page_task;

	static Db_connect db_con;

	public static VBox generate_gridpane_page(int page_index) {
		db_con = new Db_connect();

		Label lbl_description_of_task = new Label("Opis");
		lbl_description_of_task.getStyleClass().add("text_description");
		lbl_description_of_task.setMinWidth(290);

		Label lbl_status_of_task = new Label("Status");
		lbl_status_of_task.setMinSize(60, 20);

		Label lbl_delete_task = new Label("Zakoñcz");
		lbl_delete_task.setMinSize(40, 20);

		Label lbl_finish_task = new Label("Usuñ");
		lbl_finish_task.setMinSize(50, 20);

		HBox first_row = new HBox();
		first_row.getStyleClass().add("hbox");
		first_row.getChildren().addAll(lbl_description_of_task, lbl_status_of_task, lbl_finish_task, lbl_delete_task);
		first_row.autosize();

		VBox column_of_task_list = new VBox();
		column_of_task_list.getChildren().add(first_row);

		for (int i = 0; i < db_con.get_task_obj().size(); i++) {

			if (i < db_con.get_task_obj().size()
					&& (DateTime.now().plusDays(page_index).getDayOfYear() == db_con.get_task_obj().get(i)
							.get_start_task_date().getDayOfYear()
							|| (DateTime.now().plusDays(page_index)
									.isAfter(db_con.get_task_obj().get(i).get_start_task_date()) && DateTime.now()
											.plusDays(page_index)
											.isBefore(db_con.get_task_obj().get(i).get_start_task_date().plusDays(
													db_con.get_task_obj().get(i).get_time_for_task_in_days()))))
					&& (db_con.get_task_obj().get(i).get_task_status() < 2)) {

				row_of_task_list = new HBox();

				TextArea description_of_task = new TextArea();
				description_of_task.setMaxHeight(10);
				description_of_task.setWrapText(true);
				description_of_task.setMaxWidth(290);
				description_of_task.setEditable(false);
				description_of_task.setText(db_con.get_task_obj().get(i).get_description_of_task());

				Button btn_delete_task = new Button("\u2718");
				btn_delete_task.getStyleClass().add("del_btn");
				btn_delete_task.setOnAction(new PageActionListener(db_con.get_task_obj().get(i).get_id_task()));

				Button btn_finish_task = new Button("\u2713");
				btn_finish_task.getStyleClass().add("check_btn");
				btn_finish_task.setOnAction(new TaskFinishActionListener(db_con.get_task_obj().get(i)));

				row_of_task_list.getChildren().addAll(description_of_task,
						new Text(db_con.get_task_obj().get(i).get_task_status_string()), btn_delete_task,
						btn_finish_task);
				row_of_task_list.getStyleClass().add("hbox_description");

				column_of_task_list.getChildren().add(row_of_task_list);
			}

		}

		column_of_task_list.getStylesheets().add("myStyle.css");
		return column_of_task_list;
	}

	public static Node createPage(int page_index) {
		main_vbox_container = new VBox();
		main_vbox_container.setAlignment(Pos.TOP_CENTER);
		scroll_page_task = new ScrollPane(generate_gridpane_page(page_index));
		scroll_page_task.setMinWidth(300);
		scroll_page_task.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll_page_task.getStyleClass().add("default_bg");
		scroll_page_task.setFitToHeight(true);
		scroll_page_task.setFitToWidth(true);
		scroll_page_task.autosize();

		txt_day_today = new Text(day_and_month_today(page_index));
		txt_day_today.getStyleClass().add("date_task");

		main_vbox_container.getChildren().addAll(txt_day_today, scroll_page_task);
		main_vbox_container.getStyleClass().add("hbox_description1");
		return main_vbox_container;
	}

	protected static void refresh() {

		Gui_planner.reload_s();
	}

	private static String day_and_month_today(int index) {
		int date_today_day = new DateTime().plusDays(index).getDayOfMonth();
		int date_today_month = new DateTime().plusDays(index).getMonthOfYear();
		return date_today_day + " " + get_name_of_month_pl(date_today_month);

	}

	private static String get_name_of_month_pl(int month) {
		String month_name = Month.of(month).getDisplayName(TextStyle.FULL_STANDALONE, new Locale("pl", "PL"));
		return month_name;
	}

	public static void delete_task_action(int id_task) {
		db_con.delete_task_from_db(id_task);

	}

	public static void finish_task_action(Task task) {
		db_con.finish_task_db(task);

	}

	public static Callback<Integer, Node> createPageFactory() {
		return new Callback<Integer, Node>() {
			@Override
			public Node call(Integer pageIndex) {
				if (pageIndex > 15) {
					return null;
				} else {
					return createPage(pageIndex);

				}

			}

		};

	}
}

class PageActionListener implements EventHandler<ActionEvent> {
	private int page;

	public PageActionListener(int page) {
		this.page = page;

	}

	@Override
	public void handle(ActionEvent event) {
		GuiLayoutListTask.delete_task_action(page);
		GuiLayoutListTask.refresh();

	}

}

class TaskFinishActionListener implements EventHandler<ActionEvent> {
	private Task task;

	public TaskFinishActionListener(Task task) {
		this.task = task;

	}

	@Override
	public void handle(ActionEvent event) {
		try {
			task.finish_task();
			GuiLayoutListTask.finish_task_action(task);
			GuiLayoutListTask.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}

	}

}
