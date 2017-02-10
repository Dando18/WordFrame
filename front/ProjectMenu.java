package front;

import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ProjectMenu extends Scene {
	
	Stage stage;
	User user;

	public ProjectMenu(Stage stage, double width, double height, User user) {
		super(new StackPane(), width, height);
		
		this.stage = stage;
		this.user = user;
		
		buildUI();
		
	}
	
	private void buildUI() {
		StackPane root = (StackPane) getRoot();
		getStylesheets().add(ProjectMenu.class.getResource("Projects.css").toExternalForm());
		
		
		GridPane grid = new GridPane();
		grid.getStyleClass().add("grid");
		grid.setGridLinesVisible(false);
		root.getChildren().add(grid);
		
		RowConstraints titleRow = new RowConstraints();
		titleRow.setPercentHeight(20);
		titleRow.setVgrow(Priority.ALWAYS);
		RowConstraints projectRow = new RowConstraints();
		projectRow.setPercentHeight(35);
		projectRow.setVgrow(Priority.ALWAYS);
		grid.getRowConstraints().addAll(titleRow, projectRow, projectRow);
		
		int cols = (int) Math.ceil(user.projects.size() / 2) + 1;
		ColumnConstraints col = new ColumnConstraints();
		col.setHalignment(HPos.CENTER);
		col.setPercentWidth(100.0 / cols);
		col.setHgrow(Priority.ALWAYS);
		for(int i=0; i<cols; i++) {
			grid.getColumnConstraints().add(col);
		}
		
		Text title = new Text(MainApplication.TITLE);
		title.getStyleClass().add("title");
		grid.add(title, 0, 0, cols, 1);
		
		for(int i=0; i<=user.projects.size(); i++) {
			if (i == user.projects.size()) {
				Card add_card = new Card("+", "New Project", e -> System.out.println("create new project..."));
				grid.add(add_card, (int) Math.ceil(i / 2), i%2+1);
				break;
			}
			
			Project cur = user.projects.get(i);
			Card project_card = new Card(cur.name, cur.description, e -> projectPressed(cur));
			grid.add(project_card, (int) Math.ceil(i / 2), i%2+1); 
		}
	
	}
	
	private void projectPressed(Project proj) {
		stage.setScene(new Main(stage, getWidth(), getHeight(), user, proj));
	}

}
