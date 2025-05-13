package pl.koncerty.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;
import pl.koncerty.util.HibernateUtil;
import pl.koncerty.exceptions.LoginException;
import pl.koncerty.model.Uzytkownik;
import org.hibernate.query.Query;
import pl.koncerty.util.SceneUtil;

import javax.persistence.NoResultException;

public class LoginController extends SceneUtil {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField hasloField;

    @FXML
    private Label statusLabel;

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

        try {
            if (login.equals("admin") && haslo.equals("admin")) {
                otworzPanel("/pl/koncerty/gui/admin_panel.fxml", "Panel administratora");
                return;
            }

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Query<Uzytkownik> query = session.createQuery(
                        "FROM Uzytkownik WHERE login = :login AND haslo = :haslo", Uzytkownik.class);
                query.setParameter("login", login);
                query.setParameter("haslo", haslo);

                Uzytkownik uzytkownik = query.getSingleResult();

                if (uzytkownik != null) {
                    otworzPanel("/pl/koncerty/gui/uzytkownik_panel.fxml", "Panel użytkownika");

                    Stage currentStage = (Stage) loginField.getScene().getWindow();
                    currentStage.close();
                }
            } catch (NoResultException e) {
                throw new LoginException("Nieprawidłowy login lub hasło.");
            }

        } catch (LoginException e) {
           pokazAlert(Alert.AlertType.WARNING, e.getMessage());
        } catch (Exception e) {
            pokazAlert(Alert.AlertType.WARNING, "Bląd logowania!");
            e.printStackTrace();
        }
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