package front.graph;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleCell extends Cell {

    public RectangleCell( String id) {
        super( id);

        StackPane view = new StackPane();
        
        Rectangle rectangle = new Rectangle( 50,50);

        rectangle.setStroke(Color.DODGERBLUE);
        rectangle.setFill(Color.DODGERBLUE);

        Label text = new Label(id);
        
        view.getChildren().addAll(rectangle, text);
        
        setView(view);

    }

}