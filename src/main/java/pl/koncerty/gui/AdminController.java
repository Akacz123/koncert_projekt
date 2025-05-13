package pl.koncerty.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.koncerty.exceptions.NieprawidlowaCenaException;
import pl.koncerty.model.Koncert;
import pl.koncerty.util.HibernateUtil;
import pl.koncerty.util.SceneUtil;

import java.io.IOException;
import java.time.LocalDate;

public class AdminController extends SceneUtil {

    @FXML private Button zarzadzajBtn;
    @FXML private TextField wykonawcaField, miejsceField, cenaField;
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

        if (wykonawca.isEmpty() || data == null || miejsce.isEmpty() || cenaText.isEmpty()) {
            pokazAlert(Alert.AlertType.WARNING, "Uzupelnij wszystkie pola!");
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
            pokazAlert(Alert.AlertType.INFORMATION, "Koncert dodany!");
        } catch (NumberFormatException e) {
            pokazAlert(Alert.AlertType.WARNING, "Cena musi być liczbą!");
        } catch (NieprawidlowaCenaException e) {
            pokazAlert(Alert.AlertType.WARNING, e.getMessage());
        }
    }


    @FXML
    private void przejdzDoZarzadzania() {
        otworzPanel("pl/koncerty/gui/zarzadzanie_koncertami.fxml", "Zarządzanie Koncertami");
    }
    @FXML
    private void wyloguj() {
        try{
            wyloguj("pl/koncerty/gui/login.fxml", "Logowanie", cenaField);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
