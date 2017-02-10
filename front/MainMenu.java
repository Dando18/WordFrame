package front;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainMenu extends Scene {

	Stage stage;
	User user;

	public MainMenu(Stage stage, double width, double height, User user) {
		super(new StackPane(), width, height);

		this.stage = stage;
		this.user = user;
		buildUI();

	}

	private void buildUI() {
		StackPane root = (StackPane) getRoot();

		getStylesheets().add(MainMenu.class.getResource("MenuStyle.css").toExternalForm());

		GridPane grid = new GridPane();
		grid.getStyleClass().add("grid");
		grid.setGridLinesVisible(false);

		ColumnConstraints col = new ColumnConstraints();
		col.setHgrow(Priority.ALWAYS);
		col.setPercentWidth(50);
		col.setFillWidth(true);
		col.setHalignment(HPos.CENTER);
		grid.getColumnConstraints().addAll(col, col);

		RowConstraints titleRowConstraints = new RowConstraints();
		titleRowConstraints.setVgrow(Priority.ALWAYS);
		titleRowConstraints.setPercentHeight(20);

		RowConstraints row = new RowConstraints();
		row.setVgrow(Priority.ALWAYS);
		row.setPercentHeight(40);
		row.setFillHeight(true);
		grid.getRowConstraints().addAll(titleRowConstraints, row, row);

		Text title_txt = new Text(MainApplication.TITLE);
		title_txt.getStyleClass().add("title");
		grid.add(title_txt, 0, 0, 2, 1);
		
		final Animation anim = new Transition() {
			{
				setCycleDuration(Duration.millis(2000));
			}
			protected void interpolate(double frac) {
				final int length = MainApplication.TITLE.length();
				final int n = Math.round(length * (float) frac);
				title_txt.setText(MainApplication.TITLE.substring(0,n));
			}
		};
		anim.play();

		Card projects = new Card("My Projects", "Here is where my projects is shown", this::projects);
		grid.add(projects, 0, 1);

		Card account = new Card("Account", "See account information", this::account);
		grid.add(account, 1, 1);

		Card settings = new Card("Settings", "Adjust settings", this::settings);
		grid.add(settings, 0, 2);

		Card logout = new Card("Logout", "Logout", this::logout);
		grid.add(logout, 1, 2);

		root.getChildren().add(grid);
	}

	private void projects(MouseEvent e) {
		stage.setScene(new ProjectMenu(stage, getWidth(), getHeight(), user));
	}

	private void account(MouseEvent e) {
		System.out.println("opening account information...");
	}

	private void settings(MouseEvent e) {
		System.out.println("opening settings...");
	}

	private void logout(MouseEvent e) {
		stage.setScene(new LoginMenu(stage, getWidth(), getHeight()));
	}

}
