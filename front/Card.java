package front;

import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Card extends VBox {

	public Card(String title, String description, EventHandler<? super MouseEvent> event){
		
		Text title_txt = new Text(title);
		Text description_txt = new Text(description);
		description_txt.setVisible(false);
		
		getChildren().addAll(title_txt, description_txt);
		
		getStyleClass().add("card");
		title_txt.getStyleClass().add("cardtext");
		description_txt.getStyleClass().add("carddesc");
		
		setOnMouseEntered(e -> {
			description_txt.setVisible(true);
			FadeTransition ft = new FadeTransition(Duration.millis(500), description_txt);
			ft.setFromValue(0.0);
			ft.setToValue(0.9);
			ft.play();
		});
		setOnMouseExited(e -> {
			description_txt.setVisible(false);
		});
		setOnMouseClicked(event);
	}
	

}
