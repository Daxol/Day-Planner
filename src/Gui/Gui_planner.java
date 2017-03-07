package Gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Gui_planner extends Application {

	static BorderPane border;
	protected static Pagination pagination;
	private static Scene main_scene;
	private VBox topMenu = GuiLayoutNewTask.vbox();

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {

			pagination = new Pagination(30, 0);
			pagination.setPageFactory(GuiLayoutListTask.createPageFactory());
			pagination.autosize();

			border = new BorderPane();
			border.setTop(topMenu);
			border.setCenter(pagination);

			main_scene = new Scene(border);

			primaryStage.setTitle("Day Planner");
			primaryStage.setWidth(560);
			primaryStage.setHeight(630);
			primaryStage.setScene(main_scene);
			
			main_scene.getStylesheets().add("myStyle.css");
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			return;
		}

	}

	public static void reload_s() {

		int page_index = pagination.getCurrentPageIndex();
		border.getChildren().remove(1);
		pagination = new Pagination(30, page_index);
		pagination.setPageFactory(GuiLayoutListTask.createPageFactory());
		border.setCenter(pagination);

	}

	@Override
	public void stop() throws Exception {
		super.stop();

		Platform.exit();
		System.exit(0);
	}

}
