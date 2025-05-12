package pl.koncerty.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UzytkownikController {
    @FXML private TextField wykonawcaField, miejsceField, cenaField;
    @FXML private DatePicker dataPicker;
    @FXML private Label statusLabel;

    @FXML
    private void wyloguj() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/koncerty/gui/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Logowanie");
            stage.setScene(scene);
            stage.show();

            Stage obecneOkno = (Stage) cenaField.getScene().getWindow();
            obecneOkno.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
