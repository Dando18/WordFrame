package front.graph;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Plan extends HBox{

	Stage stage;
	
	Graph graph;
	
	VBox menu;
	Button add, delete;
	TextField text;
	Label info;
	ComboBox<CellType> type;
	
	public Plan(Stage stage) {
		this.stage = stage;
		
		createGraph();
		
		createMenu();
		
		HBox.setHgrow(graph.getScrollPane(), Priority.ALWAYS);
		getChildren().addAll(graph.getScrollPane(), menu);
	}
	
	private void createGraph() {
		graph = new Graph();
		
		Model model = graph.getModel();

        graph.beginUpdate();

        model.addCell("Cell A", CellType.RECTANGLE);
        model.addCell("Cell B", CellType.RECTANGLE);
        model.addCell("Cell C", CellType.RECTANGLE);
        model.addCell("Cell D", CellType.TRIANGLE);
        model.addCell("Cell E", CellType.TRIANGLE);
        model.addCell("Cell F", CellType.RECTANGLE);
        model.addCell("Cell G", CellType.RECTANGLE);

        model.addEdge("Cell A", "Cell B");
        model.addEdge("Cell A", "Cell C");
        model.addEdge("Cell B", "Cell C");
        model.addEdge("Cell C", "Cell D");
        model.addEdge("Cell B", "Cell E");
        model.addEdge("Cell D", "Cell F");
        model.addEdge("Cell D", "Cell G");

        graph.endUpdate();
        
        RandomLayout layout = new RandomLayout(graph);
        layout.execute();
	}
	
	private void createMenu() {
		menu = new VBox();
		menu.setSpacing(10);
		menu.setPadding(new Insets(10, 10, 0, 10));
		
		info = new Label("Edit Node");
		text = new TextField("content...");
		type = new ComboBox<> ();
		
		add = new Button("+");
		add.getStyleClass().add("nodebutton");
		
		delete = new Button("-");
		delete.getStyleClass().add("nodebutton");
		
		HBox buttons = new HBox();
		buttons.getChildren().addAll(delete, add);
		
		
		menu.getChildren().addAll(info, text, type, buttons);
	}

}
