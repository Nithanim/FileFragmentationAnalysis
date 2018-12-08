package me.nithanim.filefragmentationanalysis.gui;

import io.netty.util.concurrent.Future;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CancellationException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import me.nithanim.filefragmentationanalysis.scanning.PathFragmentationScanner;

@Slf4j
public class GuiMain extends Application {
    private Parent scanView;
    private ScanController scanController;
    private Parent statisticsView;
    private StatisticsController statisticsController;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadScanView();
        loadStatisticsView();

        scene = new Scene(statisticsView);
        primaryStage.setScene(scene);
        primaryStage.setTitle("File Fragmentation Analysis");
        primaryStage.show();
    }

    private void onNewScanButtonPress() {
        newScan();
    }

    private void newScan() {
        DirectoryChooser fc = new DirectoryChooser();
        File file = fc.showDialog(scene.getWindow());
        if (file != null) {
            Future<PathFragmentationScanner.ScanResult> f = scanController.load(file.toPath());
            scene.setRoot(scanView);
            f.addListener((Future<PathFragmentationScanner.ScanResult> future) -> {
                PathFragmentationScanner.ScanResult r = future.getNow();

                Platform.runLater(() -> {
                    if (future.isSuccess()) {
                        statisticsController.updatePath(file.toPath());

                        statisticsController.displayIndex(r.getIndex());
                        scene.setRoot(statisticsView);
                    } else {
                        statisticsController.updatePath(null);
                        statisticsController.displayIndex(null);
                        scene.setRoot(statisticsView);

                        if (future.cause().getClass() != CancellationException.class) {
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error analyzing " + file);
                            alert.setHeaderText("Error analyzing " + file);
                            log.error("Error analyzing chosen folder!", future.cause());
                            alert.setContentText(future.cause().getMessage());
                            alert.showAndWait();
                        }
                    }
                });
            });

            scene.getWindow().setOnCloseRequest(e -> {
                if (!f.isDone()) {
                    f.cancel(true);
                }
            });
            f.addListener(a -> scene.getWindow().setOnCloseRequest(null));
        }
    }

    private void loadScanView() throws IOException {
        scanController = new ScanController();
        FXMLLoader l = new FXMLLoader();
        l.setController(scanController);
        l.setLocation(GuiMain.class.getResource("/fxml/scanning.fxml"));
        scanView = l.load();
    }

    private void loadStatisticsView() throws IOException {
        statisticsController = new StatisticsController(this::onNewScanButtonPress);
        FXMLLoader l = new FXMLLoader();
        l.setController(statisticsController);
        l.setLocation(GuiMain.class.getResource("/fxml/statistics.fxml"));
        statisticsView = l.load();
    }

    public static void main(String[] args) {
        launch(GuiMain.class, args);
    }
}
