package pl.koncerty.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneUtil {

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
}
