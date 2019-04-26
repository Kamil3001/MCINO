import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import results.Occurrence;
import results.OverallResult;
import results.SmellResult;
import smells.AbstractCodeSmell;
import smells.SmellDetector;
import utils.Comments;
import utils.HTMLUtil;
import utils.SyntaxHighlighter;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class UI implements Initializable {

    @FXML
    private Button btnHome, btnAnalysis, btnDetails, btnAbout, btnProject,btnShowDetails;

    @FXML
    private FontAwesomeIconView btnClose, btnMinimize;

    @FXML
    private AnchorPane pnHome, pnDetails, pnAnalysis, pnAbout;

    @FXML
    private Label lblPage, lblMsg, lblSource, lblSmell, barValue, lblComment;
    
    @FXML
    private ComboBox<String> comboSource,comboSmell;

    @FXML
    private AnchorPane root;

    @FXML
    private TabPane tabAbout;

    @FXML
    private StackPane codePane;

    @FXML
    private HBox severityBox,occurrenceBox;

    private BarChart<?, ?> barSeverity, barOccurrence;

    private double xOffset = 0;
    private double yOffset = 0;

    private boolean isDirChosen = false;
    private boolean isNotified = false;
    private boolean isCreated = false;
    private String projectDir;
    private SmellDetector smellDetector;
    private SyntaxHighlighter highlight;
    private OverallResult overallResult;


    private HashMap<String, String> defaultColors = new HashMap<>(){
        {
            put("Long Methods","#f3622d");
            put("Long Class","#fba71b");
            put("Cyclomatic Complexity","#57b757");
            put("Data Clumps","#41a9c9");
            put("Duplicated Code","#4258c9");
            put("Feature Envy","#9a42c8");
            put("Heavy Commenting","#c84164");
            put("Lazy Class","#888888");
            put("Refused Bequest","#ff69b4");
        }};
    


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        highlight = new SyntaxHighlighter();

        makeStageDraggable();
        makeTabPaneListener();

        openHome();
    }

    /** Handlers **/
    @FXML
    void handleSideBarClicks(MouseEvent event) {
        if(event.getSource() == btnHome)
        {
            hideAllPanes();
            openHome();

        }
        else if(event.getSource() == btnAnalysis)
        {
            hideAllPanes();
            openAnalysis();
        }
        else if(event.getSource() == btnDetails)
        {
            hideAllPanes();
            openDetails();
        }
        else if(event.getSource() == btnAbout)
        {
            hideAllPanes();
            openAbout();
        }
    }

    @FXML
    void handleClose(MouseEvent event) {
        if(event.getSource() == btnClose)
        {
            System.exit(0);
        }
    }

    @FXML
    void handleDirChooser(MouseEvent event) {
        if(event.getSource() == btnProject){
            File dir = showDirChooser();
            if(dir!=null) {

                if(projectDir!=null) reset(); // If directory was already chosen, remove all previous data

                isDirChosen = true; // Used for determining which buttons/pane to show

                projectDir = dir.getAbsolutePath();
                smellDetector = new SmellDetector(projectDir);
                smellDetector.detectSmells();

                overallResult = new OverallResult(smellDetector.getSmellResults());

                Alert notification = new Alert(Alert.AlertType.CONFIRMATION);
                notification.setHeaderText("Project loaded.");
                notification.setTitle("Message");
                notification.showAndWait();

                initializeComboBox();

            }else{
                reset();
            }

        }
    }

    @FXML
    void handleDetails(MouseEvent event) {
        if(event.getSource() == btnShowDetails)
        {
            String file = comboSource.getSelectionModel().getSelectedItem();
            String smell = comboSmell.getSelectionModel().getSelectedItem();
            if(comboSource.getSelectionModel().getSelectedItem() != null && comboSmell.getSelectionModel().getSelectedItem() != null) {
                codePane.getChildren().clear();
                CodeArea codeSource = new CodeArea();
                codeSource.getStylesheets().add(getClass().getResource("highlight.css").toExternalForm());
                codeSource.setParagraphGraphicFactory(LineNumberFactory.get(codeSource));
                codeSource.richChanges()
                        .filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
                        .subscribe(change -> codeSource.setStyleSpans(0, highlight.computeHighlighting(codeSource.getText())));
                codeSource.replaceText(0, 0, smellDetector.getSourceFiles().get(comboSource.getSelectionModel().getSelectedItem()));
                codeSource.setEditable(false);


                List<Occurrence> occurrence = smellDetector.getSmellResults().get(smell).getOccurrencesPerFile().get(file);
                if(occurrence!=null) {
                    for (Occurrence occ : occurrence) {
                        //System.out.println(occ.getStartLine() + " " +occ.getEndLine());
                        codeSource.setParagraphStyle(occ.getStartLine()-1, Collections.singleton("smell"));
                        for(int i =occ.getStartLine()-1;i < occ.getEndLine();i++)
                        {
                            codeSource.setParagraphStyle(i,Collections.singleton("smell"));
                        }
                        //System.out.println(codeSource.getText(occ.getStartLine(),0,occ.getEndLine(),occ.getEndLine()+1));

                    }
                }
                codePane.getChildren().add(new VirtualizedScrollPane<>(codeSource));
                lblSource.setText(file);
                int severity = 0;
                for(String sm: smellDetector.getSmellResults().keySet())
                {
                    SmellResult sr = smellDetector.getSmellResults().get(sm);
                    if(smellDetector.getSmellResults().get(sm).getSmellName().equals(smell)){
                        severity= sr.getSeverityPerFile().get(file);
                    }
                }
                lblSmell.setText(Comments.getCommentsClass().getComment(smell, severity) +" \nSeverity is " + severity);
            }else{
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setHeaderText("Please make a selection first.");
                error.showAndWait();
            }
        }
    }

    @FXML
    void handleMinimize(MouseEvent event) {
        if(event.getSource() == btnMinimize)
        {
            Main.stage.setIconified(true);
        }
    }

    private void makeStageDraggable() {
        root.setOnMousePressed((event)-> {

            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });


        root.setOnMouseDragged((event)->{
            Main.stage.setX(event.getScreenX() - xOffset);
            Main.stage.setY(event.getScreenY() - yOffset);
            Main.stage.setOpacity(0.8f);
        });

        root.setOnMouseReleased((event) -> Main.stage.setOpacity(1.0f));

        root.setOnDragDone((DragEvent mouseEvent) -> Main.stage.setOpacity(1.0f));
    }

    private void makeTabPaneListener() {
        for(Tab currTab: tabAbout.getTabs()) {
            currTab.setOnSelectionChanged(e-> setHtmlContent(currTab));
        }

    }

    private void makeBarListener(BarChart<?,?> chart) {
        DecimalFormat df = new DecimalFormat("0.00");

        for (XYChart.Series<?, ?> series : chart.getData()) {
            for (XYChart.Data data : series.getData()) {
                data.getNode().setOnMouseEntered((event) -> {
                    barValue.setVisible(true);
                    barValue.setText(df.format(data.getYValue()));
                    lblComment.setVisible(true);
                    lblComment.setText(overallResult.getComments((String) data.getXValue()));
                    barValue.setStyle("-fx-text-fill: " + defaultColors.get((String)data.getXValue()) + ";");
                });

                data.getNode().setOnMouseExited((event) -> {
                    lblComment.setVisible(false);
                    barValue.setVisible(false);
                    barValue.setText("");
                    lblComment.setText("");
                });
            }
        }

    }


    /** Helper Functions **/

    private File showDirChooser(){
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.dir")));
        return dc.showDialog(null); // returns selected directory
    }

    private void setupChart(){
        CategoryAxis xAxisOcc = new CategoryAxis();
        CategoryAxis xAxisSev = new CategoryAxis();
        NumberAxis yAxisOcc = new NumberAxis();
        NumberAxis yAxisSev = new NumberAxis("Severity Rating", 0, 3, 1);
        barOccurrence = new BarChart<>(xAxisOcc, yAxisOcc);
        barSeverity = new BarChart<>(xAxisSev, yAxisSev);

        Font font = new Font("Dubai", 6.5);

        yAxisOcc.setLabel("Number of Occurrences");

        xAxisSev.setTickLabelFont(font);
        xAxisOcc.setTickLabelFont(font);

        xAxisOcc.setTickLabelRotation(25);
        xAxisSev.setTickLabelRotation(25);

        barOccurrence.setTitle("Average Occurrence of Smells Per File");
        barSeverity.setTitle("Average Severity of Smells Across Files");

        barOccurrence.setLegendVisible(false);
        barSeverity.setLegendVisible(false);
    }

    private void fillChart(){

        XYChart.Series severityData = new XYChart.Series<>();
        XYChart.Series occurrenceData = new XYChart.Series<>();

        int SMELLS = smellDetector.getSmells().length;
        String smell;
        double average;
        XYChart.Data<String, Number> dataOcc;
        XYChart.Data<String, Number> dataSev;

        for (int i = 0; i < SMELLS; i++) {
            smell = smellDetector.getSmells()[i].getSmellName();

            average = overallResult.getOverallOccurrences().get(smell);
            dataOcc = new XYChart.Data<>(smell, average);
            addData(occurrenceData, dataOcc);

            average = overallResult.getOverallSeverities().get(smell);
            dataSev = new XYChart.Data<>(smell, average);
            addData(severityData, dataSev);
                

        }

        barOccurrence.getData().addAll(occurrenceData);
        barSeverity.getData().addAll(severityData);

        occurrenceBox.getChildren().add(barOccurrence);
        severityBox.getChildren().add(barSeverity);

        isCreated = true;

        makeBarListener(barOccurrence);
        makeBarListener(barSeverity);

    }

    private void hideAllPanes() {
        pnHome.setVisible(false);
        pnAnalysis.setVisible(false);
        pnDetails.setVisible(false);
        pnAbout.setVisible(false);
    }

    private void reset() {

        // Clearing data
        if(isCreated) {
            severityBox.getChildren().remove(0);
            occurrenceBox.getChildren().remove(0);
            barOccurrence.getData().clear();
            barSeverity.getData().clear();
            barOccurrence.setVisible(false);
            barSeverity.setVisible(false);
        }



        // Reset drop down menus
        comboSmell.getItems().clear();
        comboSource.getItems().clear();
        comboSource.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select source");
                } else {
                    setText(item);
                }
            }
        });

        comboSmell.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select smell");
                } else {
                    setText(item);
                }
            }
        });

        // Remove contents of CodeArea
        codePane.getChildren().clear();

        lblSource.setText("Source");
        lblSmell.setText("Smell");

        // Reset variables
        projectDir = null;
        isDirChosen = false;
        isCreated = false;

    }

    private void openHome() {
        hideAllPanes();
        pnHome.toFront();
        pnHome.setVisible(true);
        lblPage.setText("Home");
    }

    private void openAnalysis() {
        hideAllPanes();
        pnAnalysis.toFront();
        pnAnalysis.setVisible(true);
        lblPage.setText("Analysis");
        barValue.setVisible(false);
        lblComment.setVisible(false);
        if(!isDirChosen){
            lblMsg.setText("Nothing to display");
            lblMsg.setVisible(true);
        }else{
            lblMsg.setVisible(false);
            if(!isCreated) {
                setupChart();
                fillChart();
            }
            barSeverity.setVisible(true);
            barOccurrence.setVisible(true);
            if(!isNotified) {
                Alert notification = new Alert(Alert.AlertType.INFORMATION);
                notification.setHeaderText("Hover cursor over bars to see their values!");
                notification.setTitle("Hint");
                notification.showAndWait();
                isNotified = true;
            }
        }
    }

    private void openDetails() {
        hideAllPanes();
        pnDetails.toFront();
        pnDetails.setVisible(true);
        lblPage.setText("Details");
    }

    private void openAbout() {
        hideAllPanes();
        pnAbout.toFront();
        pnAbout.setVisible(true);
        lblPage.setText("About");
        setHtmlContent(tabAbout.getTabs().get(0));
    }

    private void initializeComboBox() {
        comboSource.getItems().addAll(smellDetector.getSmellResults().get("Lazy Class").getSeverityPerFile().keySet());
        ArrayList<String> smellNames = new ArrayList<>();
        for(AbstractCodeSmell smell: smellDetector.getSmells())
        {
            smellNames.add(smell.getSmellName());
        }
        comboSmell.getItems().addAll(smellNames);

        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> arg0) {
                ListCell<String> cell = new ListCell<String>() {

                    @Override
                    public void updateItem(String smell, boolean empty) {
                        super.updateItem(smell, empty);
                        if (smell != null) {
                            setText(smell);
                            setStyle("-fx-background-color: #333645;" +
                                    "-fx-text-fill: #7c8184;");
                        }
                    }

                };

                cell.hoverProperty().addListener((observable -> {
                    if (cell.isHover()) {
                        cell.setStyle("-fx-background-color: #4c8bf5;" +
                                "-fx-text-fill: white;");
                    } else {
                        cell.setStyle("-fx-background-color: #333645;" +
                                "-fx-text-fill: #7c8184;");
                    }
                }));

                cell.pressedProperty().addListener((observable -> cell.setStyle("-fx-background-color: #333645;" +
                        "-fx-text-fill: #7c8184;")));

                return cell;
            }
        };


        comboSource.setCellFactory(cellFactory);
        comboSmell.setCellFactory(cellFactory);
    }
    
    private void addData(XYChart.Series series, XYChart.Data<String, Number> data){
        series.getData().add(data);
        data.nodeProperty().addListener((ov, oldNode, newNode) -> {
            newNode.setStyle("-fx-bar-fill: " + defaultColors.get(data.getXValue()) );
        });
        System.out.println(data.getXValue() + " " + data.getYValue());
    }

    private void setHtmlContent(Tab currTab){
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        String tabName = currTab.getText();
        webEngine.loadContent(HTMLUtil.getHTMLUtil().getHtml(tabName));
        webEngine.setUserStyleSheetLocation(getClass().getResource("tab-content.css").toString());
        currTab.setContent(webView);

    }

}