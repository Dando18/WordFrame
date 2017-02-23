package front.graph;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Cell extends Pane {

    String cellId;
    
    StringProperty title = new SimpleStringProperty ();
    StringProperty content = new SimpleStringProperty ();
    ObjectProperty<Color> backgroundColor = new SimpleObjectProperty<> ();

    List<Cell> children = new ArrayList<>();
    List<Cell> parents = new ArrayList<>();

    Node view;
    boolean selected = false;
    
    public Cell(String cellId, String title, String content) {
        this.cellId = cellId;
        this.title.setValue(title);
        this.content.setValue(content);
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
    
    public void setTitle(String title) {
    	this.title.setValue(title);
    }
    
    public String getTitle() {
    	return title.getValue();
    }
    
    public void setContent(String content) {
    	this.content.setValue(content);
    }
    
    public String getContent() {
    	return content.getValue();
    }
    
    public void setBackgroundColor(Color col) {
    	this.backgroundColor.setValue(col);
    }
    
    public Color getBackgroundColor() {
    	return backgroundColor.getValue();
    }
    
    public static boolean hasCellAsParent(Node node) {
    	for(Node n = node; n != null; n = n.getParent()) {
    		if (n instanceof Cell) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * 
     * @param node Node to find parent Cell
     * @return the Cell parented to the node; \nnull if the node has no Cell parent
     */
    public static Cell getCellParent(Node node) {
    	for(Node n = node; n != null; n = n.getParent()) {
    		if(n instanceof Cell) {
    			return (Cell) n;
    		}
    	}
    	return null;
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (obj == this) return true;
    	if(!(obj instanceof Cell)) return false;
    	
    	Cell cell = (Cell) obj;
    	return cell.cellId.equals(cellId);
    }
    
    @Override
    public int hashCode() {
    	int result = 17;
    	result = 31 * result + cellId.hashCode();
    	return result;
    }
}