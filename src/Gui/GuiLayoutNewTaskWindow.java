package Gui;

import java.time.LocalDate;
import User.Task;
import db_connector.Db_connect;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class GuiLayoutNewTaskWindow {

	private static TextField description_of_task_textfield;
	private static TextField days_for_task_textfield;
	private static Button btn_add_task_action;
	private static DatePicker date_picker;

	public GuiLayoutNewTaskWindow() {
		GuiLayoutNewTaskWindowShow();
	}

	public static void GuiLayoutNewTaskWindowShow() {

		final Stage new_task_window = new Stage();
		new_task_window.setTitle("Dodaj zadanie");
		new_task_window.initModality(Modality.APPLICATION_MODAL);

		VBox new_task_windowVbox = new VBox();
		new_task_windowVbox.setPadding(new Insets(20, 20, 20, 20));
		new_task_windowVbox.setSpacing(10);
		new_task_windowVbox.setAlignment(Pos.CENTER);
		new_task_window.setResizable(false);
		new_task_windowVbox.setShape(new Circle(1.5));
		new_task_windowVbox.setPadding(new Insets(15));

		Label lbl_description_of_task = new Label("Opis zadania");
		Label lbl_days_for_task = new Label("Iloœæ dni na zadanie");

		description_of_task_textfield = new TextField();

		days_for_task_textfield = new TextField();
		days_for_task_textfield.setMaxSize(50, 20);

		date_picker = new DatePicker();
		date_picker.setEditable(false);
		date_picker.setValue(LocalDate.now());
		date_picker.setDayCellFactory(getDayCellFactory());
		date_picker.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				LocalDate date = date_picker.getValue();
				System.err.println("Selected date: " + date);

			}
		});

		btn_add_task_action = new Button("Dodaj");

		new_task_windowVbox.getChildren().addAll(lbl_description_of_task, description_of_task_textfield,
				lbl_days_for_task, days_for_task_textfield, date_picker, btn_add_task_action);

		Scene new_task_windowScene = new Scene(new_task_windowVbox, 300, 250);
		new_task_window.setScene(new_task_windowScene);
		new_task_window.show();

		btn_add_task_action.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try_add_new_task();

			}
		});
	}

	private static void try_add_new_task() {
		try {
			add_new_task();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();

		}

	}

	private static void add_new_task() {

		try {
			Task new_task = new Task(get_task_description_gui(), get_days_for_task_gui(), get_task_start_date_gui());
			Db_connect connect_db = new Db_connect();
			connect_db.insertTask(new_task);
			connect_db.closeConnection();
			GuiLayoutListTask.createPageFactory();
			close_new_task_window();
			Gui_planner.reload_s();
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	private static void show_alert_number_days() {
		Alert alert = new Alert(Alert.AlertType.NONE);
		alert.getButtonTypes().add(ButtonType.CLOSE);
		alert.setTitle("B³êdna iloœæ dni");
		alert.setContentText(
				"Iloœæ dni na zadanie musi zawieraæ siê w przedziale od 1 do 5, przydzielono jeden dzieñ na zadanie");
		alert.show();
	}

	private static void close_new_task_window() {
		Stage stage = (Stage) btn_add_task_action.getScene().getWindow();
		stage.close();
	}

	private static String get_task_description_gui() {
		try {
			return description_of_task_textfield.getText();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return "";
	}

	private static Integer get_days_for_task_gui() {
		try {
			return Integer.parseInt(days_for_task_textfield.getText());
		} catch (Exception e) {
			System.err.println("z get days catch");
			show_alert_number_days();

		}
		return 1;
	}

	private static String get_task_start_date_gui() {

		try {

			return date_picker.getValue().toString();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return null;
	}

	private static Callback<DatePicker, DateCell> getDayCellFactory() {

		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {

			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						if (item.isBefore(LocalDate.now())) {
							setDisable(true);
							setStyle("-fx-background-color: #ffc0cb;");
						}
					}
				};
			}
		};
		return dayCellFactory;
	}

}