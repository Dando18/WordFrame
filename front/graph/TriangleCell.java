package front.graph;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
        
        Label title_lbl = new Label(title);
        title_lbl.textProperty().bind(this.title);
        
        Label content_lbl = new Label(content);
        content_lbl.textProperty().bind(this.content);
        
        VBox text = new VBox();
        text.setAlignment(Pos.CENTER);
        text.getChildren().addAll(title_lbl, content_lbl);
        
        view.getChildren().addAll(triangle, text);

        setView( view);

    }

}
