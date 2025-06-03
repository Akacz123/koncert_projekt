package pl.koncerty.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.Session;
import pl.koncerty.model.Bilet;
import pl.koncerty.model.Uzytkownik;
import pl.koncerty.util.HibernateUtil;
import pl.koncerty.model.Koncert;
import pl.koncerty.util.SceneUtil;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.UUID;

public class ListaBiletController implements Initializable {
    @FXML
    private TableColumn<Koncert, String> wykonawcaCol, miejsceCol;
    @FXML private TableColumn<Koncert, LocalDate> dataCol;
    @FXML private TableColumn<Koncert, Double> cenaCol;
    @FXML private TableView<Koncert> koncertTableView;
    @FXML private TextField wykonawcaField, miejsceField, cenaField;
    @FXML private DatePicker dataPicker;
    @FXML private ObservableList<Koncert> koncertList = FXCollections.observableArrayList();

    private Uzytkownik uzytkownik;

    @FXML private Button powrotBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ListaBiletController - initialize() wywołane");
        uzytkownik = SceneUtil.getAktualnyUzytkownik();
        if (uzytkownik != null) {
            initUzytkownik(uzytkownik);
        } else {
            System.out.println("UWAGA: Brak użytkownika w SceneUtil podczas initialize() w ListaBiletController");
        }

        wykonawcaCol.setCellValueFactory(new PropertyValueFactory<>("wykonawca"));
        dataCol.setCellValueFactory(new PropertyValueFactory<>("data"));
        miejsceCol.setCellValueFactory(new PropertyValueFactory<>("miejsce"));
        cenaCol.setCellValueFactory(new PropertyValueFactory<>("cena"));

        zaladujKoncerty();

        koncertTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                wykonawcaField.setText(newSelection.getWykonawca());
                dataPicker.setValue(newSelection.getData());
                miejsceField.setText(newSelection.getMiejsce());
                cenaField.setText(String.valueOf(newSelection.getCena()));
            } else {
                wykonawcaField.setText("");
                dataPicker.setValue(null);
                miejsceField.setText("");
                cenaField.setText("");
            }
        });
    }

    private void zaladujKoncerty() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            koncertList.clear();
            koncertList.addAll(session.createQuery("FROM Koncert", Koncert.class).list());
            koncertTableView.setItems(koncertList);
        } catch (Exception e) {
            System.out.println("BŁĄD podczas ładowania koncertów: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void kupBilet() {
        Koncert wybrany = koncertTableView.getSelectionModel().getSelectedItem();
        if (wybrany == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Wybierz koncert, aby kupić bilet.");
            alert.show();
            return;
        }

        if (uzytkownik == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Błąd: Brak zalogowanego użytkownika. Zaloguj się ponownie.");
            alert.show();
            return;
        }

        Bilet bilet = new Bilet();
        bilet.setUzytkownik(uzytkownik);
        bilet.setKoncert(wybrany);
        bilet.setDataZakupu(LocalDateTime.now());
        bilet.setNumerBiletu(UUID.randomUUID().toString());

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(bilet);
            session.getTransaction().commit();

            System.out.println("Kupiono bilet dla użytkownika: " + uzytkownik.getLogin() +
                    " na koncert: " + wybrany.getWykonawca());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukces");
            alert.setHeaderText(null);
            alert.setContentText("Bilet kupiony pomyślnie! Numer biletu: " + bilet.getNumerBiletu());
            alert.showAndWait();

        } catch (Exception e) {
            System.out.println("BŁĄD podczas kupowania biletu: " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Błąd podczas kupowania biletu: " + e.getMessage() + ". Sprawdź logi aplikacji.");
            alert.show();
        }
    }

    @FXML
    private void powrot() {
        SceneUtil.powrot(powrotBtn);
    }

    @FXML private Button wylogujBtn;
    @FXML
    private void wyloguj() {
        System.out.println("ListaBiletController - wylogowywanie użytkownika");
        SceneUtil.wyloguj(wylogujBtn);
    }

    public void initUzytkownik(Uzytkownik u) {
        System.out.println("ListaBiletController - inicjalizacja użytkownika: " +
                (u != null ? u.getLogin() + " (ID: " + u.getId() + ")" : "NULL"));
        this.uzytkownik = u;
    }
}