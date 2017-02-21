package front.graph;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class Cell extends Pane {

    String cellId;
    String title;
    String content;

    List<Cell> children = new ArrayList<>();
    List<Cell> parents = new ArrayList<>();

    Node view;
    boolean selected = false;
    
    public Cell(String cellId, String title, String content) {
        this.cellId = cellId;
        this.title = title;
        this.content = content;
    }

    public void addCellChild(Cell cell) {
        children.add(cell);
    }

    public List<Cell> getCellChildren() {
        return children;
    }

    public void addCellParent(Cell cell) {
        parents.add(cell);
    }

    public List<Cell> getCellParents() {
        return parents;
    }

    public void removeCellChild(Cell cell) {
        children.remove(cell);
    }

    public void setView(Node view) {

        this.view = view;
        
        getChildren().add(view);

    }

    public Node getView() {
        return this.view;
    }

    public String getCellId() {
        return cellId;
    }
    
    public void setSelected(boolean selected) {
    	this.selected = selected;
    	if(selected) {
    		view.setStyle("-fx-border-color: blue;"
    				+ "-fx-border-width: 3;");
    	} else {
    		view.setStyle("-fx-border-color: transparent;");
    	}
    }
    
    public boolean getSelected() {
    	return selected;
    }
    
    public void setContent(String content) {
    	this.content = content;
    }
    
    public String getContent() {
    	return content;
    }
    
}