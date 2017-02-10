package front;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Scene {

	Stage stage;
	
	SideMenu menu;
	StackPane contentPane;

	public Main(Stage stage, double width, double height, User user, Project proj) {
		super(new StackPane(), width, height);
		
		this.stage = stage;
		stage.setMaxWidth(Double.MAX_VALUE);
		stage.setMaxHeight(Double.MAX_VALUE);
		stage.setMaximized(true);
		
		buildUI();
	}

	void buildUI() {
		StackPane root = (StackPane) getRoot();
		getStylesheets().add(Main.class.getResource("Main.css").toExternalForm());
		
		contentPane = new StackPane();
		
		menu = new SideMenu(getWidth()*0.15, getHeight());
		menu.addButton("write", e -> System.out.println("write"));
		menu.addButton("plan", e -> System.out.println("plan"));
		menu.addButton("notes", e -> System.out.println("notes"));
		menu.addButton("settings", e -> System.out.println("settings"));
		menu.addButton("exit", e -> System.out.println("exit"));

		HBox split = new HBox();
		split.getChildren().addAll(menu, contentPane);

		root.getChildren().add(split);
	}

}
