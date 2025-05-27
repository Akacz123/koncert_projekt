package pl.koncerty.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.koncerty.model.Bilet;
import pl.koncerty.model.Koncert;
import pl.koncerty.model.Uzytkownik;
import pl.koncerty.util.HibernateUtil;

import java.time.LocalDateTime;

public class BuyTicketController {

    @FXML private ChoiceBox<String> metodaPlatnosciBox;

    private Koncert wybranyKoncert;
    private Uzytkownik zalogowanyUzytkownik;

    public void initData(Koncert koncert, Uzytkownik uzytkownik) {
        this.wybranyKoncert = koncert;
        this.zalogowanyUzytkownik = uzytkownik;
    }

    @FXML
    private void kupBilet() {
        if (wybranyKoncert == null || zalogowanyUzytkownik == null) return;

        Bilet bilet = new Bilet();
        bilet.setUzytkownik(zalogowanyUzytkownik);
        bilet.setKoncert(wybranyKoncert);
        bilet.setDataZakupu(LocalDateTime.now());
        bilet.setNumerBiletu("B" + System.currentTimeMillis());

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(bilet);
            tx.commit();
        }

        Stage stage = (Stage) metodaPlatnosciBox.getScene().getWindow();
        stage.close();
    }
}
