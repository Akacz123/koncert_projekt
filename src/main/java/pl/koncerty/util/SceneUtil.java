package pl.koncerty.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

public class SceneUtil {

    private static final Stack<SceneData> historiaScen = new Stack<>();

    public static void otworzPanel(String sciezkaFXML, String tytul, javafx.scene.Node komponentZOknaDoZamkniecia) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneUtil.class.getResource(sciezkaFXML));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(tytul);
            stage.setScene(new Scene(root));
            stage.show();

            Stage obecneOkno = (Stage) komponentZOknaDoZamkniecia.getScene().getWindow();
            obecneOkno.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T otworzPanelIZwrocKontroler(String sciezkaFXML, String tytul, javafx.scene.Node komponentZOknaDoZamkniecia) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneUtil.class.getResource(sciezkaFXML));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(tytul);
            stage.setScene(new Scene(root));
            stage.show();

            Stage obecneOkno = (Stage) komponentZOknaDoZamkniecia.getScene().getWindow();
            obecneOkno.close();

            return loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void powrot(Node komponent) {
        if (komponent != null && komponent.getScene() != null) {
            SceneData poprzednia = historiaScen.pop();

            Stage obecneOkno = (Stage) komponent.getScene().getWindow();
            Stage nowyStage = new Stage();
            nowyStage.setTitle(poprzednia.tytul);
            nowyStage.setScene(poprzednia.scene);
            nowyStage.show();

            obecneOkno.close();
        }
    }

    private static class SceneData {
        Scene scene;
        String tytul;

        SceneData(Scene scene, String tytul) {
            this.scene = scene;
            this.tytul = tytul;
        }
    }

}
