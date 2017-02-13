package front;

import org.fxmisc.richtext.StyleClassedTextArea;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Writer extends VBox{

	HBox top;
	// font name, font size, bold, italic, underline, color, justify, list
	Button bold, italic, underline;
	StyleClassedTextArea area;
	
	class StyleInfo {
		boolean bold = false;
		boolean italic = false;
		Color textColor = Color.BLACK;
		
		String toCss() {
			return "-fx-font-weight: " + (bold?"bold":"normal") + ";"
					+ "-fx-font-style: " + (italic?"italic":"normal") + ";"
					+ "-fx-text-fill: " + "rgb("+textColor.getRed()+","+textColor.getGreen()+","+textColor.getBlue()+");";
					
		}
	}
	
	public Writer() {
		
		top = new HBox();
		top.getStyleClass().add("formatbar");
		top.setAlignment(Pos.CENTER);
		
		bold = new Button("B");
		bold.setOnAction(this::boldPressed);
		bold.getStyleClass().add("format");
		bold.setStyle("-fx-font-weight: bold;");
		
		italic = new Button("I");
		italic.setOnAction(this::italicPressed);
		italic.getStyleClass().add("format");
		italic.setStyle("-fx-font-style: italic;");
		
		underline = new Button("U");
		underline.setOnAction(this::underlinePressed);
		underline.getStyleClass().add("format");
		underline.setUnderline(true);
		
		top.getChildren().addAll(bold, italic, underline);
		
		area = new StyleClassedTextArea ();
		VBox.setVgrow(area, Priority.ALWAYS);
		
		area.setStyle("-fx-padding: 20;");
		
		getChildren().addAll(top, area);
	}
	
	void boldPressed (ActionEvent e) {
		//IndexRange range = area.getSelection();
		
	}
	
	void italicPressed (ActionEvent e) {
		
	}

	void underlinePressed (ActionEvent e) {
	
	}

}
