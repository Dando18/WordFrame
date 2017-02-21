package front.graph;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleCell extends Cell {

    public RectangleCell( String id, String title, String content) {
        super(id, title, content);
        

        StackPane view = new StackPane();
        
        Rectangle rectangle = new Rectangle(50,50);

        rectangle.setStroke(Color.DODGERBLUE);
        rectangle.setFill(Color.DODGERBLUE);

        Label content_lbl = new Label(title + "\n" + content);
        content_lbl.setAlignment(Pos.CENTER);
        
        view.getChildren().addAll(rectangle, content_lbl);
        
        setView(view);

    }

}