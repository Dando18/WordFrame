package front.graph;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Plan extends HBox {

	Stage stage;

	Graph graph;

	VBox menu;
	Button add, delete;
	TextArea text;
	TextField title_txt;
	Label info, title;
	ComboBox<CellType> type;
	ColorPicker color;

	public Plan(Stage stage) {
		this.stage = stage;

		createGraph();

		createMenu();

		HBox.setHgrow(graph.getScrollPane(), Priority.SOMETIMES);
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
		//DragResizer.makeResizable(menu);

		info = new Label("Edit Node");
		
		title = new Label("title: ");
		title_txt = new TextField();
		title_txt.textProperty().addListener((observable, oldVal, newVal) -> {
			if(graph.getSelectedCell() != null) {
				graph.getSelectedCell().setTitle(title_txt.getText());
			}
		}); 
		HBox titles = new HBox();
		titles.getChildren().addAll(title, title_txt);
		
		text = new TextArea("content...");
		text.setMaxWidth(200);
		text.setPrefRowCount(4);
		text.textProperty().addListener((observable, oldVal, newVal) -> {
			if (graph.getSelectedCell() != null) {
				graph.getSelectedCell().setContent(newVal);
			}
		});

		type = new ComboBox<>();
		type.getItems().setAll(CellType.values());
		
		color = new ColorPicker();
		color.setOnAction(e -> {
			if (graph.getSelectedCell() != null) {
				graph.getSelectedCell().setBackgroundColor(color.getValue());
			}
		});

		add = new Button("+");
		add.getStyleClass().add("nodebutton");
		add.setOnAction(e -> {
			Model model = graph.getModel();
			graph.beginUpdate();

			String id = "" + model.allCells.size();

			model.addCell("" + model.allCells.size(), CellType.RECTANGLE, "New Cell", "default content");

			if (graph.getSelectedCell() != null) {
				model.addEdge(id, graph.getSelectedCell().cellId);
			}

			graph.endUpdate();
		});

		delete = new Button("-");
		delete.getStyleClass().add("nodebutton");
		delete.setOnAction(e -> {
			Cell selected = graph.getSelectedCell();
			if (selected != null) {
				Model model = graph.getModel();
				graph.beginUpdate();
				
				model.getRemovedCells().add(selected);
				
				for(Edge edge : model.allEdges) {
					if(edge.getSource().equals(selected) || edge.getTarget().equals(selected)) {
						model.getRemovedEdges().add(edge);
					}
				}
				
				graph.endUpdate();
			}
		});

		HBox buttons = new HBox();
		buttons.getChildren().addAll(delete, add);

		menu.getChildren().addAll(info, titles, text, type, color, buttons);
	}

	public void updateCellInfo(Cell cell) {
		String id = "";
		String content = "";
		Color col = null;

		if (cell != null) {
			id = cell.getCellId();
			content = cell.getContent();
			col = cell.getBackgroundColor();
		}
		info.setText("id: " + id);
		text.setText(content);
		type.getSelectionModel().select(getCellType(cell));
		color.setValue(col);
	}

	private CellType getCellType(Cell cell) {
		if (cell instanceof RectangleCell) {
			return CellType.RECTANGLE;
		} else if (cell instanceof TriangleCell) {
			return CellType.TRIANGLE;
		} else {
			return CellType.RECTANGLE;
		}
	}

}
