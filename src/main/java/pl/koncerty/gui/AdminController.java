package pl.koncerty.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.koncerty.model.Koncert;
import pl.koncerty.HibernateUtil;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AdminController {

    @FXML private Button zarzadzajBtn;
    @FXML private TextField wykonawcaField, miejsceField, cenaField;
    @FXML private DatePicker dataPicker;
    @FXML private Label statusLabel;

    @FXML
    private void dodajKoncert() {
        String wykonawca = wykonawcaField.getText();
        LocalDate data = dataPicker.getValue();
        String miejsce = miejsceField.getText();
        String cenaText = cenaField.getText();

        System.out.println(data);

        if (wykonawca.isEmpty() || data == null || miejsce.isEmpty() || cenaText.isEmpty()) {
            statusLabel.setText("Uzupełnij wszystkie pola.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            double cena = Double.parseDouble(cenaText);

            Koncert koncert = new Koncert(wykonawca, data, miejsce, cena);

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction tx = session.beginTransaction();
                session.save(koncert);
                tx.commit();
            }

            statusLabel.setText("Koncert dodany!");
            statusLabel.setStyle("-fx-text-fill: green;");
        } catch (NumberFormatException e) {
            statusLabel.setText("Cena musi być liczbą.");
            statusLabel.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            statusLabel.setText("Błąd: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void przejdzDoZarzadzania() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/koncerty/gui/zarzadzanie_koncertami.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Zarządzanie koncertami");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
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

}
