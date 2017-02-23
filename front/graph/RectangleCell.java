package front.graph;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleCell extends Cell {

	Rectangle rectangle;
	Label title_lbl;
	Label content_lbl;
	
	double padding = 10f;
	
    public RectangleCell( String id, String title, String content) {
        super(id, title, content);
        

        StackPane view = new StackPane();
        
        rectangle = new Rectangle(50,50);

        title_lbl = new Label(title);
        title_lbl.setAlignment(Pos.CENTER);
        
        content_lbl = new Label(content);
        content_lbl.setAlignment(Pos.CENTER);
        content_lbl.textProperty().bind(this.content);
        
        rectangle.widthProperty().bind(content_lbl.widthProperty().add(padding));
        rectangle.heightProperty().bind(content_lbl.heightProperty().add(title_lbl.heightProperty()).add(padding));
        
        rectangle.fillProperty().bind(this.backgroundColor);
        rectangle.strokeProperty().bind(this.backgroundColor);
        
        backgroundColor.setValue(Color.DODGERBLUE);
        
        VBox text = new VBox();
        text.getChildren().addAll(title_lbl, content_lbl);
        
        view.getChildren().addAll(rectangle, text);
        
        setView(view);

    }
    

}