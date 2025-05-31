package pl.koncerty.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;
import pl.koncerty.model.Uzytkownik;
import pl.koncerty.util.HibernateUtil;
import pl.koncerty.util.SceneUtil;

import javax.persistence.NoResultException;
import java.util.Optional;

public class LoginController {

    @FXML private TextField loginField;
    @FXML private PasswordField hasloField;
    @FXML private ImageView logo;
    @FXML private Label statusLabel;


    private void pokazAlert(Alert.AlertType typ, String tresc) {
        Alert alert = new Alert(typ);
        alert.setTitle("Informacja");
        alert.setHeaderText(null);
        alert.setContentText(tresc);
        alert.showAndWait();
    }

    @FXML
    private void zaloguj() {
        String login = loginField.getText();
        String haslo = hasloField.getText();
        statusLabel.setText("");

        try {
            if (login.isEmpty() || haslo.isEmpty()) {
                statusLabel.setText("Login i hasło nie mogą być puste.");
                return;
            }

            if (login.equals("admin") && haslo.equals("admin")) {
                SceneUtil.otworzPanel("/pl/koncerty/gui/admin_panel.fxml", "Panel administratora", loginField);
                return;
            }

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Query<Uzytkownik> query = session.createQuery(
                        "FROM Uzytkownik WHERE login = :login AND haslo = :haslo", Uzytkownik.class);
                query.setParameter("login", login);
                query.setParameter("haslo", haslo);

                Uzytkownik uzytkownik = query.getSingleResult();

                SceneUtil.setAktualnyUzytkownik(uzytkownik);

                SceneUtil.otworzPanel("/pl/koncerty/gui/uzytkownik_panel.fxml", "Panel użytkownika", loginField);

            } catch (NoResultException e) {
                statusLabel.setText("Nieprawidłowy login lub hasło.");
            }

        } catch (Exception e) {
            statusLabel.setText("Błąd logowania: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void przejdzDoRejestracji() {
        SceneUtil.otworzPanel("/pl/koncerty/gui/rejestracja.fxml", "Rejestracja", loginField);
    }

    @FXML
    private void obslugaZapomnialemHasla() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Resetowanie Hasła");
        dialog.setHeaderText("Podaj swój adres e-mail, aby otrzymać instrukcje resetowania hasła.");
        dialog.setContentText("E-mail:");

        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().clear();

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(email -> {
            if (!email.trim().isEmpty()) {
                pokazAlert(Alert.AlertType.INFORMATION, "Jeśli podany adres e-mail istnieje w systemie, instrukcje resetowania hasła zostały wysłane na adres: " + email);
            } else {
                pokazAlert(Alert.AlertType.WARNING, "Adres e-mail nie może być pusty.");
            }
        });
    }
}