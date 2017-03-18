package front;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Settings {

	Stage stage;
	Scene scene;
	
	public static boolean open = false;
	private static Settings current = null;
	
	public Settings() {
		
		if (current == null) {
			current = this;
		} else {
			System.out.println("Too many settings instances");
		}
		
		stage = new Stage();
		stage.setTitle("Settings");
		
		scene = new Scene(new StackPane(), 400, 500);
		stage.setScene(scene);
		
		stage.show();
		
	}
	
	public static void show() {
		if (!open) {
			new Settings();
			open = true;
		}
	}
	
	public static void hide() {
		if(open) {
			current.stage.close();
			current = null;
		}
	}

}
