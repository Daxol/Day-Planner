package Gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class GuiLayoutNewTask extends VBox {

	public GuiLayoutNewTask() {
		vbox();
	}

	public static VBox vbox() {
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(15, 15, 15, 15));
		vbox.setSpacing(10);
		vbox.getStyleClass().add("default_bg");

		Button btn_add_task = new Button("Dodaj zadanie");
		btn_add_task.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				GuiLayoutNewTaskWindow.GuiLayoutNewTaskWindowShow();
			}
		});
		vbox.getChildren().add(btn_add_task);
		vbox.setAlignment(Pos.CENTER);
		return vbox;

	}

}
