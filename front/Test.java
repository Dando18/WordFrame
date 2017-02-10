package front;

import org.fxmisc.richtext.StyleClassedTextArea;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.IndexRange;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test extends Application{

	private final StyleClassedTextArea area = new StyleClassedTextArea();
	
	public static void main(String[] args) {
        // The following properties are required on Linux for improved text rendering
        //System.setProperty("prism.lcdtext", "false");
        //System.setProperty("prism.text", "t2k");
        launch(args);
	}

    @Override
    public void start(Stage primaryStage) {
       	VBox root = new VBox();
    	
    	HBox top = new HBox();
    	
    	Button bold = new Button("B");
    	setPushHandler(bold, "bold");
    	
    	Button italic = new Button("I");
    	setPushHandler(italic, "italic");
    	
    	top.getChildren().addAll(bold, italic);
    	    	
    	root.getChildren().addAll(top, area);
    	
    	Scene main = new Scene(root, 600, 300);
    	main.getStylesheets().add(Test.class.getResource("Main.css").toExternalForm());
        primaryStage.setTitle("Rich Text");
        primaryStage.setScene(main);
        primaryStage.show();
        
        area.setWrapText(true);
        area.requestFocus();
    }
    
    void setPushHandler(Button button, String style) {
    	button.setOnAction(e -> {
    		IndexRange range = area.getSelection();
    		System.out.println(range.getStart() + " | " + range.getEnd() + " | " + style);
    		area.setStyleClass(range.getStart(), range.getEnd(), style);
    		area.requestFocus();
    	});
    }
    
}
