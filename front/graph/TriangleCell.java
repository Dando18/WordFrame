package front.graph;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class TriangleCell extends Cell {

    public TriangleCell( String id, String title, String content) {
        super(id, title, content);

        double width = 50;
        double height = 50;
        
        StackPane view = new StackPane();

        Polygon triangle = new Polygon( width / 2, 0, width, height, 0, height);

        triangle.setStroke(Color.RED);
        triangle.setFill(Color.RED);
        
        Label text = new Label(title);
        
        view.getChildren().addAll(triangle, text);

        setView( view);

    }

}
