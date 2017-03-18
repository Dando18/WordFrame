package front.writer;

import static org.fxmisc.richtext.model.TwoDimensional.Bias.Backward;
import static org.fxmisc.richtext.model.TwoDimensional.Bias.Forward;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.MouseOverTextEvent;
import org.fxmisc.richtext.StyledTextArea;
import org.fxmisc.richtext.TextExt;
import org.fxmisc.richtext.model.Codec;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.ReadOnlyStyledDocument;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyledDocument;
import org.fxmisc.richtext.model.StyledText;
import org.fxmisc.richtext.model.TextOps;
import org.reactfx.SuspendableNo;
import org.reactfx.util.Either;
import org.reactfx.util.Tuple2;

import front.util.DictUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class RichText extends VBox {

	Stage stage;
	
	private String padding = "5 40 2 40";

	private final TextOps<StyledText<TextStyle>, TextStyle> styledTextOps = StyledText.textOps();
	private final LinkedImageOps<TextStyle> linkedImageOps = new LinkedImageOps<>();

	private final GenericStyledArea<ParStyle, Either<StyledText<TextStyle>, LinkedImage<TextStyle>>, TextStyle> area = new GenericStyledArea<>(
			ParStyle.EMPTY, // default paragraph style
			(paragraph, style) -> paragraph.setStyle(style.toCss()), // paragraph style setter

			TextStyle.EMPTY.updateFontSize(12).updateFontFamily("Serif").updateTextColor(Color.BLACK), // default segment style
			styledTextOps._or(linkedImageOps), // segment operations
			seg -> createNode(seg, (text, style) -> text.setStyle(style.toCss()))); // create nodes and style segments
	{
		area.setWrapText(true);
		area.setStyleCodecs(ParStyle.CODEC,
				Codec.eitherCodec(StyledText.codec(TextStyle.CODEC), LinkedImage.codec(TextStyle.CODEC)));
	}
	
	private Stage mainStage;

	private final SuspendableNo updatingToolbar = new SuspendableNo();

	public RichText(Stage stage) {
		this.stage = stage;
		
		Button loadBtn = createButton("L", "loadfile", this::loadDocument, "Load document");
		Button saveBtn = createButton("S", "savefile", this::saveDocument, "Save document");
		CheckBox wrapToggle = new CheckBox("Wrap");
		wrapToggle.setSelected(true);
		area.wrapTextProperty().bind(wrapToggle.selectedProperty());
		Button undoBtn = createButton("<-", "undo", area::undo, "undo");
		Button redoBtn = createButton("->", "redo", area::redo, "redo");
		Button cutBtn = createButton("C", "cut", area::cut, "cut");
		Button copyBtn = createButton("C", "copy", area::copy, "copy");
		Button pasteBtn = createButton("P", "paste", area::paste, "paste");
		Button boldBtn = createButton("B", "bold", this::toggleBold, "bold");
		Button italicBtn = createButton("I", "italic", this::toggleItalic, "italic");
		Button underlineBtn = createButton("U", "underline", this::toggleUnderline, "underline");
		Button strikeBtn = createButton("S", "strikethrough", this::toggleStrikethrough, "strikethrough");
		Button insertImageBtn = createButton("Ins", "insertimage", this::insertImage, "Insert Image");
		ToggleGroup alignmentGrp = new ToggleGroup();
		ToggleButton alignLeftBtn = createToggleButton("", alignmentGrp, "align-left", this::alignLeft);
		ToggleButton alignCenterBtn = createToggleButton("", alignmentGrp, "align-center", this::alignCenter);
		ToggleButton alignRightBtn = createToggleButton("", alignmentGrp, "align-right", this::alignRight);
		ToggleButton alignJustifyBtn = createToggleButton("J", alignmentGrp, "align-justify", this::alignJustify);
		ColorPicker paragraphBackgroundPicker = new ColorPicker();
		ComboBox<Integer> sizeCombo = new ComboBox<>(FXCollections.observableArrayList(5, 6, 7, 8, 9, 10, 11, 12, 13,
				14, 16, 18, 20, 22, 24, 28, 32, 36, 40, 48, 56, 64, 72));
		sizeCombo.getSelectionModel().select(Integer.valueOf(12));
		ComboBox<String> familyCombo = new ComboBox<>(FXCollections.observableList(Font.getFamilies()));
		familyCombo.getSelectionModel().select("Serif");
		ColorPicker textColorPicker = new ColorPicker(Color.BLACK);
		ColorPicker backgroundColorPicker = new ColorPicker();

		@SuppressWarnings("unused")
		Node[] buttons = { undoBtn, redoBtn, cutBtn, copyBtn, pasteBtn, boldBtn, italicBtn, underlineBtn, strikeBtn,
				insertImageBtn, alignLeftBtn, alignCenterBtn, alignRightBtn, alignJustifyBtn };

		paragraphBackgroundPicker.setTooltip(new Tooltip("Paragraph background"));
		textColorPicker.setTooltip(new Tooltip("Text color"));
		backgroundColorPicker.setTooltip(new Tooltip("Text background"));

		paragraphBackgroundPicker.valueProperty().addListener((o, old, color) -> updateParagraphBackground(color));
		sizeCombo.setOnAction(evt -> updateFontSize(sizeCombo.getValue()));
		familyCombo.setOnAction(evt -> updateFontFamily(familyCombo.getValue()));
		textColorPicker.valueProperty().addListener((o, old, color) -> updateTextColor(color));
		backgroundColorPicker.valueProperty().addListener((o, old, color) -> updateBackgroundColor(color));

		undoBtn.disableProperty().bind(Bindings.not(area.undoAvailableProperty()));
		redoBtn.disableProperty().bind(Bindings.not(area.redoAvailableProperty()));

		BooleanBinding selectionEmpty = new BooleanBinding() {
			{
				bind(area.selectionProperty());
			}

			@Override
			protected boolean computeValue() {
				return area.getSelection().getLength() == 0;
			}
		};

		cutBtn.disableProperty().bind(selectionEmpty);
		copyBtn.disableProperty().bind(selectionEmpty);

		area.beingUpdatedProperty().addListener((o, old, beingUpdated) -> {
			if (!beingUpdated) {
				boolean bold, italic, underline, strike;
				Integer fontSize;
				String fontFamily;
				Color textColor;
				Color backgroundColor;

				IndexRange selection = area.getSelection();
				if (selection.getLength() != 0) {
					StyleSpans<TextStyle> styles = area.getStyleSpans(selection);
					bold = styles.styleStream().anyMatch(s -> s.bold.orElse(false));
					italic = styles.styleStream().anyMatch(s -> s.italic.orElse(false));
					underline = styles.styleStream().anyMatch(s -> s.underline.orElse(false));
					strike = styles.styleStream().anyMatch(s -> s.strikethrough.orElse(false));
					int[] sizes = styles.styleStream().mapToInt(s -> s.fontSize.orElse(-1)).distinct().toArray();
					fontSize = sizes.length == 1 ? sizes[0] : -1;
					String[] families = styles.styleStream().map(s -> s.fontFamily.orElse(null)).distinct()
							.toArray(String[]::new);
					fontFamily = families.length == 1 ? families[0] : null;
					Color[] colors = styles.styleStream().map(s -> s.textColor.orElse(null)).distinct()
							.toArray(Color[]::new);
					textColor = colors.length == 1 ? colors[0] : null;
					Color[] backgrounds = styles.styleStream().map(s -> s.backgroundColor.orElse(null)).distinct()
							.toArray(i -> new Color[i]);
					backgroundColor = backgrounds.length == 1 ? backgrounds[0] : null;
				} else {
					int p = area.getCurrentParagraph();
					int col = area.getCaretColumn();
					TextStyle style = area.getStyleAtPosition(p, col);
					bold = style.bold.orElse(false);
					italic = style.italic.orElse(false);
					underline = style.underline.orElse(false);
					strike = style.strikethrough.orElse(false);
					fontSize = style.fontSize.orElse(-1);
					fontFamily = style.fontFamily.orElse(null);
					textColor = style.textColor.orElse(null);
					backgroundColor = style.backgroundColor.orElse(null);
				}

				int startPar = area.offsetToPosition(selection.getStart(), Forward).getMajor();
				int endPar = area.offsetToPosition(selection.getEnd(), Backward).getMajor();
				List<Paragraph<ParStyle, Either<StyledText<TextStyle>, LinkedImage<TextStyle>>, TextStyle>> pars = area
						.getParagraphs().subList(startPar, endPar + 1);

				@SuppressWarnings("unchecked")
				Optional<TextAlignment>[] alignments = pars.stream().map(p -> p.getParagraphStyle().alignment)
						.distinct().toArray(Optional[]::new);
				Optional<TextAlignment> alignment = alignments.length == 1 ? alignments[0] : Optional.empty();

				@SuppressWarnings("unchecked")
				Optional<Color>[] paragraphBackgrounds = pars.stream().map(p -> p.getParagraphStyle().backgroundColor)
						.distinct().toArray(Optional[]::new);
				Optional<Color> paragraphBackground = paragraphBackgrounds.length == 1 ? paragraphBackgrounds[0]
						: Optional.empty();

				updatingToolbar.suspendWhile(() -> {
					if (bold) {
						if (!boldBtn.getStyleClass().contains("pressed")) {
							boldBtn.getStyleClass().add("pressed");
						}
					} else {
						boldBtn.getStyleClass().remove("pressed");
					}

					if (italic) {
						if (!italicBtn.getStyleClass().contains("pressed")) {
							italicBtn.getStyleClass().add("pressed");
						}
					} else {
						italicBtn.getStyleClass().remove("pressed");
					}

					if (underline) {
						if (!underlineBtn.getStyleClass().contains("pressed")) {
							underlineBtn.getStyleClass().add("pressed");
						}
					} else {
						underlineBtn.getStyleClass().remove("pressed");
					}

					if (strike) {
						if (!strikeBtn.getStyleClass().contains("pressed")) {
							strikeBtn.getStyleClass().add("pressed");
						}
					} else {
						strikeBtn.getStyleClass().remove("pressed");
					}

					if (alignment.isPresent()) {
						TextAlignment al = alignment.get();
						switch (al) {
						case LEFT:
							alignmentGrp.selectToggle(alignLeftBtn);
							break;
						case CENTER:
							alignmentGrp.selectToggle(alignCenterBtn);
							break;
						case RIGHT:
							alignmentGrp.selectToggle(alignRightBtn);
							break;
						case JUSTIFY:
							alignmentGrp.selectToggle(alignJustifyBtn);
							break;
						}
					} else {
						alignmentGrp.selectToggle(null);
					}

					paragraphBackgroundPicker.setValue(paragraphBackground.orElse(null));

					if (fontSize != -1) {
						sizeCombo.getSelectionModel().select(fontSize);
					} else {
						sizeCombo.getSelectionModel().clearSelection();
					}

					if (fontFamily != null) {
						familyCombo.getSelectionModel().select(fontFamily);
					} else {
						familyCombo.getSelectionModel().clearSelection();
					}

					if (textColor != null) {
						textColorPicker.setValue(textColor);
					}

					backgroundColorPicker.setValue(backgroundColor);
				});
			}
		});
		
		padding();

		HBox panel1 = new HBox();
		// HBox panel2 = new HBox();
		panel1.getChildren().addAll(loadBtn, saveBtn, new Separator(Orientation.VERTICAL), wrapToggle, undoBtn, redoBtn,
				new Separator(Orientation.VERTICAL), boldBtn, italicBtn, underlineBtn, strikeBtn,
				new Separator(Orientation.VERTICAL), alignLeftBtn, alignCenterBtn, alignRightBtn, alignJustifyBtn,
				insertImageBtn, sizeCombo, familyCombo, textColorPicker, backgroundColorPicker,
				paragraphBackgroundPicker);
		// panel2.getChildren().addAll(sizeCombo, familyCombo, textColorPicker,
		// backgroundColorPicker);
		panel1.getStyleClass().add("formatbar");
		// panel2.getStyleClass().add("formatbar");
		
		//WritableImage snap = area.snapshot(new SnapshotParameters(), null);
		
		Popup popup = new Popup();
		Label popup_txt = new Label("");
		popup_txt.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75);" +
                "-fx-text-fill: white;" +
                "-fx-padding: 5;");
		popup.getContent().add(popup_txt);
		
		area.setMouseOverTextDelay(java.time.Duration.ofMillis(500));
		area.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, e -> {
			String txt = area.getText();
			int mouse = e.getCharacterIndex();
			String tmp = txt.substring(txt.substring(0, mouse).lastIndexOf(" ") + 1, txt.indexOf(" ", mouse));
			if(tmp != null && !"".equals(tmp.trim())) {
				try {
					tmp = tmp.replaceAll("[,\\.'\\s]", "");
					tmp += DictUtil.getDefinition(tmp);
				}catch(IOException ex) {
				
				}catch(NullPointerException ex) {
					
				}
				popup_txt.setText(tmp);
			}
			Point2D pos = e.getScreenPosition();
			popup.show(area, pos.getX(), pos.getY() + 10);
		});
		area.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END, e -> {
			popup.hide();
		});


		VirtualizedScrollPane<GenericStyledArea<ParStyle, Either<StyledText<TextStyle>, LinkedImage<TextStyle>>, TextStyle>> vsPane = new VirtualizedScrollPane<>(
				area);
		// VBox vbox = new VBox();
		VBox.setVgrow(vsPane, Priority.ALWAYS);
		setSpacing(0);
		getChildren().addAll(panel1, vsPane);

		area.requestFocus();

	}

	private Node createNode(Either<StyledText<TextStyle>, LinkedImage<TextStyle>> seg,
			BiConsumer<? super TextExt, TextStyle> applyStyle) {
		if (seg.isLeft()) {
			return StyledTextArea.createStyledTextNode(seg.getLeft(), styledTextOps, applyStyle);
		} else {
			return seg.getRight().createNode();
		}
	}

	private Button createButton(String text, String styleClass, Runnable action, String toolTip) {
		Button button = new Button(text);
		button.getStyleClass().add(styleClass);
		button.getStyleClass().add("format");
		button.setOnAction(evt -> {
			action.run();
			area.requestFocus();
		});
		// button.setPrefWidth(25);
		button.setMaxWidth(50);
		button.setPrefHeight(30);
		if (toolTip != null) {
			button.setTooltip(new Tooltip(toolTip));
		}
		return button;
	}

	private ToggleButton createToggleButton(String text, ToggleGroup grp, String styleClass, Runnable action) {
		ToggleButton button = new ToggleButton(text);
		button.setToggleGroup(grp);
		button.getStyleClass().add(styleClass);
		button.getStyleClass().add("format");
		button.setOnAction(evt -> {
			action.run();
			area.requestFocus();
		});
		button.setPrefWidth(20);
		button.setPrefHeight(20);
		return button;
	}

	private void toggleBold() {
		updateStyleInSelection(
				spans -> TextStyle.bold(!spans.styleStream().allMatch(style -> style.bold.orElse(false))));
	}

	private void toggleItalic() {
		updateStyleInSelection(
				spans -> TextStyle.italic(!spans.styleStream().allMatch(style -> style.italic.orElse(false))));
	}

	private void toggleUnderline() {
		updateStyleInSelection(
				spans -> TextStyle.underline(!spans.styleStream().allMatch(style -> style.underline.orElse(false))));
	}

	private void toggleStrikethrough() {
		updateStyleInSelection(spans -> TextStyle
				.strikethrough(!spans.styleStream().allMatch(style -> style.strikethrough.orElse(false))));
	}

	private void alignLeft() {
		updateParagraphStyleInSelection(ParStyle.alignLeft());
	}

	private void alignCenter() {
		updateParagraphStyleInSelection(ParStyle.alignCenter());
	}

	private void alignRight() {
		updateParagraphStyleInSelection(ParStyle.alignRight());
	}

	private void alignJustify() {
		updateParagraphStyleInSelection(ParStyle.alignJustify());
	}
	
	private void padding() {
		updateParagraphStyleInSelection(ParStyle.padding(padding));	
	}

	private void loadDocument() {
		String initialDir = System.getProperty("user.dir");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Load document");
		fileChooser.setInitialDirectory(new File(initialDir));
		File selectedFile = fileChooser.showOpenDialog(mainStage);
		if (selectedFile != null) {
			area.clear();
			load(selectedFile);
		}
	}

	private void load(File file) {
		if (area.getStyleCodecs().isPresent()) {
			Tuple2<Codec<ParStyle>, Codec<Either<StyledText<TextStyle>, LinkedImage<TextStyle>>>> codecs = area
					.getStyleCodecs().get();
			Codec<StyledDocument<ParStyle, Either<StyledText<TextStyle>, LinkedImage<TextStyle>>, TextStyle>> codec = ReadOnlyStyledDocument
					.codec(codecs._1, codecs._2, area.getSegOps());

			try {
				FileInputStream fis = new FileInputStream(file);
				DataInputStream dis = new DataInputStream(fis);
				StyledDocument<ParStyle, Either<StyledText<TextStyle>, LinkedImage<TextStyle>>, TextStyle> doc = codec
						.decode(dis);
				fis.close();

				if (doc != null) {
					area.replaceSelection(doc);
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveDocument() {
		String initialDir = System.getProperty("user.dir");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save document");
		fileChooser.setInitialDirectory(new File(initialDir));
		File selectedFile = fileChooser.showSaveDialog(mainStage);
		if (selectedFile != null) {
			save(selectedFile);
		}
	}

	private void save(File file) {
		StyledDocument<ParStyle, Either<StyledText<TextStyle>, LinkedImage<TextStyle>>, TextStyle> doc = area
				.getDocument();

		// Use the Codec to save the document in a binary format
		area.getStyleCodecs().ifPresent(codecs -> {
			Codec<StyledDocument<ParStyle, Either<StyledText<TextStyle>, LinkedImage<TextStyle>>, TextStyle>> codec = ReadOnlyStyledDocument
					.codec(codecs._1, codecs._2, area.getSegOps());
			try {
				FileOutputStream fos = new FileOutputStream(file);
				DataOutputStream dos = new DataOutputStream(fos);
				codec.encode(dos, doc);
				fos.close();
			} catch (IOException fnfe) {
				fnfe.printStackTrace();
			}
		});
	}

	/**
	 * Action listener which inserts a new image at the current caret position.
	 */
	private void insertImage() {
		String initialDir = System.getProperty("user.dir");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Insert image");
		fileChooser.setInitialDirectory(new File(initialDir));
		File selectedFile = fileChooser.showOpenDialog(mainStage);
		if (selectedFile != null) {
			String imagePath = selectedFile.getAbsolutePath();
			imagePath = imagePath.replace('\\', '/');
			ReadOnlyStyledDocument<ParStyle, Either<StyledText<TextStyle>, LinkedImage<TextStyle>>, TextStyle> ros = ReadOnlyStyledDocument
					.fromSegment(Either.right(new RealLinkedImage<>(imagePath, TextStyle.EMPTY)), ParStyle.EMPTY,
							TextStyle.EMPTY, area.getSegOps());
			area.replaceSelection(ros);
		}
	}

	private void updateStyleInSelection(Function<StyleSpans<TextStyle>, TextStyle> mixinGetter) {
		IndexRange selection = area.getSelection();
		if (selection.getLength() != 0) {
			StyleSpans<TextStyle> styles = area.getStyleSpans(selection);
			TextStyle mixin = mixinGetter.apply(styles);
			StyleSpans<TextStyle> newStyles = styles.mapStyles(style -> style.updateWith(mixin));
			area.setStyleSpans(selection.getStart(), newStyles);
		}
	}

	private void updateStyleInSelection(TextStyle mixin) {
		IndexRange selection = area.getSelection();
		if (selection.getLength() != 0) {
			StyleSpans<TextStyle> styles = area.getStyleSpans(selection);
			StyleSpans<TextStyle> newStyles = styles.mapStyles(style -> style.updateWith(mixin));
			area.setStyleSpans(selection.getStart(), newStyles);
		}
	}

	private void updateParagraphStyleInSelection(Function<ParStyle, ParStyle> updater) {
		IndexRange selection = area.getSelection();
		int startPar = area.offsetToPosition(selection.getStart(), Forward).getMajor();
		int endPar = area.offsetToPosition(selection.getEnd(), Backward).getMajor();
		for (int i = startPar; i <= endPar; ++i) {
			Paragraph<ParStyle, Either<StyledText<TextStyle>, LinkedImage<TextStyle>>, TextStyle> paragraph = area
					.getParagraph(i);
			area.setParagraphStyle(i, updater.apply(paragraph.getParagraphStyle()));
		}
	}

	private void updateParagraphStyleInSelection(ParStyle mixin) {
		updateParagraphStyleInSelection(style -> style.updateWith(mixin));
	}

	private void updateFontSize(Integer size) {
		if (!updatingToolbar.get()) {
			updateStyleInSelection(TextStyle.fontSize(size));
		}
	}

	private void updateFontFamily(String family) {
		if (!updatingToolbar.get()) {
			updateStyleInSelection(TextStyle.fontFamily(family));
		}
	}

	private void updateTextColor(Color color) {
		if (!updatingToolbar.get()) {
			updateStyleInSelection(TextStyle.textColor(color));
		}
	}

	private void updateBackgroundColor(Color color) {
		if (!updatingToolbar.get()) {
			updateStyleInSelection(TextStyle.backgroundColor(color));
		}
	}

	private void updateParagraphBackground(Color color) {
		if (!updatingToolbar.get()) {
			updateParagraphStyleInSelection(ParStyle.backgroundColor(color));
		}
	}
}