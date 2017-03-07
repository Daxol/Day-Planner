package User;

import Gui.Gui_planner;
import javafx.application.Application;


public abstract class DayPlanner extends Application {

	public static void main(String[] args) {

		try {
			launch(Gui_planner.class, args);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	

		System.exit(0);

	}

	
}
