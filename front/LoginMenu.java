package front;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginMenu extends Scene{

	List<User> users;
	
	Stage stage;
	TextField username_field;
	PasswordField password_field;
	Text info;
	
	public LoginMenu(Stage stage, double width, double height) {
		super(new StackPane(), width, height);
		
		this.stage = stage;
		getUsers();
		
		buildUI();
	}
	
	private void buildUI(){
		StackPane root = (StackPane) getRoot();
		getStylesheets().add(LoginMenu.class.getResource("Login.css").toExternalForm());
		
		GridPane grid = new GridPane();
		grid.getStyleClass().add("grid");
		grid.setGridLinesVisible(false);
		
		ColumnConstraints col = new ColumnConstraints();
		col.setPercentWidth(25);
		col.setHalignment(HPos.CENTER);
		ColumnConstraints inputCol = new ColumnConstraints();
		inputCol.setPercentWidth(75);
		inputCol.setHalignment(HPos.LEFT);
		grid.getColumnConstraints().addAll(col,inputCol);
		
		RowConstraints titleRow = new RowConstraints();
		titleRow.setPercentHeight(30);
		RowConstraints infoRow = new RowConstraints();
		infoRow.setPercentHeight(5);
		RowConstraints inputRow = new RowConstraints();
		inputRow.setPercentHeight(15);
		grid.getRowConstraints().addAll(titleRow, inputRow, inputRow, infoRow, inputRow);
		
		Text title = new Text(MainApplication.TITLE);
		title.getStyleClass().add("title");
		grid.add(title, 0, 0, 2, 1);
		
		Text username_txt = new Text("Username: ");
		grid.add(username_txt, 0, 1);
		
		username_field = new TextField();
		grid.add(username_field, 1, 1);
		
		Text password_txt = new Text("Password: ");
		grid.add(password_txt, 0, 2);
		
		password_field = new PasswordField();
		grid.add(password_field, 1, 2);
		
		info = new Text("Login");
		info.getStyleClass().add("info");
		grid.add(info, 1, 3);
		
		HBox buttons = new HBox();
		buttons.setSpacing(15);
		buttons.setAlignment(Pos.CENTER);
		
		Button signup_btn = new Button("Signup");
		signup_btn.setOnAction(this::signupPressed);
		
		Button login_btn = new Button("Login");
		login_btn.setOnAction(this::loginPressed);
		
		buttons.getChildren().addAll(signup_btn, login_btn);
		grid.add(buttons, 1, 4);
		
		root.getChildren().add(grid);
		root.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.ENTER) {
				login_btn.fire();
				e.consume();
			}
		});
	}
	
	private void signupPressed(ActionEvent e){
		stage.setScene(new SignupMenu(stage, getWidth(), getHeight(), users));
	}
	
	private void loginPressed(ActionEvent e){
		for(User user : users) {
			if (username_field.getText().equals(user.username)) { 
				if(password_field.getText().equals(user.password)) {
					stage.setScene(new MainMenu(stage, getWidth(), getHeight(), user));
					break;
				} else {
					setInfo("Wrong Password", true);
					password_field.setText("");
					return;
				}
			}
		}
		setInfo("Username does not Exist", true);
	}
	
	private void setInfo(String text, boolean isError){
		info.setText(text);
		if(isError){
			info.setStyle("-fx-fill: red; -fx-font-style: italic");
		} else {
			info.setStyle("-fx-fill: #555555; -fx-font-style: normal");
		}
	}
	
	private void getUsers() {
		users = new ArrayList<User>();
		try {
			users = load("src/front/dat/meta.xml");
		} catch(ParserConfigurationException | SAXException ex) {
			System.err.println("Could not parse XML: " + ex.getLocalizedMessage());
		} catch(IOException ex) {
			System.err.println("Could not load file: " + ex.getLocalizedMessage());
		} catch(Exception ex) {
			System.err.println("Invalid Data: " + ex.getLocalizedMessage());
		}
	}
	
	private List<User> load(String filename) throws ParserConfigurationException, IOException, SAXException{
		List<User> users = new ArrayList<User>();
		
		File xml = new File(filename);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xml);
		
		doc.getDocumentElement().normalize();
		
		NodeList nodes = doc.getElementsByTagName("user");
		
		for(int i=0; i<nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				long id = Long.parseLong(element.getAttribute("id"));
				String username = element.getElementsByTagName("username").item(0).getTextContent();
				String email = element.getElementsByTagName("email").item(0).getTextContent();
				String password = element.getElementsByTagName("password").item(0).getTextContent();
				
				List<Project> projects = new ArrayList<Project>();
				NodeList project_elem = element.getElementsByTagName("project");
				for(int j=0; j<project_elem.getLength(); j++) {
					Element elem = (Element) project_elem.item(j);
					long project_id = Long.parseLong(elem.getAttribute("id"));
					String name = elem.getElementsByTagName("name").item(0).getTextContent();
					String description = elem.getElementsByTagName("description").item(0).getTextContent();
					projects.add(new Project(name, description, project_id));
				}
				
				users.add(new User(username, password, email, projects, id));
			}
		}
		return users;
	}

}
