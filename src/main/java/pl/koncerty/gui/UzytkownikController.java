package pl.koncerty.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pl.koncerty.util.SceneUtil;

import java.io.IOException;

public class UzytkownikController extends SceneUtil{


    @FXML private TextField cenaField;

    @FXML
    private void buyTicket(){
        otworzPanel("/pl/koncerty/gui/kup_bilet.fxml", "Kupno Biletu", cenaField);
    }

    @FXML
    private void showTicket(){

    }

    @FXML
    private void wyloguj() {
        otworzPanel("/pl/koncerty/gui/login.fxml", "Logowanie", cenaField);
    }

}
