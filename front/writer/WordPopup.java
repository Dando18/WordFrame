package front.writer;

import javafx.scene.control.Label;
import javafx.scene.control.PopupControl;
import javafx.scene.layout.StackPane;

public class WordPopup extends PopupControl {

	private Label lbl;
	
	public WordPopup(String word) {
		StackPane sp = new StackPane();
		sp.setStyle("-fx-background-color: red;");
		
		lbl = new Label(word);
		sp.getChildren().add(lbl);
		
		getScene().setRoot(sp);
	}
	
	public void setText(String txt) {
		lbl.setText(txt);
	}

}
