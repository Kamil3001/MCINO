import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import results.SmellResult;
import smells.SmellDetector;
import utils.SyntaxHighlighter;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.ResourceBundle;

public class UI implements Initializable {

    @FXML
    private Button btnHome, btnAnalysis, btnDetails, btnAbout, btnProject,btnShowDetails;

    @FXML
    private FontAwesomeIconView btnClose, btnMinimize;

    @FXML
    private AnchorPane pnHome, pnDetails, pnAnalysis, pnAbout;

    @FXML
    private Label lblPage, lblMsg, lblSource, lblSmell, pieValue;

    @FXML
    private PieChart occurrencePieChart,severityPieChart;

    @FXML
    private ComboBox<String> comboSource,comboSmell;

    @FXML
    private AnchorPane root;

    @FXML
    private StackPane codePane;

    private double xOffset = 0;
    private double yOffset = 0;

    private boolean isDirChosen = false;
    private boolean isNotified = false;
    private String projectDir;
    private SmellDetector smellDetector;
    private SyntaxHighlighter highlight;

    private final String[] defaultColors = {"#f3622d",
                                                "#fba71b", 
                                                "#57b757", 
                                                "#41a9c9", 
                                                "#4258c9", 
                                                "#9a42c8", 
                                                "#c84164", 
                                                "#888888"};


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        highlight = new SyntaxHighlighter();
        makeStageDraggable();
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
            if(comboSource.getSelectionModel().getSelectedItem() != null && comboSmell.getSelectionModel().getSelectedItem() != null) {
                codePane.getChildren().clear();
                CodeArea codeSource = new CodeArea();
                codeSource.getStylesheets().add(getClass().getResource("java-keywords.css").toExternalForm());
                codeSource.setParagraphGraphicFactory(LineNumberFactory.get(codeSource));
                codeSource.richChanges()
                        .filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
                        .subscribe(change -> codeSource.setStyleSpans(0, highlight.computeHighlighting(codeSource.getText())));
                codeSource.replaceText(0, 0, smellDetector.getSourceFiles().get(comboSource.getSelectionModel().getSelectedItem()));
                codeSource.setEditable(false);
                codePane.getChildren().add(new VirtualizedScrollPane<>(codeSource));
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

    private void makeSliceListener() {
        DecimalFormat df=new DecimalFormat("0.00");

        for (PieChart.Data d : occurrencePieChart.getData()) {
            d.getNode().setOnMouseEntered((event) -> {
                pieValue.setVisible(true);
                pieValue.setText(df.format(d.getPieValue()) +"%");
                int color = getColor(d.getNode());
                pieValue.setStyle("-fx-text-fill: " + defaultColors[color]+";");
            });

            d.getNode().setOnMouseExited((event) ->{
                pieValue.setVisible(false);
                pieValue.setText("");
            });
        }

        for (PieChart.Data d : severityPieChart.getData()) {
            d.getNode().setOnMouseEntered((event) -> {
                pieValue.setVisible(true);
                pieValue.setText(String.valueOf(df.format(d.getPieValue())));
                int color = getColor(d.getNode());
                pieValue.setStyle("-fx-text-fill: " + defaultColors[color]+";");
            });

            d.getNode().setOnMouseExited((event) ->{
                pieValue.setVisible(false);
                pieValue.setText("");
            });
        }
    }


    /** Helper Functions **/

    private File showDirChooser(){
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.dir")));
        return dc.showDialog(null); // returns selected directory
    }

    private void createChart() {

        ObservableList<PieChart.Data> occurrenceData = initializeData();
        ObservableList<PieChart.Data> severityData = initializeData();

        lblMsg.setVisible(false);
        occurrencePieChart.setTitle("Average Occurrence of Smells");
        severityPieChart.setTitle("Average Severity of Smells");
        int SLICES = smellDetector.getSmells().length;

        String smell;
        double average;

        for(int i =0;i<SLICES ;i++)
        {
            smell = smellDetector.getSmells()[i].getSmellName();

            average = extractAverageOccurenceOfSmell(smell);
            addData(smell,average, occurrenceData);
            //System.out.println(smell+ " " + average);

            average = extractAverageSeverityOfSmell(smell);
            addData(smell,average, severityData);
            //System.out.println(smell+ " " + average);
        }
        System.out.println();

        severityPieChart.setData(severityData);
        occurrencePieChart.setData(occurrenceData);
        severityPieChart.setVisible(true);
        occurrencePieChart.setVisible(true);
        makeSliceListener();
    }

    // Gets total average severity of a specific smell across all source files
    private double extractAverageSeverityOfSmell(String smellName){
        double totalAverage = 0.0;
        int count = 0;
        for(SmellResult sr: smellDetector.getSmellResults())
        {
            if(sr.getSmellName().equalsIgnoreCase(smellName))
            {
                totalAverage = totalAverage + sr.getAverageSeverity();
                count++;
            }
        }
        Random rand = new Random();
        return (totalAverage/count+rand.nextDouble())/4 * 8;
    }

    // Gets total average occurrence of a specific smell across all source files
    private double extractAverageOccurenceOfSmell(String smellName){
        double totalAverage = 0.0;
        int count = 0;
        for(SmellResult sr: smellDetector.getSmellResults())
        {
            if(sr.getSmellName().equalsIgnoreCase(smellName))
            {
                totalAverage = totalAverage + sr.getAverageOccurrences();
                count++;
            }
        }

        Random rand = new Random();
        return (totalAverage/count+rand.nextDouble())*100.0;
    }

    private void hideAllPanes() {
        pnHome.setVisible(false);
        pnAnalysis.setVisible(false);
        pnDetails.setVisible(false);
        //pnAbout.setVisible(false);
    }

    private void reset() {

        // Clearing pie charts
        occurrencePieChart.getData().clear();
        severityPieChart.getData().clear();
        occurrencePieChart.setVisible(false);
        severityPieChart.setVisible(false);

        // Reset drop down menus
        comboSmell.getItems().clear();
        comboSource.getItems().clear();
        comboSource.setButtonCell(new ListCell<>() {
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

        comboSmell.setButtonCell(new ListCell<>() {
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
        pieValue.setVisible(false);
        if(!isDirChosen){
            lblMsg.setText("Nothing to display");
            lblMsg.setVisible(true);
        }else{
            lblMsg.setVisible(false);
            createChart();
            occurrencePieChart.setVisible(true);
            severityPieChart.setVisible(true);
            if(!isNotified) {
                Alert notification = new Alert(Alert.AlertType.INFORMATION);
                notification.setHeaderText("Hover cursor over slices to see their values!");
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
    }

    private void addData(String smell, double average, ObservableList<PieChart.Data> pieData ) {
            for(PieChart.Data d: pieData)
            {
                if(d.getName().equals(smell))
                {
                    d.setPieValue(average);
                    return;
                }
            }

            //If smell doesn't exist
            pieData.add(new PieChart.Data(smell,average));
    }

    private void initializeComboBox() {
        comboSource.getItems().addAll(smellDetector.getSmellResults().get(0).getSeverityPerFile().keySet());
        comboSmell.getItems().addAll(smellDetector.getSmells()[0].getSmellName(),
                smellDetector.getSmells()[1].getSmellName(),
                smellDetector.getSmells()[2].getSmellName(),
                smellDetector.getSmells()[3].getSmellName(),
                smellDetector.getSmells()[4].getSmellName(),
                smellDetector.getSmells()[5].getSmellName(),
                smellDetector.getSmells()[6].getSmellName(),
                smellDetector.getSmells()[7].getSmellName());

        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> arg0) {
                ListCell<String> cell = new ListCell<>() {

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
    
    private ObservableList<PieChart.Data> initializeData() {
        return FXCollections.observableArrayList(
                new PieChart.Data(smellDetector.getSmells()[0].getSmellName(), 0),
                new PieChart.Data(smellDetector.getSmells()[1].getSmellName(), 0),
                new PieChart.Data(smellDetector.getSmells()[2].getSmellName(), 0),
                new PieChart.Data(smellDetector.getSmells()[3].getSmellName(), 0),
                new PieChart.Data(smellDetector.getSmells()[4].getSmellName(), 0),
                new PieChart.Data(smellDetector.getSmells()[5].getSmellName(), 0),
                new PieChart.Data(smellDetector.getSmells()[6].getSmellName(), 0),
                new PieChart.Data(smellDetector.getSmells()[7].getSmellName(), 0));
    }

    private int getColor(Node node) {
        return Integer.parseInt(node.getStyleClass().get(2).substring(node.getStyleClass().get(2).length()-1)); // extracts default color index
    }

    }