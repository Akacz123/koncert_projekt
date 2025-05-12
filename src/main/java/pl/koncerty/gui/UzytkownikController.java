package pl.koncerty.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.hibernate.Session;
import pl.koncerty.HibernateUtil;
import pl.koncerty.model.Koncert;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UzytkownikController{


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
