import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new  FXMLLoader(getClass().getResource("ui.fxml"));
        System.out.println(getClass().getResource("ui.fxml"));
        AnchorPane root = fxmlLoader.load();


        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });


        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });


        Scene scene = new Scene(root);
        primaryStage.setMaximized(false);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        primaryStage.setTitle("NoseJob v1.0");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String args[]){
        launch(args);
    }
}
