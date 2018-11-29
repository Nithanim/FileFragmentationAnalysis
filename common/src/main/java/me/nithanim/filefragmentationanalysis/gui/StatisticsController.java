package me.nithanim.filefragmentationanalysis.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;
import me.nithanim.filefragmentationanalysis.filetypes.FileType;
import me.nithanim.filefragmentationanalysis.filetypes.FileTypeCategory;
import me.nithanim.filefragmentationanalysis.statistics.FileStatisticsReport;
import me.nithanim.filefragmentationanalysis.statistics.StatisticalAnalysis;
import me.nithanim.filefragmentationanalysis.statistics.StatisticsCalculator;
import me.nithanim.filefragmentationanalysis.storage.Index;
import me.nithanim.filefragmentationanalysis.storage.StorageFormatReader;
import me.nithanim.filefragmentationanalysis.storage.StorageFormatWriter;

@Slf4j
public class StatisticsController implements Initializable {
    @FXML
    private TreeView tvFiles;
    @FXML
    private TextArea textArea;
    @FXML
    private Button btnOpen;
    @FXML
    private Label lblRootPath;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnLoad;
    @FXML
    private Button btnUpload;

    private final Runnable onNewScanButtonPress;
    private Index index;

    public StatisticsController(Runnable onNewScanButtonPress) {
        this.onNewScanButtonPress = onNewScanButtonPress;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updatePath(null);
        btnSave.setOnAction(this::onSave);
        btnLoad.setOnAction(this::onLoad);
        btnUpload.setOnAction(this::onUpload);

        btnOpen.setOnAction((ActionEvent event) -> {
            onNewScanButtonPress.run();
        });

        tvFiles.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            TreeItem ti = (TreeItem) newValue;
            if (ti == null || ti.getValue() == null) {
                textArea.setText("");
            } else if (ti.getValue() instanceof String) {
                textArea.setText("");
            } else {
                StatisticNode w = (StatisticNode) ti.getValue();
                textArea.setText(buildText(w));
            }
        });
    }

    private String buildText(StatisticNode w) {
        return "Size:\n"
            + toString(StatisticalAnalysis.from(w.getSize()))
            + "\n\n\nFragments:\n"
            + toString(StatisticalAnalysis.from(w.getFragments()));
    }

    private String toString(FileStatisticsReport r) {
        StringBuilder sb = new StringBuilder();

        sb.append(">>> Fragmentation for ").append(r.getExtension()).append('\n');
        sb.append("Size:").append('\n');
        sb.append(toString(r.getSize())).append('\n');
        sb.append("Fragments:").append('\n');
        sb.append(toString(r.getFragments())).append('\n');

        return sb.toString();
    }

    private String toString(StatisticalAnalysis sa) {
        StringBuilder sb = new StringBuilder();

        String f = "    %11s: ";
        sb.append(String.format(f, "n")).append(sa.getN()).append('\n');
        sb.append(String.format(f, "min")).append(sa.getMin()).append('\n');
        sb.append(String.format(f, "max")).append(sa.getMax()).append('\n');
        sb.append(String.format(f, "mean")).append(sa.getMean()).append('\n');
        sb.append(String.format(f, "stand. dev.")).append(sa.getStandardDeviation()).append('\n');
        sb.append(String.format(f, "25th perc.")).append(sa.getPercentile25()).append('\n');
        sb.append(String.format(f, "75th perc.")).append(sa.getPercentile75()).append('\n');

        return sb.toString();
    }

    public void displayIndex(Index index) {
        this.index = index;
        StatisticsCalculator sc = new StatisticsCalculator();
        index.getAll().forEach(sc::add);

        updateData(sc);
    }

    private void updateData(StatisticsCalculator sc) {
        if (sc == null) {
            tvFiles.setRoot(null);
            return;
        }
        TreeItem root = newTreeItem("All files", sc.getOverall());
        root.setExpanded(true);
        tvFiles.setRoot(root);

        TreeItem unknown = newTreeItem("Unknown", sc.getUnknownOnly());
        unknown.setExpanded(true);
        root.getChildren().add(unknown);

        TreeItem known = newTreeItem("Known", sc.getKnownOnly());
        known.setExpanded(true);
        root.getChildren().add(known);

        for (FileTypeCategory ftc : FileTypeCategory.values()) {
            StatisticsCalculator.FileStatWrapper dtcd = sc.getByFileTypeCategory().get(ftc);
            if (dtcd != null) {
                TreeItem ftcti = newTreeItem(ftc.name().toLowerCase(), dtcd);
                ftcti.setExpanded(true);
                known.getChildren().add(ftcti);

                for (FileType ft : ftc.getFiletypes()) {
                    StatisticsCalculator.FileStatWrapper ftd = sc.getByFileType().get(ft);
                    if (ftd != null) {
                        TreeItem ftti = newTreeItem(ft.name().toLowerCase(), ftd);
                        ftti.setExpanded(true);
                        ftcti.getChildren().add(ftti);
                    }
                }
            }
        }
    }

    private TreeItem newTreeItem(String name, StatisticsCalculator.FileStatWrapper w) {
        TreeItem ti = new TreeItem(new StatisticNode(name, w.getSize(), w.getFragments()));
        return ti;
    }

    public void updatePath(Path p) {
        if (p == null) {
            lblRootPath.setText("<-- Choose path to analyze");
        } else {
            lblRootPath.setText(p.toString());
        }
    }

    private void onSave(ActionEvent ae) {
        if (index == null) {
            return;
        }
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("File Fragmentation Index files (*.ffi)", "*.ffi"));
        fc.setSelectedExtensionFilter(fc.getExtensionFilters().get(0));
        File f = fc.showSaveDialog(btnSave.getScene().getWindow());
        if (f != null) {
            if(!f.getName().endsWith(".ffi")) {
                f = new File(f.getParentFile(), f.getName()+".ffi");
            }
            try {
                try(FileOutputStream out = new FileOutputStream(f)) {
                    new StorageFormatWriter().write(out, index);
                }
            } catch (IOException ex) {
                log.error("Error saving index", ex);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error saving index!");
                alert.setHeaderText("Error saving index!");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        }
    }

    private void onLoad(ActionEvent ae) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("File Fragmentation Index files (*.ffi)", "*.ffi"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files (*.*)", "*.*"));
        fc.setSelectedExtensionFilter(fc.getExtensionFilters().get(0));
        File f = fc.showOpenDialog(btnLoad.getScene().getWindow());
        if (f != null) {
            try {
                FileInputStream in = new FileInputStream(f);
                Index index = new StorageFormatReader().read(in);
                displayIndex(index);
            } catch (IOException ex) {
                log.error("Error reading index", ex);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error reading index!");
                alert.setHeaderText("Error readingindex!");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        }
    }

    private void onUpload(ActionEvent ae) {

    }
}
