import controller.Controller;
import controller.SimulationManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class QueueSimulation extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/QueueSimulator.fxml"));
        Parent root = myLoader.load();
        primaryStage.setTitle("Queue Simulator");
        Scene scene = new Scene(root, 1000, 710);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
