package front;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application{
	
	public static String TITLE = "WordFrame";

	public static final int MIN_WIDTH = 450;
	public static final int MIN_HEIGHT = 325;
	public static final int MAX_WIDTH = 600;
	public static final int MAX_HEIGHT = 400;
	
	public static void main(String[] args) { launch(args); }

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(MainApplication.TITLE);
		
		primaryStage.setMinWidth(MIN_WIDTH);
		primaryStage.setMinHeight(MIN_HEIGHT);
		primaryStage.setMaxWidth(MAX_WIDTH);
		primaryStage.setMaxHeight(MAX_HEIGHT);
		
		LoginMenu loginMenu = new LoginMenu(primaryStage, MAX_WIDTH, MAX_HEIGHT);
		primaryStage.setScene(loginMenu);
		primaryStage.show();
	}
	
	
	
	
}
