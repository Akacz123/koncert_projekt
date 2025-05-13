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
import pl.koncerty.util.HibernateUtil;
import pl.koncerty.model.Koncert;
import pl.koncerty.util.SceneUtil;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ListaBiletController implements Initializable {
    @FXML
    private TableColumn<Koncert, String> wykonawcaCol, miejsceCol;
    @FXML private TableColumn<Koncert, LocalDate> dataCol;
    @FXML private TableColumn<Koncert, Double> cenaCol;
    @FXML private TableView<Koncert> koncertTableView;
    @FXML private TextField wykonawcaField, miejsceField, cenaField;
    @FXML private DatePicker dataPicker;
    @FXML private Label statusLabel;

    @FXML private ObservableList<Koncert> koncertList = FXCollections.observableArrayList();

    private void otworzPanel(String sciezka, String tytul) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(sciezka));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setTitle(tytul);
        stage.setScene(scene);
        stage.show();
    }

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
    private void oknoDoKupowaniaBiletow() throws Exception {
        otworzPanel("pl/koncerty/gui/okno_kupowania_biletu.fxml", "Kup Bilet");
    }
    @FXML
    private void wyloguj() {
        try{
            SceneUtil.wyloguj("pl/koncerty/gui/login.fxml", "Logowanie", cenaField);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


}
