package pl.koncerty.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Bilet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "uzytkownik_id")
    private Uzytkownik uzytkownik;

    @ManyToOne
    @JoinColumn(name = "koncert_id")
    private Koncert koncert;

    private LocalDateTime dataZakupu;
    private String numerBiletu;

    public Bilet(Uzytkownik uzytkownik, Koncert wybrany, LocalDateTime now) {
    }

    public Bilet() {

    }


    public String getNumerBiletu() {
        return numerBiletu;
    }

    public void setNumerBiletu(String numerBiletu) {
        this.numerBiletu = numerBiletu;
    }

    public LocalDateTime getDataZakupu() {
        return dataZakupu;
    }

    public void setDataZakupu(LocalDateTime dataZakupu) {
        this.dataZakupu = dataZakupu;
    }

    public Koncert getKoncert() {
        return koncert;
    }

    public void setKoncert(Koncert koncert) {
        this.koncert = koncert;
    }

    public Uzytkownik getUzytkownik() {
        return uzytkownik;
    }

    public void setUzytkownik(Uzytkownik uzytkownik) {
        this.uzytkownik = uzytkownik;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
