package front;

import front.graph.Plan;
import front.util.Util;
import front.writer.RichText;
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
	final RichText rtPane = new RichText(stage);
	final Plan planPane = new Plan(stage);
	
	private enum ContentType {
		WRITE,
		PLAN,
		NOTE,
		SETTINGS
		;
	}
	

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
		menu.addButton("write", e -> setContent(ContentType.WRITE));
		menu.addButton("plan", e -> setContent(ContentType.PLAN));
		menu.addButton("notes", e -> System.out.println("notes"));
		menu.addButton("settings", e -> System.out.println("settings"));
		menu.addButton("exit", e -> System.out.println("exit"));

		HBox split = new HBox();
		split.getChildren().addAll(menu, contentPane);

		root.getChildren().add(split);
	}
	
	private void setContent(ContentType type) {
		contentPane.getChildren().clear();
		switch(type) {
		case WRITE:
			contentPane.getChildren().add(rtPane);
			break;
		case PLAN:
			contentPane.getChildren().add(planPane);
			break;
		default:
			contentPane.getChildren().add(rtPane);
		}
	}

}
