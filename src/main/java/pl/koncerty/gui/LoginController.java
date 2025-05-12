package pl.koncerty.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


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

        System.out.println("Login lub hasło niepoprawne.");
    }

    @FXML
    private void przejdzDoRejestracji() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/pl/koncerty/gui/rejestracja.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Rejestracja użytkownika");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
