package pl.koncerty.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.koncerty.exceptions.NieprawidlowaCenaException;
import pl.koncerty.model.Koncert;
import pl.koncerty.util.HibernateUtil;
import pl.koncerty.util.SceneUtil;

import java.time.LocalDate;

public class AdminController {

    @FXML private TextField wykonawcaField;
    @FXML private TextField miejsceField;
    @FXML private TextField cenaField;
    @FXML private DatePicker dataPicker;
    @FXML private Label statusLabel;

    private void pokazAlert(Alert.AlertType typ, String tresc) {
        Alert alert = new Alert(typ);
        alert.setTitle("Informacja");
        alert.setHeaderText(null);
        alert.setContentText(tresc);
        alert.showAndWait();
    }

    @FXML
    private void dodajKoncert() {
        String wykonawca = wykonawcaField.getText();
        LocalDate data = dataPicker.getValue();
        String miejsce = miejsceField.getText();
        String cenaText = cenaField.getText();
        statusLabel.setText("");

        if (wykonawca.isEmpty() || data == null || miejsce.isEmpty() || cenaText.isEmpty()) {
            statusLabel.setText("Uzupełnij wszystkie pola!");
            return;
        }

        try {
            double cena = Double.parseDouble(cenaText);
            if (cena <= 0) {
                throw new NieprawidlowaCenaException("Cena musi być większa niż 0.");
            }

            Koncert koncert = new Koncert(wykonawca, data, miejsce, cena);

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction tx = session.beginTransaction();
                session.save(koncert);
                tx.commit();
            }
            statusLabel.setText("Koncert dodany pomyślnie!");
            wykonawcaField.clear();
            dataPicker.setValue(null);
            miejsceField.clear();
            cenaField.clear();

        } catch (NumberFormatException e) {
            statusLabel.setText("Cena musi być liczbą!");
        } catch (NieprawidlowaCenaException e) {
            statusLabel.setText(e.getMessage());
        } catch (Exception e) {
            statusLabel.setText("Błąd dodawania koncertu: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void przejdzDoZarzadzania() {
        SceneUtil.otworzPanel("/pl/koncerty/gui/zarzadzanie_koncertami.fxml", "Zarządzanie Koncertami", cenaField);
    }

    @FXML
    private void wyloguj() {
        SceneUtil.otworzPanel("/pl/koncerty/gui/login.fxml", "Logowanie", cenaField);
    }

    @FXML
    private void powrot() {
        SceneUtil.powrot(cenaField);
    }
}