package front.graph;

import javafx.beans.binding.When;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleCell extends Cell {

	Rectangle rectangle;
	Label title_lbl;
	TextArea content_txt;
	
	double padding = 20f;
	
    public RectangleCell( String id, String title, String content) {
        super(id, title, content);
        

        StackPane view = new StackPane();
        
        rectangle = new Rectangle(50,50);

        title_lbl = new Label(title);        
        title_lbl.setAlignment(Pos.CENTER);
        title_lbl.setStyle("-fx-font-weight: bold;");
        title_lbl.textProperty().bind(this.title);
        
        content_txt = new TextArea(content);
        //content_lbl.setAlignment(Pos.CENTER);
        content_txt.setPrefRowCount(2);
        content_txt.setMaxWidth(100);
        //content_lbl.textProperty().bind(this.content);
        content_txt.autosize();
        content_txt.setWrapText(true);
        content_txt.getStyleClass().add("celltext");
        
        content_txt.textProperty().addListener(e -> {
        	this.content.set(content_txt.getText());
        });
        
        rectangle.widthProperty().bind(
        		new When(content_txt.widthProperty().greaterThan(title_lbl.widthProperty()))
        		.then(
        				content_txt.widthProperty()
        		).otherwise(
        				title_lbl.widthProperty().add(padding)
        		));
        rectangle.heightProperty().bind(content_txt.heightProperty().add(title_lbl.heightProperty()).add(padding));
        
        rectangle.fillProperty().bind(this.backgroundColor);
        rectangle.strokeProperty().bind(this.backgroundColor);
        
        backgroundColor.setValue(Color.LIGHTBLUE);
        
        VBox text = new VBox();
        text.setAlignment(Pos.CENTER);
        text.getChildren().addAll(title_lbl, content_txt);
        
        view.getChildren().addAll(rectangle, text);
        
        setView(view);

    }
    

}