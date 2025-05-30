package pl.koncerty.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.hibernate.Session;
import pl.koncerty.model.Bilet;
import pl.koncerty.model.Uzytkownik;
import pl.koncerty.util.HibernateUtil;
import pl.koncerty.model.Koncert;
import pl.koncerty.util.SceneUtil;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ListaBiletController implements Initializable {
    @FXML
    private TableColumn<Koncert, String> wykonawcaCol, miejsceCol;
    @FXML private TableColumn<Koncert, LocalDate> dataCol;
    @FXML private TableColumn<Koncert, Double> cenaCol;
    @FXML private TableView<Koncert> koncertTableView;
    @FXML private TextField wykonawcaField, miejsceField, cenaField;
    @FXML private DatePicker dataPicker;
    @FXML private ObservableList<Koncert> koncertList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        wykonawcaCol.setCellValueFactory(new PropertyValueFactory<>("wykonawca"));
        dataCol.setCellValueFactory(new PropertyValueFactory<>("data"));
        miejsceCol.setCellValueFactory(new PropertyValueFactory<>("miejsce"));
        cenaCol.setCellValueFactory(new PropertyValueFactory<>("cena"));

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            koncertList.addAll(session.createQuery("from Koncert", Koncert.class).list());
        }

        koncertTableView.setItems(koncertList);

        koncertTableView.setOnMouseClicked(event -> {
            Koncert wybrany = koncertTableView.getSelectionModel().getSelectedItem();
            if (wybrany != null) {
                wykonawcaField.setText(wybrany.getWykonawca());
                dataPicker.setValue(wybrany.getData());
                miejsceField.setText(wybrany.getMiejsce());
                cenaField.setText(String.valueOf(wybrany.getCena()));
            }
        });
    }

    @FXML
    private void kupBilet() {
        Koncert wybrany = koncertTableView.getSelectionModel().getSelectedItem();

        if (wybrany == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Wybierz koncert z listy.");
            alert.show();
            return;
        }

        Bilet bilet = new Bilet(uzytkownik, wybrany, LocalDateTime.now());

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(bilet);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukces");
        alert.setHeaderText(null);
        alert.setContentText("Bilet kupiony pomyślnie!");
        alert.showAndWait();

    }

    @FXML
    private void wyloguj() {
        SceneUtil.otworzPanel("/pl/koncerty/gui/login.fxml", "Logowanie", cenaField);
    }

    public void initData(Uzytkownik u) {
        this.uzytkownik = u;
    }

    private Uzytkownik uzytkownik;

    public void initUzytkownik(Uzytkownik u) {
        this.uzytkownik = u;
    }
    @FXML
    private void powrot() {
        SceneUtil.powrot(cenaField);
    }


}
