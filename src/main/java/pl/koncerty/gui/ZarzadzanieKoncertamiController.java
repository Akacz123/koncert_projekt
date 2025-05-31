package pl.koncerty.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.koncerty.util.HibernateUtil;
import pl.koncerty.model.Koncert;
import pl.koncerty.util.SceneUtil;

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

        zaladujKoncerty();

        koncertTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            Koncert wybrany = koncertTableView.getSelectionModel().getSelectedItem();
            if (wybrany != null) {
                wykonawcaField.setText(wybrany.getWykonawca());
                dataPicker.setValue(wybrany.getData());
                miejsceField.setText(wybrany.getMiejsce());
                cenaField.setText(String.valueOf(wybrany.getCena()));
            }
        });
    }

    private void zaladujKoncerty() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            koncertList.clear();
            koncertList.addAll(session.createQuery("FROM Koncert", Koncert.class).list());
            koncertTableView.setItems(koncertList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void dodajKoncert() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            String wykonawca = wykonawcaField.getText();
            LocalDate data = dataPicker.getValue();
            String miejsce = miejsceField.getText();
            double cena = Double.parseDouble(cenaField.getText());

            Koncert nowyKoncert = new Koncert(wykonawca, data, miejsce, cena);
            session.save(nowyKoncert);
            tx.commit();

            zaladujKoncerty();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void usunKoncert() {
        Koncert wybrany = koncertTableView.getSelectionModel().getSelectedItem();
        if (wybrany == null) return;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(wybrany);
            tx.commit();
            zaladujKoncerty();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void clearFields() {
        wykonawcaField.clear();
        dataPicker.setValue(null);
        miejsceField.clear();
        cenaField.clear();
    }

    @FXML private Button wylogujBtn;
    @FXML
    private void wyloguj(MouseEvent event) {
        Node source = (Node) event.getSource();
        SceneUtil.wyloguj(source);
    }

    @FXML private Button powrotBtn;
    @FXML
    private void powrot() {
        SceneUtil.powrot(powrotBtn);
    }
}