package pl.koncerty.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import pl.koncerty.model.Uzytkownik;
import pl.koncerty.util.HibernateUtil;
import pl.koncerty.util.SceneUtil;


public class RejestracjaController {

    @FXML private TextField imieField;
    @FXML private TextField nazwiskoField;
    @FXML private TextField emailField;
    @FXML private TextField loginField;
    @FXML private PasswordField hasloField;
    @FXML private Label statusRejestracjiLabel;

    private void pokazBlad(String wiadomosc) {
        statusRejestracjiLabel.setText(wiadomosc);
        statusRejestracjiLabel.setVisible(true);
        statusRejestracjiLabel.setManaged(true);
    }

    private void ukryjBlad() {
        statusRejestracjiLabel.setVisible(false);
        statusRejestracjiLabel.setManaged(false);
        statusRejestracjiLabel.setText("");
    }

    @FXML
    private void zarejestrujUzytkownika() {
        ukryjBlad();

        String imie = imieField.getText();
        String nazwisko = nazwiskoField.getText();
        String email = emailField.getText();
        String login = loginField.getText();
        String haslo = hasloField.getText();

        if (imie.isEmpty() || nazwisko.isEmpty() || email.isEmpty() || login.isEmpty() || haslo.isEmpty()) {
            pokazBlad("Wszystkie pola muszą być wypełnione.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Uzytkownik user = new Uzytkownik(imie, nazwisko, email, login, haslo);
                session.save(user);
                tx.commit();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sukces");
                alert.setHeaderText(null);
                alert.setContentText("Użytkownik " + login + " został zarejestrowany pomyślnie!");
                alert.showAndWait();
                powrotDoLogowania();

            } catch (ConstraintViolationException cve) {
                if (tx != null) tx.rollback();
                if (cve.getConstraintName() != null) {
                    if (cve.getConstraintName().contains("uzytkownik_email_key")) {
                        pokazBlad("Adres e-mail jest już zajęty.");
                    } else if (cve.getConstraintName().contains("uzytkownik_login_key")) {
                        pokazBlad("Login jest już zajęty.");
                    } else {
                        pokazBlad("Błąd danych: " + cve.getMessage());
                    }
                } else {
                    pokazBlad("Błąd bazy danych: naruszenie ograniczenia.");
                }
                cve.printStackTrace();
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                pokazBlad("Błąd podczas rejestracji: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void powrotDoLogowania() {
        SceneUtil.otworzPanel("/pl/koncerty/gui/login.fxml", "Logowanie", loginField);
    }
}