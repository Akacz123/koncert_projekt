package pl.koncerty.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import pl.koncerty.HibernateUtil;
import pl.koncerty.model.Uzytkownik;
import org.hibernate.query.Query;

public class LoginController {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField hasloField;

    @FXML
    private void zaloguj() {
        String login = loginField.getText();
        String haslo = hasloField.getText();

        if (login.equals("admin") && haslo.equals("admin")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/koncerty/gui/admin_panel.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setTitle("Panel administratora");
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        MenuItem statusLabel = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Uzytkownik> query = session.createQuery(
                    "FROM Uzytkownik WHERE login = :login AND haslo = :haslo", Uzytkownik.class);
            query.setParameter("login", login);
            query.setParameter("haslo", haslo);
            Uzytkownik uzytkownik = query.getSingleResult();

            if (uzytkownik != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/koncerty/gui/uzytkownik_panel.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setTitle("Panel użytkownika");
                stage.setScene(scene);
                stage.show();

                Stage currentStage = (Stage) loginField.getScene().getWindow();
                currentStage.close();
            } else {
                statusLabel.setText("Nieprawidłowy login lub hasło.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Błąd logowania.");
        }
        System.out.println("Login lub hasło niepoprawne.");
    }
    @FXML
    private void przejdzDoRejestracji() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/koncerty/gui/rejestracja.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Rejestracja użytkownika");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

