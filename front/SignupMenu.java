package front;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SignupMenu extends Scene {

	Stage stage;
	List<User> users;

	TextField username_field;
	TextField email_field;
	PasswordField password_field;
	PasswordField passwordconfirm_field;
	Text info;

	public SignupMenu(Stage stage, double width, double height, List<User> users) {
		super(new StackPane(), width, height);

		this.stage = stage;
		this.users = users;

		buildUI();
	}

	private void buildUI() {
		StackPane root = (StackPane) getRoot();
		this.getStylesheets().add(SignupMenu.class.getResource("Login.css").toExternalForm());

		GridPane grid = new GridPane();
		grid.getStyleClass().add("grid");
		grid.setGridLinesVisible(false);

		ColumnConstraints labelCol = new ColumnConstraints();
		labelCol.setPercentWidth(20);
		labelCol.setHalignment(HPos.CENTER);
		ColumnConstraints inputCol = new ColumnConstraints();
		inputCol.setPercentWidth(80);
		grid.getColumnConstraints().addAll(labelCol, inputCol);

		RowConstraints titleRow = new RowConstraints();
		titleRow.setPercentHeight(20);
		RowConstraints inputRow = new RowConstraints();
		inputRow.setPercentHeight(15);
		RowConstraints infoRow = new RowConstraints();
		infoRow.setPercentHeight(5);
		// title at top, username, email, password, password confirm, info,
		// buttons
		grid.getRowConstraints().addAll(titleRow, inputRow, inputRow, inputRow, inputRow, infoRow, inputRow);

		Text title = new Text(MainApplication.TITLE);
		title.getStyleClass().add("title");
		grid.add(title, 0, 0, 2, 1);

		Text username_txt = new Text("Username:");
		grid.add(username_txt, 0, 1);
		username_field = new TextField();
		username_field.setOnKeyReleased(e -> checkFields());
		grid.add(username_field, 1, 1);

		Text email_txt = new Text("Email:");
		grid.add(email_txt, 0, 2);
		email_field = new TextField();
		email_field.setOnKeyReleased(e -> checkFields());
		grid.add(email_field, 1, 2);

		Text password_txt = new Text("Pasword:");
		grid.add(password_txt, 0, 3);
		password_field = new PasswordField();
		password_field.setOnKeyReleased(e -> checkFields());
		grid.add(password_field, 1, 3);

		Text passwordconfirm_txt = new Text("Confirm:");
		grid.add(passwordconfirm_txt, 0, 4);
		passwordconfirm_field = new PasswordField();
		passwordconfirm_field.setOnKeyReleased(e -> checkFields());
		grid.add(passwordconfirm_field, 1, 4);

		info = new Text("Signup for new account.");
		info.getStyleClass().add("info");
		grid.add(info, 1, 5);

		HBox buttons = new HBox();
		buttons.setSpacing(15);
		buttons.setAlignment(Pos.CENTER);

		Button cancel_btn = new Button("Cancel");
		cancel_btn.setOnAction(this::cancelPressed);

		Button signup_btn = new Button("Signup");
		signup_btn.setOnAction(this::signupPressed);

		buttons.getChildren().addAll(cancel_btn, signup_btn);
		grid.add(buttons, 1, 6);

		root.getChildren().add(grid);
	}

	private void cancelPressed(ActionEvent e) {
		stage.setScene(new LoginMenu(stage, getWidth(), getHeight()));
	}

	private void signupPressed(ActionEvent e) {
		if (!checkFields()) {
			setInfo("[-] Could not create account", true);
			return;
		}
		String username_content = username_field.getText().trim();
		String email_content = email_field.getText().trim();
		final String password_content = password_field.getText().trim();
		try {
			createAccount(username_content, email_content, password_content);
		} catch (Exception ex) {
			System.err.println("[-] could not create account");
		}

		stage.setScene(new LoginMenu(stage, getWidth(), getHeight()));
	}

	private void createAccount(String username, String email, String password)
			throws ParserConfigurationException, IOException, SAXException, TransformerException {
		final String FILE_PATH = "src/front/dat/meta.xml";
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setIgnoringComments(true);
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File(FILE_PATH));

		Element username_elem = doc.createElement("username");
		org.w3c.dom.Text username_val = doc.createTextNode(username);
		username_elem.appendChild(username_val);

		Element password_elem = doc.createElement("password");
		org.w3c.dom.Text password_val = doc.createTextNode(password);
		password_elem.appendChild(password_val);

		Element email_elem = doc.createElement("email");
		org.w3c.dom.Text email_val = doc.createTextNode(email);
		email_elem.appendChild(email_val);

		Element user_elem = doc.createElement("user");
		user_elem.setAttribute("id", Integer.toString((int) (Math.random() * 1000)));
		user_elem.appendChild(username_elem);
		user_elem.appendChild(password_elem);
		user_elem.appendChild(email_elem);

		Element meta = doc.getDocumentElement();
		meta.appendChild(user_elem);

		Transformer tr = TransformerFactory.newInstance().newTransformer();
		tr.setOutputProperty(OutputKeys.INDENT, "yes");
		tr.setOutputProperty(OutputKeys.METHOD, "xml");
		tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		tr.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(FILE_PATH)));
	}

	/**
	 * Check if the input fields have acceptable data
	 * 
	 * @return true if all the data is good
	 */
	private boolean checkFields() {
		String username_content = username_field.getText().trim();
		String email_content = email_field.getText().trim();
		String password_content = password_field.getText().trim();
		String passwordconfirm_content = passwordconfirm_field.getText().trim();
		if (!"".equals(username_content)) {
			for (User user : users) {
				if (user.username.equals(username_content)) {
					setInfo("Username already in use", true);
					return false;
				}
			}
		}
		if (!"".equals(email_content)
				&& !Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", email_content)) {
			setInfo("Invalid Email Address", true);
			return false;
		}
		if (!"".equals(password_content) && !password_content.equals(passwordconfirm_content)) {
			setInfo("Passwords do not match", true);
			return false;
		}
		setInfo("Signup for new account.", false);
		if (anyEquals("", username_content, email_content, password_content, passwordconfirm_content)) {
			return false;
		}
		return true;
	}

	private void setInfo(String text, boolean isError) {
		info.setText(text);
		if (isError) {
			info.setStyle("-fx-fill: red; -fx-font-style: italic");
		} else {
			info.setStyle("-fx-fill: #555555; -fx-font-style: normal");
		}
	}

	private boolean anyEquals(Object obj, Object... ary) {
		for (Object o : ary) {
			if (o.equals(obj)) {
				return true;
			}
		}
		return false;
	}

}
