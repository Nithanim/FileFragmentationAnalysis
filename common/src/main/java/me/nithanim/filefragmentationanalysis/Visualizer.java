package me.nithanim.filefragmentationanalysis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Visualizer extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader l = new FXMLLoader();
        l.setController(new VisualizerController());
        l.setLocation(Visualizer.class.getResource("/fxml/visualizer.fxml"));
        Parent p = l.load();

        Scene scene = new Scene(p);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Visualizer");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(Visualizer.class, args);
    }
}
