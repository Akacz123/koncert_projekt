package pl.koncerty.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
        System.out.println("ShowTicketController - inicjalizacja użytkownika: " +
                (u != null ? u.getLogin() + " (ID: " + u.getId() + ")" : "NULL"));
        this.uzytkownik = u;
        if (u != null) {
            zaladujBiletyUzytkownika();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ShowTicketController - initialize() wywołane");

        koncertCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKoncert().getWykonawca()));
        miejsceCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKoncert().getMiejsce()));
        dataKoncertuCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKoncert().getData().toString()));
        dataZakupuCol.setCellValueFactory(new PropertyValueFactory<>("dataZakupu"));

        Uzytkownik u = SceneUtil.getAktualnyUzytkownik();
        if (u != null) {
            initUzytkownik(u);
        } else {
            System.out.println("UWAGA: Brak użytkownika w SceneUtil podczas initialize() w ShowTicketController");
        }
    }
    @FXML private Button powrotBtn;

    @FXML
    private void powrot() {
        System.out.println("ShowTicketController - powrót do poprzedniej sceny");
        SceneUtil.powrot(powrotBtn);
    }

    @FXML
    private void wyloguj(MouseEvent event) {
        Node source = (Node) event.getSource();
        SceneUtil.wyloguj(source);
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

            System.out.println("Zwrócono bilet ID: " + wybrany.getId());
            zaladujBiletyUzytkownika(); // Odśwież listę biletów po usunięciu
            new Alert(Alert.AlertType.INFORMATION, "Bilet zwrócono pomyślnie.").show();
        } catch (Exception e) {
            System.out.println("BŁĄD podczas zwracania biletu: " + e.getMessage());
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Błąd podczas zwracania biletu: " + e.getMessage()).show();
        }
    }

    private void zaladujBiletyUzytkownika() {
        if (uzytkownik == null) {
            System.out.println("Brak zalogowanego użytkownika do załadowania biletów.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Bilet> query = session.createQuery(
                    "FROM Bilet WHERE uzytkownik = :uzytkownik", Bilet.class);
            query.setParameter("uzytkownik", uzytkownik);
            ObservableList<Bilet> bilety = FXCollections.observableArrayList(query.getResultList());
            biletTable.setItems(bilety);
            System.out.println("Załadowano " + bilety.size() + " biletów dla użytkownika ID: " + uzytkownik.getId());
        } catch (Exception e) {
            System.out.println("BŁĄD podczas ładowania biletów użytkownika: " + e.getMessage());
            e.printStackTrace();
        }
    }
}