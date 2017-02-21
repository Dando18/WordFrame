package front.graph;

public enum CellType {

    RECTANGLE("rectangle"),
    TRIANGLE("triangle")
    ;
	
	private String label;
	
	CellType(String label) {
		this.label = label;
	}
	
	public String toString() {
		return label;
	}

}