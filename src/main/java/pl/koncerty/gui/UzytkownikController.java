package pl.koncerty.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pl.koncerty.model.Uzytkownik;
import pl.koncerty.util.SceneUtil;

import java.io.IOException;

public class UzytkownikController extends SceneUtil{


    @FXML private TextField cenaField;

    @FXML
    private void buyTicket() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/koncerty/gui/kup_bilet.fxml"));
            Scene scene = new Scene(loader.load());

            ListaBiletController controller = loader.getController();
            controller.initUzytkownik(this.uzytkownik); // przekaż użytkownika

            Stage stage = new Stage();
            stage.setTitle("Kupno biletu");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showTicket() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/koncerty/gui/wyswietl_bilety.fxml"));
            Scene scene = new Scene(loader.load());

            ShowTicketController controller = loader.getController();
            controller.initUzytkownik(this.uzytkownik);

            Stage stage = new Stage();
            stage.setTitle("Twoje bilety");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void wyloguj() {
        otworzPanel("/pl/koncerty/gui/login.fxml", "Logowanie", cenaField);
    }

    private Uzytkownik uzytkownik;

    public void initData(Uzytkownik uzytkownik) {
        this.uzytkownik = uzytkownik;
    }

    public void initUzytkownik(Uzytkownik uzytkownik) {
        this.uzytkownik = uzytkownik;
    }

}
