package front;

import front.test.RichText;
import front.util.Util;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Scene {

	Stage stage;
	
	SideMenu menu;
	StackPane contentPane;
	
	final Writer writerPane = new Writer();
	final RichText rtPane = new RichText();

	public Main(Stage stage, double width, double height, User user, Project proj) {
		super(new StackPane(), 
				Screen.getPrimary().getVisualBounds().getWidth(), 
				Screen.getPrimary().getVisualBounds().getHeight());
		
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
		HBox.setHgrow(contentPane, Priority.ALWAYS);
		
		contentPane.getChildren().add(rtPane);
		
		menu = new SideMenu(Util.clamp(5.0, getWidth()*0.15, 100.0), getHeight());
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
