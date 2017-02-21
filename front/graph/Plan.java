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
		graph = new Graph(this);
		
		Model model = graph.getModel();

        graph.beginUpdate();

        
        model.addCell("Cell A", CellType.RECTANGLE, "A", "this is cell a");
        model.addCell("Cell B", CellType.RECTANGLE, "B", "this is cell b");
        model.addCell("Cell C", CellType.RECTANGLE, "C", "this is cell c");
        model.addCell("Cell D", CellType.TRIANGLE, "D", "this is cell d");
        model.addCell("Cell E", CellType.TRIANGLE, "E", "this is cell e");
        model.addCell("Cell F", CellType.RECTANGLE, "F", "this is cell f");
        model.addCell("Cell G", CellType.RECTANGLE, "G", "this is cell g");

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
		text.textProperty().addListener((observable, oldVal, newVal) -> {
			if(graph.getSelectedCell() != null) {
				graph.getSelectedCell().setContent(newVal);
			}
		});
		
		type = new ComboBox<> ();
		type.getItems().setAll(CellType.values());
		
		add = new Button("+");
		add.getStyleClass().add("nodebutton");
		
		delete = new Button("-");
		delete.getStyleClass().add("nodebutton");
		
		HBox buttons = new HBox();
		buttons.getChildren().addAll(delete, add);
		
		
		menu.getChildren().addAll(info, text, type, buttons);
	}
	
	
	
	public void updateCellInfo(Cell cell) {
		if(cell == null) {
			
		}
		info.setText("id: " + cell.getCellId()
					+ "\ntitle: " + cell.title);
		text.setText(cell.content);
		type.getSelectionModel().select(getCellType(cell));
	}
	
	private CellType getCellType (Cell cell) {
		if (cell instanceof RectangleCell) {
			return CellType.RECTANGLE;
		} else if(cell instanceof TriangleCell) {
			return CellType.TRIANGLE;
		} else {
			return CellType.RECTANGLE;
		}
	}

}
