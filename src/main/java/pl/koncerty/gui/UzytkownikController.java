package pl.koncerty.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import pl.koncerty.model.Uzytkownik;
import pl.koncerty.util.SceneUtil;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;


public class UzytkownikController implements Initializable {

    @FXML private Label powitanieLabel;
    @FXML private Button kupBiletyBtn;
    @FXML private Button mojeBiletyBtn;

    @FXML private Button wylogujBtn;

    private Uzytkownik uzytkownik;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("UzytkownikController - initialize() wywołane");
        uzytkownik = SceneUtil.getAktualnyUzytkownik();
        if (uzytkownik != null) {
            initUzytkownik(uzytkownik);
        } else {
            System.out.println("UWAGA: Brak użytkownika w SceneUtil podczas initialize()");
        }
    }

    public void initUzytkownik(Uzytkownik u) {
        System.out.println("UzytkownikController - inicjalizacja użytkownika: " +
                (u != null ? u.getLogin() + " (ID: " + u.getId() + ")" : "NULL"));
        this.uzytkownik = u;

        if (u != null && powitanieLabel != null) {
            powitanieLabel.setText("Witaj, " + u.getImie() + " " + u.getNazwisko() + "!");
        }
    }

    @FXML
    private void otworzKupBilety() {
        System.out.println("UzytkownikController - otwieranie panelu kupowania biletów");
        if (kupBiletyBtn != null) {
            System.out.println("kupBiletyBtn is not null.");
            if (kupBiletyBtn.getScene() != null) {
                System.out.println("kupBiletyBtn has a scene: " + kupBiletyBtn.getScene());
                if (kupBiletyBtn.getScene().getWindow() != null) {
                    System.out.println("kupBiletyBtn's scene has a window: " + kupBiletyBtn.getScene().getWindow());
                } else {
                    System.out.println("kupBiletyBtn's scene DOES NOT have a window (but has a scene).");
                }
            } else {
                System.out.println("kupBiletyBtn DOES NOT have a scene. This is the problem for closing the panel.");
            }
        } else {
            System.out.println("kupBiletyBtn IS null. This is also a problem.");
        }
        SceneUtil.otworzPanel("/pl/koncerty/gui/kup_bilet.fxml", "Kup bilety", kupBiletyBtn);
    }

    @FXML
    private void otworzMojeBilety() {
        System.out.println("UzytkownikController - otwieranie panelu moich biletów");
        if (mojeBiletyBtn != null) {
            System.out.println("mojeBiletyBtn is not null.");
            if (mojeBiletyBtn.getScene() != null) {
                System.out.println("mojeBiletyBtn has a scene: " + mojeBiletyBtn.getScene());
                if (mojeBiletyBtn.getScene().getWindow() != null) {
                    System.out.println("mojeBiletyBtn's scene has a window: " + mojeBiletyBtn.getScene().getWindow());
                } else {
                    System.out.println("mojeBiletyBtn's scene DOES NOT have a window (but has a scene).");
                }
            } else {
                System.out.println("mojeBiletyBtn DOES NOT have a scene. This is the problem for closing the panel.");
            }
        } else {
            System.out.println("mojeBiletyBtn IS null. This is also a problem.");
        }
        SceneUtil.otworzPanel("/pl/koncerty/gui/wyswietl_bilety.fxml", "Moje bilety", mojeBiletyBtn);
    }

    @FXML
    private void wyloguj(MouseEvent event) { // Dodaj parametr MouseEvent
        System.out.println("UzytkownikController - wylogowywanie użytkownika (przez Label)");
        Node source = (Node) event.getSource();
        if (source != null) {
            System.out.println("Wyloguj Label (source) is not null.");
            if (source.getScene() != null) {
                System.out.println("Wyloguj Label (source) has a scene: " + source.getScene());
                if (source.getScene().getWindow() != null) {
                    System.out.println("Wyloguj Label (source)'s scene has a window: " + source.getScene().getWindow());
                } else {
                    System.out.println("Wyloguj Label (source)'s scene DOES NOT have a window (but has a scene).");
                }
            } else {
                System.out.println("Wyloguj Label (source) DOES NOT have a scene. This is the problem for logout.");
            }
        } else {
            System.out.println("Wyloguj Label (source) IS null. This is also a problem for logout.");
        }
        SceneUtil.wyloguj(source);
    }
}