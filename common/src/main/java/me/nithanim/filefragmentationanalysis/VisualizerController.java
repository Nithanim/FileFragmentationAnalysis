package me.nithanim.filefragmentationanalysis;

import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.SneakyThrows;
import me.nithanim.filefragmentationanalysis.fragmentation.commonapi.Fragment;
import me.nithanim.filefragmentationanalysis.fragmentation.win.apiimpl.WindowsFileFragmentationAnalyzer;
import me.nithanim.fragmentationstatistics.natives.windows.WinapiNative;

public class VisualizerController implements Initializable {
    @FXML
    private Canvas canvas;
    private List<Fragment> fragments;

    @Override
    @SneakyThrows
    public void initialize(URL location, ResourceBundle resources) {
        WindowsFileFragmentationAnalyzer a = new WindowsFileFragmentationAnalyzer(new WinapiNative());
        fragments = a.analyze(Paths.get("E:\\Programme\\MyDefrag v4.3.1\\FTB_Launcher.jar"));
        //r = a.analyze(Paths.get("E:\\videos\\Wow-64 2018-05-05 23-45-55-18.avi"));
        repaint();
    }

    private void repaint() {
        long min = fragments.stream().mapToLong(Fragment::getDiskOffset).min().getAsLong();
        Fragment maxP = fragments.stream().sorted((Fragment o1, Fragment o2) -> Long.compare(o2.getDiskOffset(), o1.getDiskOffset())).findFirst().get();
        long max = maxP.getDiskOffset() + maxP.getSize();
        long width = max - min;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double scale = canvas.getWidth() / width;
        for (Fragment p : fragments) {
            double w = (double) p.getSize() * scale;

            gc.setFill(Color.RED);
            gc.fillRect((p.getDiskOffset() - min) * scale, 0, w, canvas.getHeight());
            System.out.println("Pos " + ((p.getDiskOffset() - min) * scale));
        }
    }
}
