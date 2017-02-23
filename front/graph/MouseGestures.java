package front.graph;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class MouseGestures {

	final DragContext dragContext = new DragContext();

	Cell sourceCell = null;

	Graph graph;

	public MouseGestures(Graph graph) {
		this.graph = graph;
	}

	public void makeDraggable(final Node node) {

		node.setOnMousePressed(onMousePressedEventHandler);
		node.setOnMouseDragged(onMouseDraggedEventHandler);
		node.setOnMouseReleased(onMouseReleasedEventHandler);

	}

	EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {

			Node node = (Node) event.getSource();

			if (event.isShiftDown()) {
				if (node instanceof Cell) {
					sourceCell = ((Cell) node);
				}
			} else {
				double scale = graph.getScale();

				dragContext.x = node.getBoundsInParent().getMinX() * scale - event.getScreenX();
				dragContext.y = node.getBoundsInParent().getMinY() * scale - event.getScreenY();

				if (node instanceof Cell) {
					graph.setSelectedCell(((Cell) node).getCellId());
				}
			}
		}
	};

	EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {

			Node node = (Node) event.getSource();

			if (!event.isShiftDown()) {
				double offsetX = event.getScreenX() + dragContext.x;
				double offsetY = event.getScreenY() + dragContext.y;

				// adjust the offset in case we are zoomed
				double scale = graph.getScale();

				offsetX /= scale;
				offsetY /= scale;

				node.relocate(offsetX, offsetY);
			}
		}
	};

	EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			if (event.isShiftDown() && sourceCell != null) {
				Node node = event.getPickResult().getIntersectedNode();
				Cell cell = Cell.getCellParent(node);
				if (cell != null) {
					
					// edge0 and edge1; each starting at different ends
					Edge edge0 = new Edge(sourceCell, cell);
					Edge edge1 = new Edge(cell, sourceCell);
					if (graph.getModel().getAllEdges().contains(edge0)){
						graph.disconnect(edge0);
					} else if (graph.getModel().getAllEdges().contains(edge1)) {
						graph.disconnect(edge1);
					} else {
						graph.connect(sourceCell.cellId, cell.cellId);
					}
					sourceCell = null;   // set the source cell back to null
										 // so it's not connected to again
				}
			}
		}
	};

	class DragContext {

		double x;
		double y;

	}
}