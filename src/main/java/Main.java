import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new  FXMLLoader(getClass().getResource("sample.fxml"));
        AnchorPane root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setMaximized(false);

        primaryStage.setTitle("NoseJob v1.0");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String args[]){
        launch(args);
    }
}
