package pl.koncerty.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pl.koncerty.util.SceneUtil;

import java.io.IOException;

public class UzytkownikController extends SceneUtil{


    @FXML private TextField wykonawcaField, miejsceField, cenaField;
    @FXML private DatePicker dataPicker;
    @FXML private Label statusLabel;

    @FXML
    private void buyTicket(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/koncerty/gui/kup_bilet.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Kupno biletu");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void showTicket(){

    }
}
