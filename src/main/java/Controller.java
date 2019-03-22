import com.github.javaparser.ast.CompilationUnit;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import metrics.FileMetrics;
import results.SmellResult;
import smells.SmellDetector;

import java.io.File;
import java.util.ArrayList;


public class Controller {

    @FXML
    private PieChart occurrencePieChart,severityPieChart;

    @FXML
    private Button projectBtn, chartsBtn, detailsBtn;

    @FXML
    private TextArea detailsField;

    @FXML
    private ImageView logo;

    private boolean isDirChosen = false;
    private String projectDir;
    private SmellDetector smellDetector;

    @FXML
    private void initialize(){

        /*occurrencePieChart.setVisible(false);
        severityPieChart.setVisible(false);*/
        chartsBtn.setDisable(true);
        detailsBtn.setDisable(true);
        detailsField.setVisible(false);

        projectBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                File dir = showDirChooser();
                if(dir!=null) {

                    if(projectDir!=null) reset(); // If directory was already chosen, remove all previous data

                    isDirChosen = true; // Used for determining which buttons/pane to show

                    projectDir = dir.getAbsolutePath();
                    smellDetector = new SmellDetector(projectDir);
                    smellDetector.detectSmells();

                    fillCharts();
                    fillField();
                    chartsBtn.setDisable(true);
                    detailsBtn.setDisable(false);

                }else{
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setHeaderText("ERROR");
                    error.showAndWait();
                }

            }
        });

        chartsBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(isDirChosen && detailsBtn.isDisabled()) {
                        detailsField.setVisible(false);
                        occurrencePieChart.setVisible(true);
                        severityPieChart.setVisible(true);
                        detailsBtn.setDisable(false);
                        chartsBtn.setDisable(true);
                }
            }
        });

        detailsBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(isDirChosen && chartsBtn.isDisabled()) {
                        occurrencePieChart.setVisible(false);
                        severityPieChart.setVisible(false);
                        detailsField.setVisible(true);
                        detailsBtn.setDisable(true);
                        chartsBtn.setDisable(false);
                    }
                }
            }
        );


       /* if(severityPieChart.isVisible()) {
            final Label caption = new Label("");
            caption.setTextFill(Color.BLACK);
            caption.setStyle("-fx-font: 12 arial;");

            for (final PieChart.Data data : occurrencePieChart.getData()) {
                data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                        new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent e) {
                                System.out.println("LOL");
                                caption.setTranslateX(e.getSceneX());
                                caption.setTranslateY(e.getSceneY());
                                caption.setText(String.valueOf(data.getPieValue()) + "%");
                            }
                        });
            }

            for (final PieChart.Data data : severityPieChart.getData()) {
                data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                        new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent e) {
                                caption.setTranslateX(e.getSceneX());
                                caption.setTranslateY(e.getSceneY());
                                caption.setText(String.valueOf(data.getPieValue()) + "%");
                            }
                        });
            }
        }*/

    }

    private void reset(){
        /**Clear data from previous project**/
        detailsField.clear();
        occurrencePieChart.getData().clear();
        severityPieChart.getData().clear();
        projectDir = null;

        /** Open Charts **/
        detailsField.setVisible(false);
        occurrencePieChart.setVisible(true);
        severityPieChart.setVisible(true);

    }
    private File showDirChooser(){
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.dir")));
        File selectedDir = dc.showDialog(null);
        return selectedDir;
    }

    private void fillCharts() {

        occurrencePieChart.setTitle("Average Occurrence of Smells");
        severityPieChart.setTitle("Average Severity of Smells");
        int SLICES = smellDetector.getSmellResults().size();
        ArrayList<PieChart.Data> occurrenceData = new ArrayList<>();
        ArrayList<PieChart.Data> severityData = new ArrayList<>();

        String prevSmellName = "";
        String currSmellName = smellDetector.getSmellResults().get(0).getSmellName();
        int index = 0;
        double average;


        for(int i =1;i<SLICES ;i++)
        {

            /**Occurence Pie Chart**/
            if(!prevSmellName.equals(currSmellName)){
                average = extractAverageOccurenceOfSmell(currSmellName);
                occurrenceData.add(new PieChart.Data(currSmellName, average));
                occurrencePieChart.getData().add(occurrenceData.get(index));
                System.out.println(currSmellName + " " + average);

                /**Severity Pie Chart**/
                average = extractAverageSeverityOfSmell(currSmellName);
                severityData.add(new PieChart.Data(currSmellName, average));
                severityPieChart.getData().add(severityData.get(index));

                index++;

            }
            prevSmellName = currSmellName;
            currSmellName = smellDetector.getSmellResults().get(i).getSmellName();
        }

    }

    private void fillField(){
        detailsField.appendText("Directory chosen: " + projectDir + "\n");
        
        for(CompilationUnit cu: smellDetector.getCUs()) {
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
        }
    }

    /** Gets total average severity of a specific smell across all source files **/
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
        return totalAverage/count;
    }

    /** Gets total average occurrence of a specific smell across all source files **/
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
        return totalAverage/count;
    }


}
