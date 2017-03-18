package front;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class SideMenu extends VBox {
	
	Button toggle;
	VBox menu;
	
	boolean sliding = false;
	boolean isShowing = true;
	boolean isAnimating = false;
	
	public SideMenu(double prefWidth, double prefHeight) {
		setPrefWidth(prefWidth);
		setPrefHeight(prefHeight);
		
		toggle = new Button("≡");
		toggle.setMaxWidth(Double.MAX_VALUE);
		menu = new VBox();
		menu.getStyleClass().add("vbox");
		VBox.setVgrow(menu, Priority.ALWAYS);
		
		toggle.setOnAction(this::toggleMenu);
		
		if(!isShowing) {
			menu.setTranslateX(-menu.getWidth());
		}
		
		if(sliding) {
			getChildren().add(toggle);
		}
		getChildren().add(menu);
	}
	
	public void addButton(String text, EventHandler<ActionEvent> e) {
		Button tmp = new Button(text);
		tmp.getStyleClass().add("sidemenu");
		tmp.setOnAction(e);
		tmp.setMinWidth(getPrefWidth());
		tmp.setMaxHeight(Double.MAX_VALUE);
		VBox.setVgrow(tmp, Priority.ALWAYS);
		menu.getChildren().add(tmp);
	}
	
	void toggleMenu(ActionEvent e) {
		if(!isAnimating) {
			TranslateTransition tt = new TranslateTransition(Duration.millis(200), menu);
			tt.setByX(menu.getWidth() * (isShowing ? -1 : 1));
			tt.setOnFinished(v -> isAnimating = false);
			tt.play();
			isAnimating = true;
			isShowing = !isShowing;
		}
	}

}
