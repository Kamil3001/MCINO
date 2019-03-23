import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import results.SmellResult;
import smells.SmellDetector;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class UI implements Initializable {

    @FXML
    private Button btnHome, btnAnalysis, btnDetails, btnAbout, btnProject,btnOccurrence,btnSeverity,btnShowDetails;

    @FXML
    private FontAwesomeIconView btnClose;

    @FXML
    private AnchorPane pnHome, pnDetails, pnAnalysis, pnAbout;

    @FXML
    private Label lblPage, lblMsg, lblSource, lblSmell;

    @FXML
    private PieChart pieChart;

    @FXML
    private ComboBox<String> comboSource,comboSmell;


    private boolean isDirChosen = false;
    private boolean hasChartCreated = false;
    private String projectDir;
    private SmellDetector smellDetector;
    private ObservableList<PieChart.Data> pieChartData;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        openHome();
        lblSmell.setVisible(false);
        lblSource.setVisible(false);

        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> arg0) {
                ListCell<String> cell = new ListCell<String>() {

                    @Override
                    public void updateItem(String smell, boolean empty) {
                        super.updateItem(smell, empty);
                        if (smell != null) {
                            setText(smell);
                            setStyle("-fx-background-color: #333645;"+
                                    "-fx-text-fill: #7c8184;" );
                        } else {
                            setText(null);
                        }
                    }

                };

                cell.hoverProperty().addListener((observable -> {
                    if(cell.isHover())
                    {
                        cell.setStyle("-fx-background-color: #4c8bf5;"+
                                "-fx-text-fill: white;");
                    }else
                    {
                        cell.setStyle("-fx-background-color: #333645;"+
                                "-fx-text-fill: #7c8184;");
                    }
                }));

                cell.pressedProperty().addListener((observable -> {
                    cell.setStyle("-fx-background-color: #333645;"+
                            "-fx-text-fill: #7c8184;");
                }));

                return cell;
            }
        };
        comboSource.setCellFactory(cellFactory);
        comboSmell.setCellFactory(cellFactory);

    }

    /** Handlers **/
    @FXML
    void handleAnalysisClicks(MouseEvent event) {
        if(event.getSource() == btnSeverity && isDirChosen)
        {
            createChart("Severity");

        }
        else if(event.getSource() == btnOccurrence && isDirChosen)
        {
            createChart("Occurence");
        }
        else
        {
            error("Please choose a directory.");
        }
    }

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
                prepPieData();
                fillComboBox();
                openAnalysis();

            }

        }
    }

    @FXML
    void handleDetails(MouseEvent event) {
        if(event.getSource() == btnShowDetails)
        {
            if(comboSource.getSelectionModel().getSelectedItem() != null && comboSmell.getSelectionModel().getSelectedItem() != null) {
                lblSource.setText(comboSource.getSelectionModel().getSelectedItem());
                lblSmell.setText(comboSmell.getSelectionModel().getSelectedItem());
            }else{
                error("Please make a selection first.");
            }
        }
    }




    /** Helper Functions **/

    private File showDirChooser(){
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.dir")));
        File selectedDir = dc.showDialog(null);
        return selectedDir;
    }

    private void createChart(String chart) {
        if(hasChartCreated)
        {
            //TODO Glitch with charts, don't refresh by themselves once changed
        }
        lblMsg.setVisible(false);
        pieChart.setTitle("Average " +chart+" of Smells");
        int SLICES = smellDetector.getSmells().length;
        String smell;
        double average;

        if(chart.equalsIgnoreCase("Severity"))
        {
            for(int i =0;i<SLICES ;i++)
            {
                smell = smellDetector.getSmells()[i].getSmellName();
                average = extractAverageSeverityOfSmell(smell);
                addData(smell,average);
                System.out.println(smell+ " " + average);
            }
            System.out.println();
        }
        else if(chart.equalsIgnoreCase("Occurence"))
        {
            for(int i =0;i<SLICES ;i++)
            {
                smell = smellDetector.getSmells()[i].getSmellName();
                average = extractAverageOccurenceOfSmell(smell);
                addData(smell,average);
                System.out.println(smell+ " " + average);
            }
            System.out.println();
        }

        pieChart.setData(pieChartData);
        pieChart.setVisible(true);

        hasChartCreated = true;
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
        return totalAverage/count+1;
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

        return totalAverage/count+1;
    }

    private void hideAllPanes() {
        pnHome.setVisible(false);
        pnAnalysis.setVisible(false);
        pnDetails.setVisible(false);
        //pnAbout.setVisible(false);
    }

    private void reset() {
        //pieChart.getData().clear();
        pieChart.setVisible(false);
        projectDir = null;
        hasChartCreated = false;
        comboSmell.getItems().clear();
        comboSource.getItems().clear();
    }

    private void error(String err) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setHeaderText(err);
        error.showAndWait();
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
        if(!isDirChosen){
            lblMsg.setText("Nothing to display");
            lblMsg.setVisible(true);
        }else if(isDirChosen && !hasChartCreated){
            lblMsg.setText("Choose analysis type:");
            lblMsg.setVisible(true);
        }
        else
        {
            lblMsg.setVisible(false);
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

    private void addData(String smell, double average) {
        for(PieChart.Data d: pieChartData)
        {
            if(d.getName().equals(smell))
            {
                d.setPieValue(average);
                return;
            }
        }

        //If smell doesn't exist
        pieChartData.add(new PieChart.Data(smell,average));
    }

    private void fillComboBox() {
        comboSource.getItems().addAll(smellDetector.getSmellResults().get(0).getSeverityPerFile().keySet());
        comboSmell.getItems().addAll(smellDetector.getSmells()[0].getSmellName(),
                smellDetector.getSmells()[1].getSmellName(),
                smellDetector.getSmells()[2].getSmellName(),
                smellDetector.getSmells()[3].getSmellName(),
                smellDetector.getSmells()[4].getSmellName(),
                smellDetector.getSmells()[5].getSmellName(),
                smellDetector.getSmells()[6].getSmellName(),
                smellDetector.getSmells()[7].getSmellName());

       /* comboSource.getSelectionModel().selectFirst();
        comboSmell.getSelectionModel().selectFirst();*/
    }

    private void prepPieData() {
        pieChartData = FXCollections.observableArrayList(
                new PieChart.Data(smellDetector.getSmells()[0].getSmellName(), 0),
                new PieChart.Data(smellDetector.getSmells()[1].getSmellName(), 0),
                new PieChart.Data(smellDetector.getSmells()[2].getSmellName(), 0),
                new PieChart.Data(smellDetector.getSmells()[3].getSmellName(), 0),
                new PieChart.Data(smellDetector.getSmells()[4].getSmellName(), 0),
                new PieChart.Data(smellDetector.getSmells()[5].getSmellName(), 0),
                new PieChart.Data(smellDetector.getSmells()[6].getSmellName(), 0),
                new PieChart.Data(smellDetector.getSmells()[7].getSmellName(), 0));
    }
}

/*for(CompilationUnit cu: smellDetector.getCUs()) {
        FileMetrics classMetricsclass = new FileMetrics(cu);
        detailsField.appendText("------------------\n");
        detailsField.appendText("Class name: " + classMetricsclass.getClassNames().toString() + "\n");
        detailsField.appendText("Class constructors: "+classMetricsclass.getNumOfClassConstructors() + "\n");
        detailsField.appendText("Class length: " + classMetricsclass.getClassLengths().toString() + "\n");
        detailsField.appendText("Num fields: " + classMetricsclass.getNumOfFields() + "\n");
        detailsField.appendText("Num public fields: " + classMetricsclass.getNumOfPublicFields() + "\n");
        detailsField.appendText("Num methods: " + classMetricsclass.getNumOfMethods() + "\n");
        detailsField.appendText("Num public methods: " + classMetricsclass.getNumOfPublicMethods() + "\n");
        detailsField.appendText("Num comments: " + classMetricsclass.getClassComments().size()+"\n");
        detailsField.appendText("\n");
        }*/