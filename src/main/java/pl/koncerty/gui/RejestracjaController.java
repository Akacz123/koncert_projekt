package pl.koncerty.gui;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.koncerty.util.HibernateUtil;
import pl.koncerty.model.Uzytkownik;

public class RejestracjaController {

    @FXML
    private TextField imieField, nazwiskoField, emailField, loginField;
    @FXML
    private PasswordField hasloField;

    @FXML
    private void zarejestrujUzytkownika() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Uzytkownik user = new Uzytkownik(
                    imieField.getText(),
                    nazwiskoField.getText(),
                    emailField.getText(),
                    loginField.getText(),
                    hasloField.getText()
            );

            session.save(user);
            tx.commit();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukces");
            alert.setHeaderText(null);
            alert.setContentText("Użytkownik został zarejestrowany.");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Błąd podczas rejestracji: " + e.getMessage());
            alert.showAndWait();
        }
    }

}
