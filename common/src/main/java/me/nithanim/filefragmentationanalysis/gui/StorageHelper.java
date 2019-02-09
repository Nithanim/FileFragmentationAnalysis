package me.nithanim.filefragmentationanalysis.gui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import lombok.Value;
import me.nithanim.filefragmentationanalysis.storage.Index;
import me.nithanim.filefragmentationanalysis.storage.formats.reader.FragStorageFormatReader;
import me.nithanim.filefragmentationanalysis.storage.formats.writer.StorageFormatSelector;
import me.nithanim.filefragmentationanalysis.storage.formats.writer.StorageFormatSelectorFx;
import me.nithanim.filefragmentationanalysis.storage.formats.writer.StorageFormatType;
import me.nithanim.filefragmentationanalysis.storage.formats.writer.StorageFormatWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class StorageHelper {
    private static final Logger log = LoggerFactory.getLogger(StorageHelper.class);

    public static void onSave(Index index, Window window) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(StorageFormatSelectorFx.getAll());
        fc.setSelectedExtensionFilter(fc.getExtensionFilters().get(0));
        File f = fc.showSaveDialog(window);
        if (f != null) {
            SaveFile sv = getSaveFile(fc, f);
            try {
                try (FileOutputStream out = new FileOutputStream(sv.getF())) {
                    sv.getSfw().write(new BufferedOutputStream(out), index);
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

    private static SaveFile getSaveFile(FileChooser fs, File chosen) {
        String ext = fs.getSelectedExtensionFilter().getExtensions().get(0).substring(1);
        String origName = chosen.getName();
        int lastDot = origName.lastIndexOf('.');
        if (lastDot == -1) {
            origName = origName + ext;
            lastDot = origName.lastIndexOf('.');
            chosen = new File(chosen.getParent(), origName);
        }

        boolean gzip = false;
        String last = origName.substring(lastDot + 1);
        if (last.equalsIgnoreCase("gz")) {
            gzip = true;
            int newLastDot = origName.substring(0, origName.length() - last.length() - 1).lastIndexOf('.', lastDot);
            last = origName.substring(newLastDot + 1, lastDot);
        }

        StorageFormatType type = StorageFormatType.typeFromExtension(last);
        if (type == null) {
            type = StorageFormatType.FRAG;
            gzip = false;
            chosen = new File(chosen.getParentFile(), chosen.getName() + ".ffi");
        }

        return new SaveFile(chosen, StorageFormatSelector.getWriter(type, gzip));
    }

    static Index onLoad(Window window) throws IOException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(StorageFormatSelectorFx.MAIN_EXT_FILTER);
        //fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files (*.*)", "*.*"));
        fc.setSelectedExtensionFilter(fc.getExtensionFilters().get(0));
        File f = fc.showOpenDialog(window);
        if (f != null) {
            FileInputStream in = new FileInputStream(f);
            return new FragStorageFormatReader().read(in);
        } else {
            return null;
        }
    }

    @Value
    private static class SaveFile {
        File f;
        StorageFormatWriter sfw;
    }
}
