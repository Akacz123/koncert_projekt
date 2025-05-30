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
import org.hibernate.Transaction;
import pl.koncerty.util.HibernateUtil;
import pl.koncerty.model.Koncert;
import pl.koncerty.util.SceneUtil;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ZarzadzanieKoncertamiController implements Initializable {

    @FXML private TableColumn<Koncert, String> wykonawcaCol, miejsceCol;
    @FXML private TableColumn<Koncert, LocalDate> dataCol;
    @FXML private TableColumn<Koncert, Double> cenaCol;
    @FXML private TableView<Koncert> koncertTableView;

    @FXML private TextField wykonawcaField, miejsceField, cenaField;
    @FXML private DatePicker dataPicker;

    private ObservableList<Koncert> koncertList = FXCollections.observableArrayList();

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
    private void zapiszZmiany() {
        Koncert wybrany = koncertTableView.getSelectionModel().getSelectedItem();
        if (wybrany == null) return;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            wybrany.setWykonawca(wykonawcaField.getText());
            wybrany.setData(dataPicker.getValue());
            wybrany.setMiejsce(miejsceField.getText());
            wybrany.setCena(Double.parseDouble(cenaField.getText()));

            session.update(wybrany);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        koncertTableView.refresh();
    }
    @FXML private Button wylogujBtn;
    @FXML
    private void wyloguj() {
        SceneUtil.otworzPanel("/pl/koncerty/gui/login.fxml", "Logowanie", wylogujBtn);
    }
    @FXML private Button powrotBtn;

    @FXML
    private void powrot() {
        SceneUtil.otworzPanel("/pl/koncerty/gui/uzytkownik_panel.fxml", "Panel użytkownika", powrotBtn);
    }


}
