package pl.koncerty.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;
import pl.koncerty.model.Bilet;
import pl.koncerty.model.Uzytkownik;
import pl.koncerty.util.HibernateUtil;
import pl.koncerty.util.SceneUtil;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ShowTicketController implements Initializable {
    @FXML
    private TableView<Bilet> biletTable;

    @FXML
    private TableColumn<Bilet, String> koncertCol;

    @FXML private TableColumn<Bilet, String> miejsceCol;
    @FXML private TableColumn<Bilet, String> dataKoncertuCol;
    @FXML private TableColumn<Bilet, LocalDateTime> dataZakupuCol;


    private Uzytkownik uzytkownik;

    public void initUzytkownik(Uzytkownik u) {
        this.uzytkownik = u;
        zaladujBilety();
    }

    private void zaladujBilety() {
        ObservableList<Bilet> bilety = FXCollections.observableArrayList();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Bilet> query = session.createQuery("FROM Bilet WHERE uzytkownik.id = :id", Bilet.class);
            query.setParameter("id", uzytkownik.getId());
            bilety.addAll(query.getResultList());
        }

        biletTable.setItems(bilety);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        koncertCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getKoncert().getWykonawca()));

        miejsceCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getKoncert().getMiejsce()));

        dataKoncertuCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getKoncert().getData().toString()));

        dataZakupuCol.setCellValueFactory(new PropertyValueFactory<>("dataZakupu"));
    }

    @FXML private Button powrotBtn;

    @FXML
    private void powrot() {
        SceneUtil.otworzPanel("/pl/koncerty/gui/uzytkownik_panel.fxml", "Panel użytkownika", powrotBtn);
    }

    @FXML private Button wylogujBtn;
    @FXML
    private void wyloguj() {
        SceneUtil.otworzPanel("/pl/koncerty/gui/login.fxml", "Logowanie", wylogujBtn);
    }
    @FXML
    private void zwrocBilet() {
        Bilet wybrany = biletTable.getSelectionModel().getSelectedItem();
        if (wybrany == null) {
            new Alert(Alert.AlertType.WARNING, "Nie wybrano biletu.").show();
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Bilet b = session.get(Bilet.class, wybrany.getId());
            session.delete(b);
            session.getTransaction().commit();
        }

        biletTable.getItems().remove(wybrany);
        new Alert(Alert.AlertType.INFORMATION, "Bilet został zwrócony.").show();
    }

}
