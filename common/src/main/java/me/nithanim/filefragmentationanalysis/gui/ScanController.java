package me.nithanim.filefragmentationanalysis.gui;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ImmediateEventExecutor;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import lombok.SneakyThrows;
import me.nithanim.filefragmentationanalysis.fragmentation.FragmentationAnalyzationException;
import me.nithanim.filefragmentationanalysis.scanning.PathFragmentationScanner;

public class ScanController implements Initializable {
    @FXML
    private Label lblRootFolder;
    @FXML
    private Label lblCurrentFile;
    @FXML
    private TextArea taError;
    @FXML
    private Button btnAbort;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @SneakyThrows
    public Future<PathFragmentationScanner.ScanResult> load(Path p) {
        lblCurrentFile.setText("Starting...");
        lblRootFolder.setText(p.toString());
        DefaultPromise<PathFragmentationScanner.ScanResult> cf = new DefaultPromise<>(ImmediateEventExecutor.INSTANCE);

        Thread t = new Thread(() -> {
            PathFragmentationScanner.Scan scan = new PathFragmentationScanner().scan(p, this::addError);
            try {
                Future<PathFragmentationScanner.ScanResult> externalFuture = scan.getFuture();
                cf.addListener((Future<PathFragmentationScanner.ScanResult> future) -> {
                    if (future.isCancelled()) {
                        externalFuture.cancel(true);
                    }
                });

                while (!externalFuture.isDone()) {
                    Thread.sleep(200);
                    updateCurrentFile(scan.getCurrentPath().get());
                }

                PathFragmentationScanner.ScanResult r = externalFuture.get();
                cf.trySuccess(r);
            } catch (InterruptedException ex) {
                scan.getFuture().cancel(true);
            } catch (Exception ex) {
                cf.tryFailure(ex);
            }
        });
        t.start();

        btnAbort.setOnAction((event) -> {
            cf.cancel(true);
        });

        return cf;
    }

    private void addError(FragmentationAnalyzationException ex) {
        Platform.runLater(() -> {
            taError.setText(taError.getText() + ex.getPath() + ": " + ex.getCause() + "\n");
        });
    }

    private final AtomicReference<Path> currentPath = new AtomicReference<>();

    private void updateCurrentFile(Path p) {
        //only issue new gui update if AR was reset to null to prevent overload
        if (currentPath.compareAndSet(null, p)) {
            if (p == null) { //take care of the ini phase where path (and our indicator) is null
                //noop
            } else {
                Platform.runLater(() -> {
                    lblCurrentFile.setText(currentPath.toString());
                    currentPath.set(null);
                });
            }
        }
    }
}
